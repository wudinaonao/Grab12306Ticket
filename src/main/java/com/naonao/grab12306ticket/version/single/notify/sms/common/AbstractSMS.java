package com.naonao.grab12306ticket.version.single.notify.sms.common;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-06 22:52
 **/
public abstract class AbstractSMS {

    protected static final Integer HTTP_SUCCESS = 200;

    // SMS
    protected static final String NOT_FOUND_SEND_INTERFACE = "not found send interface";
    protected static final String READ_CONFIGURE_FAILED = "read configure failed, because properties is null";


    // Twilio
    protected static final String QUEUED = "QUEUED";

    // Nexmo
    protected static final String OK = "OK";

}
