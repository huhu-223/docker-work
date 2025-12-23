package com.test.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private Long categoryId;
    private Integer status;
    private LocalDateTime createTime;
    private String categoryName;
}
