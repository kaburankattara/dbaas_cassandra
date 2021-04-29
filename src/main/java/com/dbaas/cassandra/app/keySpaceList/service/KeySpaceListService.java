package com.dbaas.cassandra.app.keySpaceList.service;

import static com.dbaas.cassandra.domain.sysDate.SysDateContext.getSysDate;

import java.util.List;

import com.dbaas.cassandra.domain.endPoint.EndPointService;
import com.dbaas.cassandra.shared.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.app.keySpaceList.dto.KeySpaceListInitServiceResultDto;
import com.dbaas.cassandra.app.keySpaceList.service.async.KeySpaceListAsyncService;
import com.dbaas.cassandra.app.keySpaceList.service.bean.KeySpaceListInitService;
import com.dbaas.cassandra.domain.cassandra.CassandraService;
import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlanService;
import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class  KeySpaceListService {

	private KeySpaceListInitService keySpaceListInitService;

	private KeySpaceListAsyncService keySpaceListAsyncService;
	
	private KeyspaceRegistPlanService keyspaceRegistPlanService;

	private EndPointService endPointService;
	
	@Autowired
	KeySpaceListService(KeySpaceListInitService keySpaceListInitService, KeySpaceListAsyncService keySpaceListAsyncService, CassandraService cassandraManagerService,
			KeyspaceRegistPlanService keyspaceRegistPlanService, EndPointService endPointService) {
		this.keySpaceListInitService = keySpaceListInitService;
		this.keySpaceListAsyncService = keySpaceListAsyncService;
		this.keyspaceRegistPlanService = keyspaceRegistPlanService;
		this.endPointService = endPointService;
	}
	
	/**
	 * キースペース一覧画面の初期表示用処理
	 * 
	 * @param user
	 * @return キースペースリスト
	 * @throws Exception 
	 */
	public KeySpaceListInitServiceResultDto init(LoginUser user) throws Exception {
		// 画面の一覧表示用にキースペースマネージャに登録されているキースペースリストを取得
		KeyspaceRegistPlans keyspaceRegistPlans = keyspaceRegistPlanService.findKeyspaceRegistPlanByUserId(user);

		// cassandraサーバに登録済のキースペースリストを取得する
		List<String> createdKeyspaceList = keySpaceListInitService.findCreatedKeyspaceList(user);
		
		// cassandraサーバの起動状況をリフレッシュする
		try {
			keySpaceListAsyncService.refreshCassandra(user, keyspaceRegistPlans, getSysDate());
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			throw new SystemException();
		}

		String endPoint = endPointService.getEndPoint(user);

		return new KeySpaceListInitServiceResultDto(keyspaceRegistPlans, createdKeyspaceList, endPoint);
	}
}
