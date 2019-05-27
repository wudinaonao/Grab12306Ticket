package com.naonao.grab12306ticket.version.single.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-09 23:43
 **/

@ToString
@Getter
@Setter
public class StatusInformationEntity {

    private Integer id;
    private String status;
    private String taskStartTime;
    private String taskEndTime;
    private String taskLastRunTime;
    private Long tryCount;
    private String message;
    private String hash;


}
