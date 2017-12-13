package com.lbi.tile.service;

import com.lbi.map.Tile;
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
import java.nio.charset.Charset;


@Service("tmsService")
public class TMSService {

    public byte[] getTMS(String tileset,Tile tile){
        UriComponents uriComponents = UriComponentsBuilder.
                newInstance().
                scheme("http").
                host("54.223.166.139").
                port(8888).
                path("/geoserver/gwc/service/tms/1.0.0").
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
        return null;
    }

}
