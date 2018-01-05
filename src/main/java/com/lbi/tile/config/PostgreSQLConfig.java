package com.lbi.tile.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class PostgreSQLConfig {
    @Value("${spring.datasource.url}")
    String jdbc_url;
    @Value("${spring.datasource.username}")
    String jdbc_username;
    @Value("${spring.datasource.password}")
    String jdbc_password;

    @Bean(name = "dataSource")
    public BasicDataSource getDataSource() {
        try {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl(jdbc_url);
            dataSource.setUsername(jdbc_username);
            dataSource.setPassword(jdbc_password);
            dataSource.setMinIdle(10);
            dataSource.setMaxIdle(100);
            dataSource.setInitialSize(10);
            dataSource.setMaxActive(100);
            return dataSource;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate getJdbcTemplate(){
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        jdbcTemplate.setDataSource(getDataSource());
        return jdbcTemplate;
    }
}
