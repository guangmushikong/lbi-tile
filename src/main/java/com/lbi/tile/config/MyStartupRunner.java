package com.lbi.tile.config;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.lbi.tile.service.IOUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * 服务启动执行
 *
 */
@Component
public class MyStartupRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>服务启动执行，执行加载数据等操作<<<<<<<<<<<<<");
        /*String accessKeyId="LTAIelgAxlE5sJMB";
        String accessKeySecret="RE0tSBGvQjD7EkCNILYfSqC7JGArn3";
        String endpoint="oss-cn-beijing.aliyuncs.com";
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        String bucketName="cateye-tile";
        String key="gujiao_satellite_raster/10/829/628.png";
        long star=System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            OSSObject ossObject = client.getObject(bucketName, key);
            InputStream in = ossObject.getObjectContent();
            byte[] body= IOUtils.readFully(in);
            in.close();
            System.out.println(i+":"+key+",size="+body.length);
        }
        long end=System.currentTimeMillis();
        System.out.println("total=1000,cost time="+(end-star)/1000+"ms");
        client.shutdown();*/
    }
}
