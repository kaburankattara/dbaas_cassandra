package com.dbaas.cassandra.domain.cassandra;

import com.dbaas.cassandra.domain.cassandra.cql.CqlFactory;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspaces;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.cassandra.table.Tables;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.shared.exception.SystemException;
import com.dbaas.cassandra.utils.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.*;
import static com.dbaas.cassandra.domain.cassandra.table.Table.createEmptyTable;
import static com.dbaas.cassandra.utils.ObjectUtils.isEmpty;
import static com.dbaas.cassandra.utils.ObjectUtils.isNotEmpty;
import static com.dbaas.cassandra.utils.StringUtils.isNotEmpty;
import static com.dbaas.cassandra.utils.StringUtils.replaceAll;
import static com.dbaas.cassandra.utils.StringUtils.split;
import static java.util.Arrays.asList;

@Service
@Transactional
public class CassandraService {

	@Autowired
	public CassandraService() {
	}

	/**
	 * 全てのインスタンスに対し、cassandraをセットアップする
	 * c
	 * @param user ユーザー
	 * @param instances インスタンスリスト
	 */
	public void setup(LoginUser user, Instances instances) {
		for (Instance instance : instances.getInstanceList()) {
			setup(user, instance);
		}
	}

	public void setup(LoginUser user, Instance instance) {
		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// cassandraをインストールする
//			cassandraServer.installCassandra();

		// casssandra.yamlを設定し、cassandraを起動する
		cassandraServer.setCassandraYaml(user, instance);

		// cassandraを実行する
		cassandraServer.execCassandra(instance);
	}

	public void execCassandra(Instance instance) {
		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// cassandraを実行する
		cassandraServer.execCassandra(instance);
	}

	/**
	 * 起動確認まで考慮してcassandraを起動する
	 * 
	 * @param instances インスタンスリスト
	 */
	public void execCassandraByWait(Instances instances) {
		boolean hasNotExecCassandra = true;
		// TODO application.properties化する
		int execCassandraCount = instances.getHasInstanceCount() * 3;
		while (hasNotExecCassandra) {
			hasNotExecCassandra = false;
			
			// 未起動のcassandraを再起動
			for (Instance instance : instances.getInstanceList()) {
				// プロセスIDが取得出来なければcassandraをリスタートする
				if (!isExecCassandra(instance)) {
					execCassandra(instance);
					
					// 再起動のリトライ制御
					execCassandraCount--;
					if (execCassandraCount < 0) {
						// 実行対象cassandra数×3回再起動を試みたが起動仕切らない場合、システムエラーとする
						throw new SystemException();
					}
				}
			}
			
			// CQLが実行可能か確認
			for (Instance instance : instances.getInstanceList()) {
				// 試験的なcqlコマンドを定期実行し、実行可能になるまで待つ
				if (canExecCql(instance)) {
					continue;
				}
				hasNotExecCassandra = true;
				ThreadUtils.sleep();
				ThreadUtils.sleep();
				ThreadUtils.sleep();
				ThreadUtils.sleep();
				ThreadUtils.sleep();
			}
		}
	}

