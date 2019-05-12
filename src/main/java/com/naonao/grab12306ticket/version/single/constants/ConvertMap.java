package com.naonao.grab12306ticket.version.single.constants;

import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-05 00:06
 **/
@Log4j
public class ConvertMap {

    private static Map<String, String> seatNameToNumber;
    private static Map<String, String> seatNumberToName;
    private static Map<String, String> cityIdMap;

    static {
        setCityIdMap();
        setSeatNameToNumber();
        setSeatNumberToName();
    }

    /**
     *  initialization city id map
     */
    private static void setCityIdMap(){
        String[] cityIdList = GeneralTools.readFileText("./city_id").split("\n");
        cityIdMap = new HashMap<>(16);
        for (String line: cityIdList){
            String[] cityInfo = line.split("\\|");
            String cityName = cityInfo[0];
            String cityId = cityInfo[1];
            cityIdMap.put(cityName, cityId);
        }
    }



    /**
     *  initialization seat name to number
     */
    private static void setSeatNameToNumber(){
        seatNameToNumber = new HashMap<>();
        seatNameToNumber.put("商务座", "9");
        seatNameToNumber.put("一等座", "M");
        seatNameToNumber.put("二等座", "O");
        seatNameToNumber.put("无座", "WZ");
        seatNameToNumber.put("硬座", "A1");
        seatNameToNumber.put("硬卧", "A3");
        seatNameToNumber.put("软卧", "A4");
        seatNameToNumber.put("高级软卧", "A6");
    }
    /**
     *  initialization seat number to name
     */
    private static void setSeatNumberToName(){
        seatNumberToName = new HashMap<>();
        seatNumberToName.put("9", "商务座");
        seatNumberToName.put("M", "一等座");
        seatNumberToName.put("O", "二等座");
        seatNumberToName.put("WZ", "无座");
        seatNumberToName.put("A1", "硬座");
        seatNumberToName.put("A3", "硬卧");
        seatNumberToName.put("A4", "软卧");
        seatNumberToName.put("A6", "高级软卧");
    }

    /**
     * city name to id
     *
     * @param cityName  name
     * @return          id
     */
    public static String cityNameToID(String cityName){
        return cityIdMap.get(cityName);
    }
    /**
     * seat name to number
     *
     * @param seatName  name
     * @return          number
     */
    public static String seatNameToNumber(String seatName){
        return seatNameToNumber.get(seatName);
    }
    /**
     * seat number to name
     *
     * @param seatNumber    number
     * @return              name
     */
    public static String seatNumberToName(String seatNumber){
        return seatNumberToName.get(seatNumber);
    }

    /**
     * seat name to number
     *
     * @param seatNameArr      seatNameArr
     * @return                 seatNumberArr
     */
    public static String[] seatNameToNumber(String[] seatNameArr){
        List<String> seatNumberList = new ArrayList<>();
        for (String seatName: seatNameArr){
            seatNumberList.add(seatNameToNumber(seatName));
        }
        return seatNumberList.toArray(new String[0]);
    }

    /**
     * city name list
     *
     * @return  List<String>
     */
    public static List<String> cityNameList(){
        String[] cityLines = GeneralTools.readFileText("./city_id").split("\n");
        List<String> cityNameList = new ArrayList<>();
        for (String line: cityLines){
            String[] cityInfo = line.split("\\|");
            String cityName = cityInfo[0].trim();
            cityNameList.add(cityName);
        }
        if (cityNameList.size() > 0){
            return cityNameList;
        }
        return null;
    }

    public static List<String> seatTypeNameList(){
        List<String> seatTypeNameList = new ArrayList<>();
        seatTypeNameList.add("商务座");
        seatTypeNameList.add("一等座");
        seatTypeNameList.add("二等座");
        seatTypeNameList.add("无座");
        seatTypeNameList.add("硬座");
        seatTypeNameList.add("硬卧");
        seatTypeNameList.add("软卧");
        seatTypeNameList.add("高级软卧");
        return seatTypeNameList;
    }
}
