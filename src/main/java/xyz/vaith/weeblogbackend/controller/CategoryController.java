package xyz.vaith.weeblogbackend.controller;

import org.springframework.web.bind.annotation.*;
import xyz.vaith.weeblogbackend.model.Category;
import xyz.vaith.weeblogbackend.service.CatergoryService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/category")
//@CrossOrigin(origins = "*")
public class CategoryController {

    @Resource
    CatergoryService service;
    @PostMapping("/add")
    public String add(@RequestBody Category category) throws Exception {
        service.addCategory(category.getName());
        return "success";
    }
}
