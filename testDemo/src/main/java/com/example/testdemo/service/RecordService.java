package com.example.testdemo.service;

import com.example.testdemo.entity.Record;
import com.example.testdemo.mapper.RecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RecordService {
    @Resource
    RecordMapper recordMapper;

    public int add(Record record) {
        recordMapper.add(record);
        return recordMapper.getId(record);
    }

    public void deleteAll() {
        recordMapper.deleteAll();
    }

}
