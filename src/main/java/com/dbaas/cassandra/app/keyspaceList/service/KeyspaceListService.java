package com.dbaas.cassandra.app.keyspaceList.service;

import static com.dbaas.cassandra.domain.sysDate.SysDateContext.getSysDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.app.keyspaceList.dto.KeyspaceListInitServiceResultDto;
import com.dbaas.cassandra.app.keyspaceList.service.async.KeyspaceListAsyncService;
import com.dbaas.cassandra.app.keyspaceList.service.bean.KeyspaceListInitService;
import com.dbaas.cassandra.domain.cassandra.CassandraService;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.cassandra.keyspace.Keyspaces;
import com.dbaas.cassandra.domain.cassandra.keyspace.service.KeyspaceService;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.shared.exception.SystemException;

@Service
@Transactional
public class KeyspaceListService {

	private KeyspaceListInitService keyspaceListInitService;

	private KeyspaceListAsyncService keyspaceListAsyncService;
	
	private KeyspaceService keyspaceService;
	
	@Autowired
	KeyspaceListService(KeyspaceListInitService keyspaceListInitService, KeyspaceListAsyncService keyspaceListAsyncService, CassandraService cassandraManagerService,
						KeyspaceService keyspaceService) {
		this.keyspaceListInitService = keyspaceListInitService;
		this.keyspaceListAsyncService = keyspaceListAsyncService;
		this.keyspaceService = keyspaceService;
	}
	
	/**
	 * キースペース一覧画面の初期表示用処理
	 * 
	 * @param user
	 * @return キースペースリスト
	 * @throws Exception 
	 */
	public KeyspaceListInitServiceResultDto init(LoginUser user) throws Exception {
		// 画面の一覧表示用にキースペースマネージャに登録されているキースペースリストを取得
		KeyspaceRegistPlans keyspaceRegistPlans = keyspaceService.findKeyspaceRegistPlanByUserId(user);

		// cassandraサーバに登録済のキースペースリストを取得する
		Keyspaces createdKeyspaces = keyspaceListInitService.findCreatedKeyspaceList(user);
		
		// cassandraサーバの起動状況をリフレッシュする
		try {
			keyspaceListAsyncService.refreshCassandra(user, keyspaceRegistPlans, getSysDate());
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			throw new SystemException();
		}

		return new KeyspaceListInitServiceResultDto(keyspaceRegistPlans, createdKeyspaces);
	}
}
