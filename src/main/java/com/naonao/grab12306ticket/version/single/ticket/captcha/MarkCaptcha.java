package com.naonao.grab12306ticket.version.single.ticket.captcha;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.captcha.MarkCaptchaReturnResult;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import com.naonao.grab12306ticket.version.single.ticket.base.AbstractCaptcha;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: 12306grabticket_java
 * @description: mark captcha
 * @author: Wen lyuzhao
 * @create: 2019-04-29 22:59
 **/
@Log4j
public class MarkCaptcha extends AbstractCaptcha {

    

    /**
     *  mark captcha
     *  get mark result by self-built mark server
     *  you can over write this method use to other mark server, but
     *  need to ensure that the mark result format.
     *  markResult example: "12, 12, 12, 12"
     *
     * @param captchaBase64Str      captcha base64 string
     * @return                      MarkCaptchaReturnResult
     *                              status, message, markResult
     */

    protected MarkCaptchaReturnResult markCaptcha(String captchaBase64Str){
        // get a new session
        CloseableHttpClient markHttpClient = HttpTools.getSession(30000);
        // set requests params
        String markURL  = "https://www.markcaptcha.com/Mark12306Captcha/mark/captcha";
        HttpPost httpPostMark = HttpTools.setRequestHeader(new HttpPost(markURL), false, true, false);
        Map<String, String> captchaDataMap = new HashMap<>();
        captchaDataMap.put("CaptchaBase64Str", captchaBase64Str);
        httpPostMark.setEntity(HttpTools.doPostDataFromJson(captchaDataMap));
        CloseableHttpResponse response = null;
        MarkCaptchaReturnResult markCaptchaReturnResult = new MarkCaptchaReturnResult();
        try{
            response = markHttpClient.execute(httpPostMark);
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                String responseText = HttpTools.responseToString(response);
                JSONObject checkJsonData = JSONObject.parseObject(responseText);
                // mark result json
                Boolean status = checkJsonData.getBoolean("status");
                String result = checkJsonData.getString("result");
                Integer resultCode = checkJsonData.getInteger("resultCode");
                String message = checkJsonData.getString("message");
                // mark success
                if (status && resultCode == 0) {
                    markCaptchaReturnResult.setStatus(true);
                    markCaptchaReturnResult.setMessage(message);
                    markCaptchaReturnResult.setSession(null);
                    markCaptchaReturnResult.setResult(markResultHandle(result));
                    return markCaptchaReturnResult;
                }
            }
        }catch (Exception e){
            log.error("mark captcha time out");
        } finally {
            if (response != null){
                try{
                    response.close();
                }catch (IOException e){
                    log.error(e.getMessage());
                }
            }
            try{
                markHttpClient.close();
            }catch (IOException e){
                log.error(e.getMessage());
            }
        }
        return failedMarkCaptchaReturnResult(MARK_CAPTCHA_FAILED);
    }

    private String markResultHandle(String result){
        // "[[21, 119], [246, 43]]"
        result = result.replace("[", "");
        result = result.replace("]", "");
        return result.trim();
    }

    private MarkCaptchaReturnResult failedMarkCaptchaReturnResult(String message){
        MarkCaptchaReturnResult markCaptchaReturnResult = new MarkCaptchaReturnResult();
        markCaptchaReturnResult.setStatus(false);
        markCaptchaReturnResult.setMessage(message);
        markCaptchaReturnResult.setSession(null);
        markCaptchaReturnResult.setResult("");
        return markCaptchaReturnResult;

    }

}
