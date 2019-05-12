package com.naonao.grab12306ticket.version.single.scheduler.initialization;


import com.naonao.grab12306ticket.version.single.notify.email.Email;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;
import java.util.Properties;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-12 14:14
 **/
@Log4j
public class CheckNotificationConfiguration {



    private Properties properties;


    public CheckNotificationConfiguration(){
        properties = GeneralTools.getConfig();
    }

    public Boolean check(){
        // Boolean emailResult = email();
        return email();
    }


    private Boolean email(){
        String subject = "12306 grab ticket notification test";
        String text = "if you can look this information, then you can receive notification.";
        String receiverEmail = properties.getProperty("Email.receiverEmail").trim();
        String senderEmail = properties.getProperty("Email.senderEmail").trim();
        String senderHost = properties.getProperty("Email.senderHost").trim();
        String senderPort = properties.getProperty("Email.senderPort").trim();
        String senderUsername = properties.getProperty("Email.senderUsername").trim();
        String senderPassword = properties.getProperty("Email.senderPassword").trim();
        String[] receiversEmail = receiverEmail.split(",");
        if (receiversEmail.length > 1){
            for (int i = 0; i < receiversEmail.length; i++) {
                receiversEmail[i] = receiversEmail[i].trim();
            }
            return new Email(
                    senderHost,
                    senderPort,
                    senderUsername,
                    senderPassword,
                    senderEmail,
                    receiversEmail,
                    true
            ).sendEmailText(subject, text);
        }else{
            return new Email(
                    senderHost,
                    senderPort,
                    senderUsername,
                    senderPassword,
                    senderEmail,
                    receiverEmail,
                    true
            ).sendEmailText(subject, text);
        }
    }

    public static void main(String[] args) {
        CheckNotificationConfiguration checkNotificationConfiguration = new CheckNotificationConfiguration();
        System.out.println(checkNotificationConfiguration.check());
    }
}
