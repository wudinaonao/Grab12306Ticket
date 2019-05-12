package com.naonao.grab12306ticket.version.single.initialization;

import com.naonao.grab12306ticket.version.single.database.Database;
import lombok.extern.log4j.Log4j;


/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-10 17:50
 **/
@Log4j
public class Initialization  {

    private Database database;

    private Initialization(){
        database = new Database();
    }

    /**
     * create table in database
     */
    private void createTableStructure(){
        database.executeSQL().createUserInformationTable();
        database.executeSQL().createGrabTicketInformationTable();
        database.executeSQL().createNotifyInformationTable();
        database.executeSQL().createStatusInformationTable();
    }

    public static void main(String[] args) {
        Initialization initialization = new Initialization();
        initialization.createTableStructure();
    }

}
