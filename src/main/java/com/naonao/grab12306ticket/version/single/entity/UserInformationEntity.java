package com.naonao.grab12306ticket.version.single.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 15:30
 **/
@ToString
@Getter
@Setter
public class UserInformationEntity {

    private Integer id;
    private String username12306;
    private String password12306;
    private String hash;

}
