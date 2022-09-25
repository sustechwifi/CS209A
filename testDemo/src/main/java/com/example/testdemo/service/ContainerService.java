package com.example.testdemo.service;

import com.example.testdemo.entity.Container;
import com.example.testdemo.mapper.ContainerMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ContainerService {
    @Resource
    ContainerMapper containerMapper;

    public int add(Container container){
        int res = containerMapper.add(container);
        return res == -1 ? containerMapper.getId(container) : res;
    }

    public int getId(Container container){
        return containerMapper.getId(container);
    }
}
