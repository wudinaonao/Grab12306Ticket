package com.naonao.grab12306ticket.version.single.notify;

import com.naonao.grab12306ticket.version.single.resultclass.ticket.booking.BookingReturnResult;
import com.naonao.grab12306ticket.version.single.constants.NotifyMethodName;
import com.naonao.grab12306ticket.version.single.database.table.NotifyInformationTable;
import com.naonao.grab12306ticket.version.single.notify.common.AbstractNotify;
import com.naonao.grab12306ticket.version.single.notify.email.Email;
import com.naonao.grab12306ticket.version.single.notify.phone.Phone;
import com.naonao.grab12306ticket.version.single.notify.sms.SMS;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-09 00:28
 **/
@Log4j
public class Notify extends AbstractNotify {

    

    private Properties properties;

    /**
     * initialization properties file
     */
    public Notify(){
        properties = GeneralTools.getConfig();
        if (properties == null){
            log.error(READ_CONFIG_FAILED);
        }
    }

    /**
     * Select the delivery method according to the notification method
     *
     * @param bookingReturnResult       bookingReturnResult
     * @param notifyInformationTable    notifyInformationTable
     * @return                          Boolean
     */
    public Boolean sendNotification(BookingReturnResult bookingReturnResult, NotifyInformationTable notifyInformationTable){
        // here is send notification method,
        // get notification mode from grabTicketInformationTable
        // notification mode format ---> email, phone, sms

        // get user set notification mode list
        List<NotifyMethodName> notifyMethodNameList = getNotifyMethodNameList(notifyInformationTable.getNotifyMode());
        if (notifyMethodNameList == null){
            log.error(NOT_FOUND_ELIGIBLE_NOTIFICATION_MODE);
            return false;
        }
        // Maybe have multi result
        List<Boolean> resultList = new ArrayList<>();
        for (NotifyMethodName notifyMethodName: notifyMethodNameList){
            resultList.add(sendNotification(notifyMethodName, bookingReturnResult, notifyInformationTable));
        }
        return handleResult(resultList);
    }

    /**
     *  send notification
     *  if add a new method later, just override this method
     *
     * @param notifyMethodName  notification method name
     * @return                  Boolean
     */
    private Boolean sendNotification(NotifyMethodName notifyMethodName,
                                     BookingReturnResult bookingReturnResult,
                                     NotifyInformationTable notifyInformationTable){
        if (notifyMethodName == NotifyMethodName.EMAIL){
            return email(bookingReturnResult, notifyInformationTable);
        }
        if (notifyMethodName == NotifyMethodName.PHONE){
            return phone(notifyInformationTable);
        }
        if (notifyMethodName == NotifyMethodName.SMS){
            return sms(notifyInformationTable);
        }
        return false;
    }

    /**
     * send email
     * database version
     *
     * @param bookingReturnResult       bookingReturnResult
     * @param notifyInformationTable    notifyInformationTable
     * @return                          Boolean
     */
    private Boolean email(BookingReturnResult bookingReturnResult, NotifyInformationTable notifyInformationTable){
        String subject = String.format(
                "12306抢票通知 --- 从: %s 到: %s",
                bookingReturnResult.getBookingResultObject().getFromStationName(),
                bookingReturnResult.getBookingResultObject().getToStationName()
        );
        String text = bookingReturnResult.getBookingResultString();
        Email email = new Email(notifyInformationTable);
        return email.sendEmailText(subject, text);
    }

    /**
     * send email
     * single version
     *
     * @param bookingReturnResult       bookingReturnResult
     * @return                          Boolean
     */
    private Boolean email(BookingReturnResult bookingReturnResult){
        String subject = String.format(
                "12306抢票通知 --- 从: %s 到: %s",
                bookingReturnResult.getBookingResultObject().getFromStationName(),
                bookingReturnResult.getBookingResultObject().getToStationName()
        );
        String text = bookingReturnResult.getBookingResultString();
        String host = properties.getProperty("Email.senderHost").trim();
        String port = properties.getProperty("Email.senderPort").trim();
        String username = properties.getProperty("Email.senderUsername").trim();
        String password = properties.getProperty("Email.senderPassword").trim();
        String sender = properties.getProperty("Email.senderEmail").trim();
        String receiver = properties.getProperty("Email.receiverEmail").trim();
        String[] receivers = receiver.split(",");
        for (int i = 0; i < receivers.length; i++) {
            receivers[i] = receivers[i].trim();
        }
        Boolean ssl = true;
        if (receivers.length > 1){
            Email email = new Email(
                    host,
                    port,
                    username,
                    password,
                    sender,
                    receivers,
                    ssl
            );
            return email.sendEmailText(subject, text);
        }else{
            Email email = new Email(
                    host,
                    port,
                    username,
                    password,
                    sender,
                    receiver,
                    ssl
            );
            return email.sendEmailText(subject, text);
        }
    }

