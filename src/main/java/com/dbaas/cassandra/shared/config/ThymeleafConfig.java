package com.dbaas.cassandra.shared.config;

import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;

public class ThymeleafConfig {

    @Bean
    public SpringTemplateEngine templateEngine() {
    	SpringTemplateEngine engine = new SpringTemplateEngine();
    	engine.addDialect(new SpringSecurityDialect());
//    	engine.addTemplateResolver(templateResolver());
    	return engine;
    }
}
