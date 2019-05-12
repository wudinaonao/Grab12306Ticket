package com.naonao.grab12306ticket.version.single.database.table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 15:31
 **/
@ToString
@Getter
@Setter
public class NotifyInformationTable {

    private Integer id;
    private String receiverEmail;
    private String sendEmail;
    private String sendEmailHost;
    private String sendEmailPort;
    private String sendEmailUsername;
    private String sendEmailPassword;
    private String receiverPhone;
    private String notifyMode;
    private String hash;

}
