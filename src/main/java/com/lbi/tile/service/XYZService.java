package com.lbi.tile.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;

import com.lbi.tile.config.MyConfig;
import com.lbi.tile.dao.TileDao;
import com.lbi.tile.model.Admin_Region;
import com.lbi.tile.model.FeatureVO;
import com.lbi.model.Tile;
import com.lbi.tile.model.TileMap;

import com.lbi.util.IOUtils;
import com.lbi.util.ImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("xyzService")
public class XYZService {
    @Resource(name="tileDao")
    TileDao tileDao;
    @Resource(name="ossClient")
    OSSClient ossClient;
    @Resource(name="myConfig")
    MyConfig myConfig;

    @Value("${service.geoserver}")
    String geoserver;
    @Value("${service.tiledata}")
    String tiledata;
    @Value("${oss.bucket}")
    String bucketName;

    public byte[] getXYZ_Tile(
            String version,
            String layerName,
            String srs,
            String extension,
            Tile tile){
        String tileset=layerName+"@"+srs+"@"+extension;
        TileMap tileMap=myConfig.getXYZMap(tileset);
        if(tileMap==null)return null;

        if(tileMap.getKind()==1){
            return getRemoteTile(tileMap,tile);
        }else if(tileMap.getKind()==2){
            return getOSSTile(tileMap,tile);
        }else if(tileMap.getKind()==3){
            return getOSSTimeTile(tileMap,tile);
        }else if(tileMap.getKind()==4){
            return getCacheTile(tileMap,tile);
        }else if(tileMap.getKind()==5){
            return getCacheTimeTile(tileMap,tile);
        }

        return null;
    }
    public byte[] getXYZ_Tile(String layerName,String extension,Tile tile){
        return getXYZ_Tile("1.0.0",layerName,"EPSG:900913",extension,tile);
    }

