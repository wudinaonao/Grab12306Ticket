package com.naonao.grab12306ticket.version.single.scheduler.initialization;

import com.naonao.grab12306ticket.version.single.constants.ConvertMap;
import com.naonao.grab12306ticket.version.single.scheduler.initialization.common.AbstractInitialization;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-11 22:42
 **/
@Log4j
public class CheckTicketConfiguration extends AbstractInitialization {

    

    private List<String> cityNameList;
    private Properties properties;
    private List<String> seatTypeNameList;

    public CheckTicketConfiguration(){
        cityNameList = ConvertMap.cityNameList();
        seatTypeNameList = ConvertMap.seatTypeNameList();
        properties = GeneralTools.getConfig();
    }

    public Boolean check(){
        if (cityNameList == null || properties == null || seatTypeNameList == null){
            log.error("initialization ticket configuration failed.");
            return false;
        }
        if (!checkTime(properties.getProperty(TICKET_AFTERTIME))){
            log.error("check after time failed.");
            return false;
        }
        if (!checkTime(properties.getProperty(TICKET_BEFORETIME))){
            log.error("check before time failed.");
            return false;
        }
        if (!checkDate(properties.getProperty(TICKET_TRAINDATE))){
            log.error("check train date failed.");
            return false;
        }
        if (!checkStation(properties.getProperty(TICKET_FROMSTATION))){
            log.error("check from station failed.");
            return false;
        }
        if (!checkStation(properties.getProperty(TICKET_TOSTATION))){
            log.error("check to station failed.");
            return false;
        }
        if (!checkMobile(properties.getProperty(TICKET_MOBILE))){
            log.error("check mobile failed.");
            return false;
        }
        if (!checkSeatType(properties.getProperty(TICKET_SEATTYPE))){
            log.error("check seat type failed.");
            return false;
        }
        return true;
    }

    private Boolean checkTime(String text){
        if (text == null){
            log.error("time is null.");
            return false;
        }
        try{
            String[] textArray = text.split(":");
            for (String element: textArray) {
                if (element.trim().length() > 2){
                    log.error("time format is error.");
                    return false;
                }
            }
            Integer h = Integer.valueOf(textArray[0].trim());
            Integer min = Integer.valueOf(textArray[1].trim());
            if (!(0 <= h && h < HOUR_MAX_VALUE)){
                log.error("time format is error.");
                return false;
            }
            if (!(0 <= min && min < MIN_MAX_VALUE)){
                log.error("time format is error.");
                return false;
            }
        } catch (Exception e){
            log.error("time parse failed.");
            return false;
        }
        return true;
    }

    private Boolean checkDate(String text){
        if (text == null){
            log.error("date is null.");
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentData = new Date();
        try{
            Date inputDate = dateFormat.parse(text);
            if (inputDate.getTime() < currentData.getTime()){
                log.error("set date it is less than the current date.");
                return false;
            }
            return true;
        }catch (ParseException e) {
            log.error("date parse failed.");
            return false;
        }
    }

    private Boolean checkStation(String text){
        if (text == null){
            log.error("station name is null.");
            return false;
        }
        return cityNameList.contains(text);
    }

    private Boolean checkMobile(String text){
        if (text == null){
            log.error("mobile is null.");
            return false;
        }
        for (int i = text.length(); --i >=0;) {
            if (!Character.isDigit(text.charAt(i))){
                log.error("mobile has invalid symbol.");
                return false;
            }
        }
        return true;
    }

    private Boolean checkSeatType(String text){
        if (text == null){
            log.error("seat type is null.");
            return false;
        }
        boolean status = true;
        for (String seatTypeName: text.split(",")){
            seatTypeName = seatTypeName.trim();
            status &= seatTypeNameList.contains(seatTypeName);
        }
        return status;
    }



}
