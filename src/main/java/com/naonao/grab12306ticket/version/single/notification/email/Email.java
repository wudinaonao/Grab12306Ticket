package com.naonao.grab12306ticket.version.single.notification.email;

import com.naonao.grab12306ticket.version.single.entity.NotificationInformationEntity;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-06 22:51
 **/
public class Email {

    private String host;
    private String port;
    private String username;
    private String password;
    private String sender;
    private String receiver;
    private String[] receivers;
    private boolean ssl;

    public Email(String host,
                 String port,
                 String username,
                 String password,
                 String sender,
                 String receiver,
                 Boolean ssl){
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.sender = sender;
        this.receiver = receiver;
        this.ssl = ssl;
    }

    public Email(String host,
                 String port,
                 String username,
                 String password,
                 String sender,
                 String[] receivers,
                 Boolean ssl){
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.sender = sender;
        this.receivers = receivers;
        this.ssl = ssl;
    }

    public Email(NotificationInformationEntity notificationInformationEntity){
        host = notificationInformationEntity.getSendEmailHost();
        port = notificationInformationEntity.getSendEmailPort();
        username = notificationInformationEntity.getSendEmailUsername();
        password = notificationInformationEntity.getSendEmailPassword();
        sender = notificationInformationEntity.getSendEmail();
        receiver = notificationInformationEntity.getReceiverEmail();
        ssl = true;
    }

    public Boolean sendEmailText(String subject, String text){
        SendEmail sendEmail = new SendEmail();
        if (receiver != null){
            return sendEmail.sendEmailText(
                    host,
                    port,
                    username,
                    password,
                    sender,
                    receiver,
                    subject,
                    text,
                    ssl
            );
        }
        if (receivers != null){
            return sendEmail.sendEmailText(
                    host,
                    port,
                    username,
                    password,
                    sender,
                    receivers,
                    subject,
                    text,
                    ssl
            );
        }
        return false;
    }

    public Boolean sendEmailHtml(String subject, String text){
        SendEmail sendEmail = new SendEmail();
        if (receiver != null){
            return sendEmail.sendEmailHtml(
                    host,
                    port,
                    username,
                    password,
                    sender,
                    receiver,
                    subject,
                    text,
                    ssl
            );
        }
        if (receivers != null){
            return sendEmail.sendEmailHtml(
                    host,
                    port,
                    username,
                    password,
                    sender,
                    receivers,
                    subject,
                    text,
                    ssl
            );
        }
        return false;
    }

}
