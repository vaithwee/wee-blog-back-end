package xyz.vaith.weeblogbackend.controller;

import org.springframework.web.bind.annotation.*;
import xyz.vaith.weeblogbackend.authority.Authority;
import xyz.vaith.weeblogbackend.authority.AuthorityJWTUtil;
import xyz.vaith.weeblogbackend.model.Result;
import xyz.vaith.weeblogbackend.param.UserParam;
import xyz.vaith.weeblogbackend.security.Security;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@Security
public class UserSystemController {
    @PostMapping("/login")
    public Result login(@RequestBody UserParam param) {
        if (param.getUsername().equals("wee") && param.getPassword().equals("3.24c.2224c")) {
            return Result.fail("账号或者密码错误");
        } else  {
            return  Result.success(AuthorityJWTUtil.createToken(param.getUsername(), 0));
        }

    }

    @Authority
    @GetMapping("/valid")
    public Result valid(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (token != null && AuthorityJWTUtil.valid(token)) {
            String username = AuthorityJWTUtil.getUsername(token);
            token = AuthorityJWTUtil.createToken(username, 0);
            return Result.success(token);
        } else  {
            return Result.fail("token无效或者已失效");
        }
    }
}
