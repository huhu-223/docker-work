package com.test.service;

import com.test.entity.Address;
import com.test.mapper.AddressMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
public class AddressService {
    @Autowired
    private AddressMapper addressMapper;

    public List<Address> listByUserId(Long userId) {
        return addressMapper.selectByUserId(userId);
    }

    public Address getById(Long id) {
        return addressMapper.selectById(id);
    }

    public Address create(Address address) {
        if (address.getIsDefault() == null) address.setIsDefault(0);
        addressMapper.insert(address);
        log.info("创建地址: {}", address.getId());
        return address;
    }

    public Address update(Address address) {
        addressMapper.update(address);
        log.info("更新地址: {}", address.getId());
        return address;
    }

    public void delete(Long id) {
        addressMapper.deleteById(id);
        log.info("删除地址: {}", id);
    }

    @Transactional
    public void setDefault(Long userId, Long addressId) {
        addressMapper.clearDefault(userId);
        addressMapper.setDefault(addressId);
        log.info("设置默认地址: {}", addressId);
    }
}
