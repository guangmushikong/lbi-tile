package com.lbi.tile;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@SpringBootApplication
@PropertySource(value = {"classpath:tile.properties"})
@ComponentScan(basePackages = { "com.lbi.tile" })
public class Application{
    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
