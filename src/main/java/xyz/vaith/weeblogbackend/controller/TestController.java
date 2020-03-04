package xyz.vaith.weeblogbackend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.vaith.weeblogbackend.model.Result;
import xyz.vaith.weeblogbackend.param.TestParam;
import xyz.vaith.weeblogbackend.security.SecurityBody;

@SecurityBody
@RequestMapping("/test")
@RestController
public class TestController {
    @PostMapping("/sec")
    public Result sec(@RequestBody TestParam param) {
        return Result.success(param);
    }
}