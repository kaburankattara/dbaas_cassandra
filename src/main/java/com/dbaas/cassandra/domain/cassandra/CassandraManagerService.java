package com.dbaas.cassandra.domain.cassandra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.cassandra.table.Table;
import com.dbaas.cassandra.domain.serverManager.instance.Instance;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;
import com.dbaas.cassandra.utils.StringUtils;
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
				result = StringUtils.replaceAll(result, "  ", " ");
				keySpaceList.addAll(Arrays.asList(StringUtils.split(result, ' ')));
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

	public List<String> findTableByKeySpace(Instances instances, String keySpace) {
		// 各サーバの保持しているテーブル一覧を取得
		List<String> tableList = new ArrayList<>();
		for (Instance instance : instances.getInstanceList()) {
			List<String> result = null;
				result = findTableByKeySpace(instance, keySpace);
				tableList.addAll(result);
				return tableList;
		}
		return null;	
		
		// 各サーバで取得したテーブルの重複を削除
		// TODO マルチノードで
	}

	public List<String> findTableByKeySpace(Instance instance, String keySpace) {
		String ipAddress = instance.getPublicIpAddress();
		String identityKeyFilName = "src/main/resources/static/cassandra/identityKeyFile/cassandra2.pem";
		List<String> tableList = new ArrayList<>();
		String result = null;
		try {
			// サーバインスタンスを生成する
			Cassandra cassandraServer = Cassandra.createInstance(ipAddress, 22, "ec2-user", identityKeyFilName);

			// create文を作成して実行
			String cqlCommand = "use " + keySpace + "; DESCRIBE tables;";
			cassandraServer.execCql(instance, cqlCommand);
			result = cassandraServer.execCql(instance, cqlCommand);
			result = StringUtils.replaceAll(result, "  ", " ");
			tableList.addAll(Arrays.asList(StringUtils.split(result, ' ')));
			return tableList;
		} catch (JSchException | SftpException e) {
			throw new RuntimeException();
		}
	}
}
