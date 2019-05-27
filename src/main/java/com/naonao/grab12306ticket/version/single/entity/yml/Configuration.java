package com.naonao.grab12306ticket.version.single.entity.yml;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-24 22:12
 **/
@Getter
@Setter
@ToString
public class Configuration {

    private Platform platform;
    private Notification notification;
    private Setting setting;
    private Ticket ticket;
    private User user;

}
