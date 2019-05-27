package com.naonao.grab12306ticket.version.single.ticket.base;

import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description: abstract captcha
 * @author: Wen lyuzhao
 * @create: 2019-04-30 00:55
 **/
public abstract class AbstractCaptcha extends AbstractTicket {


    protected static final String JSON = "json";
    protected static final String XML = "xml";
    protected static final String UNKNOWN = "unknown";
    protected static final String SUCCESS = "success";
    protected static final String FAILED = "failed";

    protected static final String GET_CAPTCHA_FAILED = "get captcha failed";
    protected static final String GET_CAPTCHA_SUCCESS = "get captcha success";
    protected static final String MARK_CAPTCHA_FAILED = "mark captcha failed";
    protected static final String MARK_CAPTCHA_SUCCESS = "mark captcha success";
    protected static final String CHECK_CAPTCHA_FAILED = "check captcha failed";
    protected static final String CHECK_CAPTCHA_SUCCESS = "check captcha success";

    protected static final Integer CHECK_CAPTCHA_SUCCESS_CODE = 4;

    protected static final String CAPTCHA_HANDLE_SUCCESS = "captcha handle success";

    protected static final String GET_CAPTCHA = "get captcha";
    protected static final String MARK_CAPTCHA = "mark captcha";
    protected static final String CHECK_CAPTCHA = "check captcha";

    /**
     *  session
     */
    protected CloseableHttpClient session;

}
