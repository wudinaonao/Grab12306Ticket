package com.naonao.grab12306ticket.version.single.ticket.login;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.login.InitLoginReturnResult;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import com.naonao.grab12306ticket.version.single.ticket.base.AbstractLogin;
import lombok.extern.log4j.Log4j;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: 12306grabticket_java
 * @description: initialization login get cookies
 * @author: Wen lyuzhao
 * @create: 2019-05-02 13:39
 **/
@Log4j
public class InitLogin extends AbstractLogin {

    

    protected InitLogin(CloseableHttpClient session){
        this.session = session;
    }

    /**
     * initialization login cookies
     * get cookies RAIL_EXPIRATION and RAIL_DEVICEID
     *
     * @return  InitLoginReturnResult
     */
    protected InitLoginReturnResult initLogin(){
        // get cookies RAIL_EXPIRATION and RAIL_DEVICEID
        if (!logDevice()){
            log.error(LOGIN_DEVICE_FAILED);
            return failedInitLoginReturnResult(session, LOGIN_DEVICE_FAILED);
        }
        if (!loginConf()){
            log.error(LOGIN_CONF_FAILED);
            return failedInitLoginReturnResult(session, LOGIN_CONF_FAILED);
        }
        if (!getLoginBanner()){
            log.error(GET_LOGIN_BANNER_FAILED);
            return failedInitLoginReturnResult(session, GET_LOGIN_BANNER_FAILED);
        }
        if (!umatkStatic()){
            log.error(UMATK_STATIC_FAILED);
            return failedInitLoginReturnResult(session, UMATK_STATIC_FAILED);
        }
        // initialization cookies success
        InitLoginReturnResult initLoginReturnResult = new InitLoginReturnResult();
        initLoginReturnResult.setStatus(true);
        initLoginReturnResult.setSession(session);
        initLoginReturnResult.setMessage(INIT_COOKIES_SUCCESS);
        return initLoginReturnResult;
    }


