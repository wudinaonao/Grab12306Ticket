package com.naonao.grab12306ticket.version.single.ticket.captcha;


import com.naonao.grab12306ticket.version.single.resultclass.ticket.captcha.CaptchaReturnResult;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.captcha.CheckCaptchaReturnResult;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.captcha.GetCaptchaReturnResult;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.captcha.MarkCaptchaReturnResult;
import com.naonao.grab12306ticket.version.single.ticket.captcha.common.AbstractCaptcha;
import lombok.extern.log4j.Log4j;

import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description: Captcha
 * @author: Wen lyuzhao
 * @create: 2019-04-29 19:02
 **/
@Log4j
public class Captcha extends AbstractCaptcha {


    

    public Captcha(CloseableHttpClient session){
        this.session = session;
    }

    public CaptchaReturnResult captcha(){

        // get captcha
        log.info(GET_CAPTCHA);
        GetCaptcha getCaptcha = new GetCaptcha(session);
        GetCaptchaReturnResult getCaptchaReturnResult = getCaptcha.getCaptcha();
        if (!getCaptchaReturnResult.getStatus()){
            log.error(GET_CAPTCHA_FAILED);
            return failedCaptchaReturnResult(session, GET_CAPTCHA_FAILED);
        }
        session = getCaptchaReturnResult.getSession();

        // mark captcha
        log.info(MARK_CAPTCHA);
        MarkCaptcha markCaptcha = new MarkCaptcha();
        MarkCaptchaReturnResult markCaptchaReturnResult = markCaptcha.markCaptcha(getCaptchaReturnResult.getResult());
        if (!markCaptchaReturnResult.getStatus()){
            log.error(MARK_CAPTCHA_FAILED);
            return failedCaptchaReturnResult(session, MARK_CAPTCHA_FAILED);
        }

        // check captcha
        log.info(CHECK_CAPTCHA);
        CheckCaptcha checkCaptcha = new CheckCaptcha(session);
        CheckCaptchaReturnResult checkCaptchaReturnResult = checkCaptcha.checkCaptcha(getCaptchaReturnResult.getParmasCallback(),
                                                                                      markCaptchaReturnResult.getResult(),
                                                                                      getCaptchaReturnResult.getTimeValue());
        if (!checkCaptchaReturnResult.getStatus()){
            log.error(CHECK_CAPTCHA_FAILED);
            return failedCaptchaReturnResult(session, CHECK_CAPTCHA_FAILED);
        }
        session = checkCaptchaReturnResult.getSession();

        // success
        CaptchaReturnResult captchaReturnResult = new CaptchaReturnResult();
        captchaReturnResult.setStatus(true);
        captchaReturnResult.setMessage(CAPTCHA_HANDLE_SUCCESS);
        captchaReturnResult.setSession(session);
        captchaReturnResult.setMarkResult(markCaptchaReturnResult.getResult());
        return captchaReturnResult;
    }

    private CaptchaReturnResult failedCaptchaReturnResult(CloseableHttpClient session, String message){
        CaptchaReturnResult captchaReturnResult = new CaptchaReturnResult();
        captchaReturnResult.setStatus(false);
        captchaReturnResult.setMessage(message);
        captchaReturnResult.setSession(session);
        return captchaReturnResult;
    }

}
