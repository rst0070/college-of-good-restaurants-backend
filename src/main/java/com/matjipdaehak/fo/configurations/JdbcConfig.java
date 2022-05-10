package com.matjipdaehak.fo.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class JdbcConfig {

    /**
     * mysql server와의 접속생성
     * @return DataSource
     */
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/matjipdaehak");
        dataSource.setUsername("rst");
        dataSource.setPassword("wonbinkim");
        return dataSource;
    }
}
