package com.test.controller;

import com.test.common.Result;
import com.test.entity.Address;
import com.test.service.AddressService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/list")
    public Result<List<Address>> list(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return Result.success(addressService.listByUserId(userId));
    }

    @GetMapping("/{id}")
    public Result<Address> getById(@PathVariable Long id) {
        return Result.success(addressService.getById(id));
    }

    @PostMapping
    public Result<Address> create(@RequestBody Address address, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        address.setUserId(userId);
        return Result.success(addressService.create(address));
    }

    @PutMapping("/{id}")
    public Result<Address> update(@PathVariable Long id, @RequestBody Address address) {
        address.setId(id);
        return Result.success(addressService.update(address));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return Result.success(null);
    }

    @PutMapping("/{id}/default")
    public Result<Void> setDefault(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        addressService.setDefault(userId, id);
        return Result.success(null);
    }
}
