package com.naonao.grab12306ticket.version.single.ticket.login;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.login.CheckLoginTokenReturnResult;
import com.naonao.grab12306ticket.version.single.ticket.login.common.AbstractLogin;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: 12306grabticket_java
 * @description: check login token
 * @author: Wen lyuzhao
 * @create: 2019-04-30 22:45
 **/
@Log4j
public class CheckLoginToken extends AbstractLogin {

    
    protected CheckLoginToken(CloseableHttpClient session){
        this.session = session;
    }

    /**
     * check login token
     *
     * @param loginToken    login token
     * @return              CheckLoginTokenReturnResult
     */
    protected CheckLoginTokenReturnResult checkLoginToken(String loginToken) {
        CheckLoginTokenReturnResult checkLoginTokenReturnResult = new CheckLoginTokenReturnResult();
        String checkLoginTkURL = "https://kyfw.12306.cn/otn/uamauthclient";
        // create requests
        HttpPost checkLoginTkRequest = HttpTools.setRequestHeader(new HttpPost(checkLoginTkURL), true, false, false);
        Map<String, String> checkLoginTkData = new HashMap<>();
        checkLoginTkData.put("tk", loginToken);
        checkLoginTkRequest.setEntity(HttpTools.doPostData(checkLoginTkData));
        CloseableHttpResponse response = null;
        try{
            response = this.session.execute(checkLoginTkRequest);
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                String responseText = HttpTools.responseToString(response);
                JSONObject checkLoginTkJsonData = JSONObject.parseObject(responseText);
                int resultCode = checkLoginTkJsonData.getIntValue("result_code");
                // success
                if (resultCode == 0){
                    checkLoginTokenReturnResult.setStatus(true);
                    checkLoginTokenReturnResult.setSession(session);
                    checkLoginTokenReturnResult.setMessage(checkLoginTkJsonData.getString("result_message"));
                    checkLoginTokenReturnResult.setLoginUserName(checkLoginTkJsonData.getString("username"));
                    checkLoginTokenReturnResult.setApptk(checkLoginTkJsonData.getString("apptk"));
                    return checkLoginTokenReturnResult;
                }else{
                // failed
                    return failedCheckLoginTokenReturnResult(session, checkLoginTkJsonData.getString("result_message"));
                }
            }
        }catch (IOException e){
            log.error(e.getMessage());
        }finally {
            if (response!=null){
                try{
                    response.close();
                }catch (IOException e){
                    log.error(e.getMessage());
                }

            }
        }
        return failedCheckLoginTokenReturnResult(session, CHECK_LOGIN_TOKEN_FAILED);
    }

    private CheckLoginTokenReturnResult failedCheckLoginTokenReturnResult(CloseableHttpClient session, String message){
        CheckLoginTokenReturnResult checkLoginTokenReturnResult = new CheckLoginTokenReturnResult();
        checkLoginTokenReturnResult.setStatus(false);
        checkLoginTokenReturnResult.setMessage(message);
        checkLoginTokenReturnResult.setSession(session);
        return checkLoginTokenReturnResult;
    }
}
