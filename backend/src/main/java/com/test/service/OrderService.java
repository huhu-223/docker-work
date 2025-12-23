package com.test.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.common.PageResult;
import com.test.entity.*;
import com.test.exception.BusinessException;
import com.test.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CartItemMapper cartItemMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private AddressMapper addressMapper;

    @Transactional
    public Order createOrder(Long userId, Long addressId) {
        List<CartItem> cartItems = cartItemMapper.selectByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new BusinessException("购物车为空");
        }
        Address address = addressMapper.selectById(addressId);
        if (address == null) {
            throw new BusinessException("地址不存在");
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Product product = productMapper.selectById(cartItem.getProductId());
            if (product.getStock() < cartItem.getQuantity()) {
                throw new BusinessException("商品[" + product.getName() + "]库存不足");
            }
            int updated = productMapper.updateStock(product.getId(), cartItem.getQuantity());
            if (updated == 0) {
                throw new BusinessException("商品[" + product.getName() + "]库存不足");
            }
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalAmount = totalAmount.add(subtotal);
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSubtotal(subtotal);
            orderItems.add(orderItem);
        }
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus(0);
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getPhone());
        order.setReceiverAddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getDetailAddress());
        orderMapper.insert(order);
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
        }
        orderItemMapper.insertBatch(orderItems);
        cartItemMapper.deleteByUserId(userId);
        log.info("创建订单: {}", order.getOrderNo());
        return order;
    }

    public PageResult<Order> listByUserId(Long userId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Order> list = orderMapper.selectByUserId(userId);
        PageInfo<Order> pageInfo = new PageInfo<>(list);
        return PageResult.of(pageInfo.getList(), pageInfo.getTotal(), page, size);
    }

    public PageResult<Order> listAll(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Order> list = orderMapper.selectAll();
        PageInfo<Order> pageInfo = new PageInfo<>(list);
        return PageResult.of(pageInfo.getList(), pageInfo.getTotal(), page, size);
    }

    public Order getById(Long id) {
        Order order = orderMapper.selectById(id);
        if (order != null) {
            order.setItems(orderItemMapper.selectByOrderId(id));
        }
        return order;
    }

    @Transactional
    public void cancel(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) throw new BusinessException("订单不存在");
        if (order.getStatus() != 0) throw new BusinessException("只能取消待付款订单");
        List<OrderItem> items = orderItemMapper.selectByOrderId(id);
        for (OrderItem item : items) {
            productMapper.updateStock(item.getProductId(), -item.getQuantity());
        }
        orderMapper.updateStatus(id, 4);
        log.info("取消订单: {}", id);
    }

    public void updateStatus(Long id, Integer status) {
        if (status == 1) orderMapper.updatePayTime(id);
        else if (status == 2) orderMapper.updateShipTime(id);
        else if (status == 3) orderMapper.updateCompleteTime(id);
        else orderMapper.updateStatus(id, status);
        log.info("更新订单状态: id={}, status={}", id, status);
    }

    private String generateOrderNo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + (new Random().nextInt(9000) + 1000);
    }
}
