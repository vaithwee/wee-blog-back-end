package xyz.vaith.weeblogbackend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.vaith.weeblogbackend.exception.BuzzException;
import xyz.vaith.weeblogbackend.mapper.CategoryMapper;
import xyz.vaith.weeblogbackend.model.Category;
import xyz.vaith.weeblogbackend.service.CatergoryService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CatergoryService {
    @Resource
    CategoryMapper mapper;
    @Override
    public Category addCategory(String name) throws Exception {
        int result = mapper.selectExistCategory(name);
        if (result > 0) {
            throw new BuzzException("改分类已存在");
        }
        Category category = Category.builder().name(name).createDate(new Date()).updateDate(new Date()).build();
        mapper.insert(category);
        return category;
    }

    @Override
    public List<Category> getListByPageAndSize(int page, int size) throws Exception {
       return mapper.selectByPageAndSize(page * size, size);
    }

    @Override
    public boolean removeCategoryByID(int id) throws Exception {
        return mapper.deleteByPrimaryKey(id) > 0;
    }
}
