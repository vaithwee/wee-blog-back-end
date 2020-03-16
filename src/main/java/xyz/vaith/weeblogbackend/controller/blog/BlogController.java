package xyz.vaith.weeblogbackend.controller.blog;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.vaith.weeblogbackend.cache.Cache;
import xyz.vaith.weeblogbackend.model.Result;
import xyz.vaith.weeblogbackend.security.Security;
import xyz.vaith.weeblogbackend.service.ArticleService;
import xyz.vaith.weeblogbackend.service.BlogService;
import xyz.vaith.weeblogbackend.service.HomeInfoService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/blog")
@Security
@Log4j2
public class BlogController {

    @Resource
    BlogService blogService;

    @Resource
    ArticleService articleService;



    @RequestMapping("/info")
    public Result home() throws Exception {
        return Result.success(blogService.homeInfo());
    }

    @RequestMapping("/article/{id}")
    public Result detail(@PathVariable("id") int id) throws Exception {
        return Result.success(articleService.getArticleByID(id));
    }

    @RequestMapping("/article")
    public Result list(Integer page, Integer size)throws  Exception {
        return  Result.success(articleService.getArticleList(page, size));
    }

}
