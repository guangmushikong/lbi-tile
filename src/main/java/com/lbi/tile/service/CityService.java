package com.lbi.tile.service;

import com.alibaba.fastjson.JSONObject;
import com.lbi.tile.dao.CityDao;
import com.lbi.map.Tile;
import com.lbi.tile.model.Admin_Region;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.wololo.geojson.GeoJSON;
import org.wololo.jts2geojson.GeoJSONWriter;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;

import java.util.*;

@Service("cityService")
public class CityService {
    @Resource(name="cityDao")
    private CityDao cityDao;

    @Value("${tile.gujiao}")
    private String GUJIAO_PATH;
    @Value("${tile.world}")
    private String WORLD_PATH;


    public BufferedImage getGujiao(Tile tile){
        try{
            String fileName= GUJIAO_PATH+File.separator+tile.getZ()+File.separator+tile.getX()+File.separator+tile.getY()+".png";
            File file=new File(fileName);
            if(file.exists())return ImageIO.read(file);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    public BufferedImage getWorld(Tile tile){
        try{
            String fileName= WORLD_PATH+File.separator+tile.getZ()+File.separator+tile.getX()+File.separator+tile.getY()+".jpg";
            File file=new File(fileName);
            if(file.exists())return ImageIO.read(file);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


    public List<Map<String,String>> getCityList(){
        return cityDao.getCityList();
    }
    public List<JSONObject> getCityRegionByTile(Tile tile){
        List<JSONObject> result=new ArrayList<JSONObject>();
        try{
            List<Admin_Region> list=cityDao.getCityRegionList(tile);
            if(list!=null){
                WKTReader wktReader=new WKTReader();
                GeoJSONWriter geoJSONWriter=new GeoJSONWriter();
                for(Admin_Region u:list){
                    Geometry geom=wktReader.read(u.getWkt());
                    GeoJSON geojson=geoJSONWriter.write(geom);
                    JSONObject item=new JSONObject();
                    JSONObject prop=new JSONObject();
                    prop.put("code",u.getCode());
                    prop.put("name",u.getName());
                    item.put("properties",prop);
                    item.put("geometry",geojson);
                    item.put("type","Feature");
                    result.add(item);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
    /*public byte[] getCityMVTByTile(Tile tile){
        try{
            List<Admin_Region> list=cityDao.getCityRegionList(tile);
            if(list!=null){
                Collection<Geometry> geometries=new ArrayList<>();
                for(Admin_Region u:list){
                    HashMap<String, Object> attrs = new HashMap<>();
                    attrs.put("code",Long.parseLong(u.getCode()));
                    attrs.put("name",u.getName());
                    Geometry geoObj = new WKTReader().read(u.getWkt());
                    geoObj.setUserData(attrs);
                    geometries.add(geoObj);
                }
                JtsLayer layer = new JtsLayer("city", geometries);
                JtsMvt mvt = new JtsMvt(singletonList(layer));
                return MvtEncoder.encode(mvt);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    public byte[] getPoiMVTByTile(Tile tile){
        try{
            List<Map<String,Object>> list=cityDao.getPoiList(tile);
            if(list!=null){
                System.out.println(tile.toString()+":"+list.size());
                List<Geometry> geometries=new ArrayList<>();
                for(Map<String,Object> u:list){
                    String wkt=(String)u.get("wkt");
                    Geometry geoObj = new WKTReader().read(wkt);
                    geoObj.setUserData(u);
                    geometries.add(geoObj);
                }
                JtsLayer layer = new JtsLayer("poi", geometries);
                JtsMvt mvt = new JtsMvt(singletonList(layer));
                return MvtEncoder.encode(mvt);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }*/
}
