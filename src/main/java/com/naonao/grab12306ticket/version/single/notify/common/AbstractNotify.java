package com.naonao.grab12306ticket.version.single.notify.common;

import com.naonao.grab12306ticket.version.single.common.AbstractBackend;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-06 23:04
 **/
public abstract class AbstractNotify extends AbstractBackend {



    protected static final String NOT_FOUND_ELIGIBLE_NOTIFICATION_MODE = "not found eligible notification mode";


    // Phone
    protected static final String TWILIO = "TWILIO";
    protected static final String YUNZHIXIN = "YUNZHIXIN";

    // sms
    protected static final String NEXMO = "NEXMO";

}
