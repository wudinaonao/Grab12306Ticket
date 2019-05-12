package com.naonao.grab12306ticket.version.single.scheduler.arguments;


import com.naonao.grab12306ticket.version.single.database.table.GrabTicketInformationTable;
import com.naonao.grab12306ticket.version.single.database.table.NotifyInformationTable;
import com.naonao.grab12306ticket.version.single.database.table.StatusInformationTable;
import com.naonao.grab12306ticket.version.single.database.table.UserInformationTable;
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

    private UserInformationTable userInformationTable;
    private GrabTicketInformationTable grabTicketInformationTable;
    private NotifyInformationTable notifyInformationTable;
    private StatusInformationTable statusInformationTable;

}