	/**
	 * 全インスタンスがCQLの実行が可能か判定
	 * 
	 * @param instances インスタンスリスト
	 * @return 判定結果
	 */
	public boolean canAllExecCql(Instances instances) {
		if (instances.isEmpty()) {
			return false;
		}
		
		for (Instance instance : instances.getInstanceList()) {
			// CQL実行が出来ないインスタンスが存在すればfalse
			if (isEmpty(findAllKeyspace(instance))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * CQLが実行可能か判定
	 * 
	 * @return 判定結果
	 */
	public boolean canExecCql(Instance instance) {
		// CQLでキースペース一覧が取得出来るか判定
		return isNotEmpty(findAllKeyspace(instance));
	}

	/**
	 * casssandraが起動済みか判定
	 * 
	 * @param instance　インスタンス
	 * @return 判定結果
	 */
	public boolean isExecCassandra(Instance instance) {
		// cassandraのプロセスIDを取得し起動済みか判定
		return isNotEmpty(getProcessIdByCassandra(instance));
	}

	public String getProcessIdByCassandra(Instance instance) {
		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// cassandraの起動確認
		return cassandraServer.getProcessIdByCassandra(instance);
	}

	public boolean isExecAllCassandra(Instances instances) {
		for (Instance instance : instances.getInstanceList()) {
			if (!isExecCassandra(instance)) {
				return false;
			}
		}
		return true;
	}
	
	public void registKeyspace(Instances instances, Keyspace keyspace) {
		for (Instance instance : instances.getInstanceList()) {
			registKeyspace(instance, keyspace);
		}
	}
	
	/**
	 * 重複は無視してキースペースを登録
	 * 
	 * @param instances
	 * @param keyspaceRegistPlans
	 */
	public void registKeyspaceByDuplicatIgnore(Instances instances, KeyspaceRegistPlans keyspaceRegistPlans) {
		for (String keyspace : keyspaceRegistPlans.getKeyspaceList()) {
			registKeyspaceByDuplicatIgnore(instances, Keyspace.createInstance(keyspace));
		}
	}
	
	public void registKeyspaceByDuplicatIgnore(Instances instances, Keyspace keyspace) {
		for (Instance instance : instances.getInstanceList()) {
			registKeyspaceByDuplicatIgnore(instance, keyspace);
		}
	}
	
	/**
	 * 重複は無視してキースペースを登録
	 * 
	 * @param instance
	 * @param keyspace
	 */
	public void registKeyspaceByDuplicatIgnore(Instance instance, Keyspace keyspace) {
		// 対象インスタンスのキースペース一覧を取得
		List<String> keyspaceList = findAllKeyspaceWithoutSysKeyspace(instance);
		
		// キースペースが重複するのであれば登録しない
		if (keyspaceList.contains(keyspace)) {
			return;
		}
		
		// キースペースが重複しないのであれば登録
		registKeyspace(instance, keyspace);
	}

	public void registKeyspace(Instance instance, Keyspace keyspace) {
		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// create文を作成して実行
		String cqlCommand = "create keyspace if not exists " + keyspace.getKeyspace()
				+ " with replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };";
		cassandraServer.execCql(instance, cqlCommand);
	}

	public void deleteKeyspace(Instance instance, String keyspace) {
		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// 実行文を生成
		String cqlCommand = "drop keyspace " + keyspace + ";";

		// 実行
		cassandraServer.execCql(instance, cqlCommand);
	}

	public List<String> findAllKeyspaceWithoutSysKeyspace(Instances instances) {
		List<String> keyspaceList = new ArrayList<>();
		// システム管理用キースペース以外を抽出
		for (Instance instance : instances.getInstanceList()) {
			keyspaceList.addAll(findAllKeyspaceWithoutSysKeyspace(instance));
		}
		return keyspaceList;
	}

	public List<String> findAllKeyspaceWithoutSysKeyspace(Instance instance) {
		List<String> keyspaceList = new ArrayList<>();
		// システム管理用キースペース以外を抽出
		for (String keyspace : findAllKeyspace(instance)) {
			if (SYSTEM_KEYSPACE_LIST.contains(keyspace)) {
				continue;
			}
			keyspaceList.add(keyspace);
		}
		return keyspaceList;
	}

	public Keyspaces findAllKeyspace(Instances instances) {
		// 各サーバの保持しているキースペース一覧を取得
		List<String> keyspaceList = new ArrayList<>();
		for (Instance instance : instances.getInstanceList()) {
			keyspaceList.addAll(findAllKeyspace(instance));
		}
		return Keyspaces.createInstance(keyspaceList);
	}

	public List<String> findAllKeyspace(Instance instance) {
		// 各サーバの保持しているキースペース一覧を取得
		List<String> keyspaceList = new ArrayList<>();
		String result = null;

		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// create文を作成して実行
		String cqlCommand = "DESC KEYSPACES";
		result = cassandraServer.execCql(instance, cqlCommand);

		// 検索結果をListに整形
		result = replaceAll(result, "\n", "");
		result = replaceAll(result, "  ", " ");
		List<String> cqlResultKeyspaceList = new ArrayList<String>(asList(split(result, ' ')));

		// システム管理用キースペース以外を抽出
		for (String keyspace : cqlResultKeyspaceList) {
			keyspaceList.add(keyspace);
		}

		return keyspaceList;
	}

	public void registTable(Instance instance, String keyspace, Table table) {
		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// create文を作成して実行
		String cqlCommand = table.getCreateCql(keyspace);
		cassandraServer.execCql(instance, cqlCommand);
	}

	public Tables findAllTableByKeyspace(Instances instances, String keyspace) {
		// 各サーバの保持しているテーブル一覧を取得
		for (Instance instance : instances.getInstanceList()) {
				Tables tables = findAllTableByKeyspace(instance, keyspace);
				if (!tables.isEmpty()) {
					return tables;
				}
		}
		return new Tables();	
		
		// 各サーバで取得したテーブルの重複を削除
		// TODO マルチノードで
	}

	public Table findTableByKeyspace(Instances instances, String keyspace, String tableName) {
		// 各サーバの保持しているテーブル一覧を取得
		for (Instance instance : instances.getInstanceList()) {
				Table table = findTableByKeyspace(instance, keyspace, tableName);
				if (!table.isEmpty()) {
					return table;
				}
		}
		return createEmptyTable();	
		
		// 各サーバで取得したテーブルの重複を削除
		// TODO マルチノードで
	}

	public Tables findAllTableByKeyspace(Instance instance, String keyspace) {
		String result = null;

		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// create文を作成
		String cqlCommand = CQL_COMMAND_USE + KEYSPACE_SYSTEM_SCHEMA + "; ";
		cqlCommand = cqlCommand + "select * from " + TABLE_COLUMNS + " where keyspace_name = '" + keyspace + "'";
		cqlCommand = cqlCommand + ";";
			
		// 実行
		result = cassandraServer.execCql(instance, cqlCommand);
		return new Tables(result);
	}

	public Table findTableByKeyspace(Instance instance, String keyspace, String tableName) {
		String result = null;

		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// create文を作成
		String cqlCommand = CQL_COMMAND_USE + KEYSPACE_SYSTEM_SCHEMA + "; ";
		cqlCommand = cqlCommand + "select * from " + TABLE_COLUMNS + " where keyspace_name = '" + keyspace + "'";
		if (isNotEmpty(tableName)) {
			cqlCommand = cqlCommand + " and table_name = '" + tableName + "'";
		}
		cqlCommand = cqlCommand + ";";

		// 実行
		result = cassandraServer.execCql(instance, cqlCommand);
		return new Tables(result).getFirstTable();

	}

	public Tables addColumns(Instance instance, String keyspace, Table newTable) {
		String result = null;

		// 現行のテーブル情報を取得
		Instances instances = Instances.createInstance(instance);
		// TODO  返り値がおかしい？
		Table oldTable = findTableByKeyspace(instances, keyspace, newTable.getTableName());

		// テーブルを現行と次期で比較し、Alter文を生成
		CqlFactory cqlFactory = new CqlFactory();
		List<String> cqlAlterCommandList = cqlFactory.createAlterCqlForAddColumns(keyspace, oldTable, newTable);

		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// 実行文を生成
		StringBuilder sb = new StringBuilder();
		sb.append(CQL_COMMAND_USE + KEYSPACE_SYSTEM_SCHEMA + ";");
		for (String cqlAlterCommand : cqlAlterCommandList) {
			sb.append(cqlAlterCommand);
		}
		String cqlCommand = sb.toString();

		// 実行
		result = cassandraServer.execCql(instance, cqlCommand);
		return new Tables(result);
	}

	public void deleteTable(Instance instance, String keyspace, String tableName) {
		// サーバインスタンスを生成する
		Cassandra cassandraServer = Cassandra.createInstance();

		// 実行文を生成
		String cqlCommand = "drop table " + keyspace + "." + tableName + ";";

		// 実行
		cassandraServer.execCql(instance, cqlCommand);
	}
}
