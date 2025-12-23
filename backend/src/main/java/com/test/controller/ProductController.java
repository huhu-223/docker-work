package com.test.controller;

import com.test.common.PageResult;
import com.test.common.Result;
import com.test.entity.Product;
import com.test.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/list")
    public Result<PageResult<Product>> list(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam(required = false) Long categoryId,
                                            @RequestParam(required = false) String keyword) {
        return Result.success(productService.listByPage(page, size, categoryId, keyword));
    }

    @GetMapping("/listAll")
    public Result<PageResult<Product>> listAll(@RequestParam(defaultValue = "1") Integer page,
                                               @RequestParam(defaultValue = "10") Integer size,
                                               @RequestParam(required = false) Long categoryId,
                                               @RequestParam(required = false) String keyword) {
        return Result.success(productService.listAllByPage(page, size, categoryId, keyword));
    }

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        return Result.success(productService.getById(id));
    }

    @PostMapping
    public Result<Product> create(@RequestBody Product product) {
        return Result.success(productService.create(product));
    }

    @PutMapping("/{id}")
    public Result<Product> update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return Result.success(productService.update(product));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.success(null);
    }
}
