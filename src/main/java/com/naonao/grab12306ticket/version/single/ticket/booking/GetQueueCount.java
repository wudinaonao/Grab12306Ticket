package com.naonao.grab12306ticket.version.single.ticket.booking;

import com.naonao.grab12306ticket.version.single.resultclass.ticket.booking.GetQueueCountReturnResult;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import com.naonao.grab12306ticket.version.single.ticket.base.AbstractBooking;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-01 23:55
 **/
@Log4j
public class GetQueueCount extends AbstractBooking {



    protected GetQueueCount(CloseableHttpClient session){
        this.session = session;
    }

    /**
     *      请求 getQueueCount 页面
     *      这个页面可能会产生一个错误, 因为没有使用 mergerDataToString 这个方法?
     *
     * @param seatType  seatType
     * @return          T --> true
     *                  F --> false
     */
    protected GetQueueCountReturnResult getQueueCount(String seatType,
                                                      String repeatSubmitToken,
                                                      String leftTicketStr,
                                                      String trainDateTime,
                                                      String fromStationTelecode,
                                                      String purposeCodes,
                                                      String stationTrainCode,
                                                      String toStationTelecode,
                                                      String trainLocation,
                                                      String trainNo) {



        // check repeatSubmitToken and leftTicketStr
        if ("".equals(repeatSubmitToken) || "".equals(leftTicketStr)){
            log.error(REPEAT_SUBMIT_TOKEN_OR_LEFT_TICKET_STR_EMPTY);
            return failedGetQueueCountReturnResult(session, REPEAT_SUBMIT_TOKEN_OR_LEFT_TICKET_STR_EMPTY);
        }

        // get trainDateGMT
        String trainDateGMT = getTrainDateGMT(trainDateTime);

        // create request data
        Map<String, String> getQueueCountData = getGetQueueCountData(
                fromStationTelecode,
                leftTicketStr,
                purposeCodes,
                repeatSubmitToken,
                seatType,
                stationTrainCode,
                toStationTelecode,
                trainDateGMT,
                trainLocation,
                trainNo
        );

        // this maybe have a error
        // maybe need a method -> mergerDataToString
        // create request
        String getQueueCountURL = "https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount";
        HttpPost getQueueCountRequest = HttpTools.setRequestHeader(new HttpPost(getQueueCountURL), true, false, true);
        // getQueueCountRequest.setEntity(Tools.doPostData(getQueueCountData));
        getQueueCountRequest.setEntity(new StringEntity(mergerDataToString(getQueueCountData),"utf-8"));
        CloseableHttpResponse response = null;
        GetQueueCountReturnResult getQueueCountReturnResult = new GetQueueCountReturnResult();
        try{
            response = this.session.execute(getQueueCountRequest);
            if(response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                String responseText = HttpTools.responseToString(response);
                boolean status = GeneralTools.getResultJsonStatus(responseText);
                // success
                if(status){
                    getQueueCountReturnResult.setStatus(true);
                    getQueueCountReturnResult.setSession(session);
                    getQueueCountReturnResult.setMessage(GET_QUEUE_COUNT_SUCCESS);
                    return getQueueCountReturnResult;
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
        return failedGetQueueCountReturnResult(session, GET_QUEUE_COUNT_FAILED);
    }

    private Map<String, String> getGetQueueCountData(String fromStationTelecode,
                                                     String leftTicketStr,
                                                     String purposeCodes,
                                                     String repeatSubmitToken,
                                                     String seatType,
                                                     String stationTrainCode,
                                                     String toStationTelecode,
                                                     String trainDateGMT,
                                                     String trainLocation,
                                                     String trainNo){
        // create request data
        Map<String, String> getQueueCountData = new HashMap<>(16);
        getQueueCountData.put("_json_att","");
        getQueueCountData.put("from_station_telecode", fromStationTelecode);
        getQueueCountData.put("leftTicket", leftTicketStr);
        getQueueCountData.put("purpose_codes", purposeCodes);
        getQueueCountData.put("REPEAT_SUBMIT_TOKEN", repeatSubmitToken);
        getQueueCountData.put("seatType",seatType);
        getQueueCountData.put("stationTrainCode",stationTrainCode);
        getQueueCountData.put("toStationTelecode", toStationTelecode);
        getQueueCountData.put("train_date", trainDateGMT);
        getQueueCountData.put("train_location", trainLocation);
        getQueueCountData.put("train_no", trainNo);
        return getQueueCountData;
    }
    /**
     *      获取 trainDate GMT 字符串
     *
     * @param trainDate     String 时间戳, 到毫秒
     * @return              TrainDatePost请求字符串
     */
    private String getTrainDateGMT(String trainDate) {
        // GMT time
        Date timeValue = new Date(Long.parseLong(trainDate));
        SimpleDateFormat gmtTime = new SimpleDateFormat("HH", Locale.US);
        gmtTime.setTimeZone(TimeZone.getTimeZone("GMT"));
        long gmtTimeHour = Long.parseLong(gmtTime.format(timeValue));
        // get current time
        SimpleDateFormat currentTime = new SimpleDateFormat("HH", Locale.US);
        long currentTimeHour = Long.parseLong(currentTime.format(timeValue));
        // difference
        long diffrence = currentTimeHour - gmtTimeHour;
        // create format time string
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE/MMM/d/yyyy/HH:mm:ss", Locale.US);
        String[] trainDateList = simpleDateFormat.format(new Date(Long.parseLong(trainDate))).split("/");
        String trainDateStr = "";
        for (int i = 0; i<trainDateList.length; i++){
            if (i==trainDateList.length-1){
                trainDateStr += GeneralTools.encodeURL(trainDateList[i]);
                break;
            }
            trainDateStr += trainDateList[i] + "+";
        }
        // create GMT time zone string
        String gmtStr = "";
        if (diffrence>=0){
            gmtStr = "GMT+" + String.format("%04d",diffrence*100);
        }else{
            gmtStr = "GMT-" + String.format("%04d", Math.abs(diffrence*100));
        }
        gmtStr = trainDateStr + "+" + GeneralTools.encodeURL(gmtStr);
        return gmtStr;
    }

    /**
     *      合并Map到Post请求接受的字符串
     *      例如:{"a":"1", "b":"2"} -> a=1&b=2
     *
     * @param dataMap   请求表单
     * @return          字符串
     */
    private static String mergerDataToString(Map<String, String> dataMap){
        StringBuilder dataStr = new StringBuilder();
        for (Map.Entry<String, String> entry: dataMap.entrySet()){
            // dataStr += entry.getKey() + "=" + entry.getValue() + "&";
            dataStr.append(entry.getKey());
            dataStr.append("=");
            dataStr.append(entry.getValue());
            dataStr.append("&");
        }
        return dataStr.substring(0, dataStr.length()-1).toString();
    }

    private GetQueueCountReturnResult failedGetQueueCountReturnResult(CloseableHttpClient session, String message){
        GetQueueCountReturnResult getQueueCountReturnResult = new GetQueueCountReturnResult();
        getQueueCountReturnResult.setStatus(false);
        getQueueCountReturnResult.setSession(session);
        getQueueCountReturnResult.setMessage(message);
        return getQueueCountReturnResult;
    }

}
