package com.naonao.grab12306ticket.version.single.test;

import com.naonao.grab12306ticket.version.single.database.Database;
import com.naonao.grab12306ticket.version.single.database.table.GrabTicketInformationTable;
import com.naonao.grab12306ticket.version.single.database.table.NotifyInformationTable;
import com.naonao.grab12306ticket.version.single.database.table.StatusInformationTable;
import com.naonao.grab12306ticket.version.single.database.table.UserInformationTable;
import lombok.extern.log4j.Log4j;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-10 21:04
 **/
@Log4j
public class InsertDataToTable {


    private static String hash;
    private static CreateInformation createInformation;
    private static Database database;

    static {
        database = new Database();
        createInformation = new CreateInformation();
    }

    private static UserInformationTable userInformationTable(){
        UserInformationTable userInformationTable = new UserInformationTable();
        userInformationTable.setId(null);
        userInformationTable.setUsername12306(createInformation.username12306());
        userInformationTable.setPassword12306(createInformation.password12306());
        userInformationTable.setHash(hash);
        return userInformationTable;
    }

    private static GrabTicketInformationTable grabTicketInformationTable(){
        GrabTicketInformationTable grabTicketInformationTable = new GrabTicketInformationTable();
        grabTicketInformationTable.setId(null);
        grabTicketInformationTable.setAfterTime(createInformation.afterTime());
        grabTicketInformationTable.setBeforeTime(createInformation.beforeTime());
        grabTicketInformationTable.setTrainDate(createInformation.tranDate());
        grabTicketInformationTable.setFromStation(createInformation.fromStation());
        grabTicketInformationTable.setToStation(createInformation.toStation());
        grabTicketInformationTable.setPurposeCode(createInformation.purposeCode());
        grabTicketInformationTable.setTrainName(createInformation.trainName());
        grabTicketInformationTable.setBackTrainDate(createInformation.backTrainDate());
        grabTicketInformationTable.setPassengerName(createInformation.passengerName());
        grabTicketInformationTable.setDocumentType(createInformation.documentType());
        grabTicketInformationTable.setDocumentNumber(createInformation.documentNumber());
        grabTicketInformationTable.setMobile(createInformation.mobile());
        grabTicketInformationTable.setSeatType(createInformation.seatType());
        grabTicketInformationTable.setExpectSeatNumber(createInformation.expectSeatNumber());
        grabTicketInformationTable.setHash(hash);
        return grabTicketInformationTable;
    }

    private static NotifyInformationTable notifyInformationTable(){
        NotifyInformationTable notifyInformationTable = new NotifyInformationTable();
        notifyInformationTable.setId(null);
        notifyInformationTable.setReceiverEmail(createInformation.receiverEmail());
        notifyInformationTable.setSendEmail(createInformation.sendEmail());
        notifyInformationTable.setSendEmailHost(createInformation.sendEmailHost());
        notifyInformationTable.setSendEmailPort(createInformation.sendEmailPort());
        notifyInformationTable.setSendEmailUsername(createInformation.sendEmailUsername());
        notifyInformationTable.setSendEmailPassword(createInformation.sendEmailPassword());
        notifyInformationTable.setReceiverPhone(createInformation.receiverPhone());
        notifyInformationTable.setNotifyMode(createInformation.notifyMode());
        notifyInformationTable.setHash(hash);
        return notifyInformationTable;
    }

    private static StatusInformationTable statusInformationTable(){
        StatusInformationTable statusInformationTable = new StatusInformationTable();
        statusInformationTable.setId(null);
        statusInformationTable.setStatus(createInformation.status());
        statusInformationTable.setTaskStartTime(createInformation.taskStartTime());
        statusInformationTable.setTaskEndTime(createInformation.taskEndTime());
        statusInformationTable.setTaskLastRunTime(createInformation.taskLastRunTime());
        statusInformationTable.setTryCount(createInformation.tryCount());
        statusInformationTable.setMessage(createInformation.message());
        statusInformationTable.setHash(hash);
        return statusInformationTable;
    }

    private static void insertToDatabase(){
        database.insert().userInformation(userInformationTable());
        database.insert().grabTicketInformation(grabTicketInformationTable());
        database.insert().notifyInformation(notifyInformationTable());
        database.insert().statusInformation(statusInformationTable());
    }

    private static void start(){
        int count = 1000;
        for (int i = 0; i < count; i++) {
            hash = createInformation.hash();
            insertToDatabase();
            System.out.println("complate: " + i);
        }
    }


    public static void main(String[] args) {
        start();
    }
}

