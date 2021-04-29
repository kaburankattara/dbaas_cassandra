package com.dbaas.cassandra.app.keySpaceRegister.service;

import static com.dbaas.cassandra.domain.sysDate.SysDateContext.getSysDate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.app.keySpaceRegister.service.async.KeySpaceRegisterAsyncService;
import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlanService;
import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class  KeySpaceRegisterService {
	
	private KeySpaceRegisterAsyncService asyncService;
	
	private KeyspaceRegistPlanService keyspaceRegistPlanService;
	
	@Autowired
	KeySpaceRegisterService(KeySpaceRegisterAsyncService asyncService, KeyspaceRegistPlanService keyspaceRegistPlanService) {
		this.asyncService = asyncService;
		this.keyspaceRegistPlanService = keyspaceRegistPlanService;
	}
	
	/**
	 * キースペース一覧の取得
	 * 
	 * @param user
	 * @return
	 */
	public List<String> findAllKeyspace(LoginUser user) {
		return keyspaceRegistPlanService.findKeyspaceRegistPlanByUserId(user).getKeyspaceList();
	}
	
	/**
	 * キースペースを登録する
	 */
	public void registKeySpace(LoginUser user, String keySpace) {
		// 登録するキースペースをキースペースマネージャーテーブルに登録
		keyspaceRegistPlanService.insert(user, keySpace);
		
		// サーバ構築が必要となる場合があるため、
		// 物理的なキースペースの登録は非同期で行う
		asyncService.registKeySpace(user, keySpace, getSysDate());
	}
}
