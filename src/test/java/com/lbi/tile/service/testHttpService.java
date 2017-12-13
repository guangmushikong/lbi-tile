package com.lbi.tile.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.InputStream;
import java.nio.charset.Charset;

public class testHttpService {
    @Test
    public void test(){
        UriComponents uriComponents = UriComponentsBuilder.
                newInstance().
                scheme("http").
                host("54.223.166.139").
                port(8888).
                path("/geoserver/gwc/service/tms/1.0.0").
                pathSegment("lbi:s_ods_city_simplify@EPSG:900913@png").
                pathSegment("5").
                pathSegment("22").
                pathSegment("20.png").
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
                String html=new String(content, charset);
                System.out.println(html);
            }


        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
