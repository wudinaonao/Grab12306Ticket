package com.naonao.grab12306ticket.version.single.ticket.booking;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.booking.SubmitOrderRequestReturnResult;
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
 * @create: 2019-05-01 23:57
 **/

@Log4j
public class SubmitOrderRequest extends AbstractBooking {



    protected SubmitOrderRequest(CloseableHttpClient session){
        this.session = session;
    }

    protected SubmitOrderRequestReturnResult submitOrderRequest(String secretStr,
                                                                String trainDate,
                                                                String backTrainDate,
                                                                String purposeCode,
                                                                String queryFromStationName,
                                                                String queryToStationName) {

        String submitOrderRequestURL = "https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest";
        HttpPost submitOrderRequest = HttpTools.setRequestHeader(new HttpPost(submitOrderRequestURL), true, false, true);
        submitOrderRequest.addHeader("Referer", "https://kyfw.12306.cn/otn/leftTicket/init");
        submitOrderRequest.addHeader("Accept-Encoding", "gzip, deflate, br");
        // create post data
        Map<String, String> submitOrderRequestData = createPostData(secretStr,
                                                                    trainDate,
                                                                    backTrainDate,
                                                                    purposeCode,
                                                                    queryFromStationName,
                                                                    queryToStationName);

        // set body
        submitOrderRequest.setEntity(HttpTools.doPostData(submitOrderRequestData));
        CloseableHttpResponse response = null;
        SubmitOrderRequestReturnResult submitOrderRequestReturnResult = new SubmitOrderRequestReturnResult();
        try{
            response = session.execute(submitOrderRequest);
            if(response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                JSONObject submitOrderRequestJsonData = JSONObject.parseObject(HttpTools.responseToString(response));
                // status is true
                if(submitOrderRequestJsonData.getBoolean(STATUS)){
                    submitOrderRequestReturnResult.setStatus(true);
                    submitOrderRequestReturnResult.setMessage(SUBMIT_ORDER_REQUEST_SUCCESS);
                    submitOrderRequestReturnResult.setSession(session);
                    return submitOrderRequestReturnResult;
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
        return failedSubmitOrderRequestReturnResult(session, SUBMIT_ORDER_REQUEST_FAILED);
    }

    private SubmitOrderRequestReturnResult failedSubmitOrderRequestReturnResult(CloseableHttpClient session, String message){
        SubmitOrderRequestReturnResult submitOrderRequestReturnResult = new SubmitOrderRequestReturnResult();
        submitOrderRequestReturnResult.setStatus(false);
        submitOrderRequestReturnResult.setSession(session);
        submitOrderRequestReturnResult.setMessage(message);
        return submitOrderRequestReturnResult;
    }

    private Map<String, String> createPostData(String secretStr,
                                               String trainDate,
                                               String backTrainDate,
                                               String purposeCode,
                                               String queryFromStationName,
                                               String queryToStationName){
        // create post data, type is map
        Map<String, String> submitOrderRequestData = new HashMap<>();
        submitOrderRequestData.put("secretStr", GeneralTools.decodeURL(secretStr));
        submitOrderRequestData.put("train_date", trainDate);
        submitOrderRequestData.put("back_train_date", backTrainDate);
        submitOrderRequestData.put("tour_flag", "dc");
        submitOrderRequestData.put("purpose_codes", purposeCode);
        submitOrderRequestData.put("query_from_station_name", queryFromStationName);
        submitOrderRequestData.put("query_to_station_name", queryToStationName);
        submitOrderRequestData.put("undefined", "");
        return submitOrderRequestData;
    }
}
