package com.dbaas.cassandra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DbaasCassandraApplication {

    public static void main(String[] args) {
        SpringApplication.run(DbaasCassandraApplication.class, args);
    }

}
