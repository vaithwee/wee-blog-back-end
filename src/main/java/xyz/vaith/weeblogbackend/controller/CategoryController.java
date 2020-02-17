package xyz.vaith.weeblogbackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.vaith.weeblogbackend.mapper.CategoryMapper;
import xyz.vaith.weeblogbackend.model.Category;

import javax.annotation.Resource;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    CategoryMapper mapper;
    @RequestMapping("/add")
    public String add(String name) {
        Category category = new Category();
        category.setName(name);
        mapper.insert(category);
        return "success";
    }
}
