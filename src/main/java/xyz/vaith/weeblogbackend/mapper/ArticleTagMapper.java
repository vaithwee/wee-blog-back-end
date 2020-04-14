package xyz.vaith.weeblogbackend.mapper;

import xyz.vaith.weeblogbackend.model.ArticleTag;

import java.util.List;

public interface ArticleTagMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByArticle(Integer id);

    int insert(ArticleTag record);

    int insertSelective(ArticleTag record);

    ArticleTag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArticleTag record);

    int updateByPrimaryKey(ArticleTag record);


}