package com.test.mapper;

import com.test.entity.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AddressMapper {
    List<Address> selectByUserId(@Param("userId") Long userId);
    Address selectById(@Param("id") Long id);
    int insert(Address address);
    int update(Address address);
    int deleteById(@Param("id") Long id);
    int clearDefault(@Param("userId") Long userId);
    int setDefault(@Param("id") Long id);
}
