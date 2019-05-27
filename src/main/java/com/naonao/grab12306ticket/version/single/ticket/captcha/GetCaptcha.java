package com.naonao.grab12306ticket.version.single.ticket.captcha;

import com.naonao.grab12306ticket.version.single.resultclass.ticket.captcha.GetCaptchaReturnResult;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import com.naonao.grab12306ticket.version.single.ticket.base.AbstractCaptcha;

import lombok.extern.log4j.Log4j;
import org.apache.http.Header;

import com.alibaba.fastjson.JSONObject;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @program: 12306grabticket_java
 * @description: get captcha
 * @author: Wen lyuzhao
 * @create: 2019-04-29 19:02
 **/

@Log4j
public class GetCaptcha extends AbstractCaptcha {

    

    protected GetCaptcha(CloseableHttpClient session){
        this.session = session;
    }

    /**
     *  get captcha
     *
     * @return              GetCaptchaReturnResult
     *                      status, message, result, timeValue, paramsCallback
     */
    protected GetCaptchaReturnResult getCaptcha() {

        // create request params
        String timeValue = String.valueOf(System.currentTimeMillis());
        String paramsCallback = getCheckCode() + "_" + timeValue;
        // build requests pramas
        URI uri = doUri(timeValue, paramsCallback);
        if (uri == null){
            log.error("uri is null");
            return failedGetCaptchaReturnResult(session, GET_CAPTCHA_FAILED);
        }
        // create get request, response, result
        HttpGet httpGet = HttpTools.setRequestHeader(new HttpGet(uri), true, false, false);
        CloseableHttpResponse response = null;
        GetCaptchaReturnResult getCaptchaReturnResult = new GetCaptchaReturnResult();
        try{
            response = session.execute(httpGet);
            // is request success
            if (response.getStatusLine().getStatusCode() == HTTP_SUCCESS){
                String responseText = HttpTools.responseToString(response);
                // get result format
                String resultFormat = captchaResultFormat(response.getAllHeaders());
                if (JSON.equals(resultFormat)) {
                    String captchaBase64Str = getCaptchaBase64FromJson(responseText);
                    if (!"".equals(captchaBase64Str)){
                        getCaptchaReturnResult.setStatus(true);
                        getCaptchaReturnResult.setMessage(GET_CAPTCHA_SUCCESS);
                        getCaptchaReturnResult.setSession(session);
                        getCaptchaReturnResult.setResult(captchaBase64Str);
                        getCaptchaReturnResult.setTimeValue(timeValue);
                        getCaptchaReturnResult.setParmasCallback(paramsCallback);
                        return getCaptchaReturnResult;
                    }
                }
                if (XML.equals(resultFormat)){
                    String captchaBase64Str = getCaptchaBase64FromXml(responseText);
                    if (!"".equals(captchaBase64Str)){
                        getCaptchaReturnResult.setStatus(true);
                        getCaptchaReturnResult.setMessage(GET_CAPTCHA_SUCCESS);
                        getCaptchaReturnResult.setSession(session);
                        getCaptchaReturnResult.setResult(captchaBase64Str);
                        getCaptchaReturnResult.setTimeValue(timeValue);
                        getCaptchaReturnResult.setParmasCallback(paramsCallback);
                        return getCaptchaReturnResult;
                    }
                }
            }
        }catch (IOException e){
            log.error(e.getMessage());
        }
        finally {
            if (response != null){
                try{
                    response.close();
                }catch (IOException e){
                    log.error(e.getMessage());
                }

            }
        }
        return failedGetCaptchaReturnResult(session, GET_CAPTCHA_FAILED);
    }

    private URI doUri(String timeValue, String paramsCallback){
        URI uri;
        try{
            uri = new URIBuilder("https://kyfw.12306.cn/passport/captcha/captcha-image64")
                    .setParameter("login_site", "E")
                    .setParameter("module", "login")
                    .setParameter("rand", "sjrand")
                    .setParameter(String.valueOf(Long.valueOf(timeValue) + 100000L), "")
                    .setParameter("callback", paramsCallback)
                    .setParameter("_ ", timeValue)
                    .build();
            return uri;
        }catch (URISyntaxException e){
            log.error(e.getMessage());
            return null;
        }
    }

    private GetCaptchaReturnResult failedGetCaptchaReturnResult(CloseableHttpClient session, String message){
        GetCaptchaReturnResult getCaptchaReturnResult = new GetCaptchaReturnResult();
        getCaptchaReturnResult.setStatus(false);
        getCaptchaReturnResult.setMessage(message);
        getCaptchaReturnResult.setSession(session);
        getCaptchaReturnResult.setResult("");
        getCaptchaReturnResult.setTimeValue("");
        getCaptchaReturnResult.setParmasCallback("");
        return getCaptchaReturnResult;
    }

    private String captchaResultFormat(Header[] headers){
        // get result format by response headers
        for (Header header: headers){
            if ("Content-Type".equals(header.getName())){
                if ("application/json;charset=UTF-8".equals(header.getValue())){
                    return JSON;
                }
                if ("application/xhtml+xml;charset=UTF-8".equals(header.getValue())){
                    return XML;
                }
            }
        }
        return UNKNOWN;
    }

    /**
     * create a string use to check captcha
     *
     * @return string of check captcha
     */
    private String getCheckCode(){
        Random random = new Random();
        String checkCode = "jQuery1910";
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<16; i++){
            stringBuilder.append(random.nextInt(9));
        }
        checkCode += stringBuilder;
        return checkCode;
    }

    /**
     *  resolve captcha if return format is json
     *
     * @param responseText      response text
     * @return                  captcha base64
     */
    private String getCaptchaBase64FromJson(String responseText){
        // preprocessing result string
        String jsonStr = responseText.substring(responseText.indexOf("(")+1, responseText.length()-2);
        // create json object by return text
        JSONObject jsonData = JSONObject.parseObject(jsonStr);
        // resolve result, captcha base64 string, result message, result code
        String captchaBase64Str = jsonData.getString("image");
        String resultMsg  = jsonData.getString("result_message");
        String resultCode = jsonData.getString("result_code");
        // success result
        String trueCode = "0";
        String trueMsg  = "生成验证码成功";
        // generator catpcha success
        if (resultCode.equals(trueCode) && resultMsg.equals(trueMsg)){
            return captchaBase64Str;
        }
        return "";
    }
    /**
     *  resolve captcha if return format is xml
     *
     * @param responseText      response text
     * @return                  captcha base64
     */
    private String getCaptchaBase64FromXml(String responseText){
        try {
            // File inputFile = new File(testStr);
            InputStream inputStream = new ByteArrayInputStream(responseText.getBytes(StandardCharsets.UTF_8));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();
            XPath xPath =  XPathFactory.newInstance().newXPath();
            String expression = "/HashMap/image";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
            // for (int i = 0; i < nodeList.getLength(); ++i) {
            //     Node nNode = nodeList.item(i);
            //     return nNode.getTextContent();
            // }
            Node nNode = nodeList.item(0);
            return nNode.getTextContent();
        } catch (ParserConfigurationException e) {
            log.info("return result format is xml, but resolve failed");
        } catch (SAXException e) {
            log.info("return result format is xml, but resolve failed");
        } catch (XPathExpressionException e) {
            log.info("return result format is xml, but xpath error");
        }catch (IOException e){
            log.info("return result format is xml, but resolve failed");
        }
        return "";
    }

}
