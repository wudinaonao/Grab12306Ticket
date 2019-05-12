package com.naonao.grab12306ticket.version.single.ticket.login;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.login.StartLoginReturnResult;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
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
 * @description: login
 * @author: Wen lyuzhao
 * @create: 2019-04-30 22:58
 **/
@Log4j
public class StartLogin extends AbstractLogin {

    

    protected StartLogin(CloseableHttpClient session,
                         String username,
                         String password){
        this.session = session;
        this.username = username;
        this.password = password;
    }

    /**
     *  login
     *  this request may return a false, but message is "密码错误".
     *  if the result of "密码错误" is returned, the request after
     *  execution should be stopped immediately, because if the
     *  number of password errors exceeds 4 times, the account will
     *  be locked.
     *
     * @param markResult    captcha mark result     format：123,123,123...
     *                                              example: 123,54,125,65
     *
     * @return              StartLoginReturnResult
     */
    protected StartLoginReturnResult startLogin(String markResult) {
        String loginURL = "https://kyfw.12306.cn/passport/web/login";
        // create request
        HttpPost loginRequest = HttpTools.setRequestHeader(new HttpPost(loginURL), true, false, false);
        Map<String, String> loginData = new HashMap<>();
        loginData.put("username", username);
        loginData.put("password", password);
        loginData.put("appid", "otn");
        loginData.put("anwser", markResult);
        loginRequest.setEntity(HttpTools.doPostData(loginData));
        CloseableHttpResponse response = null;
        StartLoginReturnResult startLoginReturnResult = new StartLoginReturnResult();
        try{
            response = session.execute(loginRequest);
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
            // if (true){
                String responseText = HttpTools.responseToString(response);
                JSONObject startLoginResultJsonData = JSONObject.parseObject(responseText);
                int resultCode = startLoginResultJsonData.getIntValue("result_code");
                // password error
                if (resultCode == 1){
                    return failedStartLoginReturnResult(session, PASSWORD_ERROR);
                }
                // success login
                if (resultCode == 0){
                    startLoginReturnResult.setStatus(true);
                    startLoginReturnResult.setMessage(startLoginResultJsonData.getString("result_message"));
                    startLoginReturnResult.setSession(session);
                    startLoginReturnResult.setUamtk(startLoginResultJsonData.getString("uamtk"));
                    return startLoginReturnResult;
                }else{
                // failed login
                    return failedStartLoginReturnResult(session, startLoginResultJsonData.getString("result_message"));
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
        return failedStartLoginReturnResult(session, START_LOGIN_FAILED);
    }

    private StartLoginReturnResult failedStartLoginReturnResult(CloseableHttpClient session, String message){
        StartLoginReturnResult startLoginReturnResult = new StartLoginReturnResult();
        startLoginReturnResult.setStatus(false);
        startLoginReturnResult.setMessage(message);
        startLoginReturnResult.setSession(session);
        startLoginReturnResult.setUamtk("");
        return startLoginReturnResult;
    }
}
