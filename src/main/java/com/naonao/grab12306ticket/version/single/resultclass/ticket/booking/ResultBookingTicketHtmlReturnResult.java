package com.naonao.grab12306ticket.version.single.resultclass.ticket.booking;

import com.naonao.grab12306ticket.version.single.resultclass.IReturnResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-01 23:51
 **/
@Getter
@Setter
public class ResultBookingTicketHtmlReturnResult implements IReturnResult {

    private Boolean status;
    private String message;
    private CloseableHttpClient session;

    private String sequenceNo;
    private String passengerIdTypeName;
    private String passengerIdNo;
    private String passengerName;
    private String coachName;
    private String seatName;
    private String seatTypeName;
    private String fromStationName;
    private String toStationName;
    private String stationTrainCode;
    private String startTrainDate;
    private String ticketPrice;
    private String ticketNo;
    private String ticketTypeName;

}
