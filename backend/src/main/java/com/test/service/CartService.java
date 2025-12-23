package com.test.service;

import com.test.entity.CartItem;
import com.test.mapper.CartItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CartService {
    @Autowired
    private CartItemMapper cartItemMapper;

    public Map<String, Object> getCart(Long userId) {
        List<CartItem> items = cartItemMapper.selectByUserId(userId);
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            if (item.getProductPrice() != null) {
                total = total.add(item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("total", total);
        return result;
    }

    public void addItem(Long userId, Long productId, Integer quantity) {
        CartItem existing = cartItemMapper.selectByUserAndProduct(userId, productId);
        if (existing != null) {
            cartItemMapper.updateQuantity(existing.getId(), existing.getQuantity() + quantity);
        } else {
            CartItem item = new CartItem();
            item.setUserId(userId);
            item.setProductId(productId);
            item.setQuantity(quantity);
            cartItemMapper.insert(item);
        }
        log.info("添加购物车: userId={}, productId={}", userId, productId);
    }

    public void updateQuantity(Long id, Integer quantity) {
        cartItemMapper.updateQuantity(id, quantity);
    }

    public void removeItem(Long id) {
        cartItemMapper.deleteById(id);
    }

    public void clearCart(Long userId) {
        cartItemMapper.deleteByUserId(userId);
        log.info("清空购物车: userId={}", userId);
    }
}
