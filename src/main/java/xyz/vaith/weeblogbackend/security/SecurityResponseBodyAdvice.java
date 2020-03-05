package xyz.vaith.weeblogbackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;

@ControllerAdvice(basePackages = "xyz.vaith.weeblogbackend.controller")
public class SecurityResponseBodyAdvice implements ResponseBodyAdvice {

    @Resource
    SecurityHttpConfig securityHttpConfig;

    private boolean supportSecretResponse(MethodParameter methodParameter) {
        Security security = methodParameter.getMethodAnnotation(Security.class);
        if (security != null) {
            return security.response();
        } else {
            security = methodParameter.getContainingClass().getAnnotation(Security.class);
            if (security != null) {
                return security.response();
            }
        }
        return false;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (supportSecretResponse(methodParameter)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
                return SecurityUtil.encrypt(result, securityHttpConfig.getSecurityKey());
            } catch (Exception e) {
                e.printStackTrace();
                return body;
            }
        } else  {
            serverHttpResponse.getHeaders().set("en", "0");
            return body;
        }
    }
}
