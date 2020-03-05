package xyz.vaith.weeblogbackend.security;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import xyz.vaith.weeblogbackend.exception.SignException;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;


@Log4j2
@Order(1)
@ControllerAdvice(basePackages = "xyz.vaith.weeblogbackend.controller")
@EnableConfigurationProperties({SecurityHttpConfig.class})
public class SecurityRequestBodyAdvice implements RequestBodyAdvice {

    @Resource
    SecurityHttpConfig securityHttpConfig;


    private boolean supportSecretRequest(MethodParameter methodParameter) {

        log.info("key" + securityHttpConfig.getAccessKey());
        Security security = methodParameter.getMethodAnnotation(Security.class);
        if (security != null) {
            return security.request();
        } else {
            security = methodParameter.getContainingClass().getAnnotation(Security.class);
            if (security != null) {
                return security.request();
            }
        }
        return false;
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
                infoAES = SecurityUtil.encrypt(info, securityHttpConfig.getAccessKey());
            } catch (Exception e) {
                e.printStackTrace();
                throw new SignException("缺少头部信息");
            }
            String infoSign = SecurityUtil.MD5(infoAES);

            try {
                httpBody = decryptBody(httpInputMessage);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SignException("签名效验失败");
            }
            String bs = SecurityUtil.MD5(httpBody);

            String localSign = SecurityUtil.MD5(infoSign + bs);

            if (localSign.equals(sign)) {
                log.info("签名效验成功");
            } else {
                throw new SignException("签名效验失败");
            }
            return new SecurityHttpMessage(IOUtils.toInputStream(httpBody, "utf-8"), httpInputMessage.getHeaders());
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
        return SecurityUtil.decrypt(encryptBody, securityHttpConfig.getAccessKey());
    }

}
