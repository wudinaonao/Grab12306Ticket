package com.naonao.grab12306ticket.version.single.scheduler.initialization.common;

import com.naonao.grab12306ticket.version.single.scheduler.common.AbstractScheduler;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-11 22:40
 **/
public class AbstractInitialization extends AbstractScheduler {

    protected static final String EMAIL = "EMAIL";
    protected static final String PHONE = "PHONE";
    protected static final String SMS = "SMS";

    protected static final String TWILIO = "TWILIO";
    protected static final String NEXMO = "NEXMO";
    protected static final String YUNZHIXIN = "YUNZHIXIN";


    protected static final String[] USERNAME_12306_CONFIGURATION = {
            "12306.username12306",
            "12306.password12306"
    };

    protected static final String[] TICKET_CONFIGURATION = {
            "Ticket.afterTime",
            "Ticket.beforeTime",
            "Ticket.trainDate",
            "Ticket.fromStation",
            "Ticket.toStation",
            "Ticket.purposeCode",
            "Ticket.trainName",
            "Ticket.backTrainDate",
            "Ticket.passengerName",
            "Ticket.documentType",
            "Ticket.documentNumber",
            "Ticket.mobile",
            "Ticket.seatType",
            "Ticket.expectSeatNumber"
    };

    protected static final String[] NOTIFY_MODE_CONFIGURATION = {
            "notifyMode"
    };

    protected static final String[] EMAIL_CONFIGURATION = {
            "Email.receiverEmail",
            "Email.senderEmail",
            "Email.senderHost",
            "Email.senderPort",
            "Email.senderUsername",
            "Email.senderPassword"
    };

    protected static final String[] PHONE_CONFIGURATION = {
            "Phone.number"
    };


    protected static final String[] SMS_CONFIGURATION = {
            "Phone.number",
            "SMS.content"
    };

    protected static final String[] PLATFORM_CONFIGURATION = {
            "Platform.name"
    };

    protected static final String[] TWILIO_CONFIGURATION = {
            "Twilio.accountSid",
            "Twilio.authToken",
            "Twilio.configPath",
            "Twilio.fromPhoneNumber"
    };

    protected static final String[] NEXMO_CONFIGURATION = {
            "Nexmo.apiKey",
            "Nexmo.apiSecret"
    };

    protected static final String[] YUNZHIXIN_CONFIGURATION = {
            "Yunzhixin.appCode",
            "Yunzhixin.templateId"
    };

    protected static final String TICKET_AFTERTIME = "Ticket.afterTime";
    protected static final String TICKET_BEFORETIME = "Ticket.beforeTime";
    protected static final String TICKET_TRAINDATE = "Ticket.trainDate";
    protected static final String TICKET_FROMSTATION = "Ticket.fromStation";
    protected static final String TICKET_TOSTATION = "Ticket.toStation";
    protected static final String TICKET_PURPOSECODE = "Ticket.purposeCode";
    protected static final String TICKET_TRAINNAME = "Ticket.trainName";
    protected static final String TICKET_BACKTRAINDATE = "Ticket.backTrainDate";
    protected static final String TICKET_PASSENGERNAME = "Ticket.passengerName";
    protected static final String TICKET_DOCUMENTTYPE = "Ticket.documentType";
    protected static final String TICKET_DOCUMENTNUMBER = "Ticket.documentNumber";
    protected static final String TICKET_MOBILE = "Ticket.mobile";
    protected static final String TICKET_SEATTYPE = "Ticket.seatType";
    protected static final String TICKET_EXPECTSEATNUMBER = "Ticket.expectSeatNumber";


    protected static final Integer HOUR_MAX_VALUE = 24;
    protected static final Integer MIN_MAX_VALUE = 60;

}
