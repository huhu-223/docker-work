package com.test.controller;

import com.test.common.Result;
import com.test.entity.Category;
import com.test.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<Category>> list() {
        return Result.success(categoryService.listAll());
    }

    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Long id) {
        return Result.success(categoryService.getById(id));
    }

    @PostMapping
    public Result<Category> create(@RequestBody Category category) {
        return Result.success(categoryService.create(category));
    }

    @PutMapping("/{id}")
    public Result<Category> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        return Result.success(categoryService.update(category));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success(null);
    }
}
