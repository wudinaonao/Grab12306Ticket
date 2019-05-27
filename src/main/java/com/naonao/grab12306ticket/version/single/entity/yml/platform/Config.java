package com.naonao.grab12306ticket.version.single.entity.yml.platform;

import com.naonao.grab12306ticket.version.single.entity.yml.platform.config.Nexmo;
import com.naonao.grab12306ticket.version.single.entity.yml.platform.config.Twilio;
import com.naonao.grab12306ticket.version.single.entity.yml.platform.config.Yunzhixin;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-24 22:51
 **/
@Getter
@Setter
@ToString
public class Config {
    private Nexmo nexmo;
    private Twilio twilio;
    private Yunzhixin yunzhixin;

}
