package com.example.testdemo.service;

import com.example.testdemo.mapper.CompanyMapper;
import com.example.testdemo.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CompanyService {
    @Resource
    CompanyMapper companyMapper;

    public int add(String name){
        int res = companyMapper.addCompany(name);
        return res == -1 ? getId(name) : res;
    }

    public int getId(String name){
        return companyMapper.getId(name);
    }

    public String getName(int id) {
        return companyMapper.getCompanyName(id);
    }
}
