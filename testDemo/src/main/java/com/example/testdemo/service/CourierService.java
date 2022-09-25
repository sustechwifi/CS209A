package com.example.testdemo.service;

import com.example.testdemo.entity.Courier;
import com.example.testdemo.mapper.CourierMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CourierService {
    @Resource
    CourierMapper courierMapper;

    public int add(Courier courier){
        int res = courierMapper.add(courier);
        return res == -1 ? courierMapper.getId(courier) : res;
    }
    public int getId(Courier courier){
        return courierMapper.getId(courier);
    }

}
