package xyz.vaith.weeblogbackend.service.impl;

import org.springframework.stereotype.Service;
import xyz.vaith.weeblogbackend.mapper.CategoryMapper;
import xyz.vaith.weeblogbackend.model.Category;
import xyz.vaith.weeblogbackend.service.CatergoryService;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class CategoryServiceImpl implements CatergoryService {
    @Resource
    CategoryMapper mapper;
    @Override
    public Category addCategory(String name) throws Exception {
        Category category = Category.builder().name(name).createDate(new Date()).updateDate(new Date()).build();
        mapper.insert(category);
        return category;
    }
}
