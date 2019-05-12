package com.naonao.grab12306ticket.version.single.constants;

import lombok.Getter;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 19:31
 **/
@Getter
public enum  TaskStatusName {

    /**
     * task status type name
     */

    WAIT("wait"),
    RUNNING("running"),
    BOOKING_FAILED("booking failed"),
    BOOKING_SUCCEED_BUT_SEND_NOTIFICATION_FAILED("booking succeed but send notification failed"),
    COMPLETED("completed");


    private final String taskStatusName;

    TaskStatusName(String taskStatusName) {
        this.taskStatusName = taskStatusName;
    }


}
