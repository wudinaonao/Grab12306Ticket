package com.naonao.grab12306ticket.version.single.notify.sms;

import com.naonao.grab12306ticket.version.single.notify.sms.common.AbstractSMS;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.SmsSubmissionResponseMessage;
import com.nexmo.client.sms.messages.TextMessage;
import lombok.extern.log4j.Log4j;


/**
 * Nexmo SMS interface
 *
 * @program: 12306grabticket_java
 * @description: Nexmo SMS interface
 * @author: Wen lyuzhao
 * @create: 2019-05-06 20:22
 **/
@Log4j
public class NexmoSMS extends AbstractSMS {

    

    private String apiKey;
    private String apiSecret;

    public NexmoSMS(String apiKey, String apiSecret){
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    /**
     *      Nexmo SMS interface, single receiver
     *
     * @param from          from
     * @param to            to
     * @param text       smsText
     * @return              true, false
     */
    public Boolean sendSMS(String from, String to, String text){
        try {
            NexmoClient client = new NexmoClient.Builder()
                    .apiKey(apiKey)
                    .apiSecret(apiSecret)
                    .build();
            SmsSubmissionResponse responses = client.getSmsClient().submitMessage(
                    new TextMessage(
                            // you can set SMS subject, example: 12306Ticket
                            // this will be shown on the phone
                            from,
                            // format: 8615935582121
                            to,
                            text,
                            true
                    )
            );
            for (SmsSubmissionResponseMessage response : responses.getMessages()) {
                if (!OK.equals(response.getStatus().name())){
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     *      Nexmo SMS interface, multi receiver
     *
     * @param from          from
     * @param tos           tos
     * @param text          text
     * @return              true, false
     */
    public Boolean sendSMS(String from, String[] tos, String text){
        try {
            NexmoClient client = new NexmoClient.Builder()
                    .apiKey(apiKey)
                    .apiSecret(apiSecret)
                    .build();
            for (String to: tos) {
                SmsSubmissionResponse responses = client.getSmsClient().submitMessage(
                        new TextMessage(
                                // you can set SMS subject, example: 12306Ticket
                                // this will be shown on the phone
                                from,
                                // format: 8615935582121
                                to,
                                text,
                                true
                        )
                );
                for (SmsSubmissionResponseMessage response : responses.getMessages()) {
                    if (!OK.equals(response.getStatus().name())){
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
