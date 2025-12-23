package com.test.mapper;

import com.test.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CartItemMapper {
    List<CartItem> selectByUserId(@Param("userId") Long userId);
    CartItem selectByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
    int insert(CartItem cartItem);
    int updateQuantity(@Param("id") Long id, @Param("quantity") int quantity);
    int deleteById(@Param("id") Long id);
    int deleteByUserId(@Param("userId") Long userId);
}
