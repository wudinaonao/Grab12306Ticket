package com.naonao.grab12306ticket.version.single.test;

import com.naonao.grab12306ticket.version.single.scheduler.arguments.QueryTrainInfoArguments;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.query.QueryTrainInfoReturnResult;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.RandomUtils;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-10 13:18
 **/
@Log4j
public class CreateQueryTrainInfoReturnResult {

    private QueryTrainInfoArguments queryTrainInfoArguments;

    public CreateQueryTrainInfoReturnResult(QueryTrainInfoArguments queryTrainInfoArguments){
        this.queryTrainInfoArguments = queryTrainInfoArguments;
    }

    public QueryTrainInfoReturnResult queryTrainInfoReturnResult(){
        QueryTrainInfoReturnResult queryTrainInfoReturnResult = new QueryTrainInfoReturnResult();
        queryTrainInfoReturnResult.setStatus(RandomUtils.nextBoolean());
        queryTrainInfoReturnResult.setUserInformationTable(queryTrainInfoArguments.getUserInformationTable());
        queryTrainInfoReturnResult.setGrabTicketInformationTable(queryTrainInfoArguments.getGrabTicketInformationTable());
        queryTrainInfoReturnResult.setNotifyInformationTable(queryTrainInfoArguments.getNotifyInformationTable());
        queryTrainInfoReturnResult.setStatusInformationTable(queryTrainInfoArguments.getStatusInformationTable());
        return queryTrainInfoReturnResult;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(RandomUtils.nextBoolean());
        }
    }

}
