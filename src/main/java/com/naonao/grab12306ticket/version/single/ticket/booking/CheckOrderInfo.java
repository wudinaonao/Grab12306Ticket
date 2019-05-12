package com.naonao.grab12306ticket.version.single.ticket.booking;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.constants.ConvertMap;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.booking.CheckOrderInfoReturnResult;
import com.naonao.grab12306ticket.version.single.ticket.booking.common.AbstractBooking;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-01 23:54
 **/
@Log4j
public class CheckOrderInfo extends AbstractBooking {

    

    protected CheckOrderInfo(CloseableHttpClient session){
        this.session = session;
    }

    /**
     *      request checkOrderInfo page
     *
     * @param seatTypeArr       seatTypeArr
     * @param passengerName     passengerName
     * @param documentType      documentType
     * @param documentNumber    documentNumber
     * @param mobile            mobile
     * @return                  checkOrderInfoReturnResult 对象
     *                          -->
     *                          status，passengerTicketStr，oldPassengerStr
     *                          布尔    字符串               字符串
     */
    protected CheckOrderInfoReturnResult checkOrderInfo(String[] seatTypeArr,
                                                         String passengerName,
                                                         String documentType,
                                                         String documentNumber,
                                                         String mobile,
                                                         String repeatSubmitToken,
                                                         String[] leftDetails) {

        // get an optional seat type
        List<String> optionalSeatType = getOptionalSeatType(leftDetails);
        if (optionalSeatType == null){
            log.error(NO_SEAT_TYPE_OBTAINED);
            return failedCheckOrderInfoReturnResult(session, NO_SEAT_TYPE_OBTAINED);
        }
        // get seat type number
        String seatType = getSeatType(seatTypeArr, optionalSeatType);
        // no suitable seat type
        if (seatType == null){
            log.error(NO_SUITABLE_SEAT_TYPE);
            return failedCheckOrderInfoReturnResult(session, NO_SUITABLE_SEAT_TYPE);
        }

        // join data passengerTicketStr
        String passengerTicketStr = getPassengerTicketStr(seatType, passengerName, documentType, documentNumber, mobile);
        // join data oldPassengerStr
        String oldPassengerStr = getOldPassengerStr(passengerName, documentType, documentNumber);
        // create request data
        Map<String, String> checkOrderInfoData = getCheckOrderInfoData(passengerTicketStr, oldPassengerStr, repeatSubmitToken);

        // create request
        String checkOrderInfoURL = "https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo";
        HttpPost checkOrderInfoRequest = HttpTools.setRequestHeader(new HttpPost(checkOrderInfoURL), true, false, false);
        checkOrderInfoRequest.setEntity(HttpTools.doPostData(checkOrderInfoData));
        CloseableHttpResponse response = null;
        CheckOrderInfoReturnResult checkOrderInfoReturnResult = new CheckOrderInfoReturnResult();
        try{
            response = session.execute(checkOrderInfoRequest);
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                String responseText = HttpTools.responseToString(response);
                boolean status = GeneralTools.getResultJsonStatus(responseText);
                boolean submitStatus = JSONObject.parseObject(responseText)
                        .getJSONObject("data")
                        .getBoolean("submitStatus");
                // success
                if(status && submitStatus){
                    checkOrderInfoReturnResult.setStatus(true);
                    checkOrderInfoReturnResult.setSession(session);
                    checkOrderInfoReturnResult.setMessage(CHECK_ORDER_INFO_SUCCESS);
                    checkOrderInfoReturnResult.setPassengerTicketStr(passengerTicketStr);
                    checkOrderInfoReturnResult.setOldPassengerStr(oldPassengerStr);
                    checkOrderInfoReturnResult.setSeatType(seatType);
                    return checkOrderInfoReturnResult;
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
        return failedCheckOrderInfoReturnResult(session, CHECK_ORDER_INFO_FAILED);
    }

    /**
     *      获取座位号, 从用户输入数组和从initDc页面获取的可预订数组做匹配
     *      匹配成功返回座位ID, 失败返回null
     *
     * optionalSeatType element is number
     * seatType element is name
     *
     * @param seatType      用户设置的座位ID(数组)
     * @return              T -- 座位ID
     *                      F -- null
     */
    private String getSeatType(String[] seatType, List<String> optionalSeatType){
        for (String optional: optionalSeatType){
            for (String seatName: seatType){
                if (optional.equals(ConvertMap.seatNameToNumber(seatName))){
                    return optional;
                }
            }
        }
        return null;
    }
    /**
     *      提取可以选择的座位, 设置 optionalSeatType
     *
     * @return  T -- true
     *          F -- false
     */
    private List<String> getOptionalSeatType(String[] leftDetails){

        List<String> seatList = new ArrayList<>();
        for (String line: leftDetails){
            // 一等座(933.00元)有票
            String seatTypeName = line.split("\\(")[0].trim();
            String isHasTicket = line.split("\\)")[1].trim();
            // 有票
            if (!isHasTicket.contains("无")){
                seatList.add(ConvertMap.seatNameToNumber(seatTypeName));
            }
        }
        if (seatList.size() > 0){
            // optionalSeatType = seatList;
            return seatList;
        }
        return null;
    }

    private String getPassengerTicketStr(String seatType,
                                         String passengerName,
                                         String documentType,
                                         String documentNumber,
                                         String mobile){
        // join request data passengerTicketStr
        return StringUtils.join(new String[] {
                seatType,
                "0",
                "1",
                passengerName,
                documentType,
                documentNumber,
                mobile,
                "N"}, ",");
    }

    private String getOldPassengerStr(String passengerName,
                                      String documentType,
                                      String documentNumber){
        return StringUtils.join(new String[] {
                passengerName,
                documentType,
                documentNumber,
                "1_"}, ",");
    }

    private Map<String, String> getCheckOrderInfoData(String passengerTicketStr,
                                                      String oldPassengerStr,
                                                      String repeatSubmitToken){
        // create request data
        Map<String, String> checkOrderInfoData = new HashMap<>(16);
        checkOrderInfoData.put("cancel_flag", "2");
        checkOrderInfoData.put("bed_level_order_num", "000000000000000000000000000000");
        checkOrderInfoData.put("passengerTicketStr", passengerTicketStr);
        checkOrderInfoData.put("oldPassengerStr", oldPassengerStr);
        checkOrderInfoData.put("tour_flag", "dc");
        checkOrderInfoData.put("randCode", "");
        checkOrderInfoData.put("whatsSelect", "1");
        checkOrderInfoData.put("_json_att", "");
        checkOrderInfoData.put("REPEAT_SUBMIT_TOKEN", repeatSubmitToken);
        return checkOrderInfoData;
    }

    private CheckOrderInfoReturnResult failedCheckOrderInfoReturnResult(CloseableHttpClient session, String message){
        CheckOrderInfoReturnResult checkOrderInfoReturnResult = new CheckOrderInfoReturnResult();
        checkOrderInfoReturnResult.setStatus(false);
        checkOrderInfoReturnResult.setSession(session);
        checkOrderInfoReturnResult.setMessage(message);
        return checkOrderInfoReturnResult;
    }
}
