package com.naonao.grab12306ticket.version.single.tools;

import com.naonao.grab12306ticket.version.single.entity.GrabTicketInformationEntity;
import lombok.extern.log4j.Log4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 00:13
 **/
@Log4j
public class ComputeHash {

   

    private static final String MD5 = "MD5";
    private static final String SHA1 = "SHA1";
    private static final String SHA256 = "SHA-256";
    private static final String SHA512 = "SHA-512";

    public static String md5(String originString) {
        return computeHash(originString, MD5);
    }

    public static String sha1(String originString) {
        return computeHash(originString, SHA1);
    }

    public static String sha256(String originString) {
        return computeHash(originString, SHA256);
    }

    public static String sha512(String originString) {
        return computeHash(originString, SHA512);
    }

    public static String fromGrabTicketInformation(GrabTicketInformationEntity grabTicketInformationEntity){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(grabTicketInformationEntity.getTrainDate());
        stringBuilder.append(grabTicketInformationEntity.getFromStation());
        stringBuilder.append(grabTicketInformationEntity.getToStation());
        stringBuilder.append(grabTicketInformationEntity.getPassengerName());
        stringBuilder.append(grabTicketInformationEntity.getDocumentType());
        stringBuilder.append(grabTicketInformationEntity.getDocumentNumber());
        return md5(stringBuilder.toString());
    }

    private static String computeHash(String originString, String algorithmName){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithmName);
            messageDigest.update(originString.getBytes());
            return bytesToString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private static String bytesToString(byte[] hashByteArray){
        StringBuilder secpwd = new StringBuilder();
        for (byte hashByte: hashByteArray){
            int v = hashByte & 0xFF;
            if (v < 16){
                secpwd.append(0);
            }
            secpwd.append(Integer.toString(v, 16));
        }
        return secpwd.toString().toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(md5("a"));
    }
}
