package com.naonao.grab12306ticket.version.single.resultclass.ticket.login;

import com.naonao.grab12306ticket.version.single.resultclass.IReturnResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-02 13:40
 **/
@Getter
@Setter
public class InitLoginReturnResult implements IReturnResult {

    private Boolean status;
    private String message;
    private CloseableHttpClient session;

}
