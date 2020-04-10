package xyz.vaith.weeblogbackend.mapper;

import xyz.vaith.weeblogbackend.model.ArticleCover;

public interface ArticleCoverMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByImageId(Integer id);

    int insert(ArticleCover record);

    int insertSelective(ArticleCover record);

    ArticleCover selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArticleCover record);

    int updateByPrimaryKey(ArticleCover record);
}