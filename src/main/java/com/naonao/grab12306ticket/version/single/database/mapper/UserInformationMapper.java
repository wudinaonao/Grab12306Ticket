package com.naonao.grab12306ticket.version.single.database.mapper;

import com.naonao.grab12306ticket.version.single.database.table.UserInformationTable;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 15:33
 **/
public interface UserInformationMapper {


    UserInformationTable getUsernameAndPasswordById(Integer id);
    UserInformationTable getUsernameAndPasswordByHash(String hash);
    UserInformationTable getPasswordByUsername(String username);

    void  insert(UserInformationTable userInformationTable);
    void  delete(String hash);
    void  update(UserInformationTable userInformationTable);


}
