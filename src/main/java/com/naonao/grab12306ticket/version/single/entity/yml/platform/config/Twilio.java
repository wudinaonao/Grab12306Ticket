package com.naonao.grab12306ticket.version.single.entity.yml.platform.config;

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
public class Twilio {
    private String accountSid;
    private String authToken;
    private String voiceUrl;
    private String defaultVoiceUrl;
    private String fromPhoneNumber;
}
