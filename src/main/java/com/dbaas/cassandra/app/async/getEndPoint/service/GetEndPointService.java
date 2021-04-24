package com.dbaas.cassandra.app.async.getEndPoint.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.endPoint.EndPointService;
import com.dbaas.cassandra.domain.user.LoginUser;

@Service
@Transactional
public class GetEndPointService {

	private EndPointService endPointService;

	@Autowired
	GetEndPointService(EndPointService endPointService) {
		this.endPointService = endPointService;
	}

	/**
	 * エンドポイントを取得する
	 *
	 * @param user ユーザー
	 * @return エンドポイント
	 */
	public String getEndPoint(LoginUser user) {
		return endPointService.getEndPoint(user);
	}
}
