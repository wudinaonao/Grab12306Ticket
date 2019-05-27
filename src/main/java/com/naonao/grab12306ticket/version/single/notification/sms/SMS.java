package com.naonao.grab12306ticket.version.single.notification.sms;

import com.naonao.grab12306ticket.version.single.entity.yml.Configuration;
import com.naonao.grab12306ticket.version.single.notification.base.AbstractSMS;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-06 22:52
 **/
@Log4j
public class SMS extends AbstractSMS {


    private Configuration configuration;

    public SMS(){
        configuration = GeneralTools.getConfiguration();
    }

    public NexmoSMS nexmo(){
        String apiKey = configuration.getPlatform().getConfig().getNexmo().getApiKey().trim();
        String apiSecret = configuration.getPlatform().getConfig().getNexmo().getApiSecret().trim();
        if (!"".equals(apiKey) && !"".equals(apiSecret)){
            return new NexmoSMS(apiKey, apiSecret);
        }
        return null;
    }

    public TwilioSMS twilio(){
        String accountSid = configuration.getPlatform().getConfig().getTwilio().getAccountSid().trim();
        String authToken = configuration.getPlatform().getConfig().getTwilio().getAuthToken().trim();
        if (!"".equals(accountSid) && !"".equals(authToken)){
            return new TwilioSMS(accountSid, authToken);
        }
        return null;
    }


}
