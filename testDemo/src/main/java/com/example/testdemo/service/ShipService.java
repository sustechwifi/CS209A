package com.example.testdemo.service;

import com.example.testdemo.entity.Ship;
import com.example.testdemo.mapper.ShipMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class ShipService {
    @Resource
    ShipMapper shipMapper;

    public int add(Ship ship){
        int res = shipMapper.add(ship);
        return  res == -1 ? shipMapper.getId(ship) : res;
    }
}
