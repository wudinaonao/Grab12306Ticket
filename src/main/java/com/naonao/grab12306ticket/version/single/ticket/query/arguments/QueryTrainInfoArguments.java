package com.naonao.grab12306ticket.version.single.ticket.query.arguments;


import com.naonao.grab12306ticket.version.single.entity.GrabTicketInformationEntity;
import com.naonao.grab12306ticket.version.single.entity.NotificationInformationEntity;
import com.naonao.grab12306ticket.version.single.entity.StatusInformationEntity;
import com.naonao.grab12306ticket.version.single.entity.UserInformationEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-07 23:10
 **/
@Getter
@Setter
public class QueryTrainInfoArguments {

    private String beforeTime;
    private String afterTime;
    private String trainDate;
    private String fromStation;
    private String toStation;
    private String purposeCode;
    private String trainName;

    private String hash;

    private UserInformationEntity userInformationEntity;
    private GrabTicketInformationEntity grabTicketInformationEntity;
    private NotificationInformationEntity notificationInformationEntity;
    private StatusInformationEntity statusInformationEntity;

}
