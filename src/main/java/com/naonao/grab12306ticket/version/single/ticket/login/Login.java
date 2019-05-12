package com.naonao.grab12306ticket.version.single.ticket.login;

import com.naonao.grab12306ticket.version.single.resultclass.ticket.captcha.CaptchaReturnResult;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import com.naonao.grab12306ticket.version.single.ticket.captcha.Captcha;
import com.naonao.grab12306ticket.version.single.ticket.login.common.AbstractLogin;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.login.*;
import lombok.extern.log4j.Log4j;
import org.apache.http.impl.client.CloseableHttpClient;


/**
 * @program: 12306grabticket_java
 * @description: Login method
 * @author: Wen lyuzhao
 * @create: 2019-04-29 17:20
 *
 * Login model
 *
 *      method flow:
 *      1. initialization login cookies
 *      2. hanlde captcha
 *      3. start login
 *      4. get login token
 *      5. check login token
 *      6. login success
 *
 *
 */
@Log4j
public class Login extends AbstractLogin {

    


    public Login(CloseableHttpClient session){
        this.session = session;
    }


    /**
     * Login method
     *
     * @param username      12306 username
     * @param password      12306 password
     * @return              LoginReturnResult
     */
    public LoginReturnResult login(String username, String password) {

        //initialization login cookies
        log.info(INITIALIZATION_LOGIN);
        InitLoginReturnResult initLoginReturnResult = new InitLogin(session).initLogin();
        if (!initLoginReturnResult.getStatus()){
            log.error(initLoginReturnResult.getMessage());
            return failedLoginReturnResult(session, INIT_COOKIES_FAILED);
        }
        session = initLoginReturnResult.getSession();

        // handle captcha
        log.info(HANLDE_CAPTCHA);
        CaptchaReturnResult captchaReturnResult = new Captcha(session).captcha();
        // get success session
        if (!captchaReturnResult.getStatus()){
            log.error(captchaReturnResult.getMessage());
            return failedLoginReturnResult(captchaReturnResult.getSession(), CAPTCHA_HANDLE_FAILED);
        }
        session = captchaReturnResult.getSession();

        // start login
        log.info(START_LOGIN);
        StartLoginReturnResult startLoginReturnResult;
        startLoginReturnResult = new StartLogin(session, username, password).startLogin(captchaReturnResult.getMarkResult());
        if (!startLoginReturnResult.getStatus()){
            log.error(startLoginReturnResult.getMessage());
            return failedLoginReturnResult(startLoginReturnResult.getSession(), START_LOGIN_FAILED);
        }
        session = startLoginReturnResult.getSession();

        // get login token
        log.info(GET_LOGIN_TOKEN);
        GetLoginTokenReturnResult getLoginTokenReturnResult;
        getLoginTokenReturnResult = new GetLoginToken(session).getLoginToken();
        if (!getLoginTokenReturnResult.getStatus()){
            log.error(getLoginTokenReturnResult.getMessage());
            return failedLoginReturnResult(getLoginTokenReturnResult.getSession(), GET_LOGIN_TOKEN_FAILED);
        }
        session = getLoginTokenReturnResult.getSession();

        // check login token
        log.info(CHECK_LOGIN_TOKEN);
        CheckLoginTokenReturnResult checkLoginTokenReturnResult;
        checkLoginTokenReturnResult = new CheckLoginToken(session).checkLoginToken(getLoginTokenReturnResult.getLoginToken());
        if (!checkLoginTokenReturnResult.getStatus()){
            log.error(checkLoginTokenReturnResult.getMessage());
            return failedLoginReturnResult(checkLoginTokenReturnResult.getSession(), CHECK_LOGIN_TOKEN_FAILED);
        }
        session = checkLoginTokenReturnResult.getSession();

        // print login success username
        String loginUserName = "login success!   username: " + checkLoginTokenReturnResult.getLoginUserName();
        log.info(loginUserName);

        // login success
        LoginReturnResult loginReturnResult = new LoginReturnResult();
        loginReturnResult.setStatus(true);
        loginReturnResult.setMessage(LOGIN_SUCCESS);
        loginReturnResult.setSession(session);
        return loginReturnResult;
    }


    /**
     *  login test
     *  test username and password can be logged in
     *
     * @param username      12306 username
     * @param password      12306 password
     * @return              boolean
     */
    public LoginTestReturnResult loginTest(String username, String password){
        LoginTest loginTest = new LoginTest(HttpTools.getSession(30000));
        return loginTest.loginTest(username, password);
    }

    private LoginReturnResult failedLoginReturnResult(CloseableHttpClient session, String message){
        LoginReturnResult loginReturnResult = new LoginReturnResult();
        loginReturnResult.setStatus(false);
        loginReturnResult.setMessage(message);
        loginReturnResult.setSession(session);
        return loginReturnResult;
    }

}
