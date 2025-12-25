package com.test.service;

import com.test.entity.Product;
import com.test.mapper.ProductMapper;
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
public class ProductServiceTest {

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("iPhone 15");
        testProduct.setPrice(new BigDecimal("8999.00"));
        testProduct.setStock(100);
        testProduct.setCategoryId(1L);
        testProduct.setStatus(1);
    }

    @Test
    void testGetById() {
        when(productMapper.selectById(1L)).thenReturn(testProduct);

        Product result = productService.getById(1L);

        assertNotNull(result);
        assertEquals("iPhone 15", result.getName());
        assertEquals(100, result.getStock());
        assertEquals(0, result.getPrice().compareTo(new BigDecimal("8999.00")));
    }

    @Test
    void testGetById_NotFound() {
        when(productMapper.selectById(999L)).thenReturn(null);

        Product result = productService.getById(999L);

        assertNull(result);
    }

    @Test
    void testCreateProduct() {
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setPrice(new BigDecimal("100.00"));
        newProduct.setStock(50);
        
        when(productMapper.insert(any(Product.class))).thenReturn(1);

        Product result = productService.create(newProduct);

        assertNotNull(result);
        assertEquals(1, result.getStatus().intValue());
        verify(productMapper, times(1)).insert(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        when(productMapper.update(any(Product.class))).thenReturn(1);

        Product updated = productService.update(testProduct);

        assertNotNull(updated);
        verify(productMapper, times(1)).update(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        when(productMapper.deleteById(1L)).thenReturn(1);

        productService.delete(1L);

        verify(productMapper, times(1)).deleteById(1L);
    }
}