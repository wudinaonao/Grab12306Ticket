package com.naonao.grab12306ticket.version.single.notify.phone;

import com.naonao.grab12306ticket.version.single.notify.phone.common.AbstractPhone;
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
public class Phone extends AbstractPhone {

    

    private Properties properties;

    public Phone(){
        this.properties = GeneralTools.getConfig();
    }

    public TwilioVoice twilio(){
        if (!checkProperties()){
            return null;
        }
        String accountSid = properties.getProperty("Twilio.accountSid");
        String authToken = properties.getProperty("Twilio.authToken");
        if (!"".equals(accountSid) && accountSid != null && !"".equals(authToken) && authToken != null){
            return new TwilioVoice(accountSid, authToken);
        }
        return null;
    }

    public YunzhixinVoice yunzhixin(){
        if (!checkProperties()){
            return null;
        }
        String appCode = properties.getProperty("Yunzhixin.appCode");
        if (!"".equals(appCode) && appCode != null){
            return new YunzhixinVoice(appCode);
        }
        return null;
    }

    private Boolean checkProperties(){
        if (properties == null){
            log.error(READ_CONFIG_FAILED);
            return false;
        }
        return true;
    }

}
