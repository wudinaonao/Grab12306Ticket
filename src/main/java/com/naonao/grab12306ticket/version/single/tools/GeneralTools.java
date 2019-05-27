package com.naonao.grab12306ticket.version.single.tools;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.single.entity.yml.Configuration;
import com.naonao.grab12306ticket.version.single.exception.ConfigurationNotCompletedException;
import lombok.extern.log4j.Log4j;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: 12306grabticket_java
 * @description: GeneralTools
 * @author: Wen lyuzhao
 * @create: 2019-04-29 17:31
 **/
@Log4j
public class GeneralTools {



    /**
     * get json result status, success or failed
     *
     * @param resultStr result string
     * @return boolean
     */
    public static boolean getResultJsonStatus(String resultStr) {
        JSONObject jsonObject = JSONObject.parseObject(resultStr);
        return jsonObject.getBoolean("status");
    }

    /**
     * url encode
     *
     * @param url url
     * @return string
     */
    public static String encodeURL(String url) {
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }

    /**
     * url decode
     *
     * @param url url
     * @return string
     */
    public static String decodeURL(String url) {
        return URLDecoder.decode(url, StandardCharsets.UTF_8);
    }


    /**
     * read, write, append file, text mode
     * file path in resource path.
     * @param filePath  file path
     * @return          String
     */
    public static String readFileText(String filePath) {
        StringBuilder content = new StringBuilder();
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        if (resourceAsStream == null){
            throw new FileSystemNotFoundException("read file failed.");
        }
        try {
            InputStreamReader read = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt += "\n";
                content.append(lineTxt);
            }
            bufferedReader.close();
            read.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return content.toString().substring(0, content.toString().length() - 1);
    }

    /**
     * get configuration yml
     * @return  Configuration
     */
    public static Configuration getConfiguration(){
        Yaml yaml = new Yaml();
        return yaml.loadAs(Thread.currentThread().getContextClassLoader().getResourceAsStream("configuration.yml"), Configuration.class);
    }

    /**
     * check configuration
     * @throws ConfigurationNotCompletedException    configuration error message
     */
    public static void checkConfiguration() throws ConfigurationNotCompletedException{
        Yaml yaml = new Yaml();
        recursiveCheckMap(yaml.loadAs(Thread.currentThread().getContextClassLoader().getResourceAsStream("configuration.yml"), Map.class));
    }

    /**
     * recursive check configuration file
     * input yml map, recursive check item value is null or emtpy
     * @param ymlMap                                 ymlMap
     * @throws ConfigurationNotCompletedException    configuration error message
     */
    private static void recursiveCheckMap(Map ymlMap) throws ConfigurationNotCompletedException {
        String[] exculedArray = {
          "trainName",
          "backTrainDate",
          "expectSeatNumber"
        };
        for (Object key : ymlMap.keySet()){
            // can empty not null
            if (Arrays.asList(exculedArray).contains(key.toString().trim())){
                if(ymlMap.get(key) == null){
                    String message = String.format("[%s] is null", key.toString());
                    throw new ConfigurationNotCompletedException(message);
                }
                continue;
            }
            // not empty and null
            if (ymlMap.get(key) == null || "".equals(ymlMap.get(key))){
                String message = String.format("[%s] not set", key.toString());
                throw new ConfigurationNotCompletedException(message);
            }
            if (ymlMap.get(key).getClass() == LinkedHashMap.class){
                recursiveCheckMap((Map) ymlMap.get(key));
            }
        }
    }


    /**
     * Determine if it is in system maintenance time.
     * this is a general method, he will automatically
     * set the time zone and daylight saving time.
     * if system in maintenance time return true
     * @return  Boolean
     */
    public static Boolean systemMaintenanceTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // get Beijing date for later comparison.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(changeTimeZone(new Date(System.currentTimeMillis()), TimeZone.getDefault(), TimeZone.getTimeZone("GMT+8")));
        Date nowTime = null;
        Date beginTime = null;
        Date endTime = null;
        // get timestamp, if is summer time, then subtract 3600s.
        long timestamp = TimeZone.getDefault().inDaylightTime(new Date(System.currentTimeMillis()))? System.currentTimeMillis() - (3600 * 1000): System.currentTimeMillis();
        try {
            // get date object, time zone is GMT+8, is Beijing time.
            nowTime = changeTimeZone(new Date(timestamp), TimeZone.getDefault(), TimeZone.getTimeZone("GMT+8"));
            // this is 12306 system working time interval.
            beginTime = simpleDateFormat.parse(date + " 06:00:00");
            endTime = simpleDateFormat.parse(date + " 23:00:00");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if (nowTime == null || beginTime == null || endTime == null){
            return false;
        }
        // what i got above is Beijing time.
        // create Calendar instance no time zone specified, if the specified time zone
        // will set the local time zone to the specified time zone.
        Calendar now = Calendar.getInstance();
        now.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        // if the current time is in this interval, then system is not maintenance,
        // otherwise system is maintenance.
        if (now.after(begin) && now.before(end)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * get date string after change time zone.
     * @param date      date
     * @param oldZone   old time zone
     * @param newZone   new time zone
     * @return          date object
     */
    private static Date changeTimeZone(Date date, TimeZone oldZone, TimeZone newZone) {
        Date dateTmp = null;
        if (date != null) {
            int timeOffset = oldZone.getRawOffset() - newZone.getRawOffset();
            dateTmp = new Date(date.getTime() - timeOffset);
        }
        return dateTmp;
    }



    public static String currentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    public static String currentDateAndTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

}
