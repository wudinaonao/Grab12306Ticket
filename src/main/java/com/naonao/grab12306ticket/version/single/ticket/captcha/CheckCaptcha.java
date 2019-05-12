package com.naonao.grab12306ticket.version.single.ticket.captcha;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.captcha.CheckCaptchaReturnResult;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import com.naonao.grab12306ticket.version.single.ticket.captcha.common.AbstractCaptcha;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @program: 12306grabticket_java
 * @description: check captcha
 * @author: Wen lyuzhao
 * @create: 2019-04-29 22:59
 **/
@Log4j
public class CheckCaptcha extends AbstractCaptcha{


    protected CheckCaptcha(CloseableHttpClient session){
        this.session = session;
    }
    

    /**
     * check captcha
     *
     * @param paramsCallback    check captcha time value params
     * @param markResult        mark result
     * @param timeValue         time value
     * @return                  CheckCaptchaReturnResult
     *                          status， message, session
     */
    protected CheckCaptchaReturnResult checkCaptcha(String paramsCallback,
                                                    String markResult,
                                                    String timeValue) {
        String checkURL = "https://kyfw.12306.cn/passport/captcha/captcha-check";
        // create request
        URI uri = doUri(checkURL, paramsCallback, markResult, timeValue);
        if (uri == null){
            log.error("uri is null");
            return failedCheckCaptchaReturnResult(session, CHECK_CAPTCHA_FAILED);
        }
        HttpGet checkHttpGet = HttpTools.setRequestHeader(new HttpGet(uri), true, false, false);
        CloseableHttpResponse response = null;
        CheckCaptchaReturnResult checkCaptchaReturnResult = new CheckCaptchaReturnResult();
        try{
            // get response
            response = session.execute(checkHttpGet);
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                String responseText = HttpTools.responseToString(response);
                if (CHECK_CAPTCHA_SUCCESS_CODE.equals(getResultCode(responseText))){
                    checkCaptchaReturnResult.setStatus(true);
                    checkCaptchaReturnResult.setMessage(CHECK_CAPTCHA_SUCCESS);
                    checkCaptchaReturnResult.setSession(session);
                    return checkCaptchaReturnResult;
                }
            }
        }catch (IOException e){
            log.error(e.getMessage());
        }
        finally {
            if (response != null){
                try{
                    response.close();
                }catch (IOException e){
                    log.error(e.getMessage());
                }
            }
        }
        return failedCheckCaptchaReturnResult(session, CHECK_CAPTCHA_FAILED);
    }

    private URI doUri(String checkURL,
                      String paramsCallback,
                      String markResult,
                      String timeValue){
        timeValue = String.valueOf(Long.valueOf(timeValue) + 1L);
        // create request
        URI uri;
        try{
            uri = new URIBuilder(checkURL)
                    .setParameter("callback", paramsCallback)
                    .setParameter("answer", markResult)
                    .setParameter("rand", "sjrand")
                    .setParameter("login_site", "E")
                    .setParameter("_", timeValue)
                    .build();
        }catch (URISyntaxException e){
            log.error(e.getMessage());
            return null;
        }
        return uri;
    }

    private CheckCaptchaReturnResult failedCheckCaptchaReturnResult(CloseableHttpClient session, String message){
        CheckCaptchaReturnResult checkCaptchaReturnResult = new CheckCaptchaReturnResult();
        checkCaptchaReturnResult.setStatus(false);
        checkCaptchaReturnResult.setMessage(message);
        checkCaptchaReturnResult.setSession(session);
        return checkCaptchaReturnResult;
    }
    private Integer getResultCode(String responseText){
        responseText = responseText.substring(responseText.indexOf("(")+1, responseText.length()-2);
        JSONObject jsonData = JSONObject.parseObject(responseText);
        return jsonData.getIntValue("result_code");
    }
}
