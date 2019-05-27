package com.naonao.grab12306ticket.version.single.notification.phone;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import com.naonao.grab12306ticket.version.single.notification.base.AbstractPhone;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Yunzhixin voice interface
 * this interface can only use chinese phone numbers
 *
 * @program: 12306grabticket_java
 * @description: Yunzhixin voice interface
 * @author: Wen lyuzhao
 * @create: 2019-05-06 21:52
 **/
@Log4j
public class YunzhixinVoice extends AbstractPhone {

    private String appCode;
    private CloseableHttpClient session;

    

    public YunzhixinVoice(String appCode){
        this.appCode = appCode;
        this.session = HttpTools.getSession(30000);
    }

    public Boolean sendPhoneVoice(String[] phoneNums, String templateId){
        for (String phoneNum: phoneNums) {
            URI url;
            url = createUri(phoneNum, templateId);
            if (url == null){
                log.error(URI_IS_NULL);
                return false;
            }
            HttpPost request = new HttpPost(url);
            request.addHeader("Authorization", "APPCODE " + appCode);
            CloseableHttpResponse response = null;
            try{
                response = session.execute(request);
                if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                    String responseText = HttpTools.responseToString(response);
                    String returnCode = JSONObject.parseObject(responseText).getString("return_code");
                    // success
                    if (SEND_SUCCESS_CODE.equals(returnCode)){
                        return true;
                    }else{
                        log.error("error code：" + returnCode);
                        return false;
                    }
                }
            }catch (Exception e){
                log.error(e.getMessage());
            }finally {
                if (response != null){
                    try{
                        response.close();
                    }catch (IOException e){
                        log.error(e.getMessage());
                    }
                }
            }
        }
        return false;
    }

    public Boolean sendPhoneVoice(String phoneNum, String templateId){
        URI url;
        url = createUri(phoneNum, templateId);
        if (url == null){
            log.error(URI_IS_NULL);
            return false;
        }
        HttpPost request = new HttpPost(url);
        request.addHeader("Authorization", "APPCODE " + appCode);
        CloseableHttpResponse response = null;
        try{
            response = session.execute(request);
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                String responseText = HttpTools.responseToString(response);
                String returnCode = JSONObject.parseObject(responseText).getString("return_code");
                // success
                if (SEND_SUCCESS_CODE.equals(returnCode)){
                    return true;
                }else{
                    log.error("error code：" + returnCode);
                    return false;
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            if (response != null){
                try{
                    response.close();
                }catch (IOException e){
                    log.error(e.getMessage());
                }
            }
        }
        return false;
    }

    private URI createUri(String phoneNum, String templateId){
        try{
            return new URIBuilder("http://yzxyytz.market.alicloudapi.com/yzx/voicenotificationSms")
                    .setParameter("phone", phoneNum)
                    .setParameter("templateId", templateId)
                    .setParameter("variable", "variable")
                    .build();
        }catch (URISyntaxException e){
            log.error(e.getMessage());
        }
        return null;
    }

}
