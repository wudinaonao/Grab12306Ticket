package com.naonao.grab12306ticket.version.single.ticket.query.common;

import com.naonao.grab12306ticket.version.single.ticket.common.AbstractTicket;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-06 14:24
 **/
public abstract class AbstractQuery extends AbstractTicket {



    protected static final String UNKNOWN = "unknown";
    protected static final String SUCCESS = "success";
    protected static final String FAILED = "failed";

    protected static final String TRAIN_INFORMATION_LIST_IS_NULL = "train information list is null";
    protected static final String NOT_HAVE_ELIGIBLE_TRAIN = "not have eligible train";
    protected static final String FIND_ELIGIBLE_TRAIN = "find eligible train";

    protected CloseableHttpClient session;

}
