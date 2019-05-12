package com.naonao.grab12306ticket.version.single.constants;

import lombok.Getter;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-06 23:06
 **/
@Getter
public enum  NotifyInterfaceName {

    /**
     * notification interface name
     */

    // Email

    // Phone
    TWILIO_VOICE("TWILIO_VOICE"),
    YUNZHIXIN_VOICE("YUNZHIXIN_VOICE"),


    // SMS
    TWILIO_SMS("TWILIO_SMS"),
    NEXMO_SMS("NEXMO_SMS");

    private final String notifyInterfaceName;

    NotifyInterfaceName(String notifyInterfaceName) {
        this.notifyInterfaceName = notifyInterfaceName;
    }

}
