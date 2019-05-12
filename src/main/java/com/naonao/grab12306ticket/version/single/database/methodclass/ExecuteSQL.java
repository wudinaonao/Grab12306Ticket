package com.naonao.grab12306ticket.version.single.database.methodclass;

import com.naonao.grab12306ticket.version.single.database.common.AbstractDatabase;
import com.naonao.grab12306ticket.version.single.database.mapper.InitializationMapper;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-10 17:51
 **/
@Log4j
public class ExecuteSQL extends AbstractDatabase {

    

    private SqlSessionFactory sqlSessionFactory;

    public ExecuteSQL(){
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


    public void createUserInformationTable(){
        SqlSession session = sqlSessionFactory.openSession();
        try{
            InitializationMapper initializationMapper = session.getMapper(InitializationMapper.class);
            initializationMapper.createUserInformationTable();
            session.commit();
        }catch (PersistenceException e){
            return;
        } finally{
            session.close();
        }
    }


    public void createGrabTicketInformationTable() {
        SqlSession session = sqlSessionFactory.openSession();
        try{
            InitializationMapper initializationMapper = session.getMapper(InitializationMapper.class);
            initializationMapper.createGrabTicketInformationTable();
            session.commit();
        }catch (PersistenceException e){
            return;
        } finally{
            session.close();
        }
    }


    public void createNotifyInformationTable() {
        SqlSession session = sqlSessionFactory.openSession();
        try{
            InitializationMapper initializationMapper = session.getMapper(InitializationMapper.class);
            initializationMapper.createNotifyInformationTable();
            session.commit();
        }catch (PersistenceException e){
            return;
        } finally{
            session.close();
        }
    }


    public void createStatusInformationTable() {
        SqlSession session = sqlSessionFactory.openSession();
        try{
            InitializationMapper initializationMapper = session.getMapper(InitializationMapper.class);
            initializationMapper.createStatusInformationTable();
            session.commit();
        }catch (PersistenceException e){
            return;
        } finally{
            session.close();
        }
    }
}
