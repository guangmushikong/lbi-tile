package com.lbi.tile.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lbi.model.Tile;
import com.lbi.util.TileSystem;
import com.lbi.util.IOUtils;
import com.vividsolutions.jts.geom.Coordinate;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class testRest2OSS {
    final String layerName="jingzhuang_contour_line";

    @Test
    public void getTile()throws Exception{
        Tile tile=new Tile(202,100,8);
        String tileUrl=getTileUrl(tile);
        System.out.println(tileUrl);
        String result=getResult(tileUrl);
        System.out.println(result);
        JSONObject obj= JSONObject.parseObject(result);
        System.out.println("len:"+obj.getJSONArray("features").size());
        String filePath="F:/BaiduNetdiskDownload/"+layerName+"/"+tile.getZ()+"/"+tile.getX()+"/"+tile.getY()+".json";
        isExistTile(tile);
        saveFile(result,filePath);
    }
    @Test
    public void multiThreading(){
        ExecutorService pool = Executors.newFixedThreadPool(32);

        double x1 = 104.77085056787423;
        double y1 = 35.10560793684285;
        double x2 = 105.14244032110284;
        double y2 = 35.58053135312061;
        int zoom=8;
        Tile minTile= TileSystem.LatLongToTile(new Coordinate(x1,y2),zoom);
        Tile maxTile=TileSystem.LatLongToTile(new Coordinate(x2,y1),zoom);
        System.out.println("tile1:"+minTile.toString());
        System.out.println("tile2:"+maxTile.toString());
        long n=0;

        for(long x=minTile.getX();x<=maxTile.getX();x++) {
            for (long y = minTile.getY(); y <= maxTile.getY(); y++) {
                n++;
                final Tile tile = new Tile(x, y, zoom);
                final String tileUrl=getTileUrl(tile);
                System.out.println(tileUrl);
                pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        String result=getResult(tileUrl);
                        if(StringUtils.isNoneEmpty(result)){
                            JSONArray arr= JSONArray.parseArray(result);
                            System.out.println("tile:"+tile.toString()+",arr:"+arr.size());
                            if(arr.size()>0){
                                String filePath="F:/BaiduNetdiskDownload/"+layerName+"/"+tile.getZ()+"/"+tile.getX()+"/"+tile.getY()+".json";
                                isExistTile(tile);
                                saveFile(arr.toJSONString(),filePath);
                            }
                        }
                    }
                });
            }
        }
        //pool.shutdown();
        try {
            pool.awaitTermination(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("n:"+n);
    }
    @Test
    public void batchTile()throws Exception{
        double x1 = 104.77085056787423;
        double y1 = 35.10560793684285;
        double x2 = 105.14244032110284;
        double y2 = 35.58053135312061;
        int zoom=17;
        Tile minTile= TileSystem.LatLongToTile(new Coordinate(x1,y2),zoom);
        Tile maxTile=TileSystem.LatLongToTile(new Coordinate(x2,y1),zoom);

        System.out.println("tile1:"+minTile.toString());
        System.out.println("tile2:"+maxTile.toString());
        long n=0,m=0;
        for(long x=minTile.getX();x<=maxTile.getX();x++){
            for(long y=minTile.getY();y<=maxTile.getY();y++){
                n++;
                Tile tile=new Tile(x,y,zoom);
                String tileUrl=getTileUrl(tile);
                String result=getResult(tileUrl);
                if(StringUtils.isNoneEmpty(result)){
                    JSONObject obj= JSONObject.parseObject(result);
                    JSONArray arr= obj.getJSONArray("features");
                    System.out.println("tile:"+tile.toString()+",arr:"+arr.size());
                    if(arr.size()>0){
                        String filePath="F:/BaiduNetdiskDownload/"+layerName+"/"+tile.getZ()+"/"+tile.getX()+"/"+tile.getY()+".json";
                        isExistTile(tile);
                        saveFile(result,filePath);
                    }
                }
            }
        }
        System.out.println("n:"+n+",m:"+m);
    }

    private String getTileUrl(Tile tile){
        StringBuilder sb=new StringBuilder();
        sb.append("http://localhost:8080/xyz/contour");
        sb.append("/"+tile.getX()+"/"+tile.getY()+"/"+tile.getZ()+".json");
        return sb.toString();
    }


    private String getResult(String uri){
        String result=null;
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(uri);
            CloseableHttpResponse res = httpClient.execute(httpGet);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                result= IOUtils.readStreamAsString(entity.getContent());
            }
            httpClient.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
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
    private void isExistTile(Tile tile){
        String path="F:/BaiduNetdiskDownload/"+layerName+"/"+tile.getZ();
        File file=new File(path);
        if(!file.exists())file.mkdir();
        path="F:/BaiduNetdiskDownload/"+layerName+"/"+tile.getZ()+"/"+tile.getX();
        file=new File(path);
        if(!file.exists())file.mkdir();
    }
    private void saveFile(String body,String filePath){
        try{
            FileWriter fw = new FileWriter(filePath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(body);
            bw.close();
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void saveFile(InputStream is,String filePath){
        try{
            File file=new File(filePath);

            FileOutputStream fileOut = new FileOutputStream(file);
            byte[] buf = new byte[1024 * 8];
            while (true) {
                int read = 0;
                if (is != null) {
                    read = is.read(buf);
                }
                if (read == -1) {
                    break;
                }
                fileOut.write(buf, 0, read);
            }
            fileOut.flush();
            fileOut.close();
            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