    private byte[] getRemoteTile(TileMap tileMap, Tile tile){
        String remoteUrl=tileMap.getSource();
        remoteUrl=remoteUrl.replace("${geoserver}",geoserver);
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
            sb.append(tiledata);
            sb.append(File.separator).append(tileMap.getTitle());
            sb.append(File.separator).append(tile.getZ());
            sb.append(File.separator).append(tile.getX());
            sb.append(File.separator).append(tile.getY());
            sb.append(".").append(tileMap.getFileExtension());
            System.out.println(tile.getX()+","+tile.getY()+","+tile.getZ()+"|"+sb.toString());
            File file=new File(sb.toString());
            if(file.exists()){
                if(tileMap.getExtension().equalsIgnoreCase("tif") || tileMap.getExtension().equalsIgnoreCase("geojson")){
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
                }/*else if(tileMap.getExtension().equalsIgnoreCase("geojson")){

                }*/else{
                    BufferedImage image=ImageIO.read(file);
                    if(image!=null)return ImageUtil.toByteArray(image);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public String getCacheJsonTile(
            String version,
            String layerName,
            String srs,
            String extension,
            Tile tile){
        String body=null;
        try{
            String tileset=layerName+"@"+srs+"@"+extension;
            TileMap tileMap=myConfig.getXYZMap(tileset);
            if(tileMap==null)return null;

            StringBuilder sb=new StringBuilder();
            sb.append(tiledata);
            sb.append(File.separator).append(tileMap.getTitle());
            sb.append(File.separator).append(tile.getZ());
            sb.append(File.separator).append(tile.getX());
            sb.append(File.separator).append(tile.getY());
            sb.append(".").append(tileMap.getFileExtension());
            //System.out.println(tile.getX()+","+tile.getY()+","+tile.getZ()+"|"+sb.toString());
            File file=new File(sb.toString());
            if(file.exists()){
                FileInputStream fis = new FileInputStream(file);
                body= IOUtils.readStreamAsString(fis);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return body;
    }

    private byte[] getCacheTimeTile(TileMap tileMap, Tile tile){
        try{
            StringBuilder sb=new StringBuilder();
            sb.append(tiledata);
            String title=tileMap.getTitle();
            title=title.replace("_"+tileMap.getRecordDate(),"");
            sb.append(File.separator).append(title);
            sb.append(File.separator).append(tileMap.getRecordDate());
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

    private byte[] getOSSTimeTile(TileMap tileMap, Tile tile){
        StringBuilder sb=new StringBuilder();
        String title=tileMap.getTitle();
        title=title.replace("_"+tileMap.getRecordDate(),"");
        sb.append(title);
        sb.append("/").append(tileMap.getRecordDate());
        sb.append("/").append(tile.getZ());
        sb.append("/").append(tile.getX());
        sb.append("/").append(tile.getY());
        sb.append(".").append(tileMap.getFileExtension());
        Date expiration=new Date(new Date().getTime() + 60000);
        URL url=ossClient.generatePresignedUrl(bucketName,sb.toString(),expiration);
        return request(url.toString());
    }

    public JSONArray getCityRegionByTile(Tile tile){
        JSONArray body=new JSONArray();
        try{
            List<Admin_Region> list=tileDao.getCityRegionList(tile);
            if(list!=null){
                /*WKTReader wktReader=new WKTReader();
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
                }*/
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return body;
    }

    public JSONArray getGujiaoContour50ByTile(Tile tile){
        JSONArray body=new JSONArray();
        try{
            List<JSONObject> list=tileDao.getGeojsonListByTile("gujiao_50",tile);
            if(list!=null){
                for(JSONObject u:list){
                    body.add(u);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return body;
    }
    public JSONArray getGujiaoContour100ByTile(Tile tile){
        JSONArray body=new JSONArray();
        try{
            List<JSONObject> list=tileDao.getGeojsonListByTile("gujiao_100",tile);
            if(list!=null){
                for(JSONObject u:list){
                    body.add(u);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return body;
    }
    public JSONArray getGujiaoContour200ByTile(Tile tile){
        JSONArray body=new JSONArray();
        try{
            List<JSONObject> list=tileDao.getGeojsonListByTile("gujiao_200",tile);
            if(list!=null){
                for(JSONObject u:list){
                    body.add(u);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return body;
    }

    public JSONArray getGujiaoContourByTile(Tile tile){
        JSONArray body=new JSONArray();
        List<JSONObject> list=new ArrayList<>();
        int z=tile.getZ();
        if(z<8){

        }else if(z==8){
            list=tileDao.getContourList("data.gujiao_200_8_new",tile);
        }else if(z==9){
            list=tileDao.getContourList("data.gujiao_200_9_new",tile);
        }else if(z==10){
            list=tileDao.getContourList("data.gujiao_200_10_new",tile);
        }else if(z==11){
            list=tileDao.getContourList("data.gujiao_100_11_new",tile);
        }else if(z==12){
            list=tileDao.getContourList("data.gujiao_100_12_new",tile);
        }else if(z==13){
            list=tileDao.getContourList("data.gujiao_100_13_new",tile);
        }else if(z==14){
            list=tileDao.getContourList("data.gujiao_50_14_new",tile);
        }else if(z==15){
            list=tileDao.getContourList("data.gujiao_50_15_new",tile);
        }else if(z==16){
            list=tileDao.getContourList("data.gujiao_50_16_new",tile);
        }else if(z==17){
            list=tileDao.getContourList("data.gujiao_50_17_new",tile);
        }else{
            list=tileDao.getContourList("data.gujiao_50_17_new",tile);
        }
        if(!list.isEmpty()){
            for(JSONObject u:list){
                body.add(u);
            }
        }
        return body;
    }

    public JSONObject getGujiaoContourByTile2(Tile tile){
        JSONObject result=new JSONObject();

        List<FeatureVO> featureList=new ArrayList<>();
        int z=tile.getZ();
        if(z<8){

        }else if(z==8){
            featureList=tileDao.getContour("data.gujiao_200_8_new",tile);
        }else if(z==9){
            featureList=tileDao.getContour("data.gujiao_200_9_new",tile);
        }else if(z==10){
            featureList=tileDao.getContour("data.gujiao_200_10_new",tile);
        }else if(z==11){
            featureList=tileDao.getContour("data.gujiao_100_11_new",tile);
        }else if(z==12){
            featureList=tileDao.getContour("data.gujiao_100_12_new",tile);
        }else if(z==13){
            featureList=tileDao.getContour("data.gujiao_100_13_new",tile);
        }else if(z==14){
            featureList=tileDao.getContour("data.gujiao_50_14_new",tile);
        }else if(z==15){
            featureList=tileDao.getContour("data.gujiao_50_15_new",tile);
        }else if(z==16){
            featureList=tileDao.getContour("data.gujiao_50_16_new",tile);
        }else if(z==17){
            featureList=tileDao.getContour("data.gujiao_50_17_new",tile);
        }else{
            featureList=tileDao.getContour("data.gujiao_50_17_new",tile);
        }
        result.put("features",featureList);
        return result;
    }

    public JSONObject getJingZhuangContourByTile(Tile tile){
        JSONObject result=new JSONObject();

        List<FeatureVO> featureList=new ArrayList<>();
        int z=tile.getZ();
        if(z<8){

        }else if(z==8){
            featureList=tileDao.getContour("data.jzh_contour_200_8",tile);
        }else if(z==9){
            featureList=tileDao.getContour("data.jzh_contour_200_9",tile);
        }else if(z==10){
            featureList=tileDao.getContour("data.jzh_contour_200_10",tile);
        }else if(z==11){
            featureList=tileDao.getContour("data.jzh_contour_100_11",tile);
        }else if(z==12){
            featureList=tileDao.getContour("data.jzh_contour_100_12",tile);
        }else if(z==13){
            featureList=tileDao.getContour("data.jzh_contour_100_13",tile);
        }else if(z==14){
            featureList=tileDao.getContour("data.jzh_contour_50_14",tile);
        }else if(z==15){
            featureList=tileDao.getContour("data.jzh_contour_50_15",tile);
        }else if(z==16){
            featureList=tileDao.getContour("data.jzh_contour_50_16",tile);
        }else if(z==17){
            featureList=tileDao.getContour("data.jzh_contour_50_17",tile);
        }else{
            featureList=tileDao.getContour("data.jzh_contour_50_17",tile);
        }
        result.put("features",featureList);
        return result;
    }

    private byte[] request(String url){
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
