package com.dbaas.cassandra.domain.cassandra.keyspace.service.bean;

import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlan;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspaces;
import com.dbaas.cassandra.domain.cassandra.keyspace.dto.RegistKeyspaceResultDto;
import com.dbaas.cassandra.domain.message.MessageSourceService;
import com.dbaas.cassandra.domain.server.ServerService;
import com.dbaas.cassandra.domain.server.instance.Instances;
import com.dbaas.cassandra.domain.table.keyspaceRegistPlan.KeyspaceRegistPlanDao;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.shared.validation.ValidateResult;
import com.dbaas.cassandra.shared.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.SmartValidator;

import static com.dbaas.cassandra.domain.message.Message.MSG005E;

@Service
@Transactional
public class KeyspaceValidateService {

	private ServerService serverService;

	private KeyspaceFindService keyspaceFindService;

	private KeyspaceRegistPlanDao keyspaceRegistPlanDao;

	private Validator validator;

    @Autowired
	public KeyspaceValidateService(ServerService serverService, KeyspaceFindService keyspaceFindService
			, KeyspaceRegistPlanDao keyspaceRegistPlanDao, SmartValidator smartValidator
			, MessageSourceService messageSource) {
    	this.serverService = serverService;
    	this.keyspaceFindService = keyspaceFindService;
		this.keyspaceRegistPlanDao = keyspaceRegistPlanDao;
		this.validator = Validator.getInstance(smartValidator, messageSource);
	}

	public RegistKeyspaceResultDto validateForRegist(LoginUser user, Keyspace keyspace) {

		// 単項目チェック
		ValidateResult validateResult = keyspace.validate(validator);
		RegistKeyspaceResultDto result = new RegistKeyspaceResultDto(validateResult);
		if (validateResult.hasErrors()){
			return result;
		}

		// 引数のキースペースでキースペース登録予定を取得する
		KeyspaceRegistPlan keyspaceRegistPlan = keyspaceRegistPlanDao.findByUserIdAndKeyspace(user, keyspace);
		// キースペースが登録予定に登録済であればエラーとする
		if (!keyspaceRegistPlan.isEmpty()) {
			validateResult.addError(MSG005E);
			return result;
		}

		// cassandraからキースペース一覧を取得する
		// キーペース登録時点でcassandraサーバが起動していない場合、キースペースは空とする
		Instances instances = serverService.getInstances(user);
		Keyspaces keyspaces = Keyspaces.createEmptyInstance();
		try {
			keyspaces = keyspaceFindService.findAllKeyspaceNoRetry(instances);
		} catch (Exception e) {
			// 処理無し
		}

		// 引数のキースペースが登録済の場合、エラーとして処理を中断する
		if (keyspaces.hasKeyspace(keyspace)) {
			validateResult.addError(MSG005E);
		}

		return result;
	}
}
