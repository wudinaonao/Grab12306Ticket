package com.naonao.grab12306ticket.version.single.resultclass.ticket.query;


import com.naonao.grab12306ticket.version.single.entity.GrabTicketInformationEntity;
import com.naonao.grab12306ticket.version.single.entity.NotificationInformationEntity;
import com.naonao.grab12306ticket.version.single.entity.StatusInformationEntity;
import com.naonao.grab12306ticket.version.single.entity.UserInformationEntity;
import com.naonao.grab12306ticket.version.single.resultclass.IReturnResult;
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

    private UserInformationEntity userInformationEntity;
    private GrabTicketInformationEntity grabTicketInformationEntity;
    private NotificationInformationEntity notificationInformationEntity;
    private StatusInformationEntity statusInformationEntity;

}
