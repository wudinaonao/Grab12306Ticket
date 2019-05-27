package com.naonao.grab12306ticket.version.single.scheduler.thread.runnable.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.query.QueryTrainInfoReturnResult;
import com.naonao.grab12306ticket.version.single.scheduler.queue.QueryTrainInfoReturnResultQueue;
import com.naonao.grab12306ticket.version.single.scheduler.thread.runnable.QueryTrainInfoReturnResultConsumer;
import com.naonao.grab12306ticket.version.single.scheduler.thread.strategy.RejectExecutionHandlerBlocking;
import com.naonao.grab12306ticket.version.single.tools.ComputeHash;
import lombok.extern.log4j.Log4j;

import java.util.Set;
import java.util.concurrent.*;


/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-09 20:22
 **/
@Log4j
public class ConsumerPool implements Runnable{
    

    private QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue;

    private Set<String> hashSet;

    // /**
    //  * database version
    //  * @param queryTrainInfoReturnResultQueue   queryTrainInfoReturnResultQueue
    //  */
    // public ConsumerPool(QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue){
    //     this.queryTrainInfoReturnResultQueue = queryTrainInfoReturnResultQueue;
    // }

    /**
     * singe version
     * @param queryTrainInfoReturnResultQueue   queryTrainInfoReturnResultQueue
     */
    public ConsumerPool(QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue,
                        Set<String> hashSet){
        this.queryTrainInfoReturnResultQueue = queryTrainInfoReturnResultQueue;
        this.hashSet = hashSet;
    }


    @Override
    public void run() {
        consume();
    }

    /**
     * get QueryTrainInfoReturnResult from queue, then get a thread by thread pool
     * for handle QueryTrainInfoReturnResult.
     *
     */
    public void consume(){

        int corePoolSize = 16;
        int maximumPoolSize = 32;
        long keepAliveTime = 60L;
        int queueSize = 128;

        // create thread pool
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Consume QueryTrainInfoReturnResult[%d]")
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
        // get QueryTrainInfoReturnResult from queue
        try{
            while (true) {
                QueryTrainInfoReturnResult queryTrainInfoReturnResult = queryTrainInfoReturnResultQueue.consumer();
                if (queryTrainInfoReturnResult != null){
                    // single version
                    if (!existHashInSet(queryTrainInfoReturnResult)){
                        log.info("start task");
                        hashSet.add(ComputeHash.fromGrabTicketInformation(queryTrainInfoReturnResult.getGrabTicketInformationEntity()));
                        pool.execute(new QueryTrainInfoReturnResultConsumer(queryTrainInfoReturnResult, hashSet));
                    }
                }
            }
        } catch (Exception e){
            log.error(e.getMessage());
        }

        pool.shutdown();

    }

    private Boolean existHashInSet(QueryTrainInfoReturnResult queryTrainInfoReturnResult){
        String hash = ComputeHash.fromGrabTicketInformation(queryTrainInfoReturnResult.getGrabTicketInformationEntity());
        return hashSet.contains(hash);
    }



}
