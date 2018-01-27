package com.lbi.tile.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.lbi.map.Tile;
import com.lbi.tile.dao.TMSDao;
import com.lbi.tile.model.*;
import com.lbi.tile.model.xml.*;
import com.lbi.util.ImageUtil;
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

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Service("tmsService")
public class TMSService {
    @Resource(name="tmsDao")
    private TMSDao tmsDao;
    @Resource(name="ossClient")
    private OSSClient ossClient;
    @Autowired
    private Environment env;
    private final String bucketName="cateye-tile";

    public XmlRoot_TileMapService getTileMapService(String version){
        XmlRoot_TileMapService u = new XmlRoot_TileMapService();
        u.setVersion(version);
        u.setServices("http://tms.osgeo.org/1.0.0");
        u.setTitle("Tile Map Service");
        u.setAbstract("A Tile Map Service");
        List<T_TileMap> tileMapList1=tmsDao.getTileMapList();
        List<Xml_TileMap> tileMapList=new ArrayList<>();
        for(T_TileMap t_TileMap:tileMapList1){
            Xml_TileMap x_TileMap=new Xml_TileMap(t_TileMap.getTitle(),t_TileMap.getSrs(),t_TileMap.getProfile(),t_TileMap.getHref());
            tileMapList.add(x_TileMap);
        }
        u.setTileMaps(tileMapList);
        return u;
    }
    public XmlRoot_TileMap getTileMap(
            String version,
            String layerName,
            String srs,
            String formatExtension){
        T_TileMap u =tmsDao.getTileMapById(layerName,srs,formatExtension);
        if(u!=null){
            XmlRoot_TileMap item=new XmlRoot_TileMap();
            item.setVersion(version);
            item.setAbstract("");
            item.setTitle(u.getTitle());
            item.setServices("http://"+env.getProperty("mapserver.host")+":8080/tms/1.0.0");
            item.setSRS(u.getSrs());
            Xml_BoundingBox boundingBox =new Xml_BoundingBox();
            boundingBox.setMinX(u.getMinX());
            boundingBox.setMinY(u.getMinY());
            boundingBox.setMaxX(u.getMaxX());
            boundingBox.setMaxY(u.getMaxY());
            item.setXBoundingBox(boundingBox);
            Xml_Origin origin =new Xml_Origin();
            origin.setX(u.getOriginX());
            origin.setY(u.getOriginY());
            item.setXOrigin(origin);
            Xml_TileFormat tileFormat =new Xml_TileFormat();
            tileFormat.setWidth(u.getTileWidth());
            tileFormat.setHeight(u.getTileHeight());
            tileFormat.setMimeType(u.getMimeType());
            tileFormat.setExtension(u.getExtension());
            item.setXTileFormat(tileFormat);
            Xml_TileSets tileSets=new Xml_TileSets();
            List<Xml_TileSet> tileSetList=new ArrayList<>();
            List<T_TileSet> tileSetList1=tmsDao.getTileSetListByID(u.getId());
            for(T_TileSet t_tileSet:tileSetList1){
                Xml_TileSet x_tileSet=new Xml_TileSet(t_tileSet.getHref(),t_tileSet.getUnits_per_pixel(),t_tileSet.getOrder());
                tileSetList.add(x_tileSet);
            }
            tileSets.setProfile(u.getProfile());
            tileSets.setTileSets(tileSetList);
            item.setTileSets(tileSets);
            return item;
        }
        return null;
    }
    public byte[] getTMS_Tile(
            String version,
            String layerName,
            String srs,
            String formatExtension,
            Tile tile){
        String tileset=layerName+"@"+srs+"@"+formatExtension;
        T_TileMap tileMap=env.getProperty(tileset,T_TileMap.class);
        if(tileMap==null)return null;

        if(tileMap.getSType()==1){
            return getRemoteTile(tileMap,tile);
        }else if(tileMap.getSType()==2){
            //return getCacheTile(tileMap,tile);
            return getOSSTile(tileMap,tile);
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

    private byte[] getOSSTile(T_TileMap tileMap,Tile tile){
        byte[] body=null;
        try{
            StringBuilder sb=new StringBuilder();
            sb.append(tileMap.getLayerName());
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
}
