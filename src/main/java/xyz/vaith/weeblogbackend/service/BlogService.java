package xyz.vaith.weeblogbackend.service;

import xyz.vaith.weeblogbackend.model.Article;
import xyz.vaith.weeblogbackend.model.Category;
import xyz.vaith.weeblogbackend.model.Tag;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Map<String, Object> homeInfo() throws Exception;
    Article getArticleByID(int id) throws Exception;
    List<Category> categoryList() throws Exception;
    List<Article> recentArticleList() throws Exception;
    List<String> archiveList() throws Exception;
    List<Tag> tagList() throws Exception;
}
