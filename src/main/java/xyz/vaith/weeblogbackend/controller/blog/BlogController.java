package xyz.vaith.weeblogbackend.controller.blog;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.vaith.weeblogbackend.result.Result;
import xyz.vaith.weeblogbackend.security.Security;
import xyz.vaith.weeblogbackend.service.ArticleService;
import xyz.vaith.weeblogbackend.service.BlogService;

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

    @RequestMapping("/category")
    public Result category() throws Exception {
        return Result.success(blogService.categoryList());
    }

    @RequestMapping("/recent")
    public Result recent() throws Exception {
        return Result.success(blogService.recentArticleList());
    }

    @RequestMapping("/archive")
    public Result archive() throws Exception {
        return Result.success(blogService.archiveList());
    }

    @RequestMapping("/tags")
    public Result tags() throws Exception {
        return Result.success(blogService.tagList());
    }

    @RequestMapping("/footnote")
    public Result footnote() throws Exception {
        return Result.success(blogService.footnote());
    }

    @RequestMapping("/search")
    public Result search(String keyword) throws Exception {
        return Result.success(blogService.search(keyword, 0, 20));
    }

}
