package com.naonao.grab12306ticket.version.single.exception;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-27 15:27
 **/
public class ConfigurationFormatErrorException extends RuntimeException{

    public ConfigurationFormatErrorException(){

    }

    public ConfigurationFormatErrorException(String message){
        super(message);
    }


}
