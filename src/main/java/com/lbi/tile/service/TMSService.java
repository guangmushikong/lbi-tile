package com.lbi.tile.service;

import com.lbi.map.Tile;
import com.lbi.tile.dao.TMSDao;
import com.lbi.tile.model.*;
import com.lbi.tile.model.xml.*;
import com.lbi.util.ImageUtil;
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


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("tmsService")
public class TMSService {

    private TMSDao tmsDao;
    static Map<String,T_TileMap> TILE_MAP_DICT;

    @Value("${tile.gujiao}")
    private String GUJIAO_PATH;
    @Value("${tile.world}")
    private String WORLD_PATH;
    @Value("${tile.dem}")
    private String DEM_PATH;
    @Value("${mapserver.host}")
    private String MAPSERVER_HOST;
    @Value("${geoserver.host}")
    private String GEOSERVER_HOST;

    public TMSService(TMSDao tmsDao){
        this.tmsDao=tmsDao;
        List<T_TileMap> tileMapList=tmsDao.getTileMapList();
        TILE_MAP_DICT=new HashMap<>();
        for(T_TileMap tileMap : tileMapList){
            TILE_MAP_DICT.put(tileMap.getTitle(),tileMap);
        }
        System.out.println("tile map size:"+TILE_MAP_DICT.size());
    }

    public byte[] getTMS(
            String version,
            String layerName,
            String srs,
            String formatExtension,
            Tile tile){

        T_TileMap tileMap=tmsDao.getTileMapById(layerName,srs,formatExtension);
        if(tileMap.getSType()==1){
            String tileset=tileMap.getLayerName()+"@"+tileMap.getSrs()+"@"+tileMap.getExtension();
            UriComponents uriComponents = UriComponentsBuilder.
                    newInstance().
                    scheme("http").
                    host(GEOSERVER_HOST).
                    port(8888).
                    path("/geoserver/gwc/service/tms").
                    pathSegment(version).
                    pathSegment(tileset).
                    pathSegment(""+tile.getZ()).
                    pathSegment(""+tile.getX()).
                    pathSegment(tile.getY()+".png").
                    build().
                    encode();
            System.out.println(uriComponents.toString());
            CloseableHttpClient httpClient = HttpClients.custom().build();
            HttpGet httpGet = new HttpGet(uriComponents.toString());
            try {
                // 3.发送Get请求
                CloseableHttpResponse res = httpClient.execute(httpGet);
                if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = res.getEntity();
                    ContentType contentType = ContentType.getOrDefault(entity);
                    Charset charset = contentType.getCharset();
                    // 获取字节数组
                    byte[] content = EntityUtils.toByteArray(entity);
                    return content;
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }else if(tileMap.getSType()==2){
            if(tileMap.getTitle().equalsIgnoreCase("world_satellite_raster"))return getWorld(tile);
            else if(tileMap.getTitle().equalsIgnoreCase("gujiao_satellite_raster"))return getGujiao(tile);
            else if(tileMap.getTitle().equalsIgnoreCase("china_dem_tiff"))return getDEM(tile);
        }

        return null;
    }
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
            item.setServices("http://"+MAPSERVER_HOST+":8080/tms/1.0.0");
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
    public List<T_TileMap> getTileMapList(){
        return tmsDao.getTileMapList();
    }
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

}
