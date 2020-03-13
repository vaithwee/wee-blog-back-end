package xyz.vaith.weeblogbackend.service;

import xyz.vaith.weeblogbackend.model.Article;

import java.util.Map;

public interface BlogService {
    Map<String, Object> homeInfo() throws Exception;
    Article getArticleByID(int id) throws Exception;
}
