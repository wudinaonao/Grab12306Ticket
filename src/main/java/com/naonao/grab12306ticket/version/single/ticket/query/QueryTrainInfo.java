package com.naonao.grab12306ticket.version.single.ticket.query;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.constants.ConvertMap;
import com.naonao.grab12306ticket.version.single.entity.GrabTicketInformationEntity;
import com.naonao.grab12306ticket.version.single.entity.NotificationInformationEntity;
import com.naonao.grab12306ticket.version.single.entity.StatusInformationEntity;
import com.naonao.grab12306ticket.version.single.entity.UserInformationEntity;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.query.QueryTrainInfoReturnResult;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.query.TrainInfo;
import com.naonao.grab12306ticket.version.single.ticket.base.AbstractQuery;
import com.naonao.grab12306ticket.version.single.ticket.query.arguments.QueryTrainInfoArguments;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-06 16:03
 **/
@Log4j
public class QueryTrainInfo extends AbstractQuery {

    

    private String afterTime;
    private String beforeTime;
    private String trainDate;
    private String fromStation;
    private String fromStationId;
    private String toStation;
    private String toStationId;
    private String purposeCode;
    private String trainName;

    private String hash;

    private UserInformationEntity userInformationEntity;
    private GrabTicketInformationEntity grabTicketInformationEntity;
    private NotificationInformationEntity notificationInformationEntity;
    private StatusInformationEntity statusInformationEntity;



    public QueryTrainInfo(CloseableHttpClient session){
        this.session = session;
    }

    public QueryTrainInfoReturnResult queryTrainInfo(QueryTrainInfoArguments queryTrainInfoArguments){
        // set variable
        setVariable(queryTrainInfoArguments);
        // initialization uri
        URI uri = createQueryUri("leftTicket/query");
        List<TrainInfo> trainInfoListMap = getTrainInfoList(uri);

        if (trainInfoListMap == null){
            log.error(TRAIN_INFORMATION_LIST_IS_NULL);
            return failedQueryTrainInfoReturnResult(session, TRAIN_INFORMATION_LIST_IS_NULL);
        }
        if (trainInfoListMap.size() == 0){
            log.error(NOT_HAVE_ELIGIBLE_TRAIN);
            return failedQueryTrainInfoReturnResult(session, NOT_HAVE_ELIGIBLE_TRAIN);
        }

        // success
        QueryTrainInfoReturnResult queryTrainInfoReturnResult = new QueryTrainInfoReturnResult();
        queryTrainInfoReturnResult.setStatus(true);
        queryTrainInfoReturnResult.setSession(session);
        queryTrainInfoReturnResult.setMessage(FIND_ELIGIBLE_TRAIN);
        queryTrainInfoReturnResult.setTrainInfoList(trainInfoListMap);
        queryTrainInfoReturnResult.setUserInformationEntity(userInformationEntity);
        queryTrainInfoReturnResult.setGrabTicketInformationEntity(grabTicketInformationEntity);
        queryTrainInfoReturnResult.setNotificationInformationEntity(notificationInformationEntity);
        queryTrainInfoReturnResult.setStatusInformationEntity(statusInformationEntity);
        return queryTrainInfoReturnResult;
    }

