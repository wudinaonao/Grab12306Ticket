package com.naonao.grab12306ticket.version.single.database.mapper;

import com.naonao.grab12306ticket.version.single.database.table.NotifyInformationTable;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 15:41
 **/
public interface NotifyInformationMapper {

    NotifyInformationTable getNotifyInformationById(Integer id);
    NotifyInformationTable getNotifyInformationByHash(String hash);

    void  insert(NotifyInformationTable notifyInformationTable);
    void  delete(String hash);
    void  update(NotifyInformationTable notifyInformationTable);

}
