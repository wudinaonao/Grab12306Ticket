package com.naonao.grab12306ticket.version.single.tools;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j;


import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

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
     * read, write, append file, text mdoe
     *
     * @param filePath file path
     * @return
     */
    public static String readFileText(String filePath) {
        // List<String> lineList = new ArrayList<String>();
        StringBuilder content = new StringBuilder("");
        try {
            String encoding = "UTF-8";
            File file = new File(filePath);
            // 判断文件是否存在
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    content.append(lineTxt + "\n");
                }
                bufferedReader.close();
                read.close();
            } else {
                log.error("not found file");
            }
        } catch (Exception e) {
            log.error("read file failed");
            e.printStackTrace();
        }
        return content.toString().substring(0, content.toString().length() - 1);
    }

    public static boolean appendFileText(String filePath, String text) {

        FileWriter fileWriter = null;

        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            File file = new File(filePath);
            fileWriter = new FileWriter(file, true);
        } catch (IOException e) {
            return false;
        }
        PrintWriter PrintWriter = new PrintWriter(fileWriter);
        PrintWriter.println(text);
        PrintWriter.flush();

        try {
            PrintWriter.flush();
            PrintWriter.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean writeFileText(String filePath, String text) {
        try {
            File file = new File(filePath);
            file.delete();
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static Properties getConfig() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream("config.properties"), StandardCharsets.UTF_8);
            Properties properties = new Properties();
            properties.load(inputStreamReader);
            return properties;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }


    // public static void main(String[] args) throws Exception{
    //     Properties properties = GeneralTools.getConfig();
    //     System.out.println(properties.getProperty("Twilio.accountSid"));
    // }

    // public static void main(String[] args) {
    //     System.out.println(computedHash("wudinaonao"));
    // }

    public static String formatTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
