package xyz.vaith.weeblogbackend.service;

import xyz.vaith.weeblogbackend.model.Article;
import xyz.vaith.weeblogbackend.param.ArticleParam;

public interface ArticleService {
    Article addArticle(ArticleParam param) throws Exception;
    Article getArticleByID(Integer id) throws Exception;
}
