package com.lbi.tile.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import org.junit.Test;

public class testOSS{
    @Test
    public void test(){
        String accessKeyId="LTAIelgAxlE5sJMB";
        String accessKeySecret="RE0tSBGvQjD7EkCNILYfSqC7JGArn3";
        String endpoint="oss-cn-hangzhou.aliyuncs.com";
        String bucketName="cateye-data";
        String key="world_satellite_raster/4/10/10.jpg";
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        OSSObject ossObject = client.getObject(bucketName, "key");
        client.shutdown();
    }
}
