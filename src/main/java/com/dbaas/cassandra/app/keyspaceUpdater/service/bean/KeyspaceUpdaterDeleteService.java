package com.dbaas.cassandra.app.keyspaceUpdater.service.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.cassandra.CassandraService;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.table.keyspaceManager.KeyspaceManagerDao;
import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class KeyspaceUpdaterDeleteService {
	
	private ServerService serverService;
	
	private CassandraService cassandraService;
	
	private KeyspaceManagerDao keyspaceManagerDao;

	@Autowired
	KeyspaceUpdaterDeleteService(ServerService serverService, CassandraService cassandraService, KeyspaceManagerDao keyspaceManagerDao) {
		this.serverService = serverService;
		this.cassandraService = cassandraService;
		this.keyspaceManagerDao = keyspaceManagerDao;
	}
	
	/**
	 * キースペースの削除
	 */
	public void deleteKeyspace(LoginUser user, String keyspace) {
		Instances instances = serverService.getInstances(user);
		for (Instance instance : instances.getInstanceList()) {
			cassandraService.deleteKeyspace(instance, keyspace);
			// TODO マルチノード対応したときに複数インスタンスを考慮した修正を行う
		}
		// キースペースを削除したら、キースペースマネージャートランも削除
		keyspaceManagerDao.delete(user, keyspace);
	}

}
