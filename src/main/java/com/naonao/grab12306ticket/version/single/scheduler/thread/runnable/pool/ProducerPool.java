package com.naonao.grab12306ticket.version.single.scheduler.thread.runnable.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.naonao.grab12306ticket.version.single.entity.GrabTicketInformationEntity;
import com.naonao.grab12306ticket.version.single.entity.NotificationInformationEntity;
import com.naonao.grab12306ticket.version.single.entity.StatusInformationEntity;
import com.naonao.grab12306ticket.version.single.entity.UserInformationEntity;
import com.naonao.grab12306ticket.version.single.entity.yml.Configuration;
import com.naonao.grab12306ticket.version.single.scheduler.queue.QueryTrainInfoReturnResultQueue;
import com.naonao.grab12306ticket.version.single.scheduler.thread.runnable.QueryTrainInfoReturnResultProducer;
import com.naonao.grab12306ticket.version.single.scheduler.thread.strategy.RejectExecutionHandlerBlocking;
import com.naonao.grab12306ticket.version.single.ticket.query.arguments.QueryTrainInfoArguments;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-09 21:56
 **/
@Log4j
public class ProducerPool implements Runnable{


    private QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue;
    
    private Configuration configuration;

    /**
     * single version
     * @param queryTrainInfoReturnResultQueue   queryTrainInfoReturnResultQueue
     */
    public ProducerPool(QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue){
        this.queryTrainInfoReturnResultQueue = queryTrainInfoReturnResultQueue;
        this.configuration = GeneralTools.getConfiguration();
    }

    @Override
    public void run() {
        produce();
    }

    /**
     * get information from database status table, then encapsulation QueryTrainInfoArguments
     * object for get QueryTrainInfoReturnResult object, then put QueryTrainInfoReturnResult to
     * queue
     */
    public void produce(){

        int corePoolSize = 16;
        int maximumPoolSize = 32;
        long keepAliveTime = 60L;
        int queueSize = 128;

        // create thread pool
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Produce QueryTrainInfoReturnResult[%d]")
                .build();
        ExecutorService pool = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(queueSize),
                threadFactory,
                new RejectExecutionHandlerBlocking()
        );

