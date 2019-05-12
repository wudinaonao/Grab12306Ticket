package com.naonao.grab12306ticket.version.single.scheduler.initialization;

import com.naonao.grab12306ticket.version.single.scheduler.initialization.common.AbstractInitialization;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;


import java.util.*;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-11 18:03
 **/
@Log4j
public class CheckConfiguration extends AbstractInitialization {

    

    public Boolean check(){
        // load configuration
        Properties properties = GeneralTools.getConfig();
        if (properties == null){
            log.error(READ_CONFIG_FAILED);
            return false;
        }
        // get configuration key name Set
        Set<String> stringPropertyNames = properties.stringPropertyNames();

        // check username, ticket and notify mode
        boolean usernameConfiguration = check12306UsernameConfiguration(stringPropertyNames);
        boolean ticketConfiguration = checkTicketConfiguration(stringPropertyNames);
        boolean notificationConfiguration = checkNotificationConfiguration(stringPropertyNames);
        if (!(usernameConfiguration && ticketConfiguration && notificationConfiguration)){
            log.error("check username or ticket or notification configuration failed.");
            return false;
        }

        // check notification mode
        List<String> notificationModeList = notificationModeList(properties.getProperty("notifyMode"));
        if (notificationModeList == null){
            log.error("notification mode is null.");
            return false;
        }
        Map<String, Boolean> resultMap = new HashMap<>(16);
        resultMap.put(EMAIL, null);
        resultMap.put(PHONE, null);
        resultMap.put(SMS, null);
        for (String notificationMode: notificationModeList){
            notificationMode = notificationMode.toUpperCase();
            if (EMAIL.equals(notificationMode)){
                // check email
                resultMap.put(EMAIL, checkEmailConfiguration(stringPropertyNames));
            }
            if (PHONE.equals(notificationMode)){
                // check platform name and platform
                resultMap.put(PHONE, checkPlatformConfigurantion(stringPropertyNames) && checkPhoneConfiguration(stringPropertyNames));
            }
            if (SMS.equals(notificationMode)){
                // check platform name and platform
                resultMap.put(SMS, checkPlatformConfigurantion(stringPropertyNames) && checkSMSConfiguration(stringPropertyNames));
            }
        }
        boolean status = true;
        for (Map.Entry<String, Boolean> result: resultMap.entrySet()){
            if (result.getValue() != null){
                status &= result.getValue();
            }
        }
        return status;
    }

    private Boolean checkConfiguration(Set<String> stringPropertyNames, String[] configurations){
        boolean status = true;
        for (String key: configurations){
            if (!stringPropertyNames.contains(key)){
                log.error("check failed, lack ---> " + key);
            }
            status &= stringPropertyNames.contains(key);
        }
        return status;
    }
    private Boolean check12306UsernameConfiguration(Set<String> stringPropertyNames){
        return checkConfiguration(stringPropertyNames, USERNAME_12306_CONFIGURATION);
    }

    private Boolean checkTicketConfiguration(Set<String> stringPropertyNames){
        return checkConfiguration(stringPropertyNames, TICKET_CONFIGURATION);
    }

    private Boolean checkNotificationConfiguration(Set<String> stringPropertyNames){
        return checkConfiguration(stringPropertyNames, NOTIFY_MODE_CONFIGURATION);
    }

    private Boolean checkEmailConfiguration(Set<String> stringPropertyNames){
        return checkConfiguration(stringPropertyNames, EMAIL_CONFIGURATION);
    }

    private Boolean checkPlatformConfigurantion(Set<String> stringPropertyNames){
        return checkConfiguration(stringPropertyNames, PLATFORM_CONFIGURATION);
    }

    private Boolean checkPhoneConfiguration(Set<String> stringPropertyNames){
        return checkConfiguration(stringPropertyNames, PHONE_CONFIGURATION);
    }

    private Boolean checkSMSConfiguration(Set<String> stringPropertyNames){
        return checkConfiguration(stringPropertyNames, SMS_CONFIGURATION);
    }

    private List<String> notificationModeList(String notificationModeString){
        List<String> notificationModeList = new ArrayList<>();
        String[] notificationModeArray = notificationModeString.split(",");
        for (String nofitcationMode: notificationModeArray){
            notificationModeList.add(nofitcationMode.trim());
        }
        if (notificationModeList.size() > 0){
            return notificationModeList;
        }
        return null;
    }



}
