package com.naonao.grab12306ticket.version.single.resultclass.ticket.query;


import com.naonao.grab12306ticket.version.single.resultclass.IReturnResult;
import com.naonao.grab12306ticket.version.single.database.table.GrabTicketInformationTable;
import com.naonao.grab12306ticket.version.single.database.table.NotifyInformationTable;
import com.naonao.grab12306ticket.version.single.database.table.StatusInformationTable;
import com.naonao.grab12306ticket.version.single.database.table.UserInformationTable;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.List;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-06 16:03
 **/
@Getter
@Setter
public class QueryTrainInfoReturnResult implements IReturnResult {


    private Boolean status;
    private String message;
    private CloseableHttpClient session;

    private List<TrainInfo> trainInfoList;

    private UserInformationTable userInformationTable;
    private GrabTicketInformationTable grabTicketInformationTable;
    private NotifyInformationTable notifyInformationTable;
    private StatusInformationTable statusInformationTable;

}
