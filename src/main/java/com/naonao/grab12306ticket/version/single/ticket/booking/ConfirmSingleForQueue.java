package com.naonao.grab12306ticket.version.single.ticket.booking;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.booking.ConfirmSingleForQueueReturnResult;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import com.naonao.grab12306ticket.version.single.ticket.booking.common.AbstractBooking;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
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
public class ConfirmSingleForQueue extends AbstractBooking {

    

    protected ConfirmSingleForQueue(CloseableHttpClient session){
        this.session = session;
    }

    /**
     *      请求 confirmSingleForQueue 页面
     *
     * @param expectSeatNumber      expectSeatNumber
     * @param passengerTicketStr    passengerTicketStr
     * @param oldPassengerStr       oldPassengerStr
     * @return                      T --> true
     *                              F --> false
     */
    protected ConfirmSingleForQueueReturnResult confirmSingleForQueue(String expectSeatNumber,
                                                                      String passengerTicketStr,
                                                                      String oldPassengerStr,
                                                                      String keyCheckIsChange,
                                                                      String leftTicketStr,
                                                                      String purposeCodes,
                                                                      String repeatSubmitToken,
                                                                      String trainLocation) {

        // create request
        String confirmSingleForQueueURL = "https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue";
        HttpPost confirmSingleForQueueRequest = HttpTools.setRequestHeader(
                new HttpPost(confirmSingleForQueueURL),
                true,
                false,
                true
        );
        Map<String, String> confirmSingleForQueueData = getConfirmSingleForQueueData(
                expectSeatNumber,
                keyCheckIsChange,
                leftTicketStr,
                oldPassengerStr,
                passengerTicketStr,
                purposeCodes,
                repeatSubmitToken,
                trainLocation
        );
        confirmSingleForQueueRequest.setEntity(HttpTools.doPostData(confirmSingleForQueueData));
        CloseableHttpResponse response = null;
        ConfirmSingleForQueueReturnResult confirmSingleForQueueReturnResult = new ConfirmSingleForQueueReturnResult();
        try{
            response = this.session.execute(confirmSingleForQueueRequest);
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                String responseText = HttpTools.responseToString(response);
                boolean status = GeneralTools.getResultJsonStatus(responseText);
                boolean submitStatus = JSONObject.parseObject(responseText)
                        .getJSONObject("data")
                        .getBoolean("submitStatus");
                // success
                if (status && submitStatus){
                    confirmSingleForQueueReturnResult.setStatus(true);
                    confirmSingleForQueueReturnResult.setSession(session);
                    confirmSingleForQueueReturnResult.setMessage(CONFIREM_SINGLE_FOR_QUEUE_SUCCESS);
                    return confirmSingleForQueueReturnResult;
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
        return failedConfirmSingleForQueueReturnResult(session, CONFIREM_SINGLE_FOR_QUEUE_FAILED);
    }

    private Map<String, String> getConfirmSingleForQueueData(String expectSeatNumber,
                                                             String keyCheckIsChange,
                                                             String leftTicketStr,
                                                             String oldPassengerStr,
                                                             String passengerTicketStr,
                                                             String purposeCodes,
                                                             String repeatSubmitToken,
                                                             String trainLocation){
        expectSeatNumber = "1" + expectSeatNumber;
        Map<String, String> confirmSingleForQueueData = new HashMap<>(16);
        confirmSingleForQueueData.put("_json_att","");
        confirmSingleForQueueData.put("choose_seats",expectSeatNumber);
        confirmSingleForQueueData.put("dwAll","N");
        confirmSingleForQueueData.put("key_check_isChange", keyCheckIsChange);
        confirmSingleForQueueData.put("leftTicketStr", leftTicketStr);
        confirmSingleForQueueData.put("oldPassengerStr",oldPassengerStr);
        confirmSingleForQueueData.put("passengerTicketStr",passengerTicketStr);
        confirmSingleForQueueData.put("purpose_codes", purposeCodes);
        confirmSingleForQueueData.put("randCode", repeatSubmitToken);
        confirmSingleForQueueData.put("REPEAT_SUBMIT_TOKEN","");
        confirmSingleForQueueData.put("roomType","00");
        confirmSingleForQueueData.put("seatDetailType","000");
        confirmSingleForQueueData.put("train_location", trainLocation);
        confirmSingleForQueueData.put("whatsSelec","1");
        return confirmSingleForQueueData;
    }

    private ConfirmSingleForQueueReturnResult failedConfirmSingleForQueueReturnResult(CloseableHttpClient session, String message){
        ConfirmSingleForQueueReturnResult confirmSingleForQueueReturnResult = new ConfirmSingleForQueueReturnResult();
        confirmSingleForQueueReturnResult.setStatus(false);
        confirmSingleForQueueReturnResult.setSession(session);
        confirmSingleForQueueReturnResult.setMessage(message);
        return confirmSingleForQueueReturnResult;
    }
}
