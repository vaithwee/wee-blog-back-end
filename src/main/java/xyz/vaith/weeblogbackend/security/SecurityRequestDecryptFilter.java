package xyz.vaith.weeblogbackend.security;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.util.DigestUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.vaith.weeblogbackend.exception.SignException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebFilter(urlPatterns = {"/*"}, filterName = "requestDecryptFilter")
@Log4j2
@Configuration
@Order(2)
public class SecurityRequestDecryptFilter extends OncePerRequestFilter implements ApplicationContextAware {

    @Resource
    SecurityHttpConfig securityHttpConfig;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        if (httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.toString())) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        if (httpServletRequest.getRequestURI().equals("/error")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }


        try {
            //1. info
            String info = httpServletRequest.getHeader("info");
            Map<String, Object> object = JSONObject.parseObject(info);
            String firstSign = object.remove("sign").toString();

            String js = JSONObject.toJSONString(object);
            log.info(js);
            js = SecurityUtil.encrypt(js, securityHttpConfig.getAccessKey());
            log.info("info aes:" + js);
            String headerSign = SecurityUtil.MD5(js);
            log.info("info sign:" + headerSign);

            //2.path
            String path = httpServletRequest.getRequestURI();
            path = SecurityUtil.encrypt(path, securityHttpConfig.getAccessKey());
            log.info("path aes:" + path);
            String pathSign = SecurityUtil.MD5(path);
            log.info("path sign:" + pathSign);

            log.info(httpServletRequest.getRequestURL());

            String s = DigestUtils.md5DigestAsHex((headerSign + pathSign).getBytes());
            if (s.equals(firstSign)) {
                log.info("首层签名效验成功");
            } else {
                throw new SignException("签名校验失败");
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            log.info("首层签名效验失败");
            httpServletRequest.setAttribute("error", new SignException("签名校验失败"));
            httpServletRequest.getRequestDispatcher("/error").forward(httpServletRequest, httpServletResponse);
        }


    }
}
