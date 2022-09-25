package com.example.testdemo.service;
import com.example.testdemo.entity.Record;
import com.example.testdemo.mapper.RecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RecordService {
    @Resource
    RecordMapper recordMapper;

    public int add(Record record){
        int res = recordMapper.add(record);
        return res == -1 ? recordMapper.getIdInt(record) : res;
    }

}
