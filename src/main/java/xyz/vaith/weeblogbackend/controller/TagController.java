package xyz.vaith.weeblogbackend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.vaith.weeblogbackend.authority.Authority;
import xyz.vaith.weeblogbackend.result.Result;
import xyz.vaith.weeblogbackend.param.TagParam;
import xyz.vaith.weeblogbackend.security.Security;
import xyz.vaith.weeblogbackend.service.TagService;

import javax.annotation.Resource;

@Authority
@RestController
@RequestMapping("/tag")
@Security
public class TagController {

    @Resource
    TagService tagService;

    @PostMapping("/add")
    public Result add(@RequestBody TagParam param) throws Exception {
        return Result.success(tagService.addTag(param));
    }

    @PostMapping("/remove")
    public Result remove(@RequestBody TagParam param) throws Exception {
        return Result.success(tagService.removeTag(param));
    }

    @RequestMapping("/list")
    public Result list(int page, int size) throws Exception {
        return Result.success(tagService.getTagList(page,  size));
    }

    @RequestMapping("/update")
    public Result update(@RequestBody TagParam param) throws Exception {
        return Result.success(tagService.updateTTag(param));
    }
}
