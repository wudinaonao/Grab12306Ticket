package com.naonao.grab12306ticket.version.single.scheduler.initialization;

import com.naonao.grab12306ticket.version.single.constants.ConvertMap;
import com.naonao.grab12306ticket.version.single.entity.yml.Configuration;
import com.naonao.grab12306ticket.version.single.exception.ConfigurationFormatErrorException;
import com.naonao.grab12306ticket.version.single.scheduler.base.AbstractInitialization;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-11 22:42
 **/
@Log4j
public class CheckTicketConfiguration extends AbstractInitialization {

    

    private List<String> cityNameList;
    private List<String> seatTypeNameList;
    private Configuration configuration;
    
    public CheckTicketConfiguration(){
        cityNameList = ConvertMap.cityNameList();
        seatTypeNameList = ConvertMap.seatTypeNameList();
        configuration = GeneralTools.getConfiguration();
    }

    public void check(){
        if (cityNameList == null || seatTypeNameList == null){
            throw new ConfigurationFormatErrorException("initialization ticket configuration failed.");
        }
        if (!checkTime(configuration.getTicket().getAfterTime())){
            throw new ConfigurationFormatErrorException("check after time failed.");
        }
        if (!checkTime(configuration.getTicket().getBeforeTime())){
            throw new ConfigurationFormatErrorException("check before time failed.");
        }
        if (!checkDate(configuration.getTicket().getTrainDate())){
            throw new ConfigurationFormatErrorException("check train date failed.");
        }
        if (!checkStation(configuration.getTicket().getFromStation())){
            throw new ConfigurationFormatErrorException("check from station failed.");
        }
        if (!checkStation(configuration.getTicket().getToStation())){
            throw new ConfigurationFormatErrorException("check to station failed.");
        }
        if (!checkMobile(configuration.getTicket().getMobile())){
            throw new ConfigurationFormatErrorException("check mobile failed.");
        }
        if (!checkSeatType(configuration.getTicket().getSeatType())){
            throw new ConfigurationFormatErrorException("check seat type failed.");
        }
    }

    private Boolean checkTime(String text){
        if (text == null){
            throw new ConfigurationFormatErrorException("time is null.");
        }
        try{
            String[] textArray = text.split(":");
            for (String element: textArray) {
                if (element.trim().length() > 2){
                    throw new ConfigurationFormatErrorException("time format is error.");
                }
            }
            Integer h = Integer.valueOf(textArray[0].trim());
            Integer min = Integer.valueOf(textArray[1].trim());
            if (!(0 <= h && h < HOUR_MAX_VALUE)){
                throw new ConfigurationFormatErrorException("time format is error.");
            }
            if (!(0 <= min && min < MIN_MAX_VALUE)){
                throw new ConfigurationFormatErrorException("time format is error.");
            }
        } catch (Exception e){
            throw new ConfigurationFormatErrorException("time parse failed.");
        }
        return true;
    }

    private Boolean checkDate(String text){
        if (text == null){
            throw new ConfigurationFormatErrorException("date is null.");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentData = new Date();
        try{
            Date inputDate = dateFormat.parse(text);
            if (inputDate.getTime() < currentData.getTime()){
                throw new ConfigurationFormatErrorException("set date it is less than the current date.");
            }
            return true;
        }catch (ParseException e) {
            throw new ConfigurationFormatErrorException("date parse failed.");
        }
    }

    private Boolean checkStation(String text){
        if (text == null){
            throw new ConfigurationFormatErrorException("station name is null.");
        }
        return cityNameList.contains(text);
    }

    private Boolean checkMobile(String text){
        if (text == null){
            throw new ConfigurationFormatErrorException("mobile is null.");
        }
        for (int i = text.length(); --i >=0;) {
            if (!Character.isDigit(text.charAt(i))){
                throw new ConfigurationFormatErrorException("mobile has invalid symbol.");
            }
        }
        return true;
    }

    private Boolean checkSeatType(String text){
        if (text == null){
            throw new ConfigurationFormatErrorException("seat type is null.");
        }
        boolean status = true;
        for (String seatTypeName: text.split(",")){
            seatTypeName = seatTypeName.trim();
            status &= seatTypeNameList.contains(seatTypeName);
        }
        return status;
    }


}
