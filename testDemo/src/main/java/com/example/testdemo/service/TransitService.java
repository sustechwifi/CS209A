package com.example.testdemo.service;

import com.example.testdemo.component.RowRecord;
import com.example.testdemo.entity.City;
import com.example.testdemo.entity.Handle;
import com.example.testdemo.entity.Transit;
import com.example.testdemo.mapper.TransitMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransitService {
    @Resource
    TransitMapper transitMapper;

    @Resource
    CityService cityService;

    public static final int TRANSIT_SIZE = 4;

    public static final int DECIMAL = 1000000;

    public void add(Transit transit) {
        transitMapper.add(transit);
    }

    public void addBatch(List<Transit> transits) {
        if (transits.isEmpty()) {
            return;
        }
        transitMapper.addBatch(transits);
    }

    public void deleteAll() {
        transitMapper.deleteAll();
    }

    public List<Transit> getTransitsByRecordId(int id) {
        List<Transit> res = transitMapper.getTransitsByRecordId(id);
        res.sort(Comparator.comparingInt(Transit::getType));
        return res;
    }


    public boolean isCorrectTransits(List<Transit> transits, RowRecord model, Integer[] cityIds) {
        if (transits.size() != TRANSIT_SIZE) {
            return false;
        }
        List<Transit> models = new ArrayList<>();
        models.add(new Transit(1, model.getRetrievalDate(), null, cityIds[0], null, null));
        models.add(new Transit(2, model.getExportDate(), model.getExportTax(), cityIds[1], null, null));
        models.add(new Transit(3, model.getImportDate(), model.getImportTax(), cityIds[2], null, null));
        models.add(new Transit(4, model.getDeliveryDate(), null, cityIds[3], null, null));
        for (int i = 0; i < TRANSIT_SIZE; i++) {
            if (!judgeTransition(transits.get(i), models.get(i))) {
                //System.out.println(transits.get(i));
                //System.out.println(models.get(i));
                return false;
            }
        }
        return true;
    }

    private boolean judgeTransition(Transit transit, Transit model) {
        if (transit == null) {
            return true;
        } else {
            if (!model.getType().equals(transit.getType())) {
                return false;
            } else if (model.getTax() != null && !model.getTax().equals(transit.getTax())) {
                return false;
            } else if (model.getCityId() != null && !model.getCityId().equals(transit.getCityId())) {
                return false;
            } else {
                return model.getTime() == null || model.getTime().equals(transit.getTime());
            }
        }
    }

    public List<Integer> getRecordIdByDateAndTax(RowRecord model) {
        HashMap<Integer, Integer> flag = new HashMap<>(300);
        int cnt = 0;
        if (model.getRetrievalDate() != null) {
            cnt++;
            transitMapper.getRecordIdByDateAndType(model.getRetrievalDate(), 1).forEach(i -> {
                flag.put(i, flag.getOrDefault(i, 0) + 1);
            });
        }
        if (model.getExportDate() != null) {
            cnt++;
            transitMapper.getRecordIdByDateAndType(model.getExportDate(), 2).forEach(i -> {
                flag.put(i, flag.getOrDefault(i, 0) + 1);
            });
        }
        if (model.getImportDate() != null) {
            cnt++;
            transitMapper.getRecordIdByDateAndType(model.getImportDate(), 3).forEach(i -> {
                flag.put(i, flag.getOrDefault(i, 0) + 1);
            });
        }
        if (model.getDeliveryDate() != null) {
            cnt++;
            transitMapper.getRecordIdByDateAndType(model.getDeliveryDate(), 4).forEach(i -> {
                flag.put(i, flag.getOrDefault(i, 0) + 1);
            });
        }
        if (model.getExportTax() != null) {
            cnt++;
            transitMapper.getRecordIdByTaxAndType(model.getExportTax() / DECIMAL * DECIMAL, 2).forEach(i -> {
                flag.put(i, flag.getOrDefault(i, 0) + 1);
            });
        }
        if (model.getImportTax() != null) {
            transitMapper.getRecordIdByTaxAndType(model.getImportTax() / DECIMAL * DECIMAL, 2).forEach(i -> {
                flag.put(i, flag.getOrDefault(i, 0) + 1);
            });
        }
        final int count = cnt;
        return flag.keySet().stream().filter(f -> flag.get(f) == count).collect(Collectors.toList());
    }

    public void deleteByRecordIds(Integer[] ids) {
        transitMapper.deleteByRecordIds(ids);
    }


    public Map<String, Double> getResult3() {
        return transitMapper.getResult3();
    }
}
