package com.naonao.grab12306ticket.version.single.ticket.login.common;

import com.naonao.grab12306ticket.version.single.ticket.common.AbstractTicket;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description: abstract login
 * @author: Wen lyuzhao
 * @create: 2019-04-30 22:52
 **/
public abstract class AbstractLogin extends AbstractTicket {


    protected static final String UNKNOWN = "unknown";
    protected static final String SUCCESS = "success";
    protected static final String FAILED = "failed";

    /**
     * login
     */
    protected static final String INITIALIZATION_LOGIN = "initializaiton login";
    protected static final String HANLDE_CAPTCHA = "handle captcha";
    protected static final String START_LOGIN = "start login";
    protected static final String GET_LOGIN_TOKEN = "get login token";
    protected static final String CHECK_LOGIN_TOKEN = "check login token";
    protected static final String LOGIN_FAILED = "login failed";
    protected static final String LOGIN_SUCCESS = "login success";
    protected static final String VERIFIED_FAILED = "verified failed";
    protected static final String CAPTCHA_HANDLE_FAILED = "captcha handle failed";


    /**
     * check login token
     */
    protected static final String CHECK_LOGIN_TOKEN_SUCCESS = "check login token success";
    protected static final String CHECK_LOGIN_TOKEN_FAILED = "check login token failed";

    /**
     * get login token
     */
    protected static final String GET_LOGIN_TOKEN_SUCCESS = "get login token success";
    protected static final String GET_LOGIN_TOKEN_FAILED = "get login token failed";

    /**
     * start login
     */
    protected static final String START_LOGIN_SUCCESS = "start login success";
    protected static final String START_LOGIN_FAILED = "start login failed";
    protected static final String PASSWORD_ERROR = "password error";

    /**
     * initialization login
     */
    protected static final String LOGIN_DEVICE_FAILED = "try get cookies from logDevice url failed";
    protected static final String LOGIN_CONF_FAILED = "try get cookies from loginConf url failed";
    protected static final String GET_LOGIN_BANNER_FAILED = "try get cookies from getLoginBanner url failed";
    protected static final String UMATK_STATIC_FAILED = "try get cookies from umatkStatic url failed";
    protected static final String INIT_COOKIES_SUCCESS = "initialization cookies success";
    protected static final String INIT_COOKIES_FAILED = "initialization cookies failed";


    protected String username;
    protected String password;


    /**
     *  session
     */
    protected CloseableHttpClient session;

}
