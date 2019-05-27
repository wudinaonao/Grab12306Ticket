package com.naonao.grab12306ticket.version.single.constants;

import lombok.Getter;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-06 23:06
 **/
@Getter
public enum  NotificationInterfaceName {

    /**
     * notification interface name
     */
    TWILIO("TWILIO"),
    YUNZHIXIN("YUNZHIXIN"),
    NEXMO("NEXMO");

    private final String notificationInterfaceName;

    NotificationInterfaceName(String notificationInterfaceName) {
        this.notificationInterfaceName = notificationInterfaceName;
    }

}
