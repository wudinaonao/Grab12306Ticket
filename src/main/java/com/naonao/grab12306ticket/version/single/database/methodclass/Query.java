package com.naonao.grab12306ticket.version.single.database.methodclass;

import com.naonao.grab12306ticket.version.single.database.common.AbstractDatabase;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import com.naonao.grab12306ticket.version.single.database.mapper.GrabTicketInformationMapper;
import com.naonao.grab12306ticket.version.single.database.mapper.NotifyInformationMapper;
import com.naonao.grab12306ticket.version.single.database.mapper.StatusInformationMapper;
import com.naonao.grab12306ticket.version.single.database.mapper.UserInformationMapper;
import com.naonao.grab12306ticket.version.single.database.table.GrabTicketInformationTable;
import com.naonao.grab12306ticket.version.single.database.table.NotifyInformationTable;
import com.naonao.grab12306ticket.version.single.database.table.StatusInformationTable;
import com.naonao.grab12306ticket.version.single.database.table.UserInformationTable;
import lombok.extern.log4j.Log4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 17:10
 **/
@Log4j
public class Query extends AbstractDatabase {
    

    private SqlSessionFactory sqlSessionFactory;


    public Query(){
        String resource = null;
        Properties properties = GeneralTools.getConfig();
        if (properties == null){
            log.error(READ_CONFIG_FAILED);
        }
        try{
            resource = properties.getProperty("Mybatis.configPath");
        }catch (NullPointerException e){
            log.error(READ_CONFIG_FAILED);
        }
        InputStream inputStream = null;
        try{
            inputStream = Resources.getResourceAsStream(resource);
        }catch (IOException e){
            log.error(e.getMessage());
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    public UserInformationTable getUserInformationByHash(String hash){
        SqlSession session = sqlSessionFactory.openSession();
        try {
            UserInformationMapper userInformationMapper = session.getMapper(UserInformationMapper.class);
            return userInformationMapper.getUsernameAndPasswordByHash(hash);
        }catch (NullPointerException e) {
            return null;
        }finally {
            session.close();
        }
    }
    public UserInformationTable getUserInformationByUsername(String username){
        SqlSession session = sqlSessionFactory.openSession();
        try {
            UserInformationMapper userInformationMapper = session.getMapper(UserInformationMapper.class);
            return userInformationMapper.getPasswordByUsername(username);
        }catch (NullPointerException e) {
            return null;
        }finally {
            session.close();
        }
    }

    public GrabTicketInformationTable getGrabTicketInformationByHash(String hash){
        SqlSession session = sqlSessionFactory.openSession();
        try {
            GrabTicketInformationMapper grabTicketInformationMapper = session.getMapper(GrabTicketInformationMapper.class);
            return grabTicketInformationMapper.getGrabTicketInformationByHash(hash);
        }catch (NullPointerException e) {
            return null;
        }finally {
            session.close();
        }
    }

    public NotifyInformationTable getNotifyInformationByHash(String hash){
        SqlSession session = sqlSessionFactory.openSession();
        try {
            NotifyInformationMapper notifyInformationMapper = session.getMapper(NotifyInformationMapper.class);
            return notifyInformationMapper.getNotifyInformationByHash(hash);
        }catch (NullPointerException e) {
            return null;
        }finally {
            session.close();
        }
    }

    public StatusInformationTable getStatusInformationTableByHash(String hash){
        SqlSession session = sqlSessionFactory.openSession();
        try {
            StatusInformationMapper statusInformationMapper = session.getMapper(StatusInformationMapper.class);
            return statusInformationMapper.getStatusInformationByHash(hash);
        }catch (NullPointerException e) {
            return null;
        }finally {
            session.close();
        }
    }
    public List<StatusInformationTable> getStatusInformationTableListByStatus(String status){
        SqlSession session = sqlSessionFactory.openSession();
        try {
            StatusInformationMapper statusInformationMapper = session.getMapper(StatusInformationMapper.class);
            return statusInformationMapper.getStatusInformationListByStatus(status);
        }catch (NullPointerException e) {
            return null;
        }finally {
            session.close();
        }
    }

    /**
     * get status information list from StatusInformationTable
     * condition is not RUNNING and not COMPLETED
     *
     * @return  List<StatusInformationTable>
     */
    public List<StatusInformationTable> getStatusInformationTableListByUnfinished(){
        SqlSession session = sqlSessionFactory.openSession();
        try {
            StatusInformationMapper statusInformationMapper = session.getMapper(StatusInformationMapper.class);
            return statusInformationMapper.getStatusInformationTableListByUnfinished();
        }catch (NullPointerException e) {
            return null;
        }finally {
            session.close();
        }
    }



}
