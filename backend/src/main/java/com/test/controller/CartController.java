package com.test.controller;

import com.test.common.Result;
import com.test.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public Result<Map<String, Object>> getCart(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return Result.success(cartService.getCart(userId));
    }

    @PostMapping
    public Result<Void> addItem(@RequestBody Map<String, Object> params, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Long productId = Long.valueOf(params.get("productId").toString());
        Integer quantity = Integer.valueOf(params.get("quantity").toString());
        cartService.addItem(userId, productId, quantity);
        return Result.success(null);
    }

    @PutMapping("/{id}")
    public Result<Void> updateQuantity(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        cartService.updateQuantity(id, params.get("quantity"));
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> removeItem(@PathVariable Long id) {
        cartService.removeItem(id);
        return Result.success(null);
    }

    @DeleteMapping("/clear")
    public Result<Void> clearCart(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        cartService.clearCart(userId);
        return Result.success(null);
    }
}
