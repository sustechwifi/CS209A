package service;

import component.PageBean;
import component.Result;
import component.RowRecord;
import entity.*;
import entity.Record;

import java.util.List;

public class FileDQLServiceImpl implements FileDQLService{
    @Override
    public Result<RowRecord> getRowRecordsByRecord(String path, Record record) {
        return null;
    }

    @Override
    public Result<PageBean<List<RowRecord>>> getRowRecordsByDate(String path, Transit transit, int begin, int pageSize) {
        return null;
    }

    @Override
    public List<Integer> getAllUnExported(String path) {
        return null;
    }

    @Override
    public List<Integer> getAllExported(String path) {
        return null;
    }

    @Override
    public List<Integer> getAllUnDelivery(String path) {
        return null;
    }

    @Override
    public Result<?> getTimeServedBy(String path, Container container) {
        return null;
    }

    @Override
    public Result<Courier> getCourier(String path, Company company, City city) {
        return null;
    }

    @Override
    public Result<City> getCity(String path) {
        return null;
    }
}
