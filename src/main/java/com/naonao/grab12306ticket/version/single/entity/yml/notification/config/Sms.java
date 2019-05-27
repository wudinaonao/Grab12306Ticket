package com.naonao.grab12306ticket.version.single.entity.yml.notification.config;

import com.naonao.grab12306ticket.version.single.constants.NotificationInterfaceName;
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
public class Sms {
    private NotificationInterfaceName interfaceName;
    private String content;
    private String title;
    private String defaultContent;

}
