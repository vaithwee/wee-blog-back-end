package xyz.vaith.weeblogbackend.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.vaith.weeblogbackend.model.Result;

@ControllerAdvice
public class ExceptionHandlerManager {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        if (e instanceof BuzzException) {
            return Result.fail(e.getMessage());
        } else {
            return Result.defaultFail();
        }
    }
}
