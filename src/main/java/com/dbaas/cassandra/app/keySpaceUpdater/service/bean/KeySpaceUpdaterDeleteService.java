package com.dbaas.cassandra.app.keySpaceUpdater.service.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.cassandra.CassandraManagerService;
import com.dbaas.cassandra.domain.serverManager.ServerManagerService;
import com.dbaas.cassandra.domain.serverManager.instance.Instance;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;
import com.dbaas.cassandra.domain.table.keyspaceManager.KeyspaceManagerDao;
import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class  KeySpaceUpdaterDeleteService {
	
	private ServerManagerService serverManagerService;
	
	private CassandraManagerService cassandraManagerService;
	
	private KeyspaceManagerDao keyspaceManagerDao;

	@Autowired
	KeySpaceUpdaterDeleteService(ServerManagerService serverManagerService, CassandraManagerService cassandraManagerService, KeyspaceManagerDao keyspaceManagerDao) {
		this.serverManagerService = serverManagerService;
		this.cassandraManagerService = cassandraManagerService;
		this.keyspaceManagerDao = keyspaceManagerDao;
	}
	
	/**
	 * キースペースの削除
	 */
	public void deleteKeySpace(LoginUser user, String keySpace) {
		Instances instances = serverManagerService.getInstances(user);
		for (Instance instance : instances.getInstanceList()) {
			cassandraManagerService.deleteKeySpace(instance, keySpace);
			// TODO マルチノード対応したときに複数インスタンスを考慮した修正を行う
		}
		// キースペースを削除したら、キースペースマネージャートランも削除
		keyspaceManagerDao.delete(user, keySpace);
	}

}