package com.test.service;

import com.test.entity.User;
import com.test.exception.BusinessException;
import com.test.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setPhone("13800138000");
        testUser.setEmail("test@example.com");
        testUser.setRole(0);
    }

    @Test
    void testRegister_Success() {
        when(userMapper.selectByUsername("testuser")).thenReturn(null);
        when(userMapper.insert(any(User.class))).thenReturn(1);

        User result = userService.register("testuser", "password123", "13800138000", "test@example.com");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals(0, result.getRole());
        verify(userMapper, times(1)).insert(any(User.class));
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        when(userMapper.selectByUsername("existinguser")).thenReturn(testUser);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.register("existinguser", "password123", "13800138000", "test@example.com");
        });

        assertEquals("用户名已存在", exception.getMessage());
    }

    @Test
    void testRegister_UsernameTooShort() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.register("a", "password123", "13800138000", "test@example.com");
        });

        assertEquals("用户名长度为2-20个字符", exception.getMessage());
    }

    @Test
    void testRegister_PasswordTooShort() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.register("testuser", "123", "13800138000", "test@example.com");
        });

        assertEquals("密码长度为6-20个字符", exception.getMessage());
    }

    @Test
    void testLogin_Success() {
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);

        User result = userService.login("testuser", "password123");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testLogin_WrongPassword() {
        when(userMapper.selectByUsername("testuser")).thenReturn(testUser);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.login("testuser", "wrongpassword");
        });

        assertEquals("用户名或密码错误", exception.getMessage());
    }

    @Test
    void testLogin_UserNotFound() {
        when(userMapper.selectByUsername("nonexistent")).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.login("nonexistent", "password123");
        });

        assertEquals("用户名或密码错误", exception.getMessage());
    }

    @Test
    void testGetById() {
        when(userMapper.selectById(1L)).thenReturn(testUser);

        User result = userService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
    }
}