        while (true){
            List<QueryTrainInfoArguments> queryTrainInfoArgumentsList = getQueryTrainInfoArgumentsList();
            if (queryTrainInfoArgumentsList == null){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    continue;
                }
                continue;
            }
            for (QueryTrainInfoArguments queryTrainInfoArguments: queryTrainInfoArgumentsList){
                if (queryTrainInfoArguments != null){
                    log.info("querying train information ......");
                    pool.execute(new QueryTrainInfoReturnResultProducer(
                            queryTrainInfoReturnResultQueue,
                            queryTrainInfoArguments
                    ));
                }
            }
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                continue;
            }
        }
    }


    /**
     * single version
     * get QueryTrainInfoArguments object list by configuration file
     * make 10 QueryTrainInfoArguments each list
     * @return  List<QueryTrainInfoArguments>
     */
    private List<QueryTrainInfoArguments> getQueryTrainInfoArgumentsList(){
        List<QueryTrainInfoArguments> queryTrainInfoArgumentsList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            queryTrainInfoArgumentsList.add(makeQueryTrainInfoArguments());
        }
        return queryTrainInfoArgumentsList;
    }

    

    /**
     * single version
     * make QueryTrainInfoArguments object from configuration file
     * load information
     * @return  QueryTrainInfoArguments
     */
    private QueryTrainInfoArguments makeQueryTrainInfoArguments(){
        UserInformationEntity userInformationEntity = new UserInformationEntity();
        userInformationEntity.setUsername12306(configuration.getUser().getUsername12306());
        userInformationEntity.setPassword12306(configuration.getUser().getPassword12306());

        GrabTicketInformationEntity grabTicketInformationEntity = new GrabTicketInformationEntity();
        grabTicketInformationEntity.setAfterTime(configuration.getTicket().getAfterTime());
        grabTicketInformationEntity.setBeforeTime(configuration.getTicket().getBeforeTime());
        grabTicketInformationEntity.setTrainDate(configuration.getTicket().getTrainDate());
        grabTicketInformationEntity.setFromStation(configuration.getTicket().getFromStation());
        grabTicketInformationEntity.setToStation(configuration.getTicket().getToStation());
        grabTicketInformationEntity.setPurposeCode(configuration.getTicket().getPurposeCode());
        grabTicketInformationEntity.setTrainName(configuration.getTicket().getTrainName());
        grabTicketInformationEntity.setBackTrainDate(GeneralTools.currentDate());
        grabTicketInformationEntity.setPassengerName(configuration.getTicket().getPassengerName());
        grabTicketInformationEntity.setDocumentType(configuration.getTicket().getDocumentType());
        grabTicketInformationEntity.setDocumentNumber(configuration.getTicket().getDocumentNumber());
        grabTicketInformationEntity.setMobile(configuration.getTicket().getMobile());
        grabTicketInformationEntity.setSeatType(configuration.getTicket().getSeatType());
        grabTicketInformationEntity.setExpectSeatNumber(configuration.getTicket().getExpectSeatNumber());

        NotificationInformationEntity notificationInformationEntity = new NotificationInformationEntity();
        notificationInformationEntity.setReceiverEmail(configuration.getNotification().getConfig().getEmail().getReceiverEmail());
        notificationInformationEntity.setSendEmail(configuration.getNotification().getConfig().getEmail().getSenderEmail());
        notificationInformationEntity.setSendEmailHost(configuration.getNotification().getConfig().getEmail().getSenderEmailHost());
        notificationInformationEntity.setSendEmailPort(configuration.getNotification().getConfig().getEmail().getSenderEmailPort());
        notificationInformationEntity.setSendEmailUsername(configuration.getNotification().getConfig().getEmail().getSenderEmailUsername());
        notificationInformationEntity.setSendEmailPassword(configuration.getNotification().getConfig().getEmail().getSenderEmailPassword());
        notificationInformationEntity.setReceiverPhone(configuration.getNotification().getReceiverPhone());
        notificationInformationEntity.setNotificationMode(configuration.getNotification().getMode());

        StatusInformationEntity statusInformationEntity = new StatusInformationEntity();

        QueryTrainInfoArguments queryTrainInfoArguments = new QueryTrainInfoArguments();
        queryTrainInfoArguments.setAfterTime(configuration.getTicket().getAfterTime());
        queryTrainInfoArguments.setBeforeTime(configuration.getTicket().getBeforeTime());
        queryTrainInfoArguments.setTrainDate(configuration.getTicket().getTrainDate());
        queryTrainInfoArguments.setFromStation(configuration.getTicket().getFromStation());
        queryTrainInfoArguments.setToStation(configuration.getTicket().getToStation());
        queryTrainInfoArguments.setPurposeCode(configuration.getTicket().getPurposeCode());
        queryTrainInfoArguments.setTrainName(configuration.getTicket().getTrainName());
        queryTrainInfoArguments.setUserInformationEntity(userInformationEntity);
        queryTrainInfoArguments.setGrabTicketInformationEntity(grabTicketInformationEntity);
        queryTrainInfoArguments.setNotificationInformationEntity(notificationInformationEntity);
        queryTrainInfoArguments.setStatusInformationEntity(statusInformationEntity);
        return queryTrainInfoArguments;
    }
    /**
     * check if input queryTrainInfoArguments instance attributes has null value
     * @param queryTrainInfoArguments   queryTrainInfoArguments
     * @return                          boolean
     */
    private Boolean checkQueryTrainInfoArguments(QueryTrainInfoArguments queryTrainInfoArguments){
        Class cls = queryTrainInfoArguments.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field: fields){
            field.setAccessible(true);
            Object value = null;
            try{
                value = field.get(queryTrainInfoArguments);
            }catch (IllegalAccessException e){
                return false;
            }
            if (value == null){
                return false;
            }
        }
        return true;
    }


}
