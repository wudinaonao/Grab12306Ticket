package com.naonao.grab12306ticket.version.single.entity.yml.notification;

import com.naonao.grab12306ticket.version.single.entity.yml.notification.config.Email;
import com.naonao.grab12306ticket.version.single.entity.yml.notification.config.Phone;
import com.naonao.grab12306ticket.version.single.entity.yml.notification.config.Sms;
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
public class Config {
    private Phone phone;
    private Sms sms;
    private Email email;
}
