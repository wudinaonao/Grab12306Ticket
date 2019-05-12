package com.naonao.grab12306ticket.version.single.database;

import com.naonao.grab12306ticket.version.single.database.common.AbstractDatabase;
import com.naonao.grab12306ticket.version.single.database.methodclass.*;
import lombok.extern.log4j.Log4j;


/**
 * database handle class
 * instance a insert, delete, update, query object.
 *
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 17:09
 **/

@Log4j
public class Database extends AbstractDatabase {


    public Insert insert() {
        return new Insert();
    }

    public Delete delete() {
        return new Delete();
    }

    public Update update() {
        return new Update();
    }

    public Query query() {
        return new Query();
    }

    public ExecuteSQL executeSQL(){
        return new ExecuteSQL();
    }

}
