package com.lbi.tile.config;

import com.aliyun.oss.OSSClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

@Order(1)
@Configuration
@Slf4j
public class RootConfig {
    @Autowired
    Environment env;

    @Bean(name = "jdbcTemplate")
    public JdbcTemplate getJdbcTemplate(){
        JdbcTemplate jdbcTemplate=new JdbcTemplate();
        try{
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
            dataSource.setUrl(env.getProperty("spring.datasource.url"));
            dataSource.setUsername(env.getProperty("spring.datasource.username"));
            dataSource.setPassword(env.getProperty("spring.datasource.password"));
            dataSource.setMinIdle(10);
            dataSource.setMaxIdle(100);
            dataSource.setInitialSize(10);
            dataSource.setMaxActive(100);
            jdbcTemplate.setDataSource(dataSource);
            log.info("init jdbcTemplate");
        }catch (Exception e){
            e.printStackTrace();
        }
        return jdbcTemplate;
    }


    @Bean(name = "ossClient")
    public OSSClient getOSSClient(){
        log.info("init ossClient");
        return new OSSClient(
                env.getProperty("oss.endpoint"),
                env.getProperty("oss.accessKeyId"),
                env.getProperty("oss.accessKeySecret"));
    }

    @Bean(name = "coverage_gujiao")
    public GridCoverage2D getGridCoverage2D_gujiao(){
        GridCoverage2D coverage=null;
        String localPath=env.getProperty("dem.gujiao");
        try{
            GeoTiffReader tifReader = new GeoTiffReader(localPath);
            coverage = tifReader.read(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("init coverage_gujiao");
        return coverage;
    }
    @Bean(name = "coverage_jingzhuang")
    public GridCoverage2D getGridCoverage2D_jingzhuang(){
        GridCoverage2D coverage=null;
        String localPath=env.getProperty("dem.jingzhuang");
        try{
            GeoTiffReader tifReader = new GeoTiffReader(localPath);
            coverage = tifReader.read(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("init coverage_jingzhuang");
        return coverage;
    }

    @Bean(name = "myConfig")
    public MyConfig getMyConfig(){
        JdbcTemplate jdbcTemplate=getJdbcTemplate();
        MyConfig myConfig=new MyConfig(jdbcTemplate);
        log.info("init myConfig");
        return myConfig;
    }
}
