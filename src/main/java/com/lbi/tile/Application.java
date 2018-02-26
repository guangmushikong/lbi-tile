package com.lbi.tile;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import java.io.IOException;

@Controller
@SpringBootApplication
@ComponentScan(basePackages = { "com.lbi.tile" })
@ServletComponentScan
public class Application{
    public static void main(String[] args) throws IOException{
        /*Properties prop=new Properties();

        prop.put("mapserver.host","http://39.107.104.63:8080");
        prop.put("geoserver.host","http://localhost:8888");
        //prop.put("tiledata.path","F:/BaiduNetdiskDownload");
        //prop.put("tiledata.path","/opt/tiledata");
        List<TileMap> list=getTileMapList();
        for(int i=0;i<list.size();i++){
            TileMap tileMap=list.get(i);
            String key=tileMap.getTitle()+"@"+tileMap.getSrs()+"@"+tileMap.getExtension();
            prop.put(key,tileMap);
        }
        System.out.println("load TileMap:"+list.size());*/

        SpringApplication app=new SpringApplication(Application.class);
        //app.setDefaultProperties(prop);
        app.run(args);
    }
}
