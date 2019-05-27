package com.naonao.grab12306ticket.version.single.notification.phone;

import com.naonao.grab12306ticket.version.single.entity.yml.Configuration;
import com.naonao.grab12306ticket.version.single.notification.base.AbstractPhone;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-06 22:52
 **/
@Log4j
public class Phone extends AbstractPhone {

    

    private Configuration configuration;

    public Phone(){
        configuration = GeneralTools.getConfiguration();
    }

    public TwilioVoice twilio(){
        String accountSid = configuration.getPlatform().getConfig().getTwilio().getAccountSid().trim();
        String authToken = configuration.getPlatform().getConfig().getTwilio().getAuthToken().trim();
        if (!"".equals(accountSid) && !"".equals(authToken)){
            return new TwilioVoice(accountSid, authToken);
        }
        return null;
    }

    public YunzhixinVoice yunzhixin(){

        String appCode = configuration.getPlatform().getConfig().getYunzhixin().getAppCode().trim();
        if (!"".equals(appCode)){
            return new YunzhixinVoice(appCode);
        }
        return null;
    }

    // private Boolean checkProperties(){
    //     if (properties == null){
    //         log.error(READ_CONFIG_FAILED);
    //         return false;
    //     }
    //     return true;
    // }

}
