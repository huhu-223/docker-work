package com.test.service;

import com.test.entity.CartItem;
import com.test.mapper.CartItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private CartService cartService;

    private CartItem testCartItem;

    @BeforeEach
    void setUp() {
        testCartItem = new CartItem();
        testCartItem.setId(1L);
        testCartItem.setUserId(1L);
        testCartItem.setProductId(1L);
        testCartItem.setQuantity(2);
        testCartItem.setProductName("iPhone 15");
        testCartItem.setProductPrice(new BigDecimal("8999.00"));
    }

    @Test
    void testGetCart() {
        List<CartItem> items = Arrays.asList(testCartItem);
        when(cartItemMapper.selectByUserId(1L)).thenReturn(items);

        Map<String, Object> result = cartService.getCart(1L);

        assertNotNull(result);
        assertTrue(result.containsKey("items"));
        assertTrue(result.containsKey("total"));
        
        List<CartItem> resultItems = (List<CartItem>) result.get("items");
        assertEquals(1, resultItems.size());
        
        BigDecimal total = (BigDecimal) result.get("total");
        assertEquals(0, total.compareTo(new BigDecimal("17998.00")));
    }

    @Test
    void testGetCart_Empty() {
        when(cartItemMapper.selectByUserId(1L)).thenReturn(Arrays.asList());

        Map<String, Object> result = cartService.getCart(1L);

        assertNotNull(result);
        List<CartItem> items = (List<CartItem>) result.get("items");
        assertTrue(items.isEmpty());
        BigDecimal total = (BigDecimal) result.get("total");
        assertEquals(0, total.compareTo(BigDecimal.ZERO));
    }

    @Test
    void testAddItem_NewItem() {
        when(cartItemMapper.selectByUserAndProduct(1L, 1L)).thenReturn(null);
        when(cartItemMapper.insert(any(CartItem.class))).thenReturn(1);

        cartService.addItem(1L, 1L, 2);

        verify(cartItemMapper, times(1)).insert(any(CartItem.class));
    }

    @Test
    void testAddItem_ExistingItem() {
        when(cartItemMapper.selectByUserAndProduct(1L, 1L)).thenReturn(testCartItem);
        doNothing().when(cartItemMapper).updateQuantity(1L, 4);

        cartService.addItem(1L, 1L, 2);

        verify(cartItemMapper, times(1)).updateQuantity(1L, 4);
    }

    @Test
    void testUpdateQuantity() {
        doNothing().when(cartItemMapper).updateQuantity(1L, 5);

        cartService.updateQuantity(1L, 5);

        verify(cartItemMapper, times(1)).updateQuantity(1L, 5);
    }

    @Test
    void testRemoveItem() {
        doNothing().when(cartItemMapper).deleteById(1L);

        cartService.removeItem(1L);

        verify(cartItemMapper, times(1)).deleteById(1L);
    }

    @Test
    void testClearCart() {
        doNothing().when(cartItemMapper).deleteByUserId(1L);

        cartService.clearCart(1L);

        verify(cartItemMapper, times(1)).deleteByUserId(1L);
    }
}
