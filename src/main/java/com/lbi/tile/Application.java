package com.lbi.tile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@ServletComponentScan
@SpringBootApplication
public class Application{
    public static void main(String[] args){
        SpringApplication app=new SpringApplication(Application.class);
        app.run(args);
    }
}
