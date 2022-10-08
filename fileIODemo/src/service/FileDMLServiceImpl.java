package service;

import component.Result;
import component.RowRecord;

import java.util.List;

public class FileDMLServiceImpl implements FileDMLService {
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
    public Result<List<Integer>> updateBatch(RowRecord record, int type) {
        return null;
    }
}
