package com.naonao.grab12306ticket.version.single.scheduler.thread.runnable;

import com.naonao.grab12306ticket.version.single.scheduler.arguments.QueryTrainInfoArguments;
import com.naonao.grab12306ticket.version.single.scheduler.queue.QueryTrainInfoReturnResultQueue;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.query.QueryTrainInfoReturnResult;
import com.naonao.grab12306ticket.version.single.ticket.query.QueryTrainInfo;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import lombok.extern.log4j.Log4j;


/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-07 22:06
 **/
@Log4j
public class QueryTrainInfoReturnResultProducer implements Runnable{






    private QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue;
    private QueryTrainInfoArguments queryTrainInfoArguments;
    // private Database database;

    public QueryTrainInfoReturnResultProducer(QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue,
                                              QueryTrainInfoArguments queryTrainInfoArguments){
        this.queryTrainInfoReturnResultQueue = queryTrainInfoReturnResultQueue;
        this.queryTrainInfoArguments = queryTrainInfoArguments;
        // database = new Database();
    }

    /**
     * single version
     * according to arguments execute queryTrainInfo method, then put result to queue.
     */
    @Override
    public void run() {
        // produce
        QueryTrainInfo queryTrainInfo = new QueryTrainInfo(HttpTools.getSession(30000));
        QueryTrainInfoReturnResult queryTrainInfoReturnResult = queryTrainInfo.queryTrainInfo(queryTrainInfoArguments);
        queryTrainInfoReturnResultQueue.producer(queryTrainInfoReturnResult);

        // test
        // test();
    }

    // /**
    //  * database version
    //  * according to arguments execute queryTrainInfo method, then put result to queue.
    //  */
    // @Override
    // public void run() {
    //     // produce
    //     QueryTrainInfo queryTrainInfo = new QueryTrainInfo(HttpTools.getSession(30000));
    //     QueryTrainInfoReturnResult queryTrainInfoReturnResult = queryTrainInfo.queryTrainInfo(queryTrainInfoArguments);
    //     updateDatabase(TaskStatusName.RUNNING, queryTrainInfoReturnResult.getStatusInformationTable());
    //     queryTrainInfoReturnResultQueue.producer(queryTrainInfoReturnResult);
    //
    //     // test
    //     // test();
    // }

    // private void test(){
    //     CreateQueryTrainInfoReturnResult createQueryTrainInfoReturnResult = new CreateQueryTrainInfoReturnResult(queryTrainInfoArguments);
    //     QueryTrainInfoReturnResult queryTrainInfoReturnResult = createQueryTrainInfoReturnResult.queryTrainInfoReturnResult();
    //     updateDatabase(TaskStatusName.RUNNING, queryTrainInfoReturnResult.getStatusInformationTable());
    //     queryTrainInfoReturnResultQueue.producer(createQueryTrainInfoReturnResult.queryTrainInfoReturnResult());
    // }
    //
    // private void updateDatabase(TaskStatusName statusName, StatusInformationTable statusInformationTable){
    //     String status = statusName.getTaskStatusName();
    //     String lastRunningTime = GeneralTools.formatTime();
    //     String endTime = GeneralTools.formatTime();
    //     Long tryCount = statusInformationTable.getTryCount() + 1L;
    //     String message = statusName.getTaskStatusName();
    //     statusInformationTable.setStatus(status);
    //     statusInformationTable.setTaskLastRunTime(lastRunningTime);
    //     statusInformationTable.setTaskEndTime(endTime);
    //     statusInformationTable.setTryCount(tryCount);
    //     statusInformationTable.setMessage(message);
    //     database.update().statusInformation(statusInformationTable);
    // }

}
