package com.dbaas.cassandra.domain.keyspaceRegistPlan;

import static com.dbaas.cassandra.domain.keyspaceRegistPlan.KeyspaceRegistPlans.createEmptyKeyspaceRegistPlans;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbaas.cassandra.domain.table.keyspaceRegistPlan.KeyspaceRegistPlanDao;
import com.dbaas.cassandra.domain.table.keyspaceRegistPlan.KeyspaceRegistPlanEntity;
import com.dbaas.cassandra.domain.user.LoginUser;
import com.dbaas.cassandra.utils.ObjectUtils;

@Service
@Transactional
public class KeyspaceRegistPlanService {

	private KeyspaceRegistPlanDao KeyspaceRegistPlanDao;
	
    @Autowired
    public KeyspaceRegistPlanService(KeyspaceRegistPlanDao KeyspaceRegistPlanDao){
    	this.KeyspaceRegistPlanDao = KeyspaceRegistPlanDao;
    }
    
    public KeyspaceRegistPlans findKeyspaceRegistPlanByUserId(LoginUser user) {
    	List<KeyspaceRegistPlanEntity> findResultList = KeyspaceRegistPlanDao.findByUserId(user.getUserId());
    	
    	if (ObjectUtils.isEmpty(findResultList)) {
    		return createEmptyKeyspaceRegistPlans();
    	}

    	List<KeyspaceRegistPlan> KeyspaceRegistPlanList = new ArrayList<KeyspaceRegistPlan>();
    	for (KeyspaceRegistPlanEntity entity : findResultList) {
    		KeyspaceRegistPlanList.add(new KeyspaceRegistPlan(entity));
    	}
    	return new KeyspaceRegistPlans(KeyspaceRegistPlanList);
    }
    
    public void insert(LoginUser user, String keyspace) {
		KeyspaceRegistPlanDao.insert(user, keyspace);
    }
}
