package com.naonao.grab12306ticket.version.single.scheduler.thread.runnable.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.naonao.grab12306ticket.version.single.scheduler.arguments.QueryTrainInfoArguments;
import com.naonao.grab12306ticket.version.single.scheduler.queue.QueryTrainInfoReturnResultQueue;
import com.naonao.grab12306ticket.version.single.scheduler.thread.runnable.QueryTrainInfoReturnResultProducer;
import com.naonao.grab12306ticket.version.single.scheduler.thread.strategy.RejectExecutionHandlerBlocking;
import com.naonao.grab12306ticket.version.single.constants.TaskStatusName;
import com.naonao.grab12306ticket.version.single.database.Database;
import com.naonao.grab12306ticket.version.single.database.table.GrabTicketInformationTable;
import com.naonao.grab12306ticket.version.single.database.table.NotifyInformationTable;
import com.naonao.grab12306ticket.version.single.database.table.StatusInformationTable;
import com.naonao.grab12306ticket.version.single.database.table.UserInformationTable;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
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

    private Database database;

    private Properties properties;

    // /**
    //  * database version
    //  * @param queryTrainInfoReturnResultQueue   queryTrainInfoReturnResultQueue
    //  */
    // public ProducerPool(QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue){
    //     this.queryTrainInfoReturnResultQueue = queryTrainInfoReturnResultQueue;
    //     this.database = new Database();
    // }

    /**
     * single version
     * @param queryTrainInfoReturnResultQueue   queryTrainInfoReturnResultQueue
     */
    public ProducerPool(QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue){
        this.queryTrainInfoReturnResultQueue = queryTrainInfoReturnResultQueue;
        this.properties = GeneralTools.getConfig();
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
                    // log.info("put a information to queue ...");
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

    // /**
    //  * database version
    //  *
    //  * get QueryTrainInfoArguments object list by database
    //  * if status is wait, then get
    //  *
    //  * @return  List<QueryTrainInfoArguments>
    //  */
    // private List<QueryTrainInfoArguments> getQueryTrainInfoArgumentsList(){
    //     List<StatusInformationTable> statusInformationTableList = database.query().getStatusInformationTableListByUnfinished();
    //     if (statusInformationTableList.size() <= 0){
    //         return null;
    //     }
    //     List<QueryTrainInfoArguments> queryTrainInfoArgumentsList = new ArrayList<>();
    //     for (StatusInformationTable statusInformationTable: statusInformationTableList){
    //         QueryTrainInfoArguments queryTrainInfoArguments = makeQueryTrainInfoArguments(statusInformationTable);
    //         if (queryTrainInfoArguments != null){
    //             queryTrainInfoArgumentsList.add(queryTrainInfoArguments);
    //         }
    //     }
    //     if (queryTrainInfoArgumentsList.size() <= 0){
    //         return null;
    //     }
    //     return queryTrainInfoArgumentsList;
    // }

    /**
     * single version
     *
     * get QueryTrainInfoArguments object list by configuration file
     * make 10 QueryTrainInfoArguments each list
     *
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
     * database version
     *
     * make QueryTrainInfoArguments object by statusTable object
     *
     * first get hash from statusTable, then get UserInformationTable,
     * GrabTicketInformationTable, NotifyInformationTable object.
     * second get each property value used to encapsulate the
     * QueryTrainInfoArguments object
     *
     * @param statusInformationTable    statusInformationTable
     * @return                          QueryTrainInfoArguments
     */
    private QueryTrainInfoArguments makeQueryTrainInfoArguments(StatusInformationTable statusInformationTable){
        // get data
        String hash = statusInformationTable.getHash();
        UserInformationTable userInformationTable = database.query().getUserInformationByHash(hash);
        GrabTicketInformationTable grabTicketInformationTable = database.query().getGrabTicketInformationByHash(hash);
        NotifyInformationTable notifyInformationTable = database.query().getNotifyInformationByHash(hash);
        String beforeTime = grabTicketInformationTable.getBeforeTime();
        String afterTime = grabTicketInformationTable.getAfterTime();
        String trainDate = grabTicketInformationTable.getBackTrainDate();
        String fromStation = grabTicketInformationTable.getFromStation();
        String toStation = grabTicketInformationTable.getToStation();
        String purposeCode = grabTicketInformationTable.getPurposeCode();
        String trainName = grabTicketInformationTable.getTrainName();
        // instance a QueryTrainInfoArguments
        QueryTrainInfoArguments queryTrainInfoArguments = new QueryTrainInfoArguments();
        queryTrainInfoArguments.setBeforeTime(beforeTime);
        queryTrainInfoArguments.setAfterTime(afterTime);
        queryTrainInfoArguments.setTrainDate(trainDate);
        queryTrainInfoArguments.setFromStation(fromStation);
        queryTrainInfoArguments.setToStation(toStation);
        queryTrainInfoArguments.setPurposeCode(purposeCode);
        queryTrainInfoArguments.setTrainName(trainName);
        queryTrainInfoArguments.setHash(hash);
        queryTrainInfoArguments.setUserInformationTable(userInformationTable);
        queryTrainInfoArguments.setGrabTicketInformationTable(grabTicketInformationTable);
        queryTrainInfoArguments.setNotifyInformationTable(notifyInformationTable);
        queryTrainInfoArguments.setStatusInformationTable(statusInformationTable);
        if (!checkQueryTrainInfoArguments(queryTrainInfoArguments)){
            return null;
        }
        return queryTrainInfoArguments;
    }

    /**
     * single version
     *
     * make QueryTrainInfoArguments object from configuration file
     * load information
     *
     * @return  QueryTrainInfoArguments
     */
    private QueryTrainInfoArguments makeQueryTrainInfoArguments(){
        UserInformationTable userInformationTable = new UserInformationTable();
        userInformationTable.setUsername12306(properties.getProperty("12306.username12306"));
        userInformationTable.setPassword12306(properties.getProperty("12306.password12306"));

        GrabTicketInformationTable grabTicketInformationTable = new GrabTicketInformationTable();
        grabTicketInformationTable.setAfterTime(properties.getProperty("Ticket.afterTime"));
        grabTicketInformationTable.setBeforeTime(properties.getProperty("Ticket.beforeTime"));
        grabTicketInformationTable.setTrainDate(properties.getProperty("Ticket.trainDate"));
        grabTicketInformationTable.setFromStation(properties.getProperty("Ticket.fromStation"));
        grabTicketInformationTable.setToStation(properties.getProperty("Ticket.toStation"));
        grabTicketInformationTable.setPurposeCode(properties.getProperty("Ticket.purposeCode"));
        grabTicketInformationTable.setTrainName(properties.getProperty("Ticket.trainName"));
        grabTicketInformationTable.setBackTrainDate(currentDate());
        grabTicketInformationTable.setPassengerName(properties.getProperty("Ticket.passengerName"));
        grabTicketInformationTable.setDocumentType(properties.getProperty("Ticket.documentType"));
        grabTicketInformationTable.setDocumentNumber(properties.getProperty("Ticket.documentNumber"));
        grabTicketInformationTable.setMobile(properties.getProperty("Ticket.mobile"));
        grabTicketInformationTable.setSeatType(properties.getProperty("Ticket.seatType"));
        grabTicketInformationTable.setExpectSeatNumber(properties.getProperty("Ticket.expectSeatNumber"));

        NotifyInformationTable notifyInformationTable = new NotifyInformationTable();
        notifyInformationTable.setReceiverEmail(properties.getProperty("Email.receiverEmail"));
        notifyInformationTable.setSendEmail(properties.getProperty("Email.senderEmail"));
        notifyInformationTable.setSendEmailHost(properties.getProperty("Email.senderHost"));
        notifyInformationTable.setSendEmailPort(properties.getProperty("Email.senderPort"));
        notifyInformationTable.setSendEmailUsername(properties.getProperty("Email.senderUsername"));
        notifyInformationTable.setSendEmailPassword(properties.getProperty("Email.senderPassword"));
        notifyInformationTable.setReceiverPhone(properties.getProperty("Phone.number"));
        notifyInformationTable.setNotifyMode(properties.getProperty("notifyMode"));

        StatusInformationTable statusInformationTable = new StatusInformationTable();

        QueryTrainInfoArguments queryTrainInfoArguments = new QueryTrainInfoArguments();
        queryTrainInfoArguments.setAfterTime(properties.getProperty("Ticket.afterTime"));
        queryTrainInfoArguments.setBeforeTime(properties.getProperty("Ticket.beforeTime"));
        queryTrainInfoArguments.setTrainDate(properties.getProperty("Ticket.trainDate"));
        queryTrainInfoArguments.setFromStation(properties.getProperty("Ticket.fromStation"));
        queryTrainInfoArguments.setToStation(properties.getProperty("Ticket.toStation"));
        queryTrainInfoArguments.setPurposeCode(properties.getProperty("Ticket.purposeCode"));
        queryTrainInfoArguments.setTrainName(properties.getProperty("Ticket.trainName"));
        queryTrainInfoArguments.setUserInformationTable(userInformationTable);
        queryTrainInfoArguments.setGrabTicketInformationTable(grabTicketInformationTable);
        queryTrainInfoArguments.setNotifyInformationTable(notifyInformationTable);
        queryTrainInfoArguments.setStatusInformationTable(statusInformationTable);
        return queryTrainInfoArguments;
    }
    /**
     * check if input queryTrainInfoArguments instance attributes has null value
     *
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

    /**
     * get a queryTrainInfoArguments object,
     * then update database status table status column is RUNNING.
     *
     * @param queryTrainInfoArguments   queryTrainInfoArguments
     */
    private void updateDatabase(QueryTrainInfoArguments queryTrainInfoArguments){
        StatusInformationTable statusInformationTable = queryTrainInfoArguments.getStatusInformationTable();
        statusInformationTable.setStatus(TaskStatusName.RUNNING.getTaskStatusName());
        database.update().statusInformation(statusInformationTable);
    }


    private String currentDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date());
    }

}
