package com.dbaas.cassandra.domain.table.keyspaceManager;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyspaceManagerRepository extends JpaRepository<KeyspaceManagerEntity, KeyspaceManagerEntityKey> {

	List<KeyspaceManagerEntity> findByUserId(String userId);
}
