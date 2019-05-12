package com.naonao.grab12306ticket.version.single.ticket.booking;

import com.naonao.grab12306ticket.version.single.resultclass.ticket.booking.GetPassengerDTOsReturnResult;
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
 * @create: 2019-05-01 23:55
 **/
@Log4j
public class GetPassengerDTOs extends AbstractBooking {

    

    protected GetPassengerDTOs(CloseableHttpClient session){
        this.session = session;
    }

    /**
     *       请求 getPassengerDto 页面
     *       这个页面会返回一个json信息，里面包含当前登陆用户的“常用乘客”信息，但是似乎对订票没什么用？这里暂时不做解析。
     *
     * @return          T --> true
     *                  F --> false
     */
    protected GetPassengerDTOsReturnResult getPassengerDto(String repeatSubmitToken) {
        String getPassengerDtoURL = "https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs";
        HttpPost getPassengerDtoRequest = HttpTools.setRequestHeader(new HttpPost(getPassengerDtoURL), true, false, true);
        getPassengerDtoRequest.setEntity(HttpTools.doPostData(createGetPasengerDtoData(repeatSubmitToken)));
        CloseableHttpResponse response = null;
        GetPassengerDTOsReturnResult getPassengerDTOsReturnResult = new GetPassengerDTOsReturnResult();
        try{
            response = session.execute(getPassengerDtoRequest);
            if(response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                getPassengerDTOsReturnResult.setStatus(true);
                getPassengerDTOsReturnResult.setMessage(GET_PASSENGER_DTO_SUCCESS);
                getPassengerDTOsReturnResult.setSession(session);
                return getPassengerDTOsReturnResult;
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
        return failedGetPassengerDTOsReturnResult(session, GET_PASSENGER_DTO_FAILED);
    }

    private Map<String, String> createGetPasengerDtoData(String repeatSubmitToken){
        Map<String, String> getPasengerDtoData = new HashMap<>();
        getPasengerDtoData.put("_json_att", "");
        getPasengerDtoData.put("REPEAT_SUBMIT_TOKEN", repeatSubmitToken);
        return getPasengerDtoData;
    }

    private GetPassengerDTOsReturnResult failedGetPassengerDTOsReturnResult(CloseableHttpClient session, String message){
        GetPassengerDTOsReturnResult getPassengerDTOsReturnResult = new GetPassengerDTOsReturnResult();
        getPassengerDTOsReturnResult.setStatus(false);
        getPassengerDTOsReturnResult.setSession(session);
        getPassengerDTOsReturnResult.setMessage(message);
        return getPassengerDTOsReturnResult;
    }
}
