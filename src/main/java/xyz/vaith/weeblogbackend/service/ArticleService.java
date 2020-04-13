package xyz.vaith.weeblogbackend.service;

import xyz.vaith.weeblogbackend.model.Article;
import xyz.vaith.weeblogbackend.model.Page;
import xyz.vaith.weeblogbackend.param.ArticleParam;

import java.util.List;

public interface ArticleService {
    Article addArticle(ArticleParam param) throws Exception;
    Article getArticleByID(Integer id) throws Exception;
    Page<Article> getArticleList(Integer page, Integer size) throws Exception;
}
