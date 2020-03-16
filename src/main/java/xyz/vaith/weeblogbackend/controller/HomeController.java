package xyz.vaith.weeblogbackend.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.vaith.weeblogbackend.authority.Authority;
import xyz.vaith.weeblogbackend.cache.Cache;
import xyz.vaith.weeblogbackend.model.Result;
import xyz.vaith.weeblogbackend.param.HomeInfoParam;
import xyz.vaith.weeblogbackend.security.Security;
import xyz.vaith.weeblogbackend.service.HomeInfoService;

import javax.annotation.Resource;

@Authority
@RestController
@RequestMapping("/home")
@Security
public class HomeController {

    @Resource
    HomeInfoService homeInfoService;

    @PostMapping("/addInfo")
    public Result addInfo(@RequestBody HomeInfoParam param) throws Exception {
        return Result.success(homeInfoService.addHomeInfo(param));
    }

    @RequestMapping("/info")
    public Result info() throws Exception {
        return Result.success(homeInfoService.getHomeInfo());
    }

    @PostMapping("/info/update")
    @CacheEvict(value = Cache.Key.HOME_INFO, key = "0")
    public Result updateHomeInfo(@RequestBody HomeInfoParam param) throws Exception {
        return Result.success(homeInfoService.updateHomeInfo(param));
    }
}
