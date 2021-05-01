package com.dbaas.cassandra.domain.cassandra.keyspace.service;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlan;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspaces;
import com.dbaas.cassandra.domain.cassandra.keyspace.dto.RegistKeyspaceResultDto;
import com.dbaas.cassandra.domain.cassandra.keyspace.service.bean.KeyspaceDeleteService;
import com.dbaas.cassandra.domain.cassandra.keyspace.service.bean.KeyspaceFindService;
import com.dbaas.cassandra.domain.cassandra.keyspace.service.bean.KeyspaceRegistService;
import com.dbaas.cassandra.domain.cassandra.keyspace.service.bean.KeyspaceValidateService;
import com.dbaas.cassandra.domain.server.instance.Instance;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.table.keyspaceRegistPlan.KeyspaceRegistPlanDao;
import com.dbaas.cassandra.domain.table.keyspaceRegistPlan.KeyspaceRegistPlanEntity;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans.createEmptyKeyspaceRegistPlans;

@Service
@Transactional
public class KeyspaceService {

	private KeyspaceFindService keyspaceFindService;

	private KeyspaceRegistService keyspaceRegistService;

	private KeyspaceDeleteService keyspaceDeleteService;

	private KeyspaceValidateService keyspaceValidateService;

	private KeyspaceRegistPlanDao keyspaceRegistPlanDao;

    @Autowired
	public KeyspaceService(KeyspaceFindService keyspaceFindService, KeyspaceRegistService keyspaceRegistService
			, KeyspaceDeleteService keyspaceDeleteService, KeyspaceValidateService keyspaceValidateService
			, KeyspaceRegistPlanDao keyspaceRegistPlanDao) {
		this.keyspaceFindService = keyspaceFindService;
		this.keyspaceRegistService = keyspaceRegistService;
		this.keyspaceDeleteService = keyspaceDeleteService;
		this.keyspaceValidateService = keyspaceValidateService;
		this.keyspaceRegistPlanDao = keyspaceRegistPlanDao;
	}
    
    public KeyspaceRegistPlans findKeyspaceRegistPlanByUserId(LoginUser user) {
    	List<KeyspaceRegistPlanEntity> findResultList = keyspaceRegistPlanDao.findByUserId(user.getUserId());
    	
    	if (ObjectUtils.isEmpty(findResultList)) {
    		return createEmptyKeyspaceRegistPlans();
    	}

    	List<KeyspaceRegistPlan> KeyspaceRegistPlanList = new ArrayList<KeyspaceRegistPlan>();
    	for (KeyspaceRegistPlanEntity entity : findResultList) {
    		KeyspaceRegistPlanList.add(KeyspaceRegistPlan.createInstance(entity));
    	}
    	return new KeyspaceRegistPlans(KeyspaceRegistPlanList);
    }

	public RegistKeyspaceResultDto validateForRegist(LoginUser user, Keyspace keyspace) {
		return keyspaceValidateService.validateForRegist(user, keyspace);
	}
    
    public void insertKeyspaceRegistPlan(LoginUser user, Keyspace keyspace) {
		keyspaceRegistPlanDao.insert(user, keyspace);
    }

	public void deleteKeyspaceRegistPlan(LoginUser user, Keyspaces keyspaces) {
    	keyspaceRegistPlanDao.delete(user, keyspaces);
	}

	public Keyspaces findAllKeyspaceWithoutSysKeyspace(Instances instances) {
		return keyspaceFindService.findAllKeyspaceWithoutSysKeyspace(instances);
	}

	public Keyspaces findAllKeyspaceWithoutSysKeyspace(Instance instance) {
		return keyspaceFindService.findAllKeyspaceWithoutSysKeyspace(instance);
	}

	public Keyspaces findAllKeyspace(Instances instances) {
		return keyspaceFindService.findAllKeyspace(instances);
	}

	public Keyspaces findAllKeyspace(Instance instance) {
		return keyspaceFindService.findAllKeyspace(instance);
	}

	public void registKeyspace(Instances instances, Keyspace keyspace) {
		keyspaceRegistService.registKeyspace(instances, keyspace);
	}

	/**
	 * 重複は無視してキースペースを登録
	 *
	 * @param instances
	 * @param keyspaceRegistPlans
	 */
	public void registKeyspaceByDuplicatIgnore(Instances instances, KeyspaceRegistPlans keyspaceRegistPlans) {
		keyspaceRegistService.registKeyspaceByDuplicatIgnore(instances, keyspaceRegistPlans);
	}

	public void registKeyspaceByDuplicatIgnore(Instances instances, Keyspace keyspace) {
		keyspaceRegistService.registKeyspaceByDuplicatIgnore(instances, keyspace);
	}

	/**
	 * 重複は無視してキースペースを登録
	 *
	 * @param instance
	 * @param keyspace
	 */
	public void registKeyspaceByDuplicatIgnore(Instance instance, Keyspace keyspace) {
		keyspaceRegistService.registKeyspaceByDuplicatIgnore(instance, keyspace);
	}

	public void registKeyspace(Instance instance, Keyspace keyspace) {
		keyspaceRegistService.registKeyspace(instance, keyspace);
	}

	public void deleteKeyspace(Instance instance, Keyspace keyspace) {
		keyspaceDeleteService.deleteKeyspace(instance, keyspace);
	}
}
