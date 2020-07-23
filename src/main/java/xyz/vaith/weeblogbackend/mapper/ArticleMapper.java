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

    int selectCount();

    List<String> selectArchiveList();

    List<Article> selectArticleListWhereNameLike(String keyword, int start, int end);

    List<Article> selectArticleListWhereArchive(String archive, int start, int end);

    List<Article> selectArticleListWhereCategory(String category, int start, int end);

    List<Article> selectArticleListWhereTag(String category, int start, int end);

}