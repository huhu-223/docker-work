package com.test.service;

import com.test.entity.User;
import com.test.exception.BusinessException;
import com.test.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User register(String username, String password, String phone, String email) {
        // 参数验证
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException("密码不能为空");
        }
        if (username.length() < 2 || username.length() > 20) {
            throw new BusinessException("用户名长度为2-20个字符");
        }
        if (password.length() < 6 || password.length() > 20) {
            throw new BusinessException("密码长度为6-20个字符");
        }
        if (userMapper.selectByUsername(username) != null) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password);
        user.setPhone(phone);
        user.setEmail(email);
        user.setRole(0);
        userMapper.insert(user);
        log.info("用户注册成功: {}", username);
        return user;
    }

    public User login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new BusinessException("用户名或密码错误");
        }
        log.info("用户登录成功: {}", username);
        return user;
    }

    public User getById(Long id) {
        return userMapper.selectById(id);
    }
}
