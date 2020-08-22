package com.dbaas.cassandra.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

public class DatasourceConfig {

    @Bean
	public DataSource dataSource() {
		return new TransactionAwareDataSourceProxy(
			DataSourceBuilder
			.create()
			.username("root")
			.password("password")
			.url("jdbc:mysql://13.231.121.197/dbaas?nullCatalogMeansCurrent=true")
			.driverClassName("com.mysql.cj.jdbc.Driver")
			.build());
	}
}
