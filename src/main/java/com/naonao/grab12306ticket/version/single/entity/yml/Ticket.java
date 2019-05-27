package com.naonao.grab12306ticket.version.single.entity.yml;

import lombok.Data;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-27 14:42
 **/
@Data
public class Ticket {
    private String afterTime;
    private String beforeTime;
    private String trainDate;
    private String fromStation;
    private String toStation;
    private String purposeCode;
    private String trainName;
    private String backTrainDate;
    private String passengerName;
    private String documentType;
    private String documentNumber;
    private String mobile;
    private String seatType;
    private String expectSeatNumber;
}
