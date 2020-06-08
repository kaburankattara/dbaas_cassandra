package com.dbaas.cassandra.domain.table.serverByUser;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerByUserRepository extends JpaRepository<ServerByUserEntity, ServerByUserEntityKey> {

	List<ServerByUserEntity> findByUserId(String userId);
}
