package com.naonao.grab12306ticket.version.single.entity.yml.notification.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-24 22:53
 **/
@Getter
@Setter
@ToString
public class Email {
    private String senderEmail;
    private String senderEmailHost;
    private String senderEmailPort;
    private String senderEmailUsername;
    private String senderEmailPassword;
    private String receiverEmail;
    private String defaultTitle;
    private String defaultContent;
}