    private void setVariable(QueryTrainInfoArguments queryTrainInfoArguments){
        afterTime = queryTrainInfoArguments.getAfterTime();
        beforeTime = queryTrainInfoArguments.getBeforeTime();
        trainDate = queryTrainInfoArguments.getTrainDate();
        fromStation = queryTrainInfoArguments.getFromStation();
        fromStationId = getCityId(queryTrainInfoArguments.getFromStation());
        toStation = queryTrainInfoArguments.getToStation();
        toStationId = getCityId(queryTrainInfoArguments.getToStation());
        purposeCode = queryTrainInfoArguments.getPurposeCode();
        trainName = queryTrainInfoArguments.getTrainName();

        hash = queryTrainInfoArguments.getHash();

        userInformationEntity = queryTrainInfoArguments.getUserInformationEntity();
        grabTicketInformationEntity = queryTrainInfoArguments.getGrabTicketInformationEntity();
        notificationInformationEntity = queryTrainInfoArguments.getNotificationInformationEntity();
        statusInformationEntity = queryTrainInfoArguments.getStatusInformationEntity();
    }
    private List<TrainInfo> getTrainInfoList(URI queryUri){
        HttpGet queryTrainInfoRequest = HttpTools.setRequestHeader(new HttpGet(queryUri), true, false, true);
        CloseableHttpResponse response = null;
        try{
            response = session.execute(queryTrainInfoRequest);
            // if url is error, get new url
            if (response.getStatusLine().getStatusCode() == HTTP_REDIRECT){
                URI uri = createQueryUri(getNewQueryUrlByResponse(response));
                return getTrainInfoList(uri);
            }
            // success
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                return getTrainInfoListByResponse(response);
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
        return null;
    }

    private URI createQueryUri(String cUrl){
        String baseUrl = "https://kyfw.12306.cn/otn/";
        String queryUrl = baseUrl + cUrl;
        try{
            return new URIBuilder(queryUrl)
                    .setParameter("leftTicketDTO.train_date", trainDate)
                    .setParameter("leftTicketDTO.from_station", fromStationId)
                    .setParameter("leftTicketDTO.to_station", toStationId)
                    .setParameter("purpose_codes", purposeCode)
                    .build();
        }catch (URISyntaxException e){
            log.error(e.getMessage());
        }
        return null;
    }

    private String getNewQueryUrlByResponse(CloseableHttpResponse response){
        String responseText = HttpTools.responseToString(response);
        JSONObject jsonObject = JSONObject.parseObject(responseText);
        return jsonObject.getString("c_url");
    }

    private List<TrainInfo> getTrainInfoListByResponse(CloseableHttpResponse response){
        String responseText = HttpTools.responseToString(response);
        // get a list, element is train information string
        List<String> trainInfoListString = JSONObject.parseArray(
                JSONObject.parseObject(responseText)
                        .getJSONObject("data")
                        .getString("result"),
                String.class
        );
        List<TrainInfo> trainInfoList = new ArrayList<>();
        for (String trainInfoString: trainInfoListString){
            String[] trainInfoArray =  trainInfoString.split("\\|");
            // not have eligible train time
            if (!trainTimeEligible(trainInfoArray[8], afterTime, beforeTime) || "".equals(trainInfoArray[0])){
                continue;
            }
            // if the user has set train name and eligible
            if (trainName != null && trainInfoArray[3].equals(trainName.trim())){
                List<TrainInfo>  eligibleTrainNameList = new ArrayList<>();
                eligibleTrainNameList.add(getTrainInfo(trainInfoArray));
                return eligibleTrainNameList;
            }
            trainInfoList.add(getTrainInfo(trainInfoArray));
        }
        return trainInfoList;
    }

    private TrainInfo getTrainInfo(String[] trainInfoArray){
        TrainInfo trainInfo = new TrainInfo();
        trainInfo.setSecretStr(trainInfoArray[0]);
        trainInfo.setTrainNo(trainInfoArray[2]);
        trainInfo.setTrainName(trainInfoArray[3]);
        trainInfo.setStartTime(trainInfoArray[8]);
        trainInfo.setEndTime(trainInfoArray[9]);
        trainInfo.setDuration(trainInfoArray[10]);
        trainInfo.setTrainStatus(trainInfoArray[11]);
        trainInfo.setTrainDate(trainInfoArray[13]);
        trainInfo.setStartNum(trainInfoArray[16]);
        trainInfo.setEndNum(trainInfoArray[17]);
        trainInfo.setTrainId(trainInfoArray[35]);
        trainInfo.setHash(hash);
        return trainInfo;
    }

    private Boolean trainTimeEligible(String startTime, String afterTime, String beforeTime){
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date startTimeTypeDate = simpleDateFormat.parse(startTime);
            Date afterTimeTypeDate = simpleDateFormat.parse(afterTime);
            Date beforeTimeTypeDate = simpleDateFormat.parse(beforeTime);
            boolean isMeet = beforeTimeTypeDate.getTime() >= startTimeTypeDate.getTime() && afterTimeTypeDate.getTime() <= startTimeTypeDate.getTime();
            if (isMeet){
                return true;
            }
            return false;
        }catch (ParseException e){
            log.error(e.getMessage());
        }
        return false;
    }

    private String getCityId(String cityName){
        return ConvertMap.cityNameToID(cityName);
    }

    private QueryTrainInfoReturnResult failedQueryTrainInfoReturnResult(CloseableHttpClient session, String message){
        QueryTrainInfoReturnResult queryTrainInfoReturnResult = new QueryTrainInfoReturnResult();
        queryTrainInfoReturnResult.setStatus(false);
        queryTrainInfoReturnResult.setSession(session);
        queryTrainInfoReturnResult.setMessage(message);
        queryTrainInfoReturnResult.setUserInformationEntity(userInformationEntity);
        queryTrainInfoReturnResult.setGrabTicketInformationEntity(grabTicketInformationEntity);
        queryTrainInfoReturnResult.setNotificationInformationEntity(notificationInformationEntity);
        queryTrainInfoReturnResult.setStatusInformationEntity(statusInformationEntity);
        return queryTrainInfoReturnResult;
    }

    // public static void main(String[] args) {
    //     QueryTrainInfo queryTrainInfo = new QueryTrainInfo(HttpTools.getSession(30000));
    //     QueryTrainInfoArguments queryTrainInfoArguments = new QueryTrainInfoArguments();
    //     queryTrainInfoArguments.setAfterTime("08:00");
    //     queryTrainInfoArguments.setBeforeTime("22:00");
    //     queryTrainInfoArguments.setTrainDate("2019-05-15");
    //     queryTrainInfoArguments.setFromStation("西安");
    //     queryTrainInfoArguments.setToStation("运城");
    //     queryTrainInfoArguments.setPurposeCode("ADULT");
    //     queryTrainInfoArguments.setTrainName("D2530");
    //     queryTrainInfoArguments.setHash("0CC175B9C0F1B6A831C399E269772666");
    //     QueryTrainInfoReturnResult queryTrainInfoReturnResult = queryTrainInfo.queryTrainInfo(queryTrainInfoArguments);
    //     System.out.println(queryTrainInfoReturnResult.getMessage());
    // }
}
