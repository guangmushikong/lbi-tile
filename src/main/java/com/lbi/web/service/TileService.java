package com.lbi.web.service;


import com.alibaba.fastjson.JSONObject;
import com.lbi.web.dao.TileDao;
import com.lbi.web.model.*;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import org.springframework.stereotype.Service;
import org.wololo.geojson.GeoJSON;
import org.wololo.jts2geojson.GeoJSONWriter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("tileService")
public class TileService {
    @Resource(name="tileDao")
    private TileDao tileDao;

    public List<JSONObject> getCityRegionByTile(Tile tile){
        List<JSONObject> result=new ArrayList<JSONObject>();
        try{
            List<Admin_Region> list=tileDao.getCityRegionList(tile);
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
}
