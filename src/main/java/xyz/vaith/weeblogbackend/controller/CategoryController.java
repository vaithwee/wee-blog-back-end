package xyz.vaith.weeblogbackend.controller;

import org.springframework.web.bind.annotation.*;
import xyz.vaith.weeblogbackend.model.Category;
import xyz.vaith.weeblogbackend.model.Result;
import xyz.vaith.weeblogbackend.param.CommonPageParam;
import xyz.vaith.weeblogbackend.service.CatergoryService;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    CatergoryService service;
    @PostMapping("/add")
    public Result add(@RequestBody Category category) throws Exception {
        Category c = service.addCategory(category.getName());
        return Result.success(c);
    }

    @PostMapping("/remove")
    public Result remove(@RequestBody Category category) throws Exception {
        boolean res = service.removeCategoryByID(category.getId());
        if (res) {
            return Result.success("删除分类成功");
        } else  {
            return Result.success("删除分类失败");
        }
    }

    @RequestMapping("/list")
    public Result list(int page, int size) throws Exception {
        return  Result.success(service.getListByPageAndSize(page,size));
    }
}
