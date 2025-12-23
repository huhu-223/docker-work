package com.test.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.common.PageResult;
import com.test.entity.Product;
import com.test.mapper.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    public PageResult<Product> listByPage(Integer page, Integer size, Long categoryId, String keyword) {
        PageHelper.startPage(page, size);
        List<Product> list = productMapper.selectByCondition(categoryId, keyword);
        PageInfo<Product> pageInfo = new PageInfo<>(list);
        return PageResult.of(pageInfo.getList(), pageInfo.getTotal(), page, size);
    }

    public PageResult<Product> listAllByPage(Integer page, Integer size, Long categoryId, String keyword) {
        PageHelper.startPage(page, size);
        List<Product> list = productMapper.selectAllByCondition(categoryId, keyword);
        PageInfo<Product> pageInfo = new PageInfo<>(list);
        return PageResult.of(pageInfo.getList(), pageInfo.getTotal(), page, size);
    }

    public Product getById(Long id) {
        return productMapper.selectById(id);
    }

    public Product create(Product product) {
        if (product.getStatus() == null) product.setStatus(1);
        productMapper.insert(product);
        log.info("创建商品: {}", product.getName());
        return product;
    }

    public Product update(Product product) {
        productMapper.update(product);
        log.info("更新商品: {}", product.getId());
        return product;
    }

    public void delete(Long id) {
        productMapper.deleteById(id);
        log.info("删除商品: {}", id);
    }
}
