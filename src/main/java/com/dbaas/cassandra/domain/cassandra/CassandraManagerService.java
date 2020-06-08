package com.dbaas.cassandra.domain.cassandra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.auth.LoginUser;
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
}
