package com.dbaas.cassandra.domain.cassandra.keyspace.service;

import com.dbaas.cassandra.domain.cassandra.CassandraService;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlan;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspaces;
import com.dbaas.cassandra.domain.cassandra.keyspace.dto.RegistKeyspaceResultDto;
import com.dbaas.cassandra.domain.message.MessageSourceService;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.table.keyspaceRegistPlan.KeyspaceRegistPlanDao;
import com.dbaas.cassandra.domain.table.keyspaceRegistPlan.KeyspaceRegistPlanEntity;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.shared.validation.ValidateResult;
import com.dbaas.cassandra.shared.validation.Validator;
import com.dbaas.cassandra.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.SmartValidator;

import java.util.ArrayList;
import java.util.List;

import static com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans.createEmptyKeyspaceRegistPlans;
import static com.dbaas.cassandra.domain.message.Message.MSG005E;

@Service
@Transactional
public class KeyspaceService {

	private ServerService serverService;

	private CassandraService cassandraService;

	private KeyspaceRegistPlanDao keyspaceRegistPlanDao;

	private Validator validator;
	
    @Autowired
	public KeyspaceService(ServerService serverService, CassandraService cassandraService
			, KeyspaceRegistPlanDao keyspaceRegistPlanDao, SmartValidator smartValidator
			, MessageSourceService messageSource) {
		this.serverService = serverService;
		this.cassandraService = cassandraService;
		this.keyspaceRegistPlanDao = keyspaceRegistPlanDao;
		this.validator = Validator.getInstance(smartValidator, messageSource);
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

		// 単項目チェック
		ValidateResult validateResult = keyspace.validate(validator);
		RegistKeyspaceResultDto result = new RegistKeyspaceResultDto(validateResult);
		if (validateResult.hasErrors()){
			return result;
		}

		// cassandraからキースペース一覧を取得する
		Instances instances = serverService.getAllInstances(user);
		Keyspaces keyspaces = cassandraService.findAllKeyspace(instances);
		// 引数のキースペースが登録済の場合、エラーとして処理を中断する
		if (keyspaces.hasKeyspace(keyspace)) {
			validateResult.addError(MSG005E);
		}

		// 引数のキースペースでキースペース登録予定を取得する
		KeyspaceRegistPlan keyspaceRegistPlan = keyspaceRegistPlanDao.findByUserIdAndKeyspace(user, keyspace);
		// キースペースが登録予定に登録済であればエラーとする
		if (!keyspaceRegistPlan.isEmpty()) {
			validateResult.addError(MSG005E);
			return result;
		}

		return result;
	}
    
    public void insertKeyspaceRegistPlan(LoginUser user, Keyspace keyspace) {
		keyspaceRegistPlanDao.insert(user, keyspace);
    }

	public void deleteKeyspaceRegistPlan(LoginUser user, Keyspaces keyspaces) {
    	keyspaceRegistPlanDao.delete(user, keyspaces);
	}
}
