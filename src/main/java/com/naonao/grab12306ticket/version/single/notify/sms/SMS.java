package com.naonao.grab12306ticket.version.single.notify.sms;

import com.naonao.grab12306ticket.version.single.notify.sms.common.AbstractSMS;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;


import java.util.Properties;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-06 22:52
 **/
@Log4j
public class SMS extends AbstractSMS {

    

    private Properties properties;

    public SMS(){
        // this.interfaceName = interfaceName;
        this.properties = GeneralTools.getConfig();
    }

    public NexmoSMS nexmo(){
        if (!checkProperties()){
            return null;
        }
        String apiKey = properties.getProperty("Nexmo.apiKey");
        String apiSecret = properties.getProperty("Nexmo.apiSecret");
        if (!"".equals(apiKey) && apiKey != null && !"".equals(apiSecret) && apiSecret != null){
            return new NexmoSMS(apiKey, apiSecret);
        }
        return null;
    }

    public TwilioSMS twilio(){
        if (!checkProperties()){
            return null;
        }
        String accountSid = properties.getProperty("Twilio.accountSid");
        String authToken = properties.getProperty("Twilio.authToken");
        if (!"".equals(accountSid) && accountSid != null && !"".equals(authToken) && authToken != null){
            return new TwilioSMS(accountSid, authToken);
        }
        return null;
    }

    private Boolean checkProperties(){
        if (properties == null){
            log.error(READ_CONFIGURE_FAILED);
            return false;
        }
        return true;
    }


}
