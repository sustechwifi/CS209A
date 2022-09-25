package com.example.testdemo.service;

import com.example.testdemo.entity.Transit;
import com.example.testdemo.mapper.TransitMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TransitService {
    @Resource
    TransitMapper transitMapper;

    public void add(Transit transit){
        transitMapper.add(transit);
    }
}
