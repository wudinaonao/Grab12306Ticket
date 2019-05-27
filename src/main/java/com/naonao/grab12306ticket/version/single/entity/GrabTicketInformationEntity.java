package com.naonao.grab12306ticket.version.single.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 15:39
 **/
@ToString
@Getter
@Setter
public class GrabTicketInformationEntity {

    private Integer id;
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
    private String hash;


}
