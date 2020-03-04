package xyz.vaith.weeblogbackend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.vaith.weeblogbackend.model.Result;
import xyz.vaith.weeblogbackend.param.TestParam;
import xyz.vaith.weeblogbackend.security.Security;

import java.util.HashMap;
import java.util.Map;

@Security
@RequestMapping("/test")
@RestController
public class TestController {
    @PostMapping("/sec")
    public Result sec(@RequestBody TestParam param) {
        return Result.success(param);
    }

    @RequestMapping("/noen")
    @Security(response = false)
    public Result sec() {
        Map<String, Object> map = new HashMap();
        map.put("reslut", "hello");
        return Result.success(map);
    }
}
