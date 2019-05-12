package com.naonao.grab12306ticket.version.single.resultclass.ticket.booking;

import com.naonao.grab12306ticket.version.single.resultclass.IReturnResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-01 23:39
 **/
@Getter
@Setter
public class ConfirmSingleForQueueReturnResult implements IReturnResult {

    private Boolean status;
    private String message;
    private CloseableHttpClient session;


}
