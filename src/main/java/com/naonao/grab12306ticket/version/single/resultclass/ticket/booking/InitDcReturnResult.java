package com.naonao.grab12306ticket.version.single.resultclass.ticket.booking;

import com.naonao.grab12306ticket.version.single.resultclass.IReturnResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description: initDc
 * @author: Wen lyuzhao
 * @create: 2019-05-01 23:37
 **/
@Getter
@Setter
public class InitDcReturnResult implements IReturnResult {

    private Boolean status;
    private String message;
    private CloseableHttpClient session;

    private String repeatSubmitToken;

    private String trainDateTime;
    private String fromStationTelecode;
    private String leftTicketStr;
    private String purposeCodes;
    private String stationTrainCode;
    private String toStationTelecode;
    private String trainLocation;
    private String trainNo;
    private String[] leftDetails;
    private String keyCheckIsChange;

}
