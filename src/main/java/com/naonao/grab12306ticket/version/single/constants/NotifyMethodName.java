package com.naonao.grab12306ticket.version.single.constants;

import lombok.Getter;

/**
 * @program: grabticket
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-09 15:14
 **/

@Getter
public enum  NotifyMethodName {

    /**
     * notification method name
     */

    EMAIL("EMAIL"),
    PHONE("PHONE"),
    SMS("SMS");

    private final String notifyMethodName;

    NotifyMethodName(String notifyMethodName) {
        this.notifyMethodName = notifyMethodName;
    }
}
