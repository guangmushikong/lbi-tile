package com.lbi.tile.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class testOSS{
    OSSClient ossClient;
    @Before
    public void init(){
        //String accessKeyId="LTAIelgAxlE5sJMB";
        //String accessKeySecret="RE0tSBGvQjD7EkCNILYfSqC7JGArn3";
        String accessKeyId="LTAICVzaI6DMGWWa";
        String accessKeySecret="1oltFu5OS80KcdQcslMVZZ3bP1Px51";
        String endpoint="oss-cn-beijing.aliyuncs.com";
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    @Test
    public void once()throws IOException{
        String bucketName="cateye-tile";
        String key="gujiao_satellite_raster/10/829/628.png";
        //String key="gujiao_satellite_raster/10";
        long star=System.currentTimeMillis();
        long duration=3600*1000;
        Date expiration=new Date(new Date().getTime() + duration);
        URL url=ossClient.generatePresignedUrl(bucketName,key,expiration);
        System.out.println(url);
    }

    @Test
    public void batch()throws IOException{
        String bucketName="cateye-tile";
        String key="gujiao_satellite_raster/10/829/628.png";
        long star=System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            OSSObject ossObject = ossClient.getObject(bucketName, key);
            InputStream in = ossObject.getObjectContent();
            byte[] body=IOUtils.readFully(in);
            in.close();
            System.out.println(i+":"+key+",size="+body.length);
        }
        long end=System.currentTimeMillis();
        System.out.println("total=1000,cost time="+(end-star)/1000+"ms");
    }

    @After
    public void end(){
        if(ossClient!=null)ossClient.shutdown();
    }
    /*private URL generatePresignedUrl(String bucketName, String key, Date expiration){
        return generatePresignedUrl(bucketName, key, expiration, HttpMethod.GET);
    }*/
}
