package com.test.controller;

import com.test.common.Result;
import com.test.entity.User;
import com.test.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public Result<User> register(@RequestBody Map<String, String> params) {
        User user = userService.register(params.get("username"), params.get("password"), params.get("phone"), params.get("email"));
        user.setPassword(null);
        return Result.success(user);
    }

    @PostMapping("/login")
    public Result<User> login(@RequestBody Map<String, String> params, HttpSession session) {
        User user = userService.login(params.get("username"), params.get("password"));
        session.setAttribute("userId", user.getId());
        session.setAttribute("user", user);
        user.setPassword(null);
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        session.invalidate();
        return Result.success(null);
    }

    @GetMapping("/info")
    public Result<User> info(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) user.setPassword(null);
        return Result.success(user);
    }
}
