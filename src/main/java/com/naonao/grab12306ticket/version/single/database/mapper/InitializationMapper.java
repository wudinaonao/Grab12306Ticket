package com.naonao.grab12306ticket.version.single.database.mapper;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-10 17:42
 **/
public interface InitializationMapper {

    void createUserInformationTable();
    void createGrabTicketInformationTable();
    void createNotifyInformationTable();
    void createStatusInformationTable();

}
