package com.test.service;

import com.test.entity.*;
import com.test.exception.BusinessException;
import com.test.mapper.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock private OrderMapper orderMapper;
    @Mock private OrderItemMapper orderItemMapper;
    @Mock private CartItemMapper cartItemMapper;
    @Mock private ProductMapper productMapper;
    @Mock private AddressMapper addressMapper;

    @InjectMocks
    private OrderService orderService;

    private CartItem testCartItem;
    private Product testProduct;
    private Address testAddress;
    private Order testOrder;
    private OrderItem testOrderItem;

    @BeforeEach
    void setUp() {
        testCartItem = new CartItem();
        testCartItem.setId(1L);
        testCartItem.setUserId(1L);
        testCartItem.setProductId(1L);
        testCartItem.setQuantity(2);

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("iPhone 15");
        testProduct.setPrice(new BigDecimal("8999.00"));
        testProduct.setStock(100);

        testAddress = new Address();
        testAddress.setId(1L);
        testAddress.setUserId(1L);
        testAddress.setReceiverName("张三");
        testAddress.setPhone("13800138000");
        testAddress.setProvince("北京");
        testAddress.setCity("北京市");
        testAddress.setDistrict("朝阳区");
        testAddress.setDetailAddress("某街道123号");

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderNo("202312251234567890");
        testOrder.setUserId(1L);
        testOrder.setTotalAmount(new BigDecimal("17998.00"));
        testOrder.setStatus(0);

        testOrderItem = new OrderItem();
        testOrderItem.setId(1L);
        testOrderItem.setOrderId(1L);
        testOrderItem.setProductId(1L);
        testOrderItem.setQuantity(2);
        testOrderItem.setProductPrice(new BigDecimal("8999.00"));
        testOrderItem.setProductName("iPhone 15");
    }

    @Test
    void testCreateOrder_Success() {
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        when(cartItemMapper.selectByUserId(1L)).thenReturn(cartItems);
        when(addressMapper.selectById(1L)).thenReturn(testAddress);
        when(productMapper.selectById(1L)).thenReturn(testProduct);
        when(productMapper.updateStock(1L, 2)).thenReturn(1);
        when(orderMapper.insert(any(Order.class))).thenReturn(1);
        when(orderItemMapper.insertBatch(anyList())).thenReturn(1);
        when(cartItemMapper.deleteByUserId(1L)).thenReturn(1);

        Order result = orderService.createOrder(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertNotNull(result.getOrderNo());
        verify(orderMapper, times(1)).insert(any(Order.class));
        verify(cartItemMapper, times(1)).deleteByUserId(1L);
    }

    @Test
    void testCreateOrder_EmptyCart() {
        when(cartItemMapper.selectByUserId(1L)).thenReturn(Arrays.asList());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.createOrder(1L, 1L);
        });

        assertEquals("购物车为空", exception.getMessage());
    }

    @Test
    void testCreateOrder_AddressNotFound() {
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        when(cartItemMapper.selectByUserId(1L)).thenReturn(cartItems);
        when(addressMapper.selectById(999L)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.createOrder(1L, 999L);
        });

        assertEquals("地址不存在", exception.getMessage());
    }

    @Test
    void testCreateOrder_InsufficientStock() {
        testProduct.setStock(1);
        List<CartItem> cartItems = Arrays.asList(testCartItem);
        
        when(cartItemMapper.selectByUserId(1L)).thenReturn(cartItems);
        when(addressMapper.selectById(1L)).thenReturn(testAddress);
        when(productMapper.selectById(1L)).thenReturn(testProduct);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.createOrder(1L, 1L);
        });

        assertTrue(exception.getMessage().contains("库存不足"));
    }

    @Test
    void testGetById_WithItems() {
        when(orderMapper.selectById(1L)).thenReturn(testOrder);
        List<OrderItem> items = Arrays.asList(testOrderItem);
        when(orderItemMapper.selectByOrderId(1L)).thenReturn(items);

        Order result = orderService.getById(1L);

        assertNotNull(result);
        assertNotNull(result.getItems());
        assertEquals(1, result.getItems().size());
    }

    @Test
    void testCancelOrder_Success() {
        when(orderMapper.selectById(1L)).thenReturn(testOrder);
        List<OrderItem> items = Arrays.asList(testOrderItem);
        when(orderItemMapper.selectByOrderId(1L)).thenReturn(items);
        when(productMapper.updateStock(anyLong(), anyInt())).thenReturn(1);
        when(orderMapper.updateStatus(1L, 4)).thenReturn(1);

        orderService.cancel(1L);

        verify(orderMapper, times(1)).updateStatus(1L, 4);
        verify(productMapper, times(1)).updateStock(eq(1L), eq(-2));
    }

    @Test
    void testCancelOrder_NotFound() {
        when(orderMapper.selectById(999L)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.cancel(999L);
        });

        assertEquals("订单不存在", exception.getMessage());
    }

    @Test
    void testCancelOrder_NotPending() {
        testOrder.setStatus(1);
        when(orderMapper.selectById(1L)).thenReturn(testOrder);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.cancel(1L);
        });

        assertEquals("只能取消待付款订单", exception.getMessage());
    }
}