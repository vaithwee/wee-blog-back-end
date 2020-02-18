package xyz.vaith.weeblogbackend.service;

import xyz.vaith.weeblogbackend.model.Category;

import java.util.List;

public interface CatergoryService {
    Category addCategory(String name) throws Exception;
    List<Category> getListByPageAndSize(int page, int size) throws Exception;
    boolean removeCategoryByID(int id) throws Exception;
}
