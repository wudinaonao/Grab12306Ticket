package com.naonao.grab12306ticket.version.single.exception;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-27 13:46
 **/
public class ConfigurationNotCompletedException extends RuntimeException{

    public ConfigurationNotCompletedException(){

    }

    public ConfigurationNotCompletedException(String message){
        super(message);
    }

}
