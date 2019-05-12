package com.naonao.grab12306ticket.version.single.ticket.booking;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.booking.ResultBookingTicketHtmlReturnResult;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import com.naonao.grab12306ticket.version.single.ticket.booking.common.AbstractBooking;
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
 * @create: 2019-05-01 23:56
 **/

@Log4j
public class ResultBookingTicketHtml extends AbstractBooking {



    protected ResultBookingTicketHtml(CloseableHttpClient session){
        this.session = session;
    }


    /**
     *      获取订票返回结果页面，并设置 bookingTicketResultInfo
     *
     * @return                  T --> true
     *                          F --> false
     */
    public ResultBookingTicketHtmlReturnResult resultBookingTicketHtml(String repeatSubmitToken) {

        // create request
        String resultBookingTicketURL = "https://kyfw.12306.cn/otn//payOrder/init?random=" + String.valueOf(System.currentTimeMillis());
        HttpPost resultBookingTicketRequest = HttpTools.setRequestHeader(new HttpPost(resultBookingTicketURL), true, false, false);
        Map<String, String> resultBookingTicketData = getResultBookingTicketData(repeatSubmitToken);
        resultBookingTicketRequest.setEntity(HttpTools.doPostData(resultBookingTicketData));
        CloseableHttpResponse response = null;
        try{
            response = session.execute(resultBookingTicketRequest);
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                String responseText = HttpTools.responseToString(response);
                String resultPassangerTicketStr = getBookingTicketResultMap(responseText);
                // success
                if (resultPassangerTicketStr != null){
                    return successResultBookingTicketHtmlReturnResult(resultPassangerTicketStr);
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
        return failedResultBookingTicketHtmlReturnResult(session, RESULT_BOOKING_TICKET_HTML_FAILED);
    }

    /**
     *      提取 passangerTicketList 字段从网页源码
     *
     * @param htmlText      resultBookingTicketHtml 网页源码
     * @return              T --> passangerTicketList 字符串
     *                      F --> ""
     */
    private String getBookingTicketResultMap(String htmlText){
        try {
            String[] scriptList = htmlText.split("\n");
            for (String line: scriptList){
                if(line.contains("var passangerTicketList")){
                    return line.substring(line.indexOf("["), line.length()-1);
                }
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private Map<String, String> getResultBookingTicketData(String repeatSubmitToken){
        Map<String, String> resultBookingTicketData = new HashMap<>(16);
        resultBookingTicketData.put("_json_att", "");
        resultBookingTicketData.put("REPEAT_SUBMIT_TOKEN", repeatSubmitToken);
        return resultBookingTicketData;
    }

    private ResultBookingTicketHtmlReturnResult failedResultBookingTicketHtmlReturnResult(CloseableHttpClient session, String message){
        ResultBookingTicketHtmlReturnResult resultBookingTicketHtmlReturnResult = new ResultBookingTicketHtmlReturnResult();
        resultBookingTicketHtmlReturnResult.setStatus(false);
        resultBookingTicketHtmlReturnResult.setSession(session);
        resultBookingTicketHtmlReturnResult.setMessage(message);
        return resultBookingTicketHtmlReturnResult;
    }

    private ResultBookingTicketHtmlReturnResult successResultBookingTicketHtmlReturnResult(String resultPassangerTicketStr){

        // format resultPassangerTicketStr
        resultPassangerTicketStr = resultPassangerTicketStr.replace("'", "\"");

        // format json and create json object
        JSONArray bookingTicketResultList = JSONArray.parseArray(resultPassangerTicketStr);
        JSONObject jsonData = bookingTicketResultList.getJSONObject(0);
        JSONObject jsonPassengerDTO = jsonData.getJSONObject("passengerDTO");
        JSONObject jsonPayOrderString = jsonData.getJSONObject("payOrderString");
        JSONObject jsonStationDTO = jsonData.getJSONObject("stationTrainDTO");

        // set result
        ResultBookingTicketHtmlReturnResult resultBookingTicketHtmlReturnResult = new ResultBookingTicketHtmlReturnResult();
        resultBookingTicketHtmlReturnResult.setStatus(true);
        resultBookingTicketHtmlReturnResult.setSession(session);
        resultBookingTicketHtmlReturnResult.setMessage(RESULT_BOOKING_TICKET_HTML_SUCCESS);
        // sequence no
        resultBookingTicketHtmlReturnResult.setSequenceNo(jsonData.getString("sequence_no"));
        // passenger
        resultBookingTicketHtmlReturnResult.setPassengerIdTypeName(jsonPassengerDTO.getString("passenger_id_type_name"));
        resultBookingTicketHtmlReturnResult.setPassengerIdNo(jsonPassengerDTO.getString("passenger_id_no"));
        resultBookingTicketHtmlReturnResult.setPassengerName(jsonPassengerDTO.getString("passenger_name"));
        // seat
        resultBookingTicketHtmlReturnResult.setCoachName(jsonData.getString("coach_name"));
        resultBookingTicketHtmlReturnResult.setSeatName(jsonData.getString("seat_name"));
        resultBookingTicketHtmlReturnResult.setSeatTypeName(jsonData.getString("seat_type_name"));
        // station
        resultBookingTicketHtmlReturnResult.setFromStationName(jsonStationDTO.getString("from_station_name"));
        resultBookingTicketHtmlReturnResult.setToStationName(jsonStationDTO.getString("to_station_name"));
        resultBookingTicketHtmlReturnResult.setStationTrainCode(jsonStationDTO.getString("station_train_code"));
        // station date
        resultBookingTicketHtmlReturnResult.setStartTrainDate(jsonData.getString("start_train_date_page"));
        // ticket
        resultBookingTicketHtmlReturnResult.setTicketPrice(jsonData.getString("str_ticket_price_page"));
        resultBookingTicketHtmlReturnResult.setTicketNo(jsonData.getString("ticket_no"));
        resultBookingTicketHtmlReturnResult.setTicketTypeName(jsonData.getString("ticket_type_name"));

        return resultBookingTicketHtmlReturnResult;
    }
}
