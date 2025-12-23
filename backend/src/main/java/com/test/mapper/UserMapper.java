package com.test.mapper;

import com.test.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int insert(User user);
    User selectByUsername(@Param("username") String username);
    User selectById(@Param("id") Long id);
}
