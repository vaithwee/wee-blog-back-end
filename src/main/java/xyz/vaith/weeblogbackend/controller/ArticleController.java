package xyz.vaith.weeblogbackend.controller;

import org.springframework.web.bind.annotation.*;
import xyz.vaith.weeblogbackend.authority.Authority;
import xyz.vaith.weeblogbackend.result.Result;
import xyz.vaith.weeblogbackend.param.ArticleParam;
import xyz.vaith.weeblogbackend.security.Security;
import xyz.vaith.weeblogbackend.service.ArticleService;

import javax.annotation.Resource;

@Authority
@RestController
@RequestMapping("/article")
@Security
public class ArticleController {

    @Resource
    ArticleService service;


    @PostMapping("/add")
    public Result add(@RequestBody ArticleParam param) throws Exception {
        return  Result.success(service.addArticle(param));
    }

    @RequestMapping("/detail/{id}")
    public Result detail(@PathVariable("id") int id) throws Exception {
        return Result.success(service.getArticleByID(id));
    }


    @RequestMapping("/list")
    public Result list(Integer page, Integer size)throws  Exception {
        return  Result.success(service.getArticleList(page, size));
    }

    @PostMapping("/update")
    public Result update(@RequestBody ArticleParam param) throws Exception {
        return Result.success(service.updateArticle(param));
    }

    @PostMapping("/remove")
    public Result remove(@RequestBody ArticleParam param) throws Exception {
        return Result.success(service.removeArticle(param.getId()));
    }
}
