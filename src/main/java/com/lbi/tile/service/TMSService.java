package com.lbi.tile.service;

import com.lbi.model.Tile;
import com.lbi.tile.config.MyConfig;
import com.lbi.tile.dao.OSSDao;
import com.lbi.tile.model.*;

import com.lbi.util.ImageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Service("tmsService")
public class TMSService {
    @Resource(name="myConfig")
    MyConfig myConfig;
    @Resource(name="ossDao")
    OSSDao ossDao;

    @Value("${service.geoserver}")
    String geoserver;
    @Value("${service.tiledata}")
    String tiledata;

    public byte[] getTMS_Tile(
            String version,
            String layerName,
            String srs,
            String extension,
            Tile tile)throws Exception{
        String tileset=layerName+"@"+srs+"@"+extension;
        TileMap tileMap=myConfig.getTileMap(tileset);
        if(tileMap==null)return null;

        if(tileMap.getKind()==1){
            return getRemoteTile(tileMap,tile);
        }else if(tileMap.getKind()==2){
            //return getCacheTile(tileMap,tile);
            return getOSSTile(tileMap,tile);
        }

        return null;
    }

    private byte[] getRemoteTile(TileMap tileMap, Tile tile) throws Exception{
        String remoteUrl=tileMap.getSource();
        remoteUrl=remoteUrl.replace("${geoserver}",geoserver);
        StringBuilder sb=new StringBuilder();
        sb.append(remoteUrl);
        sb.append("/"+tile.getZ());
        sb.append("/"+tile.getX());
        sb.append("/"+tile.getY()+"."+tileMap.getExtension());
        return ossDao.request(sb.toString());
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

    private byte[] getOSSTile(TileMap tileMap, Tile tile)throws Exception{
        StringBuilder sb=new StringBuilder();
        sb.append(tileMap.getTitle());
        sb.append("/").append(tile.getZ());
        sb.append("/").append(tile.getX());
        sb.append("/").append(tile.getY());
        sb.append(".").append(tileMap.getFileExtension());
        return ossDao.getOSSObjectByURL(sb.toString());
    }
}
