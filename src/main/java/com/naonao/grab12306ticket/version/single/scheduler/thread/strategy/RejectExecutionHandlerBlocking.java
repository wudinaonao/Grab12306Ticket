package com.naonao.grab12306ticket.version.single.scheduler.thread.strategy;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-09 21:12
 **/
public class RejectExecutionHandlerBlocking  implements RejectedExecutionHandler {

    /**
     * if thread pool is full, then blocking until the thread pool has resources
     * handle request.
     *
     * @param r         Runnable
     * @param executor  ThreadPoolExecutor
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (!executor.isShutdown()){
            while (executor.getQueue().remainingCapacity() == 0){
                executor.execute(r);
            }
        }
    }

}