    /**
     *  get cookies and set session:
     *
     *      RAIL_EXPIRATION
     *      RAIL_DEVICEID
     *
     * @return boolean
     */
    private Boolean logDevice(){

        // set uri get cookie ---> RAIL_DEVICEID
        String logDevice = "https://kyfw.12306.cn/otn/HttpZF/logdevice";
        String timeValue = String.valueOf(System.currentTimeMillis());
        URI uri = logDeviceUri(logDevice, timeValue);
        if (uri == null){
            return false;
        }
        HttpGet logDeviceRequest = HttpTools.setRequestHeader(new HttpGet(uri), true, null, true);
        CloseableHttpResponse response = null;
        try{
            response = session.execute(logDeviceRequest);
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                // old session cookies
                Map<String, String> oldCookiesMap = getCookieByHeaders(response.getAllHeaders());
                // parse json
                JSONObject resultJson = JSONObject.parseObject(formatJson(HttpTools.responseToString(response)));
                // create a map from json
                Map<String, String> resultMap = new HashMap<>(16);
                resultMap.put("RAIL_EXPIRATION", resultJson.getString("exp"));
                resultMap.put("RAIL_DEVICEID", resultJson.getString("dfp"));
                resultMap.putAll(oldCookiesMap);
                // set session cookies.
                // "RAIL_DEVICEID" this cookie will always be used.
                session = getNewSession(resultMap);
                return true;
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

    /**
     * get a new session and set cookies with old cookies and new cookies
     * @param cookies cookies
     * @return session
     */
    private CloseableHttpClient getNewSession(Map<String, String> cookies){
        BasicCookieStore basicCookieStore = new BasicCookieStore();
        for (Map.Entry<String, String> element : cookies.entrySet()){
            BasicClientCookie basicClientCookie = new BasicClientCookie(element.getKey(), element.getValue());
            basicClientCookie.setDomain("kyfw.12306.cn");
            basicClientCookie.setPath("/");
            basicCookieStore.addCookie(basicClientCookie);
        }
        return HttpTools.getSession(30000, basicCookieStore);
    }

    private Map<String, String> getCookieByHeaders(Header[] headers){
        Map<String, String> map = new HashMap<>();
        for (Header header: headers){
            HeaderElement[] headerElements = header.getElements();
            for (HeaderElement headerElement: headerElements){
                if ("BIGipServerotn".equals(headerElement.getName())){
                    map.put("BIGipServerotn", headerElement.getValue());
                }
            }
        }
        return map;
    }

    private String formatJson(String resultString){
        return resultString.substring(resultString.indexOf("{"), resultString.indexOf("}") + 1);
    }


    private Boolean loginConf(){
        String loginConf = "https://kyfw.12306.cn/otn/login/conf";

        HttpPost loginConfRequest = HttpTools.setRequestHeader(new HttpPost(loginConf), true, null, true);
        CloseableHttpResponse response = null;
        try{
            response = session.execute(loginConfRequest);
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                JSONObject resultJson = JSONObject.parseObject(HttpTools.responseToString(response));
                boolean status = resultJson.getBoolean("status");
                int httpStatus = resultJson.getInteger("httpstatus");
                if (status && httpStatus == HTTP_SUCCESS){
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

    private Boolean getLoginBanner(){
        String getLoginBanner = "https://kyfw.12306.cn/otn/index12306/getLoginBanner";
        HttpGet getLoginBannerRequest = HttpTools.setRequestHeader(new HttpGet(getLoginBanner), true, null, true);
        CloseableHttpResponse response = null;
        try{
            response = session.execute(getLoginBannerRequest);
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                JSONObject resultJson = JSONObject.parseObject(HttpTools.responseToString(response));
                boolean status = resultJson.getBoolean("status");
                int httpStatus = resultJson.getInteger("httpstatus");
                if (status && httpStatus == HTTP_SUCCESS){
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

    private Boolean umatkStatic(){
        String umatkStatic = "https://kyfw.12306.cn/passport/web/auth/uamtk-static";
        HttpPost umatkStaticRequest = HttpTools.setRequestHeader(new HttpPost(umatkStatic), true, null, true);
        Map<String, String> umatkStaticData = new HashMap<>(16);
        umatkStaticData.put("appid", "otn");
        umatkStaticRequest.setEntity(HttpTools.doPostData(umatkStaticData));
        CloseableHttpResponse response = null;
        try{
            response = session.execute(umatkStaticRequest);
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                return true;
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

    private URI logDeviceUri(String url, String timeValue){
        URI uri;
        try{
            uri = new URIBuilder(url)
                    .setParameter("algID", "n92OblAvAq")
                    .setParameter("hashCode", "JS_FQ3BTVLvWMwH8qILnJjjT3w1Yy1yoRWlF6QkC6Vs")
                    .setParameter("FMQw", "0")
                    .setParameter("q4f3", "zh-CN")
                    .setParameter("VySQ", "FGHk8hqpvc5Q_Z7mhyp31spb7lnu9gYr")
                    .setParameter("VPIf", "1")
                    .setParameter("custID", "133")
                    .setParameter("VEek", "unknown")
                    .setParameter("dzuS", "0")
                    .setParameter("yD16", "0")
                    .setParameter("EOQP", "8f58b1186770646318a429cb33977d8c")
                    .setParameter("lEnu", "3232235976")
                    .setParameter("jp76", "52d67b2a5aa5e031084733d5006cc664")
                    .setParameter("hAqN", "Win32")
                    .setParameter("platform", "WEB")
                    .setParameter("ks0Q", "d22ca0b81584fbea62237b14bd04c866")
                    .setParameter("TeRS", "1040x1920")
                    .setParameter("tOHY", "24xx1080x1920")
                    .setParameter("Fvje", "i1l1o1s1")
                    .setParameter("q5aJ", "-2")
                    .setParameter("wNLf", "99115dfb07133750ba677d055874de87")
                    .setParameter("0aew", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
                    .setParameter("E3gR", "a0be7d173523aca243213e9d8aa434c9")
                    .setParameter("timestamp", timeValue)
                    .build();
            return uri;
        }catch (URISyntaxException e){
            log.error(e.getMessage());
            return null;
        }
    }

    private InitLoginReturnResult failedInitLoginReturnResult(CloseableHttpClient session, String message){
        InitLoginReturnResult initLoginReturnResult = new InitLoginReturnResult();
        initLoginReturnResult.setStatus(false);
        initLoginReturnResult.setSession(session);
        initLoginReturnResult.setMessage(message);
        return initLoginReturnResult;
    }

}
