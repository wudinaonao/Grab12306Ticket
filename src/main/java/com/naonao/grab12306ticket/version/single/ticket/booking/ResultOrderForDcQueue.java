package com.naonao.grab12306ticket.version.single.ticket.booking;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.booking.ResultOrderForDcQueueReturnResult;
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
public class ResultOrderForDcQueue extends AbstractBooking {

    

    protected ResultOrderForDcQueue(CloseableHttpClient session){
        this.session = session;
    }

    /**
     *      请求 resultOrderForQueue 页面
     *
     * @param orderId   orderId
     * @return          T --> true
     *                  F --> false
     */
    protected ResultOrderForDcQueueReturnResult resultOrderForQueue(String orderId, String repeatSubmitToken) {
        // create request
        String resultOrderForQueueURL = "https://kyfw.12306.cn/otn/confirmPassenger/resultOrderForDcQueue";
        HttpPost resultOrderForQueueRequest = HttpTools.setRequestHeader(
                new HttpPost(resultOrderForQueueURL),
                true,
                false,
                true
        );
        Map<String, String> resultOrderForQueueData = gerResultOrderForQueueData(orderId, repeatSubmitToken);
        resultOrderForQueueRequest.setEntity(HttpTools.doPostData(resultOrderForQueueData));
        CloseableHttpResponse response = null;

        try{
            response = session.execute(resultOrderForQueueRequest);
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                String responseText = HttpTools.responseToString(response);
                boolean status = JSONObject.parseObject(responseText)
                        .getBoolean("status");
                boolean submitStatus = JSONObject.parseObject(responseText)
                        .getJSONObject("data")
                        .getBoolean("submitStatus");
                // success
                if (status && submitStatus){
                    ResultOrderForDcQueueReturnResult resultOrderForDcQueueReturnResult = new ResultOrderForDcQueueReturnResult();
                    resultOrderForDcQueueReturnResult.setStatus(true);
                    resultOrderForDcQueueReturnResult.setSession(session);
                    resultOrderForDcQueueReturnResult.setMessage(RESULT_ORDER_FOR_QUEUE_SUCCESS);
                    return resultOrderForDcQueueReturnResult;
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
        return failedResultOrderForDcQueueReturnResult(session, RESULT_ORDER_FOR_QUEUE_FAILED);
    }

    private Map<String, String> gerResultOrderForQueueData(String orderId,
                                                           String repeatSubmitToken){
        Map<String, String> resultOrderForQueueData = new HashMap<>(16);
        resultOrderForQueueData.put("_json_att", "");
        resultOrderForQueueData.put("orderSequence_no", orderId);
        resultOrderForQueueData.put("REPEAT_SUBMIT_TOKEN", repeatSubmitToken);
        return resultOrderForQueueData;
    }

    private ResultOrderForDcQueueReturnResult failedResultOrderForDcQueueReturnResult(CloseableHttpClient session, String message){
        ResultOrderForDcQueueReturnResult resultOrderForDcQueueReturnResult = new ResultOrderForDcQueueReturnResult();
        resultOrderForDcQueueReturnResult.setStatus(false);
        resultOrderForDcQueueReturnResult.setSession(session);
        resultOrderForDcQueueReturnResult.setMessage(message);
        return resultOrderForDcQueueReturnResult;
    }

}
