package xyz.vaith.weeblogbackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.vaith.weeblogbackend.model.Result;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/security")
@RestController
public class SecurityController {

    @RequestMapping("/error")
    public Result error(HttpServletRequest request) {
        Exception error = (Exception) request.getAttribute("error");
        return Result.fail(error.getMessage());
    }
}
