package com.lbi.tile;

import com.lbi.tile.model.T_TileMap;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


@Controller
@SpringBootApplication
@ComponentScan(basePackages = { "com.lbi.tile" })
@ServletComponentScan
public class Application{


    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }

    public static void main(String[] args) throws IOException{
        Properties prop=new Properties();
        //InputStream in=Application.class.getClassLoader().getResourceAsStream("tile.properties");
        //prop.load(in);

        prop.put("mapserver.host","54.223.166.139");
        prop.put("geoserver.host","localhost");
        //prop.put("tiledata.path","F:/BaiduNetdiskDownload");
        prop.put("tiledata.path","/home/ec2-user/tiledata");
        List<T_TileMap> list=getTileMapList();
        for(int i=0;i<list.size();i++){
            T_TileMap tileMap=list.get(i);
            String key=tileMap.getLayerName()+"@"+tileMap.getSrs()+"@"+tileMap.getExtension();
            prop.put(key,tileMap);
        }
        System.out.println("load TileMap:"+list.size());

        SpringApplication app=new SpringApplication(Application.class);
        app.setDefaultProperties(prop);
        app.run(args);
    }
    private static List<T_TileMap> getTileMapList(){
        List<T_TileMap> list=null;
        try{
            Class.forName("org.postgresql.Driver");
            Connection conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
            String sql="select * from t_tile_map order by id";
            Statement stat=conn.createStatement();
            ResultSet rs=stat.executeQuery(sql);
            list=new ArrayList<>();
            while(rs.next()){
                T_TileMap u=new T_TileMap();
                u.setId(rs.getInt("id"));
                u.setLayerName(rs.getString("layer_name"));
                u.setTitle(rs.getString("title"));
                u.setProfile(rs.getString("profile"));
                u.setSrs(rs.getString("srs"));
                u.setHref(rs.getString("href"));
                u.setUrl(rs.getString("url"));
                u.setSType(rs.getInt("s_type"));
                u.setMinX(rs.getDouble("minx"));
                u.setMinY(rs.getDouble("miny"));
                u.setMaxX(rs.getDouble("maxx"));
                u.setMaxY(rs.getDouble("maxy"));
                u.setOriginX(rs.getDouble("origin_x"));
                u.setOriginY(rs.getDouble("origin_y"));
                u.setTileWidth(rs.getInt("tile_width"));
                u.setTileHeight(rs.getInt("tile_height"));
                u.setMimeType(rs.getString("mime_type"));
                u.setExtension(rs.getString("format_extension"));
                u.setFileExtension(rs.getString("file_extension"));
                list.add(u);
            }
            rs.close();
            stat.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
