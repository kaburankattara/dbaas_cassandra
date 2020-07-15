package com.dbaas.cassandra.app.keySpaceList.service;

import static com.dbaas.cassandra.domain.sysDate.SysDateContext.getSysDate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.app.keySpaceList.dto.KeySpaceListInitServiceResultDto;
import com.dbaas.cassandra.app.keySpaceList.service.async.KeySpaceListAsyncService;
import com.dbaas.cassandra.app.keySpaceList.service.bean.KeySpaceListInitService;
import com.dbaas.cassandra.domain.auth.LoginUser;
import com.dbaas.cassandra.domain.cassandra.CassandraManagerService;
import com.dbaas.cassandra.domain.table.keyspaceManager.KeyspaceManagerDao;

@Service
@Transactional
public class  KeySpaceListService {

	private KeySpaceListInitService keySpaceListInitService;

	private KeySpaceListAsyncService keySpaceListAsyncService;
	
	private KeyspaceManagerDao keyspaceManagerDao;
	
	@Autowired
	KeySpaceListService(KeySpaceListInitService keySpaceListInitService, KeySpaceListAsyncService keySpaceListAsyncService, CassandraManagerService cassandraManagerService,
			KeyspaceManagerDao keyspaceManagerDao) {
		this.keySpaceListInitService = keySpaceListInitService;
		this.keySpaceListAsyncService = keySpaceListAsyncService;
		this.keyspaceManagerDao = keyspaceManagerDao;
	}
	
	/**
	 * キースペース一覧画面の初期表示用処理
	 * 
	 * @param user
	 * @return キースペースリスト
	 */
	public KeySpaceListInitServiceResultDto init(LoginUser user) {
		// 画面の一覧表示用にキースペースマネージャに登録されているキースペースリストを取得
		List<String> keyspaceList = keyspaceManagerDao.findAllKeyspaceByUserId(user.getUserId());

		// cassandraサーバがCQL操作可能か判定
		List<String> createdKeyspaceList = keySpaceListInitService.findCreatedKeyspaceList(user);
		
		// cassandraサーバの起動状況をリフレッシュする
		keySpaceListAsyncService.refreshCassandra(user, keyspaceList, getSysDate());
		
		return new KeySpaceListInitServiceResultDto(keyspaceList, createdKeyspaceList);
	}
}
