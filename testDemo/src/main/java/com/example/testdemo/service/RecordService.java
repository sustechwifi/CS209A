package com.example.testdemo.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.util.StringUtils;
import com.example.testdemo.component.Result;
import com.example.testdemo.component.RowRecord;
import com.example.testdemo.entity.Record;
import com.example.testdemo.mapper.RecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
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
        return recordMapper.getIdByItemName(record.getItemName());
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

    public List<Integer> getIdsByType(int type) {
        return recordMapper.getIdsByType(type);
    }

    public List<Integer> flitIds(RowRecord model, Integer companyId, Integer shipId,
                                 Integer[] ids, Integer type) {
        if (ids.length == 0) {
            return new ArrayList<>();
        }
        List<Record> records = recordMapper.getRecordByIds(ids);
        return records.stream().filter(r ->
                        (type == 0 || r.getState() == type)
                                && (companyId == null || r.getCompanyId().equals(companyId))
                                && (r.getState() != 1 && (StrUtil.isBlank(model.getContainerType())
                                || containerService.getContainerById(r.getContainerId())
                                .getType().equals(model.getContainerType())))
                                && (shipId == null || shipId.equals(r.getShipId()))
                                && (StrUtil.isBlank(model.getItemClass()) || model.getItemClass().equals(r.getItemClass())))
                .map(Record::getId).toList();
    }

    public void deleteByIds(Integer[] ids) {
        recordMapper.deleteByIds(ids);
    }

    public Result<?> getCount() {
        int res = recordMapper.getCount();
        return new Result<>(res);
    }

    public List<Integer> getIdsByCondition(String item, String condition, int type) {
        if ("container".equals(item)) {
            return recordMapper.getIdsByContainer(condition, type);
        } else if ("itemClass".equals(item)) {
            return recordMapper.getIdsByItemClass(condition, type);
        } else if ("ship".equals(item)) {
            return recordMapper.getIdsByShip(condition, type);
        } else if ("company".equals(item)) {
            return recordMapper.getIdsByCompany(condition, type);
        } else {
            return recordMapper.getIdsByType(type);
        }
    }

    public void updateByModel(RowRecord rowRecord, int type) {
        recordMapper.updateByModel(rowRecord, type);
    }

    public void updateToExported(int id, int shipId, int containerId) {
        recordMapper.updateToExported(id, shipId, containerId);
    }

    public void updateToUnDelivery(Integer recordId) {
        recordMapper.updateToUnDelivery(recordId);
    }

    public void updateToFinished(Integer recordId) {
        recordMapper.updateToFinished(recordId);
    }

    public List<Integer> getAllUnExported() {
        return recordMapper.getAllUnExported();
    }

    public List<Integer> getAllExported() {
        return recordMapper.getAllExported();
    }

    public List<Integer> getAllUnFinished() {
        return recordMapper.getAllUnFinished();
    }
}
