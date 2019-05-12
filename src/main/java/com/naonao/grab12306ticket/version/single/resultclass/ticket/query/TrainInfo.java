package com.naonao.grab12306ticket.version.single.resultclass.ticket.query;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 23:30
 **/
@Getter
@Setter
public class TrainInfo {

    private String secretStr;
    private String trainNo;
    private String trainName;
    private String startTime;
    private String endTime;
    private String duration;
    private String trainStatus;
    private String trainDate;
    private String startNum;
    private String endNum;
    private String trainId;

    /**
     * task hash is unique in database
     */
    private String hash;

}
