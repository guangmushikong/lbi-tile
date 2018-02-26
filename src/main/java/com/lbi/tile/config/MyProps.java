package com.lbi.tile.config;

import com.lbi.tile.model.TileMap;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
//@ConfigurationProperties(prefix="service") //接收application.yml中的service下面的属性
public class MyProps {
    private String mapserver;
    private String geoserver;
    private String tiledata;
    private Map<String, TileMap> tileMaps = new HashMap<>();
    private Map<String, TileMap> xyzMaps = new HashMap<>();

    public TileMap getTileMap(String key){
        if(key==null || tileMaps.isEmpty())return null;
        return tileMaps.get(key);
    }
    public Map<String, TileMap> getTileMapList() {
        return this.tileMaps;
    }
    public void setTileMapList(Map<String, TileMap> val){
        this.tileMaps=val;
    }

    public TileMap getXYZMap(String key){
        if(key==null || xyzMaps.isEmpty())return null;
        return xyzMaps.get(key);
    }
    public Map<String, TileMap> getXYZMapList() {
        return this.xyzMaps;
    }
    public void setXYZMapList(Map<String, TileMap> val){
        this.xyzMaps=val;
    }

    public String getMapServer(){
        return this.mapserver;
    }
    public void setMapServer(String val){
        this.mapserver=val;
    }

    public String getGeoServer(){
        return this.geoserver;
    }
    public void setGeoServer(String val){
        this.geoserver=val;
    }
    public String getTiledata(){
        return this.tiledata;
    }
    public void setTiledata(String val){
        this.tiledata=val;
    }
}
