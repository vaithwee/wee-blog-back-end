package xyz.vaith.weeblogbackend.mapper;

import xyz.vaith.weeblogbackend.model.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> selectByPageAndSize(int page, int size);

    int selectExistCategory(String name);
}
