package com.lbi.tile.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lbi.map.Tile;
import com.lbi.tile.dao.CityDao;
import com.lbi.tile.model.Admin_Region;
import com.lbi.tile.model.T_TileMap;
import com.lbi.util.ImageUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.wololo.geojson.GeoJSON;
import org.wololo.jts2geojson.GeoJSONWriter;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;


@Service("xyzService")
public class XYZService {
    @Resource(name="cityDao")
    private CityDao cityDao;
    @Autowired
    private Environment env;

    public byte[] getXYZ_Tile(String layerName,String extension,Tile tile){
        String tileset=layerName+"@EPSG:900913@"+extension;
        T_TileMap tileMap=env.getProperty(tileset,T_TileMap.class);
        if(tileMap==null)return null;
        if(tileMap.getSType()==1){
            return getRemoteTile(tileMap,tile);
        }else if(tileMap.getSType()==2){
            return getCacheTile(tileMap,tile);
        }
        return null;
    }

    private byte[] getRemoteTile(T_TileMap tileMap,Tile tile){
        StringBuilder sb=new StringBuilder();
        sb.append(tileMap.getUrl());
        sb.append("/"+tile.getZ());
        sb.append("/"+tile.getX());
        sb.append("/"+tile.getY()+"."+tileMap.getExtension());
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet httpGet = new HttpGet(sb.toString());
        try {
            // 3.发送Get请求
            CloseableHttpResponse res = httpClient.execute(httpGet);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                //ContentType contentType = ContentType.getOrDefault(entity);
                //Charset charset = contentType.getCharset();
                // 获取字节数组
                byte[] content = EntityUtils.toByteArray(entity);
                return content;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    private byte[] getCacheTile(T_TileMap tileMap,Tile tile){
        try{
            StringBuilder sb=new StringBuilder();
            sb.append(env.getProperty("tiledata.path"));
            sb.append(File.separator).append(tileMap.getLayerName());
            sb.append(File.separator).append(tile.getZ());
            sb.append(File.separator).append(tile.getX());
            sb.append(File.separator).append(tile.getY());
            sb.append(".").append(tileMap.getFileExtension());
            File file=new File(sb.toString());
            if(file.exists()){
                if(tileMap.getExtension().equalsIgnoreCase("tif")){
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
                }else{
                    BufferedImage image=ImageIO.read(file);
                    if(image!=null)return ImageUtil.toByteArray(image);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public JSONArray getCityRegionByTile(Tile tile){
        JSONArray body=new JSONArray();
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
                    body.add(item);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return body;
    }

}
