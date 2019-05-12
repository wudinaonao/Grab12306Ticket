package com.naonao.grab12306ticket.version.single.resultclass;

import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description: return result interface
 * @author: Wen lyuzhao
 * @create: 2019-04-29 18:38
 **/
public interface IReturnResult {

    Boolean getStatus();

    void setStatus(Boolean status);

    String getMessage() ;

    void setMessage(String message);

    CloseableHttpClient getSession();

    void setSession(CloseableHttpClient session);

}
