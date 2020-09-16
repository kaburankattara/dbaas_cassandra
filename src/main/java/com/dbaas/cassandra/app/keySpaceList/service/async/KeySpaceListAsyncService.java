package com.dbaas.cassandra.app.keySpaceList.service.async;

import static com.dbaas.cassandra.domain.sysDate.SysDateContext.setSysDate;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.app.keySpaceList.service.bean.KeySpaceListInitService;
import com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.user.LoginUser;

@Async()
@Service
@Transactional
public class  KeySpaceListAsyncService {
	
	private KeySpaceListInitService keySpaceListInitService;

	
	@Autowired
	KeySpaceListAsyncService(KeySpaceListInitService keySpaceListInitService) {
		this.keySpaceListInitService = keySpaceListInitService;
	}
	
	/**
	 * cassandraの稼働状況をリフレッシュする
	 * 
	 * @param user
	 * @param keyspaceRegistPlans
	 * @param sysDate
	 * @throws Exception 
	 */
	public void refreshCassandra(LoginUser user, KeyspaceRegistPlans keyspaceRegistPlans, LocalDateTime sysDate) throws Exception {
		try {
			setSysDate(sysDate);
			keySpaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			throw new Exception();
		}
	}
}
