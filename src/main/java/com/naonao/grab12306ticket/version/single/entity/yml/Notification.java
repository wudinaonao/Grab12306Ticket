package com.naonao.grab12306ticket.version.single.entity.yml;

import com.naonao.grab12306ticket.version.single.entity.yml.notification.Config;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-24 22:52
 **/
@Getter
@Setter
@ToString
public class Notification {
    private Config config;
    private String mode;
    private String receiverPhone;
}
