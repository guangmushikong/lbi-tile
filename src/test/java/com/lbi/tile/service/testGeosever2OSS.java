package com.lbi.tile.service;

import com.lbi.tile.util.Tile;
import com.lbi.tile.util.TileSystem;
import com.vividsolutions.jts.geom.Coordinate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class testGeosever2OSS {
    @Test
    public void getTile(){
        //Tile tile=new Tile(1441,1203,11);
        Tile tile=new Tile(207,156,8);
        //int alterY=new Double(Math.pow(2,tile.getZ())).intValue()-1-tile.getY();
        //tile.setY(alterY);
        //isExistTile("china_city_polygon",tile);
        isExistTile("gujiao_contour50_line",tile);
        String url=getTilePath("lbi:gujiao_50",tile);
        System.out.println(url);
        InputStream is=request(url);
        String path="F:/BaiduNetdiskDownload/gujiao_contour50_line/"+tile.getZ()+"/"+tile.getX()+"/"+tile.getY()+".png";
        System.out.println(path);
        savePng(is,path);

    }
    @Test
    public void china_city_polygon(){
        double x1=73.1985473632812;
        double y1=3.58836889266968;
        double x2=135.403656005859;
        double y2=53.8118324279785;

        int zoom=11;
        Tile minTile=TileSystem.LatLongToTile(new Coordinate(x1,y2),zoom);
        Tile maxTile=TileSystem.LatLongToTile(new Coordinate(x2,y1),zoom);

        System.out.println("tile1:"+minTile.toString());
        System.out.println("tile2:"+maxTile.toString());
        long n=0;
        for(int x=minTile.getX();x<=maxTile.getX();x++){
            for(int y=minTile.getY();y<=maxTile.getY();y++){
                Tile tile=new Tile(x,y,zoom);
                n++;
                int alterY=new Double(Math.pow(2,tile.getZ())).intValue()-1-tile.getY();
                tile.setY(alterY);
                isExistTile("china_city_polygon",tile);
                String url=getTilePath("lbi:s_ods_city_simplify",tile);
                System.out.println("tile:"+tile.toString());
                InputStream is=request(url);
                String path="F:/BaiduNetdiskDownload/china_city_polygon/"+tile.getZ()+"/"+tile.getX()+"/"+tile.getY()+".png";
                savePng(is,path);
            }
        }
        System.out.println("total:"+n);
    }
    @Test
    public void gujiao_contour50_line(){
        double x1=111.6810742671935;
        double y1=37.60462333833393;
        double x2=112.52042406708985;
        double y2=38.267318646571766;

        int zoom=15;
        Tile minTile=TileSystem.LatLongToTile(new Coordinate(x1,y2),zoom);
        Tile maxTile=TileSystem.LatLongToTile(new Coordinate(x2,y1),zoom);

        System.out.println("tile1:"+minTile.toString());
        System.out.println("tile2:"+maxTile.toString());
        long n=0;
        for(int x=minTile.getX();x<=maxTile.getX();x++){
            for(int y=minTile.getY();y<=maxTile.getY();y++){
                Tile tile=new Tile(x,y,zoom);
                n++;
                int alterY=new Double(Math.pow(2,tile.getZ())).intValue()-1-tile.getY();
                tile.setY(alterY);
                isExistTile("gujiao_contour50_line",tile);
                System.out.println("tile:"+tile.toString());
                String url=getTilePath("lbi:gujiao_50",tile);
                InputStream is=request(url);
                String path="F:/BaiduNetdiskDownload/gujiao_contour50_line/"+tile.getZ()+"/"+tile.getX()+"/"+tile.getY()+".png";
                savePng(is,path);
            }
        }
        System.out.println("total:"+n);
    }
    private String getTilePath(String layerName,Tile tile){
        StringBuilder sb=new StringBuilder();
        sb.append("http://localhost:8888/geoserver/gwc/service/tms/1.0.0/"+layerName+"@EPSG:900913@png");
        sb.append("/"+tile.getZ()+"/"+tile.getX()+"/"+tile.getY()+".png");
        return sb.toString();
    }
    private void isExistTile(String layerName,Tile tile){
        String path="F:/BaiduNetdiskDownload/"+layerName+"/"+tile.getZ();
        File file=new File(path);
        if(!file.exists())file.mkdir();
        path="F:/BaiduNetdiskDownload/"+layerName+"/"+tile.getZ()+"/"+tile.getX();
        file=new File(path);
        if(!file.exists())file.mkdir();
    }
    private void savePng(InputStream is,String filePath){
        try{
            //先读入内存
            ByteArrayOutputStream buf = new ByteArrayOutputStream(8192);
            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b)) != -1) {
                buf.write(b, 0, len);
            }
            //读图像
            // 读图像
            ByteArrayInputStream in = new ByteArrayInputStream(buf.toByteArray());
            BufferedImage  image = ImageIO.read(in);
            in.close();
            File f = new File(filePath);
            ImageIO.write(image,"png", f);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    private InputStream request(String url){
        try{
            CloseableHttpClient httpClient = HttpClients.custom().build();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse res = httpClient.execute(httpGet);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                return entity.getContent();
            }
            httpClient.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
