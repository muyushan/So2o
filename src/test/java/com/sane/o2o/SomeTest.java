package com.sane.o2o;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.sane.o2o.entity.Shop;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

public class SomeTest {
    @Test
    public  void  testHttpClient() throws IOException {
        CloseableHttpClient httpClient= HttpClientBuilder.create().build();
        String url="http://localhost:8080/WebDesign/getResult";
        HttpGet httpGet=new HttpGet(url);
        HttpPost httpPost=new HttpPost(url);
        Shop shop=new Shop();
        shop.setShopName("shop1");

        StringEntity stringEntity=new StringEntity(JSON.toJSONString(shop), Charset.forName("UTF-8"));
        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Origin","http://localhost:8088");
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
       CloseableHttpResponse closeableHttpResponse= httpClient.execute(httpPost);
       closeableHttpResponse.getAllHeaders();
       closeableHttpResponse.getEntity();
       closeableHttpResponse.getProtocolVersion();
       closeableHttpResponse.getStatusLine();
       System.out.println(EntityUtils.toString(closeableHttpResponse.getEntity()));

    }

    public void testRedis(){

    }
}
