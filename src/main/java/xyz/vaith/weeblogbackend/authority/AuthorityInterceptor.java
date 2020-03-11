package xyz.vaith.weeblogbackend.authority;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.vaith.weeblogbackend.exception.BuzzException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

@Log4j2
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            Authority authority = handlerMethod.getBean().getClass().getAnnotation(Authority.class);
            if (authority == null) {
                Method method = handlerMethod.getMethod();
                authority = method.getAnnotation(Authority.class);
                if (authority == null) {
                    return true;
                }
            }
            String token = request.getHeader("token");
            String info = request.getHeader("info");
            Map<String, Object> object = JSONObject.parseObject(info);
            if (token == null) {
                response.setHeader("en" , object.get("en").toString());
                throw new BuzzException("请先登录");
            }
            if (!AuthorityJWTUtil.valid(token)) {
                response.setHeader("en" , object.get("en").toString());
                throw new BuzzException("请先登录");
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("post");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("complete");
    }
}
