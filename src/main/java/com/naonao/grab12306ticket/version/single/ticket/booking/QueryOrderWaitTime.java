package com.naonao.grab12306ticket.version.single.ticket.booking;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.booking.QueryOrderWaitTimeReturnResult;
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
public class QueryOrderWaitTime extends AbstractBooking {



    protected QueryOrderWaitTime(CloseableHttpClient session){
        this.session = session;
    }

    /**
     *      请求 queryOrderWaitTime 页面
     *      this request return orderID
     *
     * @return  queryOrderWaitTimeReturnResult 对象
     *          status,  orderId
     *          布尔      字符串
     */
    protected QueryOrderWaitTimeReturnResult queryOrderWaitTime(String repeatSubmitToken) {
        String queryOrderWaitTimeURL = "https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime";
        HttpPost queryOrderWaitTimeRequest = HttpTools.setRequestHeader(new HttpPost(queryOrderWaitTimeURL), true, false, true);

        String orderId = null;
        int waitTime = 10;
        int requestNum = 0;
        while (orderId == null && waitTime > 0){
            requestNum++;
            log.info(String.format("getting wait time information ...... number of attempts：%d", requestNum));
            Map<String, String> queryOrderWaitTimeData = getQueryOrderWaitTimeData(repeatSubmitToken);
            queryOrderWaitTimeRequest.setEntity(HttpTools.doPostData(queryOrderWaitTimeData));
            CloseableHttpResponse response = null;
            try{
                response = session.execute(queryOrderWaitTimeRequest);
                if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                    String responseText = HttpTools.responseToString(response);
                    boolean status = JSONObject.parseObject(responseText)
                            .getBoolean("status");
                    boolean queryOrderWaitTimeStatus = JSONObject.parseObject(responseText)
                            .getJSONObject("data")
                            .getBoolean("queryOrderWaitTimeStatus");
                    JSONObject jsonData = JSONObject.parseObject(responseText).getJSONObject("data");
                    // success
                    if (status && queryOrderWaitTimeStatus){
                        orderId = jsonData.getString("orderId");
                        waitTime = jsonData.getIntValue("waitTime");
                        String result = String.format("order id：%s    wait time：%d s", orderId, waitTime);
                        log.info(result);
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
        }
        QueryOrderWaitTimeReturnResult queryOrderWaitTimeReturnResult = new QueryOrderWaitTimeReturnResult();
        queryOrderWaitTimeReturnResult.setStatus(true);
        queryOrderWaitTimeReturnResult.setSession(session);
        queryOrderWaitTimeReturnResult.setMessage(QUERY_ORDER_WAIT_TIME_SUCCESS);
        queryOrderWaitTimeReturnResult.setOrderId(orderId);
        return queryOrderWaitTimeReturnResult;
    }

    private Map<String, String> getQueryOrderWaitTimeData(String repeatSubmitToken){
        Map<String, String> queryOrderWaitTimeData = new HashMap<>(16);
        queryOrderWaitTimeData.put("_json_att", "");
        queryOrderWaitTimeData.put("random", String.valueOf(System.currentTimeMillis()));
        queryOrderWaitTimeData.put("REPEAT_SUBMIT_TOKEN", repeatSubmitToken);
        queryOrderWaitTimeData.put("tourFlag", "dc");
        return queryOrderWaitTimeData;
    }

    private QueryOrderWaitTimeReturnResult failedQueryOrderWaitTimeReturnResult(CloseableHttpClient session, String message){
        QueryOrderWaitTimeReturnResult queryOrderWaitTimeReturnResult = new QueryOrderWaitTimeReturnResult();
        queryOrderWaitTimeReturnResult.setStatus(false);
        queryOrderWaitTimeReturnResult.setSession(session);
        queryOrderWaitTimeReturnResult.setMessage(message);
        return queryOrderWaitTimeReturnResult;
    }
}
