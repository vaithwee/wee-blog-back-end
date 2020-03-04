package xyz.vaith.weeblogbackend.security;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.util.DigestUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.vaith.weeblogbackend.exception.SignException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebFilter(urlPatterns = {"/*"}, filterName = "requestDecryptFilter")
@Log4j2
@Configuration
public class RequestDecryptFilter extends OncePerRequestFilter implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        if(httpServletRequest.getMethod().equals(HttpMethod.OPTIONS.toString())) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        log.info("过滤器开始");
        log.info("path:" + httpServletRequest.getRequestURI());
        log.info("param:" + httpServletRequest.getParameterMap());
        log.info("header:" + httpServletRequest.getHeaderNames());
        log.info("header:" + httpServletRequest.getHeader("info"));



        //1. info
        String info = httpServletRequest.getHeader("info");
        Map<String, Object> object = JSONObject.parseObject(info);
        String firstSign = object.remove("sign").toString();

        String js = JSONObject.toJSONString(object);
        log.info(js);
        js = AESUtil.toEncryptString(js);
        log.info("info aes:" + js);
        String headerSign = DigestUtils.md5DigestAsHex(js.getBytes());
        log.info("info sign:" + headerSign);

        //2.path
        String path = httpServletRequest.getRequestURI();
        path = AESUtil.toEncryptString(path);
        log.info("path aes:" + path);
        String pathSign = DigestUtils.md5DigestAsHex(path.getBytes());
        log.info("path sign:" + pathSign);

        log.info(httpServletRequest.getRequestURL());

        String s = DigestUtils.md5DigestAsHex((headerSign + pathSign).getBytes());
        if (s.equals(firstSign)) {
            log.info("首层签名效验成功");
        } else  {
            throw  new SignException("签名校验失败");
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);;
    }

    public static String readAsChars(HttpServletRequest request)
    {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
