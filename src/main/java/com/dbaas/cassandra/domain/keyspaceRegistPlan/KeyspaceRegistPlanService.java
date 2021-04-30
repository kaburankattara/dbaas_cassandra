package com.dbaas.cassandra.domain.keyspaceRegistPlan;

import static com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlans.createEmptyKeyspaceRegistPlans;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.table.keyspaceManager.KeyspaceManagerDao;
import com.dbaas.cassandra.domain.table.keyspaceManager.KeyspaceManagerEntity;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.utils.ObjectUtils;

@Service
@Transactional
public class KeyspaceRegistPlanService {

	private KeyspaceManagerDao keyspaceManagerDao;
	
    @Autowired
    public KeyspaceRegistPlanService(KeyspaceManagerDao keyspaceManagerDao){
    	this.keyspaceManagerDao = keyspaceManagerDao;
    }
    
    public KeyspaceRegistPlans findKeyspaceRegistPlanByUserId(LoginUser user) {
    	List<KeyspaceManagerEntity> findResultList = keyspaceManagerDao.findByUserId(user.getUserId());
    	
    	if (ObjectUtils.isEmpty(findResultList)) {
    		return createEmptyKeyspaceRegistPlans();
    	}

    	List<KeyspaceRegistPlan> KeyspaceRegistPlanList = new ArrayList<KeyspaceRegistPlan>();
    	for (KeyspaceManagerEntity entity : findResultList) {
    		KeyspaceRegistPlanList.add(new KeyspaceRegistPlan(entity));
    	}
    	return new KeyspaceRegistPlans(KeyspaceRegistPlanList);
    }
    
    public void insert(LoginUser user, String keyspace) {
    	keyspaceManagerDao.insert(user, keyspace);
    }
}
