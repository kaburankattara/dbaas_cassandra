package com.dbaas.cassandra.app.keySpaceUpdater.service.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.cassandra.CassandraManagerService;
import com.dbaas.cassandra.domain.serverManager.ServerManagerService;
import com.dbaas.cassandra.domain.serverManager.instance.Instance;
import com.dbaas.cassandra.domain.serverManager.instance.Instances;

@Service
@Transactional
public class  KeySpaceUpdaterDeleteService {
	
	private ServerManagerService serverManagerService;
	
	private CassandraManagerService cassandraManagerService;
	
	@Autowired
	KeySpaceUpdaterDeleteService(ServerManagerService serverManagerService, CassandraManagerService cassandraManagerService) {
		this.serverManagerService = serverManagerService;
		this.cassandraManagerService = cassandraManagerService;
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
	}

}
