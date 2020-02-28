package xyz.vaith.weeblogbackend.mapper;

import xyz.vaith.weeblogbackend.model.Article;

import java.util.List;

public interface ArticleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    List<Article> selectArticleListBy(int start, int size);

    List<Article> selectLastArticleList();


}