package com.example.testdemo.service;

import com.example.testdemo.component.RowRecord;
import com.example.testdemo.entity.Handle;
import com.example.testdemo.entity.Transit;
import com.example.testdemo.mapper.HandleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class HandleService {
    @Resource
    HandleMapper handleMapper;

    public static final int HANDLE_SIZE = 2;

    public void add(Handle handle) {
        handleMapper.add(handle);
    }

    public void deleteAll() {
        handleMapper.deleteAll();
    }

    public List<Handle> getHandlesByRecordId(int recordId) {
        List<Handle> res = handleMapper.getHandlesByRecordId(recordId);
        res.sort(Comparator.comparingInt(Handle::getType));
        return res;
    }

    public List<Integer> getRecordIdsByCourierIdAndType(Integer id1, Integer id2) {
        List<Integer> res = new ArrayList<>();
        if (id1 == null || id2 == null) {
            return id2 == null ? handleMapper.getRecordIdsByCourierIdAndType(id1, 1) :
                    handleMapper.getRecordIdsByCourierIdAndType(id2, 4);
        }
        Set<Integer> s = new HashSet<>(handleMapper.getRecordIdsByCourierIdAndType(id1, 1));
        handleMapper.getRecordIdsByCourierIdAndType(id2, 4).forEach(item -> {
            if (s.contains(item)) {
                res.add(item);
            }
        });
        return res;
    }

    public boolean isCorrectHandle(List<Handle> handles, RowRecord model, Integer[] couriers) {
        if (handles.size() != HANDLE_SIZE) {
            return false;
        }
        List<Handle> models = new ArrayList<>();
        models.add(new Handle(1, model.getRetrievalDate(), null, couriers[0], null));
        models.add(new Handle(4, model.getDeliveryDate(), null, couriers[1], null));
        for (int i = 0; i < HANDLE_SIZE; i++) {
            if (!judgeHandle(handles.get(i), models.get(i))) {
                //System.out.println(handles.get(i));
                //System.out.println(models.get(i));
                return false;
            }
        }
        return true;
    }

    private boolean judgeHandle(Handle handle, Handle model) {
        if (handle == null) {
            return true;
        } else {
            if (!model.getType().equals(handle.getType())) {
                return false;
            } else if (model.getCourierId() != null && !model.getCourierId().equals(handle.getCourierId())) {
                return false;
            } else {
                return model.getTime() == null || model.getTime().equals(handle.getTime());
            }
        }
    }
}
