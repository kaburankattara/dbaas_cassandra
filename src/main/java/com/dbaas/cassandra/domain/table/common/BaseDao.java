package com.dbaas.cassandra.domain.table.common;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class  BaseDao {
	
    private final EntityManager entityManager;

    @Autowired
    public BaseDao(EntityManager entityManager){
        this.entityManager = entityManager;
    }
	
    /**
     * システム日付を取得
     * 
     * @return システム日付
     */
	public LocalDateTime getSysDate() {
		SysDateEntity entity = (SysDateEntity) entityManager
	            .createNativeQuery("select now(6) as sys_date;", SysDateEntity.class)
	            .getSingleResult();
		return entity.sysDate;
	}
}
