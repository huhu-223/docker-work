package com.test.controller;

import com.test.common.PageResult;
import com.test.common.Result;
import com.test.entity.Order;
import com.test.entity.User;
import com.test.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public Result<PageResult<Order>> list(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return Result.success(orderService.listByUserId(userId, page, size));
    }

    @GetMapping("/listAll")
    public Result<PageResult<Order>> listAll(@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer size,
                                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != 1) {
            return Result.error("无权限");
        }
        return Result.success(orderService.listAll(page, size));
    }

    @GetMapping("/{id}")
    public Result<Order> getById(@PathVariable Long id) {
        return Result.success(orderService.getById(id));
    }

    @PostMapping
    public Result<Order> create(@RequestBody Map<String, Long> params, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return Result.success(orderService.createOrder(userId, params.get("addressId")));
    }

    @PutMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        orderService.cancel(id);
        return Result.success(null);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> params) {
        orderService.updateStatus(id, params.get("status"));
        return Result.success(null);
    }
}
