package xyz.vaith.weeblogbackend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.vaith.weeblogbackend.model.Result;
import xyz.vaith.weeblogbackend.param.ArticleParam;
import xyz.vaith.weeblogbackend.security.SecurityBody;
import xyz.vaith.weeblogbackend.service.ArticleService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/article")
@SecurityBody
public class ArticleController {

    @Resource
    ArticleService service;

    @PostMapping("/add")
    public Result add(@RequestBody ArticleParam param) throws Exception {
        return  Result.success(service.addArticle(param));
    }

    @RequestMapping("/detail")
    public Result add(int id) throws Exception {
        return Result.success(service.getArticleByID(id));
    }


    @RequestMapping("/list")
    public Result list(Integer page, Integer size)throws  Exception {
        return  Result.success(service.getArticleList(page, size));
    }
}