    /**
     * call phone
     * database version
     *
     * @param notifyInformationTable    notifyInformationTable
     * @return                          Boolean
     */
    private Boolean phone(NotifyInformationTable notifyInformationTable){
        String templateId = properties.getProperty("Yunzhixin.templateId");
        String to = notifyInformationTable.getReceiverPhone();
        if (templateId == null){
            return false;
        }
        return new Phone().yunzhixin().sendPhoneVoice(to, templateId);
    }

    /**
     * call phone
     * single version
     *
     * @return  Boolean
     */
    private Boolean phone(){
        String platform = properties.getProperty("Platform.name");
        if (platform == null){
            return false;
        }
        platform = platform.trim().toUpperCase();
        if (platform.equals(YUNZHIXIN)){
            String templateId = properties.getProperty("Yunzhixin.templateId");
            String to = properties.getProperty("Phone.number");
            if (templateId == null || to == null){
                return false;
            }
            return new Phone().yunzhixin().sendPhoneVoice(to, templateId);
        }
        if (platform.equals(TWILIO)){
            String from = properties.getProperty("Twilio.fromPhoneNumber");
            String to = properties.getProperty("Phone.number");
            String configPaht = properties.getProperty("Twilio.configPath");
            if (from == null || to == null || configPaht == null){
                return false;
            }
            return new Phone().twilio().sendPhoneVoice(from, to, configPaht);
        }
        return false;
    }

    /**
     * send sms
     * database version
     *
     * @param notifyInformationTable    notifyInformationTable
     * @return                          Boolean
     */
    private Boolean sms(NotifyInformationTable notifyInformationTable){
        String text = properties.getProperty("SMS.content");
        String twilioFromPhoneNumber = properties.getProperty("Twilio.fromPhoneNumber");
        String to = notifyInformationTable.getReceiverPhone().trim();
        if (text == null || twilioFromPhoneNumber == null){
            return false;
        }
        return new SMS().twilio().sendSMS(twilioFromPhoneNumber, to, text);
    }


    /**
     * send sms
     * single version
     * @return  Boolean
     */
    private Boolean sms(){
        String platform = properties.getProperty("Platform.name");
        if (platform == null){
            return false;
        }
        platform = platform.trim().toUpperCase();
        if (platform.equals(NEXMO)){
            String content = properties.getProperty("SMS.content");
            String to = properties.getProperty("Phone.number");
            if (content == null || to == null){
                return false;
            }
            return new SMS().nexmo().sendSMS("12306Notification", to, content);
        }
        if (platform.equals(TWILIO)){
            String from = properties.getProperty("Twilio.fromPhoneNumber");
            String to = properties.getProperty("Phone.number");
            String content = properties.getProperty("SMS.content");
            if (from == null || to == null || content == null){
                return false;
            }
            return new SMS().twilio().sendSMS(from, to, content);
        }
        return false;
    }

    /**
     * get notification mode list by String
     * example:
     *          String ---> email, phone, sms
     *          List   ---> ["email", "phone", "sms']
     * list element is a enum type, reference NotifyMethodName class
     *
     * @param notifyModeString      notifyModeString
     * @return                      List<NotifyMethodName>
     */
    private List<NotifyMethodName> getNotifyMethodNameList(String notifyModeString){
        String[] notifyModeArray = notifyModeString.split(",");
        List<NotifyMethodName> notifyMethodNameList = new ArrayList<>();
        for (String notifyMode: notifyModeArray){
            notifyMode = notifyMode.trim().toUpperCase();
            try{
                notifyMethodNameList.add(NotifyMethodName.valueOf(notifyMode));
            }catch (IllegalArgumentException e){
                continue;
            }
        }
        if (notifyMethodNameList.size() > 0){
            return notifyMethodNameList;
        }
        return null;
    }

    /**
     * handle notification result
     * because maybe have many notification mode, so may have many result.
     * if there are multiple results, traverse each result to determine if
     * it is ture, false or null.
     * if have false or null, then return false.
     *
     * @param resultList    resultList
     * @return              Boolean
     */
    private Boolean handleResult(List<Boolean> resultList){
        // create new result list save not null result
        List<Boolean> newResultList = new ArrayList<>();
        for (Boolean result: resultList){
            if (result != null){
                newResultList.add(result);
            }
        }
        // if result list all is null return false
        if (newResultList.size() <= 0 ){
            return false;
        }
        // compute boolean value
        Boolean returnResult = true;
        for (Boolean result: newResultList){
            returnResult = returnResult && result;
        }
        return returnResult;
    }

}
