package com.test.mapper;

import com.test.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProductMapper {
    List<Product> selectByCondition(@Param("categoryId") Long categoryId, @Param("keyword") String keyword);
    List<Product> selectAllByCondition(@Param("categoryId") Long categoryId, @Param("keyword") String keyword);
    Product selectById(@Param("id") Long id);
    int insert(Product product);
    int update(Product product);
    int deleteById(@Param("id") Long id);
    int updateStock(@Param("id") Long id, @Param("quantity") int quantity);
}
