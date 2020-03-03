package xyz.vaith.weeblogbackend.security;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import xyz.vaith.weeblogbackend.util.AESUtil;

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
            log.info("method: ["+ methodParameter.getMethod().getName() +"] decode");
            try {

                httpBody = decryptBody(httpInputMessage);

//                IOUtils.toInputStream(httpBody, "UTF-8");
                log.info("result: " + httpBody);
              return new SercurityHttpMessage(IOUtils.toInputStream(httpBody, "utf-8"), httpInputMessage.getHeaders());
            } catch (Exception e) {
                e.printStackTrace();
                return  httpInputMessage;
            }
        } else  {
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
