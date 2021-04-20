package com.dbaas.cassandra.domain.cassandra;

import com.dbaas.cassandra.domain.cassandra.cql.CqlFactory;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.cassandra.table.Tables;
import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.serverManager.instance.Instance;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.utils.ThreadUtils;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
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
public class CassandraManagerService {

	@Autowired
	public CassandraManagerService() {
	}

	/**
	 * 全てのインスタンスに対し、cassandraをセットアップする
	 * c
	 * @param user
	 * @param instances
	 */
	public void setup(LoginUser user, Instances instances) {
		for (Instance instance : instances.getInstanceList()) {
			setup(user, instance);
		}
	}

	public void setup(LoginUser user, Instance instance) {
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance();

			// cassandraをインストールする
//			cassandraServer.installCassandra();

			// casssandra.yamlを設定し、cassandraを起動する
			cassandraServer.setCassandraYaml(user, instance);

			// cassandraを実行する
			cassandraServer.execCassandra(instance);
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	public void execCassandra(Instance instance) {
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance();

			// cassandraを実行する
			cassandraServer.execCassandra(instance);
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	/**
	 * 起動確認まで考慮してcassandraを起動する
	 * 
	 * @param instances
	 */
	public void execCassandraByWait(Instances instances) {
		boolean hasNotExecCassandra = true;
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
						// 実行対象cassandra数×3回再起動を試みたが起動仕切らない場合、
						// システムエラーとする
						// TODO 専用のExceptionを用意する
						new RuntimeException();
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
	 * @param instances
	 * @return 判定結果
	 */
	public boolean canAllExecCql(Instances instances) {
		if (instances.isEmpty()) {
			return false;
		}
		
		for (Instance instance : instances.getInstanceList()) {
			// CQL実行が出来ないインスタンスが存在すればfalse
			if (isEmpty(findAllKeySpace(instance))) {
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
		return isNotEmpty(findAllKeySpace(instance));
	}

	/**
	 * casssandraが起動済みか判定
	 * 
	 * @param instance
	 * @return 判定結果
	 */
	public boolean isExecCassandra(Instance instance) {
		// cassandraのプロセスIDを取得し起動済みか判定
		return isNotEmpty(getProcessIdByCassandra(instance));
	}

	public String getProcessIdByCassandra(Instance instance) {
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance();

			// cassandraの起動確認
			return cassandraServer.getProcessIdByCassandra(instance);
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	public boolean isExecAllCassandra(Instances instances) {
		for (Instance instance : instances.getInstanceList()) {
			if (!isExecCassandra(instance)) {
				return false;
			}
		}
		return true;
	}
	
	public void registKeySpace(Instances instances, String keySpace) {
		for (Instance instance : instances.getInstanceList()) {
			registKeySpace(instance, keySpace);
		}
	}
	
	/**
	 * 重複は無視してキースペースを登録
	 * 
	 * @param instances
	 * @param keyspaceRegistPlans
	 */
	public void registKeySpaceByDuplicatIgnore(Instances instances, KeyspaceRegistPlans keyspaceRegistPlans) {
		for (String keyspace : keyspaceRegistPlans.getKeyspaceList()) {
			registKeySpaceByDuplicatIgnore(instances, keyspace);
		}
	}
	
	public void registKeySpaceByDuplicatIgnore(Instances instances, String keyspace) {
		for (Instance instance : instances.getInstanceList()) {
			registKeySpaceByDuplicatIgnore(instance, keyspace);
		}
	}
	
	/**
	 * 重複は無視してキースペースを登録
	 * 
	 * @param instance
	 * @param keyspace
	 */
	public void registKeySpaceByDuplicatIgnore(Instance instance, String keyspace) {
		// 対象インスタンスのキースペース一覧を取得
		List<String> keyspaceList = findAllKeySpaceWithoutSysKeySpace(instance);
		
		// キースペースが重複するのであれば登録しない
		if (keyspaceList.contains(keyspace)) {
			return;
		}
		
		// キースペースが重複しないのであれば登録
		registKeySpace(instance, keyspace);
	}
	
	public void registKeySpace(Instance instance, String keySpace) {
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance();

			// create文を作成して実行
			String cqlCommand = "create keyspace if not exists " + keySpace
					+ " with replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };";
			cassandraServer.execCql(instance, cqlCommand);
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	public void deleteKeySpace(Instance instance, String keySpace) {
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance();

			// 実行文を生成
			String cqlCommand = "drop keyspace " + keySpace + ";";
			
			// 実行
			cassandraServer.execCql(instance, cqlCommand);
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	public List<String> findAllKeySpaceWithoutSysKeySpace(Instances instances) {
		List<String> keySpaceList = new ArrayList<>();
		// システム管理用キースペース以外を抽出
		for (Instance instance : instances.getInstanceList()) {
			keySpaceList.addAll(findAllKeySpaceWithoutSysKeySpace(instance));
		}
		return keySpaceList;
	}

	public List<String> findAllKeySpaceWithoutSysKeySpace(Instance instance) {
		List<String> keySpaceList = new ArrayList<>();
		// システム管理用キースペース以外を抽出
		for (String keySpace : findAllKeySpace(instance)) {
			if (SYSTEM_KEYSPACE_LIST.contains(keySpace)) {
				continue;
			}
			keySpaceList.add(keySpace);
		}
		return keySpaceList;
	}

	public List<String> findAllKeySpace(Instances instances) {
		// 各サーバの保持しているキースペース一覧を取得
		List<String> keySpaceList = new ArrayList<>();
		for (Instance instance : instances.getInstanceList()) {
			keySpaceList.addAll(findAllKeySpace(instance));
		}
		return keySpaceList;
	}

	public List<String> findAllKeySpace(Instance instance) {
		// 各サーバの保持しているキースペース一覧を取得
		List<String> keySpaceList = new ArrayList<>();
		String result = null;
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance();

			// create文を作成して実行
			String cqlCommand = "DESC KEYSPACES";
			result = cassandraServer.execCql(instance, cqlCommand);

			// 検索結果をListに整形
			result = replaceAll(result, "\n", "");
			result = replaceAll(result, "  ", " ");
			List<String> cqlResultKeySpaceList = new ArrayList<String>(asList(split(result, ' ')));

			// システム管理用キースペース以外を抽出
			for (String keySpace : cqlResultKeySpaceList) {
				keySpaceList.add(keySpace);
			}

			return keySpaceList;
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	public void registTable(Instance instance, String keySpace, Table table) {
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance();

			// create文を作成して実行
			String cqlCommand = table.getCreateCql(keySpace);
			cassandraServer.execCql(instance, cqlCommand);
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	public Tables findAllTableByKeySpace(Instances instances, String keySpace) {
		// 各サーバの保持しているテーブル一覧を取得
		for (Instance instance : instances.getInstanceList()) {
				Tables tables = findAllTableByKeySpace(instance, keySpace);
				if (!tables.isEmpty()) {
					return tables;
				}
		}
		return new Tables();	
		
		// 各サーバで取得したテーブルの重複を削除
		// TODO マルチノードで
	}

	public Table findTableByKeySpace(Instances instances, String keySpace, String tableName) {
		// 各サーバの保持しているテーブル一覧を取得
		for (Instance instance : instances.getInstanceList()) {
				Table table = findTableByKeySpace(instance, keySpace, tableName);
				if (!table.isEmpty()) {
					return table;
				}
		}
		return createEmptyTable();	
		
		// 各サーバで取得したテーブルの重複を削除
		// TODO マルチノードで
	}

	public Tables findAllTableByKeySpace(Instance instance, String keySpace) {
		String result = null;
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance();

			// create文を作成
			String cqlCommand = CQL_COMMAND_USE + KEYSPACE_SYSTEM_SCHEMA + "; ";
			cqlCommand = cqlCommand + "select * from " + TABLE_COLUMNS + " where keyspace_name = '" + keySpace + "'";
			cqlCommand = cqlCommand + ";";
			
			// 実行
			result = cassandraServer.execCql(instance, cqlCommand);
			return new Tables(result);
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	public Table findTableByKeySpace(Instance instance, String keySpace, String tableName) {
		String result = null;
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance();

			// create文を作成
			String cqlCommand = CQL_COMMAND_USE + KEYSPACE_SYSTEM_SCHEMA + "; ";
			cqlCommand = cqlCommand + "select * from " + TABLE_COLUMNS + " where keyspace_name = '" + keySpace + "'";
			if (isNotEmpty(tableName)) {
				cqlCommand = cqlCommand + " and table_name = '" + tableName + "'";
			}
			cqlCommand = cqlCommand + ";";
			
			// 実行
			result = cassandraServer.execCql(instance, cqlCommand);
			return new Tables(result).getFirstTable();
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	public Tables addColumns(Instance instance, String keySpace, Table newTable) {
		String result = null;

		// 現行のテーブル情報を取得
		Instances instances = Instances.createInstance(instance);
		// TODO  返り値がおかしい？
		Table oldTable = findTableByKeySpace(instances, keySpace, newTable.getTableName());
		
		try {
			// テーブルを現行と次期で比較し、Alter文を生成
			CqlFactory cqlFactory = new CqlFactory();
			List<String> cqlAlterCommandList = cqlFactory.createAlterCqlForAddColumns(keySpace, oldTable, newTable);
			
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
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	public void deleteTable(Instance instance, String keySpace, String tableName) {
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance();

			// 実行文を生成
			String cqlCommand = "drop table " + keySpace + "." + tableName + ";";
			
			// 実行
			cassandraServer.execCql(instance, cqlCommand);
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}
}
