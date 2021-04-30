package com.dbaas.cassandra.app.keyspaceList.service.async;

import static com.dbaas.cassandra.domain.sysDate.SysDateContext.setSysDate;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.app.keyspaceList.service.bean.KeyspaceListInitService;
import com.dbaas.cassandra.domain.cassandra.keyspace.KeyspaceRegistPlans;
import com.dbaas.cassandra.domain.user.LoginUser;

@Async()
@Service
@Transactional
public class KeyspaceListAsyncService {
	
	private KeyspaceListInitService keyspaceListInitService;

	@Autowired
	KeyspaceListAsyncService(KeyspaceListInitService keyspaceListInitService) {
		this.keyspaceListInitService = keyspaceListInitService;
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
			keyspaceListInitService.refreshCassandra(user, keyspaceRegistPlans);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
			throw new Exception();
		}
	}
}
