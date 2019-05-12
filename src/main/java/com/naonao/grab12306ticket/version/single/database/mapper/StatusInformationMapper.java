package com.naonao.grab12306ticket.version.single.database.mapper;

import com.naonao.grab12306ticket.version.single.database.table.StatusInformationTable;

import java.util.List;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-09 23:43
 **/
public interface StatusInformationMapper {

    StatusInformationTable getStatusInformationById(Integer id);
    StatusInformationTable getStatusInformationByHash(String hash);
    List<StatusInformationTable> getStatusInformationListByStatus(String status);
    List<StatusInformationTable> getStatusInformationTableListByUnfinished();

    void  insert(StatusInformationTable statusInformationTable);
    void  delete(StatusInformationTable hash);
    void  update(StatusInformationTable statusInformationTable);

}
