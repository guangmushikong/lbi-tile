package com.lbi.tile.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.lbi.map.Tile;
import com.lbi.tile.config.MyProps;
import com.lbi.tile.dao.CityDao;
import com.lbi.tile.model.Admin_Region;
import com.lbi.tile.model.TileMap;
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

import org.springframework.stereotype.Service;
import org.wololo.geojson.GeoJSON;
import org.wololo.jts2geojson.GeoJSONWriter;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.List;


@Service("xyzService")
public class XYZService {
    @Resource(name="cityDao")
    private CityDao cityDao;
    @Resource(name="ossClient")
    private OSSClient ossClient;
    @Resource(name="myProps")
    private MyProps myProps;
    private final String bucketName="cateye-tile";

    public byte[] getXYZ_Tile(
            String version,
            String layerName,
            String srs,
            String extension,
            Tile tile){
        String tileset=layerName+"@"+srs+"@"+extension;
        TileMap tileMap=myProps.getXYZMap(tileset);
        if(tileMap==null)return null;

        if(tileMap.getKind()==1){
            return getRemoteTile(tileMap,tile);
        }else if(tileMap.getKind()==2){
            //return getCacheTile(tileMap,tile);
            return getOSSTile(tileMap,tile);
        }

        return null;
    }
    public byte[] getXYZ_Tile(String layerName,String extension,Tile tile){
        return getXYZ_Tile("1.0.0",layerName,"EPSG:900913",extension,tile);
    }

    private byte[] getRemoteTile(TileMap tileMap, Tile tile){
        String remoteUrl=tileMap.getSource();
        remoteUrl=remoteUrl.replace("${geoserver}",myProps.getGeoServer());
        StringBuilder sb=new StringBuilder();
        sb.append(remoteUrl);
        sb.append("/"+tile.getZ());
        sb.append("/"+tile.getX());
        sb.append("/"+tile.getY()+"."+tileMap.getExtension());

        return request(sb.toString());
    }

    private byte[] getCacheTile(TileMap tileMap, Tile tile){
        try{
            StringBuilder sb=new StringBuilder();
            sb.append(myProps.getTiledata());
            sb.append(File.separator).append(tileMap.getTitle());
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
    private byte[] getOSSTile(TileMap tileMap, Tile tile){
        StringBuilder sb=new StringBuilder();
        sb.append(tileMap.getTitle());
        sb.append("/").append(tile.getZ());
        sb.append("/").append(tile.getX());
        sb.append("/").append(tile.getY());
        sb.append(".").append(tileMap.getFileExtension());
        Date expiration=new Date(new Date().getTime() + 60000);
        URL url=ossClient.generatePresignedUrl(bucketName,sb.toString(),expiration);
        return request(url.toString());
    }

    private byte[] getOSSTile2(TileMap tileMap, Tile tile){
        byte[] body=null;
        try{
            StringBuilder sb=new StringBuilder();
            sb.append(tileMap.getTitle());
            sb.append("/").append(tile.getZ());
            sb.append("/").append(tile.getX());
            sb.append("/").append(tile.getY());
            sb.append(".").append(tileMap.getFileExtension());
            boolean found = ossClient.doesObjectExist(bucketName, sb.toString());
            //System.out.println("key:"+sb.toString()+"|"+found);
            if(found){
                OSSObject ossObject = ossClient.getObject(bucketName, sb.toString());
                InputStream in = ossObject.getObjectContent();
                if(tileMap.getExtension().equalsIgnoreCase("tif")){
                    body=IOUtils.readFully(in);
                }else{
                    BufferedImage image=ImageIO.read(in);
                    if(image!=null)body=ImageUtil.toByteArray(image);
                }
                in.close();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return body;
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

    private byte[] request(String url){
        System.out.println("url:"+url);
        byte[] body=null;
        try{
            CloseableHttpClient httpClient = HttpClients.custom().build();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse res = httpClient.execute(httpGet);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                body = EntityUtils.toByteArray(entity);
            }
            httpClient.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return body;
    }

}
