package xyz.vaith.weeblogbackend.controller;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import xyz.vaith.weeblogbackend.exception.BuzzException;
import xyz.vaith.weeblogbackend.exception.SignException;
import xyz.vaith.weeblogbackend.model.Result;
import xyz.vaith.weeblogbackend.security.Security;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@Log4j2
@Security
@ControllerAdvice
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @Resource
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = "/error", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Result error(final WebRequest webRequest, HttpServletRequest request) {
        Throwable error = this.errorAttributes.getError(webRequest);
        if (error == null) {
            error = (Throwable) request.getAttribute("error");
        }
        return Result.fail(error.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error(e.getMessage());
        if (e instanceof BuzzException) {
            return Result.fail(e.getMessage());
        } else if (e.getCause() instanceof SignException) {
            return Result.fail(e.getMessage());
        } else {
            return Result.defaultFail();
        }
    }
}
