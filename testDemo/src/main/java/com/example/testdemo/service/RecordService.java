package com.example.testdemo.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.util.StringUtils;
import com.example.testdemo.component.RowRecord;
import com.example.testdemo.entity.Record;
import com.example.testdemo.mapper.RecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordService {
    @Resource
    RecordMapper recordMapper;

    @Resource
    ContainerService containerService;

    public int add(Record record) {
        recordMapper.add(record);
        return recordMapper.getId(record);
    }

    public void deleteAll() {
        recordMapper.deleteAll();
    }

    public List<Record> getRecordsByModel(Record record, int begin, int size) {
        return recordMapper.getRecordsByModel(record, begin, size);
    }

    public List<Integer> getIds(Record record) {
        return recordMapper.getIds(record);
    }

    public Record getRecordById(int id) {
        return recordMapper.getRecordById(id);
    }

    public List<Integer> flitIds(RowRecord model, Integer companyId, Integer shipId,
                                 Integer[] ids, Integer type) {
        List<Record> records = recordMapper.getRecordByIds(ids);
        return records.stream().filter(r ->
                        (type == 0 || r.getState() == type)
                                && (companyId == null || r.getCompanyId().equals(companyId))
                                && (r.getState() != 1 && (StrUtil.isBlank(model.getContainerType())
                                ||  containerService.getContainerById(r.getContainerId())
                                .getType().equals(model.getContainerType())))
                                && (shipId == null || shipId.equals(r.getShipId()))
                                && (StrUtil.isBlank(model.getItemClass()) || model.getItemClass().equals(r.getItemClass())))
                .map(Record::getId).toList();
    }

}
