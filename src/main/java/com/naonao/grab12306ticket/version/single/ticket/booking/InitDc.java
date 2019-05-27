package com.naonao.grab12306ticket.version.single.ticket.booking;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.booking.InitDcReturnResult;
import com.naonao.grab12306ticket.version.single.ticket.base.AbstractBooking;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-01 23:55
 **/
@Log4j
public class InitDc extends AbstractBooking {

    

    protected InitDc(CloseableHttpClient session){
        this.session = session;
    }

    /**
     *      请求 initDc 页面
     *      这个页面会返回很多有用的信息，从这个页面里提取订票需要用到的信息
     *      设置属性：
     *                  reportSubmitToken   字符串
     *                  initHtmlInfo        类
     *
     * @return          T --> true
     *                  F --> false
     */
    protected InitDcReturnResult initDc() {
        String initDcURL = "https://kyfw.12306.cn/otn/confirmPassenger/initDc";
        HttpPost initDcRequest = HttpTools.setRequestHeader(new HttpPost(initDcURL), true, false, true);
        Map<String, String> initDcData = new HashMap<>(16);
        initDcData.put("_json_att", "");
        initDcRequest.setEntity(HttpTools.doPostData(initDcData));
        CloseableHttpResponse response = null;
        InitDcReturnResult initDcReturnResult = new InitDcReturnResult();
        try{
            response = this.session.execute(initDcRequest);
            if(response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                String responseText = HttpTools.responseToString(response);
                // get repeatSubmitToken
                String repeatSubmitToken = getRepeatSubmitToken(responseText);
                if (repeatSubmitToken != null){
                    initDcReturnResult.setStatus(true);
                    initDcReturnResult.setMessage(INIT_DC_SUCCESS);
                    initDcReturnResult.setSession(session);
                    initDcReturnResult.setRepeatSubmitToken(repeatSubmitToken);
                    return setInitHtmlInfo(responseText, initDcReturnResult);
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
        return failedInitDcReturnResult(session, INIT_DC_FAILED);
    }

    private InitDcReturnResult setInitHtmlInfo(String initHtmlStr, InitDcReturnResult initDcReturnResult){
        // create json object
        String jsonString = ticketInfoForPassengerForm(initHtmlStr);
        if (jsonString == null){
            return failedInitDcReturnResult(session, INIT_DC_FAILED);
        }
        JSONObject allJsonData = JSONObject.parseObject(jsonString);
        JSONObject orderRequestDTOJsonData = allJsonData.getJSONObject("orderRequestDTO");
        JSONObject orderRequestDTOTrainDateTimeJsonData = orderRequestDTOJsonData.getJSONObject("train_date");
        JSONObject queryLeftTicketRequestDTOJsonData = allJsonData.getJSONObject("queryLeftTicketRequestDTO");
        // set value
        initDcReturnResult.setTrainDateTime(String.valueOf(orderRequestDTOTrainDateTimeJsonData.getLong("time")));
        initDcReturnResult.setFromStationTelecode(orderRequestDTOJsonData.getString("from_station_telecode"));
        initDcReturnResult.setLeftTicketStr(allJsonData.getString("leftTicketStr"));
        initDcReturnResult.setPurposeCodes(allJsonData.getString("purpose_codes"));
        initDcReturnResult.setStationTrainCode(orderRequestDTOJsonData.getString("station_train_code"));
        initDcReturnResult.setToStationTelecode(orderRequestDTOJsonData.getString("to_station_telecode"));
        initDcReturnResult.setTrainLocation(allJsonData.getString("train_location"));
        initDcReturnResult.setTrainNo(queryLeftTicketRequestDTOJsonData.getString("train_no"));
        initDcReturnResult.setLeftDetails(allJsonData.getString("leftDetails")
                                                    .replace("[","")
                                                    .replace("]", "")
                                                    .replace("\"","")
                                                    .split(","));
        initDcReturnResult.setKeyCheckIsChange(allJsonData.getString("key_check_isChange"));
        return initDcReturnResult;
    }

    private String ticketInfoForPassengerForm(String initHtmlStr){
        String[] initHtmlStringArray = initHtmlStr.split("\n");
        for (String initHtmlRow: initHtmlStringArray){
            initHtmlRow = initHtmlRow.trim();
            if (initHtmlRow.length() > 32){
                String prefix = initHtmlRow.substring(0, 31);
                if ("var ticketInfoForPassengerForm=".equals(prefix)){
                    String jsonString = initHtmlRow.substring(31, initHtmlRow.length() - 1);
                    jsonString = jsonString.replace("'","\"");
                    return jsonString;
                }
            }
        }
        return null;
    }

    /**
     *      get reportSubmitToken by html
     *
     * @param htmlText  html text
     * @return          reportSubmitToken
     */
    private String getRepeatSubmitToken(String htmlText){
        try {
            String[] scriptList = htmlText.split("\n");
            for (String line: scriptList){
                if(line.contains("globalRepeatSubmitToken")){
                    return line.substring(line.indexOf("'")+1, line.length()-2);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private InitDcReturnResult failedInitDcReturnResult(CloseableHttpClient session, String message){
        InitDcReturnResult initDcReturnResult = new InitDcReturnResult();
        initDcReturnResult.setStatus(false);
        initDcReturnResult.setSession(session);
        initDcReturnResult.setMessage(message);
        return initDcReturnResult;
    }

}
