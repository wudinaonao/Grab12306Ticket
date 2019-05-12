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
import org.apache.ibatis.exceptions.PersistenceException;
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
public class Update extends AbstractDatabase {
    

    private SqlSessionFactory sqlSessionFactory;


    public Update(){
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

    public void userInformation(UserInformationTable userInformationTable){
        SqlSession session = sqlSessionFactory.openSession();
        try{
            UserInformationMapper userInformationMapper = session.getMapper(UserInformationMapper.class);
            userInformationMapper.update(userInformationTable);
            session.commit();
        }catch (PersistenceException e){
            return;
        } finally{
            session.close();
        }
    }
    public void userInformation(List<UserInformationTable> userInformationTableList){
        for (UserInformationTable userInformationTable: userInformationTableList){
            SqlSession session = sqlSessionFactory.openSession();
            try{
                UserInformationMapper userInformationMapper = session.getMapper(UserInformationMapper.class);
                userInformationMapper.update(userInformationTable);
                session.commit();
            }catch (PersistenceException e){
                return;
            } finally{
                session.close();
            }
        }
    }

    public void grabTicketInformation(GrabTicketInformationTable grabTicketInformationTable){
        SqlSession session = sqlSessionFactory.openSession();
        try{
            GrabTicketInformationMapper grabTicketInformationMapper = session.getMapper(GrabTicketInformationMapper.class);
            grabTicketInformationMapper.update(grabTicketInformationTable);
            session.commit();
        }catch (PersistenceException e){
            return;
        } finally{
            session.close();
        }
    }
    public void grabTicketInformation(List<GrabTicketInformationTable> grabTicketInformationTableList){
        for (GrabTicketInformationTable grabTicketInformationTable: grabTicketInformationTableList){
            SqlSession session = sqlSessionFactory.openSession();
            try{
                GrabTicketInformationMapper grabTicketInformationMapper = session.getMapper(GrabTicketInformationMapper.class);
                grabTicketInformationMapper.update(grabTicketInformationTable);
                session.commit();
            }catch (PersistenceException e){
                return;
            } finally{
                session.close();
            }
        }
    }

    public void notifyInformation(NotifyInformationTable notifyInformationTable){
        SqlSession session = sqlSessionFactory.openSession();
        try{
            NotifyInformationMapper notifyInformationMapper = session.getMapper(NotifyInformationMapper.class);
            notifyInformationMapper.update(notifyInformationTable);
            session.commit();
        }catch (PersistenceException e){
            return;
        } finally{
            session.close();
        }
    }
    public void notifyInformation(List<NotifyInformationTable> notifyInformationTableList){
        for (NotifyInformationTable notifyInformationTable: notifyInformationTableList){
            SqlSession session = sqlSessionFactory.openSession();
            try{
                NotifyInformationMapper notifyInformationMapper = session.getMapper(NotifyInformationMapper.class);
                notifyInformationMapper.update(notifyInformationTable);
                session.commit();
            }catch (PersistenceException e){
                return;
            } finally{
                session.close();
            }
        }
    }


    public void statusInformation(StatusInformationTable statusInformationTable){
        SqlSession session = sqlSessionFactory.openSession();
        try{
            StatusInformationMapper statusInformationMapper = session.getMapper(StatusInformationMapper.class);
            statusInformationMapper.update(statusInformationTable);
            session.commit();
        }catch (PersistenceException e){
            return;
        } finally{
            session.close();
        }
    }
    public void statusInformation(List<StatusInformationTable> statusInformationTableList){
        for (StatusInformationTable statusInformationTable: statusInformationTableList){
            SqlSession session = sqlSessionFactory.openSession();
            try{
                StatusInformationMapper statusInformationMapper = session.getMapper(StatusInformationMapper.class);
                statusInformationMapper.update(statusInformationTable);
                session.commit();
            }catch (PersistenceException e){
                return;
            } finally{
                session.close();
            }
        }
    }

}
