package com.naonao.grab12306ticket.version.single.resultclass.ticket.login;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-01 00:24
 **/
@Getter
@Setter
public class LoginTestReturnResult {

    private Boolean status;
    private String message;
    private CloseableHttpClient session;

}
