package com.naonao.grab12306ticket.version.single.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.naonao.grab12306ticket.version.single.entity.yml.Configuration;
import com.naonao.grab12306ticket.version.single.scheduler.base.AbstractScheduler;
import com.naonao.grab12306ticket.version.single.scheduler.initialization.CheckTicketConfiguration;
import com.naonao.grab12306ticket.version.single.scheduler.queue.QueryTrainInfoReturnResultQueue;
import com.naonao.grab12306ticket.version.single.scheduler.thread.runnable.pool.ConsumerPool;
import com.naonao.grab12306ticket.version.single.scheduler.thread.runnable.pool.ProducerPool;
import com.naonao.grab12306ticket.version.single.scheduler.thread.strategy.RejectExecutionHandlerBlocking;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;

import java.util.Set;
import java.util.concurrent.*;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 17:37
 **/
@Log4j
public class Scheduler extends AbstractScheduler {

    private Configuration configuration;
    private void start(){
        checkConfiguration();
        configuration = GeneralTools.getConfiguration();
        if (!configuration.getSetting().getGrabTicketCode()){
            log.info("not started grab ticket core");
            return;
        }
        ExecutorService pool = createThreadPool();
        QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue = createQueryTrainInfoReturnResultQueue();
        Set<String> hashSet = ConcurrentHashMap.<String> newKeySet();
        pool.execute(new ConsumerPool(queryTrainInfoReturnResultQueue, hashSet));
        pool.execute(new ProducerPool(queryTrainInfoReturnResultQueue));
    }

    /**
     * check configuration
     */
    private void checkConfiguration(){
        GeneralTools.checkConfiguration();
        new CheckTicketConfiguration().check();
    }

    /**
     * create a thread pool for start ProducerPool and ConsumePool
     * @return  ExecutorService
     */
    private ExecutorService createThreadPool(){
        int corePoolSize = 2;
        int maximumPoolSize = 2;
        long keepAliveTime = 60L;
        int queueSize = 4;
        // create thread pool
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Scheduler [%d]")
                .build();
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(queueSize),
                threadFactory,
                new RejectExecutionHandlerBlocking()
        );
    }

    /**
     * create a QueryTrainInfoReturnResultQueue for storage QueryTrainInfoReturnResult
     *
     * @return  QueryTrainInfoReturnResultQueue
     */
    private QueryTrainInfoReturnResultQueue createQueryTrainInfoReturnResultQueue(){
        return new QueryTrainInfoReturnResultQueue(1024);
    }

    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        scheduler.start();
    }

}
