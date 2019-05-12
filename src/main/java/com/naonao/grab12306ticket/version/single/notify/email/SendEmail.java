package com.naonao.grab12306ticket.version.single.notify.email;

import com.naonao.grab12306ticket.version.single.notify.email.common.AbstractEmail;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.log4j.Log4j;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * send email class
 * this class implements methods for sending text and html
 *
 * @program: 12306grabticket_java
 * @description: send email
 * @author: Wen lyuzhao
 * @create: 2019-05-06 18:10
 **/
@Log4j
public class SendEmail extends AbstractEmail {

   


    /**
     *      send text format email
     *
     * @param host      email host
     * @param port      port, ssh port is 465
     * @param username  username
     * @param password  password
     * @param sender    send email
     * @param receiver  recevier email
     * @param subject   subject
     * @param text      text
     * @param ssl       use ssh
     * @return          true, false
     */
    protected Boolean sendEmailText(String host,
                                     String port,
                                     String username,
                                     String password,
                                     String sender,
                                     String receiver,
                                     String subject,
                                     String text,
                                     boolean ssl){
        try {
            Properties properties = getProperties(host, port, ssl);
            Session session = getSession(properties);
            Transport transport = getTransport(session, host, username, password);
            MimeMessage message = getMessage(session, sender, receiver, subject, text, false);
            boolean isNull = properties == null || transport == null || message == null;
            if (isNull){
                log.error(INITIALIZATION_EMAIL_SERVER_FAIED);
                return false;
            }
            try{
                transport.sendMessage(message, message.getAllRecipients());
            }
            catch (MessagingException e){
                log.error(e.getMessage());
                return false;
            }
            finally {
                transport.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }


    /**
     *      send text email, multi receiver
     *
     * @param host      email host
     * @param port      port, ssh port is 465
     * @param username  username
     * @param password  password
     * @param sender    send email
     * @param receivers recevier email array
     * @param subject   subject
     * @param text      text
     * @param ssl       use ssh
     * @return          true, false
     */
    protected Boolean sendEmailText(String host,
                                  String port,
                                  String username,
                                  String password,
                                  String sender,
                                  String[] receivers,
                                  String subject,
                                  String text,
                                  boolean ssl){
        try {
            for (String receiver: receivers){
                Properties properties = getProperties(host, port, ssl);
                Session session = getSession(properties);
                Transport transport = getTransport(session, host, username, password);
                MimeMessage message = getMessage(session, sender, receiver, subject, text, false);
                boolean isNull = properties == null || transport == null || message == null;
                if (isNull){
                    log.error(INITIALIZATION_EMAIL_SERVER_FAIED);
                    return false;
                }
                try {
                    transport.sendMessage(message, message.getAllRecipients());
                } catch (MessagingException e){
                    log.error(e.getMessage());
                    return false;
                }
                finally {
                    transport.close();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     *      send html email
     *
     * @param host      email host
     * @param port      port, ssh port is 465
     * @param username  username
     * @param password  password
     * @param sender    send email
     * @param receiver  recevier email
     * @param subject   subject
     * @param text      text
     * @param ssl       use ssh
     * @return          true, false
     */
    protected Boolean sendEmailHtml(String host,
                                 String port,
                                 String username,
                                 String password,
                                 String sender,
                                 String receiver,
                                 String subject,
                                 String text,
                                 boolean ssl){
        try {
            Properties properties = getProperties(host, port, ssl);
            Session session = getSession(properties);
            Transport transport = getTransport(session, host, username, password);
            MimeMessage message = getMessage(session, sender, receiver, subject, text, true);
            boolean isNull = properties == null || transport == null || message == null;
            if (isNull){
                log.error(INITIALIZATION_EMAIL_SERVER_FAIED);
                return false;
            }
            try{
                transport.sendMessage(message, message.getAllRecipients());
            }
            catch (MessagingException e){
                log.error(e.getMessage());
                return false;
            }
            finally {
                transport.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     *      send html email, multi receiver
     *
     * @param host      email host
     * @param port      port, ssh port is 465
     * @param username  username
     * @param password  password
     * @param sender    send email
     * @param receivers recevier email array
     * @param subject   subject
     * @param text      text
     * @param ssl       use ssh
     * @return          true, false
     */
    protected Boolean sendEmailHtml(String host,
                                  String port,
                                  String username,
                                  String password,
                                  String sender,
                                  String[] receivers,
                                  String subject,
                                  String text,
                                  boolean ssl){
        try {
            for (String receiver: receivers){
                Properties properties = getProperties(host, port, ssl);
                Session session = getSession(properties);
                Transport transport = getTransport(session, host, username, password);
                MimeMessage message = getMessage(session, sender, receiver, subject, text, true);
                boolean isNull = properties == null || transport == null || message == null;
                if (isNull){
                    log.error(INITIALIZATION_EMAIL_SERVER_FAIED);
                    return false;
                }
                try {
                    transport.sendMessage(message, message.getAllRecipients());
                } catch (MessagingException e){
                    log.error(e.getMessage());
                    return false;
                }
                finally {
                    transport.close();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    private static Properties getProperties(String host, String port, boolean ssl) {
        try{
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", host);
            properties.setProperty("mail.smtp.port", port);
            properties.setProperty("mail.transport.protocol", "smtp");
            properties.setProperty("mail.smtp.auth", "true");
            // Debug
            // properties.setProperty("mail.debug", "true");

            // start ssl
            if (ssl){
                MailSSLSocketFactory mailSSLSocketFactory = new MailSSLSocketFactory();
                mailSSLSocketFactory.setTrustAllHosts(true);
                properties.put("mail.smtp.ssl.enable", "true");
                properties.put("mail.smtp.ssl.socketFactory", mailSSLSocketFactory);
            }
            return properties;
        }catch (GeneralSecurityException e){
            log.error(e.getMessage());
        }
        return null;

    }
    private Session getSession(Properties properties) {
        return Session.getInstance(properties);
    }
    private Transport getTransport(Session session, String host, String username, String password) {
        try{
            Transport transport = session.getTransport();
            transport.connect(host, username, password);
            return transport;
        }catch (MessagingException e) {
            log.error(e.getMessage());
        }
        return null;
    }
    private static MimeMessage getMessage(Session session, String sender, String receiver, String subject, String text, boolean isHtml) {
        // create email object
        try{
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(subject);
            if (isHtml) {
                message.setContent(text, "text/html;charset=utf-8");
            }else{
                message.setText(text);
            }
            return message;
        }catch (MessagingException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
