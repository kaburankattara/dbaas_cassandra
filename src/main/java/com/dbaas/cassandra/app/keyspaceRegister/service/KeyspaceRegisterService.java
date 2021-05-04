package com.dbaas.cassandra.app.keyspaceRegister.service;

import com.dbaas.cassandra.app.keyspaceRegister.service.async.KeyspaceRegisterAsyncService;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspace;
import com.dbaas.cassandra.domain.cassandra.keyspace.dto.RegistKeyspaceResultDto;
import com.dbaas.cassandra.domain.cassandra.keyspace.service.KeyspaceService;
import com.dbaas.cassandra.domain.user.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dbaas.cassandra.domain.sysDate.SysDateContext.getSysDate;

@Service
@Transactional
public class  KeyspaceRegisterService {
	
	private KeyspaceRegisterAsyncService asyncService;
	
	private KeyspaceService keyspaceService;
	
	@Autowired
	KeyspaceRegisterService(KeyspaceRegisterAsyncService asyncService, KeyspaceService keyspaceService) {
		this.asyncService = asyncService;
		this.keyspaceService = keyspaceService;
	}
	
	/**
	 * キースペースを登録する
	 *
	 * @param user ユーザー
	 * @param keyspace キースペース
	 * @return 登録の成功判定S
	 */
	public RegistKeyspaceResultDto registKeyspace(LoginUser user, Keyspace keyspace) {
		// 登録用にチェックする
		RegistKeyspaceResultDto validateResult = keyspaceService.validateForRegist(user, keyspace);
		// チェック結果がエラーの場合、処理を中断する
		if (validateResult.hasError()) {
			return validateResult;
		}

		// 引数のキースペースをキースペース登録予定に登録
		keyspaceService.insertKeyspaceRegistPlan(user, keyspace);
		
		// cassandraにキースペースを登録する
		// ※サーバ構築が必要となる場合があるため、非同期で行う
		asyncService.registKeyspace(user, keyspace, getSysDate());
		return validateResult;
	}
}
