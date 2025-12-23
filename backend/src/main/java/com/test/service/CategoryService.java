package com.test.service;

import com.test.entity.Category;
import com.test.exception.BusinessException;
import com.test.mapper.CategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> listAll() {
        return categoryMapper.selectAll();
    }

    public Category getById(Long id) {
        return categoryMapper.selectById(id);
    }

    public Category create(Category category) {
        categoryMapper.insert(category);
        log.info("创建分类: {}", category.getName());
        return category;
    }

    public Category update(Category category) {
        categoryMapper.update(category);
        log.info("更新分类: {}", category.getId());
        return category;
    }

    public void delete(Long id) {
        if (categoryMapper.countProducts(id) > 0) {
            throw new BusinessException("该分类下存在商品，无法删除");
        }
        categoryMapper.deleteById(id);
        log.info("删除分类: {}", id);
    }
}
