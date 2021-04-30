package com.dbaas.cassandra.domain.table.keyspaceRegistPlan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyspaceRegistPlanRepository extends JpaRepository<KeyspaceRegistPlanEntity, KeyspaceRegistPlanEntityKey> {

	List<KeyspaceRegistPlanEntity> findByUserId(String userId);
}
