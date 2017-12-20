package com.lbi.tile.service;

import com.alibaba.fastjson.JSONObject;
import com.lbi.map.Tile;
import com.lbi.tile.dao.CityDao;
import com.lbi.tile.model.Admin_Region;
import com.lbi.util.ImageUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.wololo.geojson.GeoJSON;
import org.wololo.jts2geojson.GeoJSONWriter;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


@Service("xyzService")
public class XYZService {
    @Resource(name="cityDao")
    private CityDao cityDao;

    @Value("${tile.gujiao}")
    private String GUJIAO_PATH;
    @Value("${tile.world}")
    private String WORLD_PATH;
    @Value("${tile.dem}")
    private String DEM_PATH;

    public byte[] getGujiao(Tile tile){
        try{
            String fileName= GUJIAO_PATH+ File.separator+tile.getZ()+File.separator+tile.getX()+File.separator+tile.getY()+".png";
            File file=new File(fileName);
            if(file.exists()){
                BufferedImage image=ImageIO.read(file);
                if(image!=null)return ImageUtil.toByteArray(image);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    public byte[] getWorld(Tile tile){
        try{
            String fileName= WORLD_PATH+File.separator+tile.getZ()+File.separator+tile.getX()+File.separator+tile.getY()+".jpg";
            File file=new File(fileName);
            if(file.exists()){
                BufferedImage image=ImageIO.read(file);
                if(image!=null)return ImageUtil.toByteArray(image);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    public byte[] getDEM(Tile tile){
        try{
            String fileName= DEM_PATH+File.separator+tile.getZ()+File.separator+tile.getX()+File.separator+tile.getY()+".tif";
            File file=new File(fileName);
            if(file.exists()){
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
                byte[] b = new byte[1000];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                fis.close();
                bos.close();
                return bos.toByteArray();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
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

}
