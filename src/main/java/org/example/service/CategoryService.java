package org.example.service;

import org.example.pojo.Category;

import java.util.List;

public interface CategoryService {
    //新增分类
    void add(Category category);
    //列表查询
    List<Category> list();
    //id查询文章分类对象

    Category findById(Integer id);
    //更新分类对象

    void update(Category category);

    //删除分类
    void delete(Integer id);
}
