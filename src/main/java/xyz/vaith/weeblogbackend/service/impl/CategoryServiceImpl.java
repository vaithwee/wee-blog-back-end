package xyz.vaith.weeblogbackend.service.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.vaith.weeblogbackend.exception.BuzzException;
import xyz.vaith.weeblogbackend.mapper.CategoryMapper;
import xyz.vaith.weeblogbackend.model.Category;
import xyz.vaith.weeblogbackend.model.Page;
import xyz.vaith.weeblogbackend.param.CategoryParam;
import xyz.vaith.weeblogbackend.redis.RedisCacheKeys;
import xyz.vaith.weeblogbackend.service.CategoryService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    @Resource
    CategoryMapper mapper;
    @Override
    @CacheEvict(value = RedisCacheKeys.CATEGORY_LIST, allEntries = true)
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
    @Cacheable(value = RedisCacheKeys.CATEGORY_LIST, key = "#page + '-' + #size")
    public Page<Category> getListByPageAndSize(int page, int size) throws Exception {
        List<Category> categories = mapper.selectByPageAndSize(page * size, size);
        int total = mapper.selectCount();
        int totalPage = total % size == 0 ? total / size : total / size + 1;
        return Page.<Category>builder().data(categories).size(size).currentPage(page).total(total).totalPage(totalPage).build();
    }

    @Override
    @CacheEvict(value = RedisCacheKeys.CATEGORY_LIST, allEntries = true)
    public boolean removeCategoryByID(int id) throws Exception {
        return mapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    @CacheEvict(value = RedisCacheKeys.CATEGORY_LIST, allEntries = true)
    public Category updateCategory(CategoryParam categoryParam) throws Exception {
        Category category = mapper.selectByPrimaryKey(categoryParam.getId());
        if (category != null) {
            category.setName(categoryParam.getName());
            category.setUpdateDate(new Date());
            mapper.updateByPrimaryKey(category);
            return category;
        }
        throw new BuzzException("分类不存在");
    }
}
