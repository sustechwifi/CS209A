package com.example.testdemo.service;

import com.example.testdemo.entity.Handle;
import com.example.testdemo.mapper.HandleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HandleService {
    @Resource
    HandleMapper handleMapper;

    public void add(Handle handle){
        handleMapper.add(handle);
    }
}
