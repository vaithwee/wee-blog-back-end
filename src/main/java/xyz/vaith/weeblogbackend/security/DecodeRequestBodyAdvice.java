package xyz.vaith.weeblogbackend.security;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.DigestUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import xyz.vaith.weeblogbackend.exception.BuzzException;
import xyz.vaith.weeblogbackend.exception.SignException;
import xyz.vaith.weeblogbackend.util.AESUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Map;


@Log4j2
@Order(1)
@ControllerAdvice(basePackages = "xyz.vaith.weeblogbackend.controller")
public class DecodeRequestBodyAdvice implements RequestBodyAdvice {


    private boolean supportSecretRequest(MethodParameter methodParameter) {
        if (methodParameter.getContainingClass().getAnnotation(SecurityBody.class) != null) {
            return true;
        } else {
            return methodParameter.getMethodAnnotation(SecurityBody.class) != null;
        }
    }


    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        boolean supportSafeMessage = supportSecretRequest(methodParameter);
        String httpBody;
        if (supportSafeMessage) {
            log.info("method: [" + methodParameter.getMethod().getName() + "] decode");
            HttpHeaders headers = httpInputMessage.getHeaders();
            String info = headers.get("info").get(0);
            String sign = headers.get("sign").get(0);
            String infoAES;
            try {
                infoAES = AESUtil.encrypt(info);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SignException("缺少头部信息");
            }
            String cs = DigestUtils.md5DigestAsHex(infoAES.getBytes());
            if (cs.equals(sign)) {
                log.info("签名效验成功");
            } else {
                throw new SignException("签名效验失败");
            }

            JSONObject object = JSONObject.parseObject(info);
            int en = Integer.parseInt(object.get("en").toString());
            if (en == 0) {
                return httpInputMessage;
            }

            try {
                httpBody = decryptBody(httpInputMessage);
                log.info("result: " + httpBody);
                return new SercurityHttpMessage(IOUtils.toInputStream(httpBody, "utf-8"), httpInputMessage.getHeaders());
            } catch (Exception e) {
                e.printStackTrace();
                return httpInputMessage;
            }
        } else {
            return httpInputMessage;
        }
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }


    private String decryptBody(HttpInputMessage inputMessage) throws Exception {
        InputStream encryptStream = inputMessage.getBody();
        String encryptBody = StreamUtils.copyToString(encryptStream, Charset.defaultCharset());
        return AESUtil.decrypt(encryptBody);
    }

}
