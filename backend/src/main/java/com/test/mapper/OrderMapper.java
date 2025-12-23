package com.test.mapper;

import com.test.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OrderMapper {
    int insert(Order order);
    List<Order> selectByUserId(@Param("userId") Long userId);
    List<Order> selectAll();
    Order selectById(@Param("id") Long id);
    int updateStatus(@Param("id") Long id, @Param("status") int status);
    int updatePayTime(@Param("id") Long id);
    int updateShipTime(@Param("id") Long id);
    int updateCompleteTime(@Param("id") Long id);
}
