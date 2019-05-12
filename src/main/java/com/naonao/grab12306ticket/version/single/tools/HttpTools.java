package com.naonao.grab12306ticket.version.single.tools;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @program: 12306grabticket_java
 * @description: Http Tools method
 * @author: Wen lyuzhao
 * @create: 2019-04-29 17:32
 **/
@Log4j
public class HttpTools {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
    private static final String HOST = "kyfw.12306.cn";

    /**
     * create a session instance
     * @param timeOut
     * @return
     */
    public static CloseableHttpClient getSession(Integer timeOut){
        // create a http request config
        RequestConfig requestConfig = RequestConfig.custom()
                // get connection time out
                .setConnectTimeout(timeOut)
                // request time out
                .setConnectionRequestTimeout(timeOut)
                // socket time out
                .setSocketTimeout(timeOut)
                // redirect url
                // .setRedirectsEnabled(false)
                // build object
                .build();
        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(new BasicCookieStore())
                .build();
    }

    /**
     * create a session instance
     * @param timeOut
     * @return
     */
    public static CloseableHttpClient getSession(Integer timeOut, CookieStore cookieStore){
        // create a http request config
        RequestConfig requestConfig = RequestConfig.custom()
                // get connection time out
                .setConnectTimeout(timeOut)
                // request time out
                .setConnectionRequestTimeout(timeOut)
                // socket time out
                .setSocketTimeout(timeOut)
                // redirect url
                // .setRedirectsEnabled(false)
                // build object
                .build();
        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore)
                .build();
    }

    /**
     * set requests header
     * @param httpGet
     * @param hasHost
     * @param isJson
     * @param hasXRequest
     * @return
     */
    public static HttpGet setRequestHeader(HttpGet httpGet, Boolean hasHost, Boolean isJson, Boolean hasXRequest){
        httpGet.addHeader("User-Agent", USER_AGENT);
        if (hasHost){
            httpGet.addHeader("Host", HOST);
        }
        if (isJson != null){
            if (isJson){
                httpGet.addHeader("Content-Type", "application/json;charset=UTF-8");
            }else {
                httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            }
        }
        if (hasXRequest){
            httpGet.addHeader("X-Requested-With", "XMLHttpRequest");
        }
        return httpGet;
    }
    public static HttpPost setRequestHeader(HttpPost httpPost, Boolean hasHost, Boolean isJson, Boolean hasXRequest){
        httpPost.addHeader("User-Agent", USER_AGENT);
        if (hasHost){
            httpPost.addHeader("Host", HOST);
        }
        if (isJson != null){
            if (isJson){
                httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
            }else {
                httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            }
        }
        if (hasXRequest){
            httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
        }
        return httpPost;
    }

    /**
     *  response to string
     * @param response response
     * @return string
     */
    public static String responseToString(CloseableHttpResponse response) {
        try{
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        }catch (IOException e){
            return "";
        }
    }

    /**
     *  create post entity by json
     * @param postDataMap post data map
     * @return StringEntity
     */
    public static StringEntity doPostDataFromJson(Map<String, String> postDataMap) {
        JSONObject jsonData = new JSONObject();
        for(Map.Entry<String, String> element: postDataMap.entrySet()){
            jsonData.put(element.getKey(), element.getValue());
        }
        StringEntity stringEntity = new StringEntity(jsonData.toJSONString(), StandardCharsets.UTF_8);
        stringEntity.setContentEncoding("UTF-8");
        return stringEntity;
    }
    public static UrlEncodedFormEntity doPostData(Map<String, String> postDataMap) {
        List<NameValuePair> postData = new LinkedList<>();
        for (Map.Entry<String, String> entry : postDataMap.entrySet()){
            BasicNameValuePair param = new BasicNameValuePair(entry.getKey(), entry.getValue());
            postData.add(param);
        }
        return new UrlEncodedFormEntity(postData, StandardCharsets.UTF_8);
    }
}
