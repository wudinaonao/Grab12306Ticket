package com.naonao.grab12306ticket.version.single.database.mapper;

import com.naonao.grab12306ticket.version.single.database.table.GrabTicketInformationTable;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 15:39
 **/
public interface GrabTicketInformationMapper {

    GrabTicketInformationTable getGrabTicketInformationById(Integer id);
    GrabTicketInformationTable getGrabTicketInformationByHash(String hash);

    void  insert(GrabTicketInformationTable grabTicketInformationTable);
    void  delete(String hash);
    void  update(GrabTicketInformationTable grabTicketInformationTable);

}
