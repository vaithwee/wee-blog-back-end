package xyz.vaith.weeblogbackend.controller;

import org.springframework.web.bind.annotation.*;
import xyz.vaith.weeblogbackend.authority.Authority;
import xyz.vaith.weeblogbackend.model.Category;
import xyz.vaith.weeblogbackend.param.CategoryParam;
import xyz.vaith.weeblogbackend.result.Result;
import xyz.vaith.weeblogbackend.security.Security;
import xyz.vaith.weeblogbackend.service.CategoryService;

import javax.annotation.Resource;

@Authority
@RestController
@RequestMapping("/category")
@Security
public class CategoryController {

    @Resource
    CategoryService service;
    @PostMapping("/add")
    public Result add(@RequestBody CategoryParam category) throws Exception {
        Category c = service.addCategory(category.getName());
        return Result.success(c);
    }

    @PostMapping("/remove")
    public Result remove(@RequestBody CategoryParam category) throws Exception {
        boolean res = service.removeCategoryByID(category.getId());
        if (res) {
            return Result.success("删除分类成功");
        } else  {
            return Result.success("删除分类失败");
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody CategoryParam categoryParam) throws Exception {
        return Result.success(service.updateCategory(categoryParam));
    }

    @RequestMapping("/list")
    public Result list(int page, int size) throws Exception {
        return  Result.success(service.getListByPageAndSize(page,size));
    }
}
