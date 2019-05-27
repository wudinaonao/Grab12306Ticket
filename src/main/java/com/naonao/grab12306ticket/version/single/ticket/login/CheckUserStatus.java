package com.naonao.grab12306ticket.version.single.ticket.login;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import com.naonao.grab12306ticket.version.single.ticket.base.AbstractLogin;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: 12306grabticket_java
 * @description: check user login status
 * @author: Wen lyuzhao
 * @create: 2019-04-30 22:49
 **/
@Log4j
public class CheckUserStatus extends AbstractLogin {

    

    public CheckUserStatus(CloseableHttpClient session){
        this.session = session;
    }

    /**
     *      check user login status
     *      input a session, check is logged in
     *
     * @return                  T --> true
     *                          F --> false
     */
    public Boolean checkUserStatus() {

        String checkUserURL = "https://kyfw.12306.cn/otn/login/checkUser";
        // create request
        HttpPost checkUserRequest = HttpTools.setRequestHeader(new HttpPost(checkUserURL), true, true, false);
        Map<String, String> checkUserStatusData = getCheckUserStatusData();
        checkUserRequest.setEntity(HttpTools.doPostDataFromJson(checkUserStatusData));
        CloseableHttpResponse response = null;
        try{
            response = session.execute(checkUserRequest);
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                String responseText = HttpTools.responseToString(response);
                boolean status = JSONObject.parseObject(responseText).getBoolean("status");
                boolean flag   = JSONObject.parseObject(responseText)
                        .getJSONObject("data")
                        .getBoolean("flag");
                // success
                if (status && flag){
                    return true;
                }
            }
        }catch (IOException e){
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

    private Map<String, String> getCheckUserStatusData(){
        Map<String, String> checkUserStatusData = new HashMap<>(16);
        checkUserStatusData.put("_json_att", "");
        return checkUserStatusData;
    }

}
