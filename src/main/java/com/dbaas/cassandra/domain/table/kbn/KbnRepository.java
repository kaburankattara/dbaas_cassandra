package com.dbaas.cassandra.domain.table.kbn;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KbnRepository extends JpaRepository<KbnEntity, KbnEntityKey> {

	@Query(value = "select * from m_kbn where type_cd = :typeCd and yuko_flg = true order by hyojijun", nativeQuery = true)
	List<KbnEntity> findByTypeCd(@Param("typeCd") String typeCd);

}
