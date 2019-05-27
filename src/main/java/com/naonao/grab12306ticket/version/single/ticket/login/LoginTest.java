package com.naonao.grab12306ticket.version.single.ticket.login;

import com.naonao.grab12306ticket.version.single.resultclass.ticket.captcha.CaptchaReturnResult;


import com.naonao.grab12306ticket.version.single.resultclass.ticket.login.*;
import com.naonao.grab12306ticket.version.single.ticket.captcha.Captcha;
import com.naonao.grab12306ticket.version.single.ticket.base.AbstractLogin;
import lombok.extern.log4j.Log4j;
import org.apache.http.impl.client.CloseableHttpClient;


/**
 * @program: 12306grabticket_java
 * @description: test username and password can be logged in
 * @author: Wen lyuzhao
 * @create: 2019-04-30 22:50
 **/
@Log4j
public class LoginTest extends AbstractLogin {

    

    protected LoginTest(CloseableHttpClient session){
        this.session = session;
    }

    /**
     * test username and password can be logged in
     *
     * @param username      12306 username
     * @param password      12306 password
     * @return              LoginTestReturnResult
     */
    protected LoginTestReturnResult loginTest(String username, String password) {

        // initialization login for get cookies
        InitLoginReturnResult initLoginReturnResult = new InitLogin(session).initLogin();
        if (!initLoginReturnResult.getStatus()){
            log.error(initLoginReturnResult.getMessage());
            return failedLoginTestReturnResult(session, INIT_COOKIES_FAILED);
        }
        session = initLoginReturnResult.getSession();

        // handle captcha
        CaptchaReturnResult captchaReturnResult = new Captcha(session).captcha();
        if (!captchaReturnResult.getStatus()){
            return failedLoginTestReturnResult(session, captchaReturnResult.getMessage());
        }
        // get verified success session
        session = captchaReturnResult.getSession();

        // start login
        StartLogin startLogin = new StartLogin(session, username, password);
        StartLoginReturnResult startLoginReturnResult;
        startLoginReturnResult = startLogin.startLogin(captchaReturnResult.getMarkResult());
        if (!startLoginReturnResult.getStatus()){
            return failedLoginTestReturnResult(session, startLoginReturnResult.getMessage());
        }
        session = startLoginReturnResult.getSession();

        // get login token
        GetLoginToken getLoginToken = new GetLoginToken(startLoginReturnResult.getSession());
        GetLoginTokenReturnResult getLoginTokenReturnResult = getLoginToken.getLoginToken();
        if (!getLoginTokenReturnResult.getStatus()){
            return failedLoginTestReturnResult(session, getLoginTokenReturnResult.getMessage());
        }
        session = getLoginTokenReturnResult.getSession();

        // check login token
        CheckLoginToken checkLoginToken = new CheckLoginToken(getLoginTokenReturnResult.getSession());
        CheckLoginTokenReturnResult checkLoginTokenReturnResult = checkLoginToken.checkLoginToken(getLoginTokenReturnResult.getLoginToken());
        if (!checkLoginTokenReturnResult.getStatus()){
            return failedLoginTestReturnResult(session, checkLoginTokenReturnResult.getMessage());
        }
        session = checkLoginTokenReturnResult.getSession();

        // success login
        LoginTestReturnResult loginTestReturnResult = new LoginTestReturnResult();
        loginTestReturnResult.setStatus(true);
        loginTestReturnResult.setMessage(LOGIN_SUCCESS);
        loginTestReturnResult.setSession(session);
        return loginTestReturnResult;
    }

    private LoginTestReturnResult failedLoginTestReturnResult(CloseableHttpClient session, String message){
        LoginTestReturnResult loginTestReturnResult = new LoginTestReturnResult();
        loginTestReturnResult.setStatus(false);
        loginTestReturnResult.setMessage(message);
        loginTestReturnResult.setSession(session);
        return loginTestReturnResult;
    }

}
