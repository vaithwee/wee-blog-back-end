package xyz.vaith.weeblogbackend.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.vaith.weeblogbackend.model.Result;

@ControllerAdvice
@Log4j2
public class ExceptionHandlerManager {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error(e.getStackTrace());
        if (e instanceof BuzzException) {
            return Result.fail(e.getMessage());
        } else {
            return Result.defaultFail();
        }
    }
}
