package com.lbi.tile.dao;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.lbi.util.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.net.URL;
import java.util.Date;


@Repository(value="ossDao")
public class OSSDao {
    @Resource(name="ossClient")
    OSSClient ossClient;
    @Value("${oss.bucket}")
    String bucketName;

    public byte[] getOSSObject(String objectName)throws Exception{
        byte[] body=null;
        OSSObject ossObject = ossClient.getObject(bucketName, objectName);
        body= IOUtils.readFully(ossObject.getObjectContent());
        ossObject.close();
        return body;
    }

    public byte[] getOSSObjectByURL(String objectName)throws Exception{
        Date expiration=new Date(new Date().getTime() + 60000);
        URL url=ossClient.generatePresignedUrl(bucketName,objectName,expiration);
        return request(url.toString());
    }

    public byte[] request(String url)throws Exception{
        byte[] body=null;
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse resp = httpClient.execute(httpGet);
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            HttpEntity entity = resp.getEntity();
            body = IOUtils.readFully(entity.getContent());
        }
        httpClient.close();
        return body;
    }
}
