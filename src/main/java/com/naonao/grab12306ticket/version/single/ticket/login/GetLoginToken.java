package com.naonao.grab12306ticket.version.single.ticket.login;

import com.alibaba.fastjson.JSONObject;

import com.naonao.grab12306ticket.version.single.resultclass.ticket.login.GetLoginTokenReturnResult;
import com.naonao.grab12306ticket.version.single.ticket.login.common.AbstractLogin;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: 12306grabticket_java
 * @description: get login token
 * @author: Wen lyuzhao
 * @create: 2019-04-30 22:43
 **/
@Log4j
public class GetLoginToken extends AbstractLogin {

    

    protected GetLoginToken(CloseableHttpClient session){
        this.session = session;
    }

    /**
     *      get login token
     *
     * @return              GetLoginTokenReturnResult
     */
    protected GetLoginTokenReturnResult getLoginToken(){

        String getLoginTkURL = "https://kyfw.12306.cn/passport/web/auth/uamtk";
        // creaete requests
        HttpPost getLoginTkRequest = HttpTools.setRequestHeader(new HttpPost(getLoginTkURL), true, false, false);
        Map<String, String> getLoginTokenData = new HashMap<>(16);
        getLoginTokenData.put("appid", "otn");
        getLoginTkRequest.setEntity(HttpTools.doPostData(getLoginTokenData));
        CloseableHttpResponse response = null;
        GetLoginTokenReturnResult getLoginTokenReturnResult = new GetLoginTokenReturnResult();
        try{
            response = session.execute(getLoginTkRequest);
            // http request success
            if(response.getStatusLine().getStatusCode() == HTTP_SUCCESS) {
                String responseText = HttpTools.responseToString(response);
                JSONObject getLoginTkResultJsonData = JSONObject.parseObject(responseText);
                int resultCode = getLoginTkResultJsonData.getIntValue("result_code");
                // verified success
                if (resultCode == 0){
                    getLoginTokenReturnResult.setStatus(true);
                    getLoginTokenReturnResult.setMessage(getLoginTkResultJsonData.getString("result_message"));
                    getLoginTokenReturnResult.setSession(session);
                    getLoginTokenReturnResult.setLoginToken(getLoginTkResultJsonData.getString("newapptk"));
                    return getLoginTokenReturnResult;
                }else{
                // verified failed
                    return failedGetLoginTokenReturnResult(session, getLoginTkResultJsonData.getString("result_message"));
                }
            }
        }catch (IOException e){
            log.error(e.getMessage());
        }
        finally {
            if (response!=null){
                try{
                    response.close();
                }catch (IOException e){
                    log.error(e.getMessage());
                }

            }
        }
        return failedGetLoginTokenReturnResult(session, GET_LOGIN_TOKEN_FAILED);
    }

    private GetLoginTokenReturnResult failedGetLoginTokenReturnResult(CloseableHttpClient session, String message){
        GetLoginTokenReturnResult getLoginTokenReturnResult = new GetLoginTokenReturnResult();
        getLoginTokenReturnResult.setStatus(false);
        getLoginTokenReturnResult.setMessage(message);
        getLoginTokenReturnResult.setSession(session);
        getLoginTokenReturnResult.setLoginToken("");
        return getLoginTokenReturnResult;
    }
}
