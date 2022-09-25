package com.example.testdemo.service;

import com.example.testdemo.entity.City;
import com.example.testdemo.mapper.CityMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CityService {
    @Resource
    CityMapper cityMapper;

    public int add(City city){
        int res = cityMapper.add(city);
        return res == -1  ? getId(city.getName()) : res;
    }

    public int getId(String id){
        return cityMapper.getId(id);
    }
}
