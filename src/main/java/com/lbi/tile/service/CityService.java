package com.lbi.tile.service;


import com.lbi.tile.dao.CityDao;

import org.springframework.stereotype.Service;


import javax.annotation.Resource;

import java.util.*;

@Service("cityService")
public class CityService {
    @Resource(name="cityDao")
    private CityDao cityDao;


    public List<Map<String,String>> getCityList(){
        return cityDao.getCityList();
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
