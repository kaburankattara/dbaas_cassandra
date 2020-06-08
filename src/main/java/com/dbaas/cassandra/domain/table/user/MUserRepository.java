package com.dbaas.cassandra.domain.table.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MUserRepository extends JpaRepository<UserEntity, String> {

}
