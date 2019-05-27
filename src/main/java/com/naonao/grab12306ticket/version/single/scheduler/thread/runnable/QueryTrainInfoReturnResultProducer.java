package com.naonao.grab12306ticket.version.single.scheduler.thread.runnable;

import com.naonao.grab12306ticket.version.single.ticket.query.arguments.QueryTrainInfoArguments;
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

    public QueryTrainInfoReturnResultProducer(QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue,
                                              QueryTrainInfoArguments queryTrainInfoArguments){
        this.queryTrainInfoReturnResultQueue = queryTrainInfoReturnResultQueue;
        this.queryTrainInfoArguments = queryTrainInfoArguments;
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


}
