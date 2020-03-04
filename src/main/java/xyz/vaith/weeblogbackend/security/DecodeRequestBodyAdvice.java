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
import xyz.vaith.weeblogbackend.exception.SignException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;


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

            log.info(httpInputMessage.getBody());
            HttpHeaders headers = httpInputMessage.getHeaders();
            String info;
            String sign;
            try {
                info = headers.get("info").get(0);
                sign = headers.get("sign").get(0);
            } catch (Exception e) {
                throw new SignException("缺少头部信息");
            }


            log.info("info: " + info + ", sign: " + sign);

            JSONObject object = JSONObject.parseObject(info);
            int en = Integer.parseInt(object.get("en").toString());
            if (en == 0) {
                return httpInputMessage;
            }


            String infoAES;
            try {
                infoAES = AESUtil.toEncryptString(info);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SignException("缺少头部信息");
            }
            String cs = DigestUtils.md5DigestAsHex(infoAES.getBytes());

            try {
                httpBody = decryptBody(httpInputMessage);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SignException("签名效验失败");
            }
            String bs = DigestUtils.md5DigestAsHex(httpBody.getBytes());

            String localSign = DigestUtils.md5DigestAsHex((cs + bs).getBytes());

            if (localSign.equals(sign)) {
                log.info("签名效验成功");
            } else {
                throw new SignException("签名效验失败");
            }
            return new SercurityHttpMessage(IOUtils.toInputStream(httpBody, "utf-8"), httpInputMessage.getHeaders());
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
        return AESUtil.toDecryptString(encryptBody);
    }

}
