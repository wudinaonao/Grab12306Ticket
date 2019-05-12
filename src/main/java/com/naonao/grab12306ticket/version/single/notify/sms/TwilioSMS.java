package com.naonao.grab12306ticket.version.single.notify.sms;

import com.naonao.grab12306ticket.version.single.notify.sms.common.AbstractSMS;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import lombok.extern.log4j.Log4j;



/**
 * Twilio SMS interface
 *
 * @program: 12306grabticket_java
 * @description: Twilio SMS interface
 * @author: Wen lyuzhao
 * @create: 2019-05-06 20:22
 **/
@Log4j
public class TwilioSMS extends AbstractSMS {




    private String accountSid;
    private String authToken;

    public TwilioSMS(String accountSid, String authToken){
        this.accountSid = accountSid;
        this.authToken = authToken;
    }

    /**
     *      Twilio SMS interface
     *
     * @param from          from
     * @param tos           tos(receiver array)
     * @param text          sms contant, if more than max length will be split
     * @return              true, false
     */
    public Boolean sendSMS(String from, String[] tos, String text){
        Twilio.init(accountSid, authToken);
        for (String to:tos) {
            com.twilio.rest.api.v2010.account.Message message = com.twilio.rest.api.v2010.account.Message
                    .creator(
                            // to
                            new PhoneNumber(to),
                            // from
                            new PhoneNumber(from),
                            text
                    ).create();
            if (!QUEUED.equals(message.getStatus().name())){
                return false;
            }
        }
        return true;
    }

    /**
     *      Twilio SMS interface
     *
     * @param from          from
     * @param to            to
     * @param text          sms contant, if more than max length will be split
     * @return              true, false
     */
    public Boolean sendSMS(String from, String to, String text){
        Twilio.init(accountSid, authToken);
        com.twilio.rest.api.v2010.account.Message message = com.twilio.rest.api.v2010.account.Message
                .creator(
                        // to
                        new PhoneNumber(to),
                        // from
                        new PhoneNumber(from),
                        text
                ).create();
        if (!QUEUED.equals(message.getStatus().name())){
            return false;
        }
        return true;
    }

}
