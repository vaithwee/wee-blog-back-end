package xyz.vaith.weeblogbackend.service;

import xyz.vaith.weeblogbackend.model.Category;

public interface CatergoryService {
    Category addCategory(String name) throws Exception;
}
