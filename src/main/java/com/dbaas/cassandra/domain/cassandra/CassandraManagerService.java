package com.dbaas.cassandra.domain.cassandra;

import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.CQL_COMMAND_USE;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.KEYSPACE_SYSTEM_SCHEMA;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.SYSTEM_KEYSPACE_LIST;
import static com.dbaas.cassandra.domain.cassandra.CassandraConsts.TABLE_COLUMNS;
import static com.dbaas.cassandra.utils.StringUtils.isNotEmpty;
import static com.dbaas.cassandra.utils.StringUtils.replaceAll;
import static com.dbaas.cassandra.utils.StringUtils.split;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.cassandra.cql.CqlFactory;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.cassandra.table.Tables;
import com.dbaas.cassandra.domain.serverManager.instance.Instance;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

@Service
@Transactional
public class CassandraManagerService {

	@Autowired
	public CassandraManagerService() {
	}

	public void setup(LoginUser user, Instance instance) {
		String ipAddress = instance.getPublicIpAddress();
		String identityKeyFilName = "src/main/resources/static/cassandra/identityKeyFile/cassandra2.pem";
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance(ipAddress, 22, "ec2-user", identityKeyFilName);

			// cassandraをインストールする
			cassandraServer.installCassandra();

			// casssandra.yamlを設定し、cassandraを起動する
			cassandraServer.setCassandraYaml(user, instance);

			// cassandraを実行する
			cassandraServer.execCassandra();
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	public void execCassandra(Instance instance) {
		String ipAddress = instance.getPublicIpAddress();
		String identityKeyFilName = "src/main/resources/static/cassandra/identityKeyFile/cassandra2.pem";
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance(ipAddress, 22, "ec2-user", identityKeyFilName);

			// cassandraを実行する
			cassandraServer.execCassandra();
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	public boolean isExecCassandra(Instance instance) {
		String ipAddress = instance.getPublicIpAddress();
		String identityKeyFilName = "src/main/resources/static/cassandra/identityKeyFile/cassandra2.pem";
		boolean isExecCassandra;
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance(ipAddress, 22, "ec2-user", identityKeyFilName);

			// cassandraの起動確認
			isExecCassandra = cassandraServer.isExecCassandra();
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
		return isExecCassandra;
	}

	public boolean isExecAllCassandra(Instances instances) {
		for (Instance instance : instances.getInstanceList()) {
			if (!isExecCassandra(instance)) {
				return false;
			}
		}
		return true;
	}

	public void registKeySpace(Instance instance, String keySpace) {
		String ipAddress = instance.getPublicIpAddress();
		String identityKeyFilName = "src/main/resources/static/cassandra/identityKeyFile/cassandra2.pem";
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance(ipAddress, 22, "ec2-user", identityKeyFilName);

			// create文を作成して実行
			String cqlCommand = "create keyspace if not exists " + keySpace
					+ " with replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };";
			cassandraServer.execCql(instance, cqlCommand);
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	public void deleteKeySpace(Instance instance, String keySpace) {
		String ipAddress = instance.getPublicIpAddress();
		String identityKeyFilName = "src/main/resources/static/cassandra/identityKeyFile/cassandra2.pem";
		
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance(ipAddress, 22, "ec2-user", identityKeyFilName);

			// 実行文を生成
			String cqlCommand = "drop keyspace " + keySpace + ";";
			
			// 実行
			cassandraServer.execCql(instance, cqlCommand);
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	public List<String> findAllKeySpace(Instances instances) {
		// 各サーバの保持しているキースペース一覧を取得
		String identityKeyFilName = "src/main/resources/static/cassandra/identityKeyFile/cassandra2.pem";
		List<String> keySpaceList = new ArrayList<>();
		for (Instance instance : instances.getInstanceList()) {
			String ipAddress = instance.getPublicIpAddress();
			String result = null;
			try {
				// サーバインスタンスを生成する
				Cassandra cassandraServer = Cassandra.createInstance(ipAddress, 22, "ec2-user", identityKeyFilName);
				
				// create文を作成して実行
				String cqlCommand = "DESC KEYSPACES";
				result = cassandraServer.execCql(instance, cqlCommand);
				
				// 検索結果をListに整形
				result = replaceAll(result, "\n", "");
				result = replaceAll(result, "  ", " ");
				List<String> cqlResultKeySpaceList = new ArrayList<String>(asList(split(result, ' ')));
				
				// システム管理用キースペース以外を抽出
				for (String keySpace : cqlResultKeySpaceList) {
					if (SYSTEM_KEYSPACE_LIST.contains(keySpace)) {
						continue;
					}
					keySpaceList.add(keySpace);
				}
				
				return keySpaceList;
			} catch (JSchException | SftpException e) {
				throw new RuntimeException();
			}
		}
		return null;	
		
		// 各サーバで取得したキースペースの重複を削除
		// TODO マルチノードで
	}

	public void registTable(Instance instance, String keySpace, Table table) {
		String ipAddress = instance.getPublicIpAddress();
		String identityKeyFilName = "src/main/resources/static/cassandra/identityKeyFile/cassandra2.pem";
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance(ipAddress, 22, "ec2-user", identityKeyFilName);

			// create文を作成して実行
			String cqlCommand = table.getCreateCql(keySpace);
			cassandraServer.execCql(instance, cqlCommand);
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	public Tables findTableByKeySpace(Instances instances, String keySpace) {
		return findTableByKeySpace(instances, keySpace, null);
	}

	public Tables findTableByKeySpace(Instances instances, String keySpace, String tableName) {
		// 各サーバの保持しているテーブル一覧を取得
		List<Table> tableList = new ArrayList<Table>();
		for (Instance instance : instances.getInstanceList()) {
				Tables tables = findTableByKeySpace(instance, keySpace, tableName);
				tableList.addAll(tables.getTableList());
				return new Tables(tableList);
		}
		return null;	
		
		// 各サーバで取得したテーブルの重複を削除
		// TODO マルチノードで
	}

	public Tables findTableByKeySpace(Instance instance, String keySpace, String tableName) {
		String ipAddress = instance.getPublicIpAddress();
		String identityKeyFilName = "src/main/resources/static/cassandra/identityKeyFile/cassandra2.pem";
		String result = null;
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance(ipAddress, 22, "ec2-user", identityKeyFilName);

			// create文を作成
			String cqlCommand = CQL_COMMAND_USE + KEYSPACE_SYSTEM_SCHEMA + "; ";
			cqlCommand = cqlCommand + "select * from " + TABLE_COLUMNS + " where keyspace_name = '" + keySpace + "'";
			if (isNotEmpty(tableName)) {
				cqlCommand = cqlCommand + " and table_name = '" + tableName + "'";
			}
			cqlCommand = cqlCommand + ";";
			
			// 実行
			result = cassandraServer.execCql(instance, cqlCommand);
			return new Tables(result);
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}

	public Tables addColumns(Instance instance, String keySpace, Table newTable) {
		String ipAddress = instance.getPublicIpAddress();
		String identityKeyFilName = "src/main/resources/static/cassandra/identityKeyFile/cassandra2.pem";
		String result = null;

		// 現行のテーブル情報を取得
		Instances instances = Instances.createInstance(instance);
		// TODO  返り値がおかしい？
		Table oldTable = findTableByKeySpace(instances, keySpace, newTable.getTableName())
				.getTable(newTable.getTableName());
		
		try {
			// テーブルを現行と次期で比較し、Alter文を生成
			CqlFactory cqlFactory = new CqlFactory();
			List<String> cqlAlterCommandList = cqlFactory.createAlterCqlForAddColumns(keySpace, oldTable, newTable);
			
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance(ipAddress, 22, "ec2-user", identityKeyFilName);

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
		String ipAddress = instance.getPublicIpAddress();
		String identityKeyFilName = "src/main/resources/static/cassandra/identityKeyFile/cassandra2.pem";
		
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance(ipAddress, 22, "ec2-user", identityKeyFilName);

			// 実行文を生成
			String cqlCommand = "drop table " + keySpace + "." + tableName + ";";
			
			// 実行
			cassandraServer.execCql(instance, cqlCommand);
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}
}
