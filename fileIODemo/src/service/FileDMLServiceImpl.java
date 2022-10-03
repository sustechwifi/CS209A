package service;

import component.Result;
import entity.Container;
import entity.Courier;
import entity.Ship;
import entity.Transit;

import java.util.List;

public class FileDMLServiceImpl implements FileDMLService{
    @Override
    public Result<?> insertFromFile(String path) {
        return null;
    }

    @Override
    public Result<?> insertFromFile(String path, int line) {
        return null;
    }

    @Override
    public Result<Integer> getCount(String path) {
        return null;
    }

    @Override
    public Result<?> deleteByRecordId(String path, Integer[] ids) {
        return null;
    }

    @Override
    public Result<List<Integer>> updateUnExported(String path, Ship ship, Container container, Transit transit) {
        return null;
    }

    @Override
    public Result<List<Integer>> updateExported(String path, Transit transit) {
        return null;
    }

    @Override
    public Result<List<Integer>> updateUnDelivery(String path, Courier courier, Transit transit) {
        return null;
    }
}
