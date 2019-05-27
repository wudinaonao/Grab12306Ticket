package com.naonao.grab12306ticket.version.single.notification;

import com.naonao.grab12306ticket.version.single.constants.NotificationMethodName;
import com.naonao.grab12306ticket.version.single.entity.NotificationInformationEntity;
import com.naonao.grab12306ticket.version.single.entity.yml.Configuration;
import com.naonao.grab12306ticket.version.single.notification.base.AbstractNotification;
import com.naonao.grab12306ticket.version.single.notification.email.Email;
import com.naonao.grab12306ticket.version.single.notification.phone.Phone;
import com.naonao.grab12306ticket.version.single.notification.sms.SMS;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.booking.BookingReturnResult;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;


/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-09 00:28
 **/
@Log4j
public class Notification extends AbstractNotification {

    private Configuration configuration;

    /**
     * initialization properties file
     */
    public Notification(){
        configuration = GeneralTools.getConfiguration();
    }

    /**
     * Select the delivery method according to the notification method
     * @param bookingReturnResult       bookingReturnResult
     * @param notificationInformationEntity    notificationInformationEntity
     * @return                          Boolean
     */
    public Boolean sendNotification(BookingReturnResult bookingReturnResult, NotificationInformationEntity notificationInformationEntity){
        // here is send notification method,
        // get notification mode from grabTicketInformationEntity
        // notification mode format ---> email, phone, sms
        // get user set notification mode list
        List<NotificationMethodName> notificationMethodNameList = getNotificationMethodNameList(notificationInformationEntity.getNotificationMode());
        if (notificationMethodNameList == null){
            log.error(NOT_FOUND_ELIGIBLE_NOTIFICATION_MODE);
            return false;
        }
        // Maybe have multi result
        List<Boolean> resultList = new ArrayList<>();
        for (NotificationMethodName notificationMethodName: notificationMethodNameList){
            resultList.add(sendNotification(notificationMethodName, bookingReturnResult));
        }
        return handleResult(resultList);
    }

    /**
     *  send notification
     *  if add a new method later, just override this method
     * @param notificationMethodName  notification method name
     * @return                  Boolean
     */
    private Boolean sendNotification(NotificationMethodName notificationMethodName,
                                     BookingReturnResult bookingReturnResult){
        if (notificationMethodName == NotificationMethodName.EMAIL){
            return email(bookingReturnResult);
        }
        if (notificationMethodName == NotificationMethodName.PHONE){
            return phone();
        }
        if (notificationMethodName == NotificationMethodName.SMS){
            return sms();
        }
        return false;
    }

    /**
     * send email
     * single version
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
        String host = configuration.getNotification().getConfig().getEmail().getSenderEmailHost().trim();
        String port = configuration.getNotification().getConfig().getEmail().getSenderEmailPort().trim();
        String username = configuration.getNotification().getConfig().getEmail().getSenderEmailUsername().trim();
        String password = configuration.getNotification().getConfig().getEmail().getSenderEmailPassword().trim();
        String sender = configuration.getNotification().getConfig().getEmail().getSenderEmail().trim();
        String receiver = configuration.getNotification().getConfig().getEmail().getReceiverEmail().trim();
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
     * single version
     * @return  Boolean
     */
    private Boolean phone(){
        String platform = configuration.getNotification().getConfig().getPhone().getInterfaceName().getNotificationInterfaceName();
        if (platform == null){
            return false;
        }
        platform = platform.trim().toUpperCase();
        if (platform.equals(YUNZHIXIN)){
            String templateId = configuration.getPlatform().getConfig().getYunzhixin().getTemplateId();
            String to = configuration.getNotification().getReceiverPhone();
            if (templateId == null || to == null){
                return false;
            }
            return new Phone().yunzhixin().sendPhoneVoice(to, templateId);
        }
        if (platform.equals(TWILIO)){
            String from = configuration.getPlatform().getConfig().getTwilio().getFromPhoneNumber();
            String to = configuration.getNotification().getReceiverPhone();
            String configPaht = configuration.getPlatform().getConfig().getTwilio().getVoiceUrl();
            if (from == null || to == null || configPaht == null){
                return false;
            }
            return new Phone().twilio().sendPhoneVoice(from, to, configPaht);
        }
        return false;
    }



    /**
     * send sms
     * single version
     * @return  Boolean
     */
    private Boolean sms(){
        String platform = configuration.getNotification().getConfig().getSms().getInterfaceName().getNotificationInterfaceName();
        if (platform == null){
            return false;
        }
        platform = platform.trim().toUpperCase();
        if (platform.equals(NEXMO)){
            String content = configuration.getNotification().getConfig().getSms().getContent();
            String to = configuration.getNotification().getReceiverPhone();
            String title = configuration.getNotification().getConfig().getSms().getTitle();
            if (content == null || to == null){
                return false;
            }
            return new SMS().nexmo().sendSMS(title, to, content);
        }
        if (platform.equals(TWILIO)){
            String from = configuration.getPlatform().getConfig().getTwilio().getFromPhoneNumber();
            String to = configuration.getNotification().getReceiverPhone();
            String content = configuration.getNotification().getConfig().getSms().getContent();
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
     * list element is a enum type, reference notificationMethodName class
     *
     * @param notificationModeString      notificationModeString
     * @return                      List<notificationMethodName>
     */
    private List<NotificationMethodName> getNotificationMethodNameList(String notificationModeString){
        String[] notificationModeArray = notificationModeString.split(",");
        List<NotificationMethodName> notificationMethodNameList = new ArrayList<>();
        for (String notificationMode: notificationModeArray){
            notificationMode = notificationMode.trim().toUpperCase();
            try{
                notificationMethodNameList.add(NotificationMethodName.valueOf(notificationMode));
            }catch (IllegalArgumentException e){
                continue;
            }
        }
        if (notificationMethodNameList.size() > 0){
            return notificationMethodNameList;
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
