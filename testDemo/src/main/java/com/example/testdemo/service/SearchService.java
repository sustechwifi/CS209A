package com.example.testdemo.service;

import com.alibaba.druid.util.StringUtils;
import com.example.testdemo.component.PageBean;
import com.example.testdemo.component.Result;
import com.example.testdemo.component.RowRecord;
import com.example.testdemo.entity.Record;
import com.example.testdemo.entity.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SearchService {

    @Resource
    CompanyService companyService;
    @Resource
    ShipService shipService;
    @Resource
    ContainerService containerService;
    @Resource
    CityService cityService;
    @Resource
    CourierService courierService;
    @Resource
    RecordService recordService;
    @Resource
    HandleService handleService;
    @Resource
    TransitService transitService;
    @Resource
    RowRecordService rowRecordService;


    public PageBean<RowRecord> handleBatchSearch(String item, int type, String search, int start, int pageSize) {
        String condition = "%" + search + "%";
        System.out.println("item = " + item);
        return getRecordsByType(item, condition, type, start, pageSize);
    }


    private PageBean<RowRecord> getRecordsByType(String item, String condition, int type, int start, int pageSize) {
        List<Integer> ids = recordService.getIdsByCondition(item, condition, type);
        int count = 0;
        System.out.println("record num:" + ids.size());
        List<RowRecord> rows = new ArrayList<>();
        for (Integer id : ids) {
            List<Transit> transits = transitService.getTransitsByRecordId(id);
            List<Handle> handles = handleService.getHandlesByRecordId(id);
            if (count >= (start - 1) * pageSize && rows.size() < pageSize) {
                RowRecord rowRecord = rowRecordService.formRowRecord(recordService.getRecordById(id), transits, handles, new RowRecord());
                if (rowRecord == null) {
                    continue;
                }
                rows.add(rowRecord);
            }
            count++;
        }
        PageBean<RowRecord> res = new PageBean<>();
        res.setIds(ids);
        res.setRows(rows);
        res.setTotalCount(count);
        return res;
    }

    public PageBean<RowRecord> handlePreciseSearch(int type, RowRecord model, int start, int pageSize) {
        Company company = new Company(model.getCompany(), null);
        Integer companyId = companyService.getSearchId(company.getName());
        if (companyId != null && companyId == -1) {
            return new PageBean<>(0, new ArrayList<>(), null, "no found");
        }
        Ship ship = new Ship(model.getShip(), companyId, null);
        Integer shipId = shipService.getSearchId(ship);
        if (shipId != null && shipId == -1) {
            return new PageBean<>(0, new ArrayList<>(), null, "no found");
        }

        Container container = new Container(model.getContainerCode(), model.getContainerType(), null);
        Integer containerId = containerService.getId(container);

        Integer c1 = cityService.getSearchId(new City(model.getRetrievalCity(), null));
        Integer c2 = cityService.getSearchId(new City(model.getExportCity(), null));
        Integer c3 = cityService.getSearchId(new City(model.getImportCity(), null));
        Integer c4 = cityService.getSearchId(new City(model.getDeliveryCity(), null));
        if ((c1 != null && c1 == -1) || (c2 != null && c2 == -1)
                || (c3 != null && c3 == -1) || (c4 != null && c4 == -1)) {
            return new PageBean<>(0, new ArrayList<>(), null, "no found");
        }
        Integer[] cityIds = new Integer[]{c1, c2, c3, c4};

        Courier courier1 = new Courier(
                model.getRetrievalCourierPhone(), model.getRetrievalCourier(), null, model.getRetrievalCourierGender(), companyId, c1, null);
        Courier courier2 = new Courier(
                model.getDeliveryCourierPhone(), model.getDeliveryCourier(), null, model.getDeliveryCourierGender(), companyId, c4, null);
        Integer cid1 = courierService.getSearchId(courier1);
        Integer cid2 = courierService.getSearchId(courier2);
        if ((cid1 != null && cid1 == -1) || (cid2 != null && cid2 == -1)) {
            return new PageBean<>(0, new ArrayList<>(), null, "no found");
        }
        Integer[] courierIds = new Integer[]{cid1, cid2};
        System.out.println(Arrays.toString(courierIds));
        return preciseByType(type, model, start, pageSize, companyId, containerId, shipId, cityIds, courierIds);
    }

    private PageBean<RowRecord> preciseByType(int type, RowRecord model, int start, int pageSize,
                                              Integer companyId, Integer containerId, Integer shipId, Integer[] cityIds, Integer[] courierIds) {
        if (!StringUtils.isEmpty(model.getItemName()) || model.getItemPrice() != null || containerId != null) {
            Record record = new Record(null, model.getItemName(), model.getItemClass(), model.getItemPrice(),
                    companyId, containerId, shipId, null, type);
            List<Integer> ids = recordService.getIds(record);
            return preciseByRecord(ids, model, start, pageSize, cityIds, courierIds);
        } else if (courierIds[0] != null || courierIds[1] != null) {
            return preciseByHandle(type, model, start, pageSize, companyId, shipId, cityIds, courierIds);
        } else if (model.getImportDate() != null
                || model.getExportDate() != null
                || model.getRetrievalDate() != null
                || model.getDeliveryDate() != null
                || model.getExportTax() != null
                || model.getImportTax() != null) {
            return preciseByTransit(type, model, start, pageSize, companyId, shipId, cityIds, courierIds);
        } else {
            return preciseByCourierAndItemClass(type, model, start, pageSize, companyId, shipId, cityIds, courierIds);
        }
    }

    private PageBean<RowRecord> preciseByCourierAndItemClass(int type, RowRecord model, int start, int pageSize,
                                                             Integer companyId, Integer shipId, Integer[] cityIds, Integer[] courierIds) {
        List<Integer> ids1 = recordService.getIdsByItemClass(type, model.getItemClass(), companyId, shipId);
        List<Integer> ids2 = handleService.getRecordIdsByCourierName(type, model.getRetrievalCourier(), model.getDeliveryCourier());
        List<Integer> res = new ArrayList<>();
        int flag = ids1 != null && ids2 != null ? 2 : 1;
        Map<Integer, Integer> m = new HashMap<>(500);
        if (ids1 != null) {
            ids1.forEach(i -> m.put(i, 1));
        }
        if (ids2 != null) {
            ids2.forEach(i -> m.put(i, Objects.requireNonNullElse(m.get(i), 0) + 1));
        }
        m.forEach((k, v) -> {
            if (v == flag) {
                res.add(k);
            }
        });
        System.out.println(res);
        if (res.isEmpty()) {
            return new PageBean<>(0, new ArrayList<>(), null, "no found");
        }
        return preciseByRecord(res, model, start, pageSize, cityIds, courierIds);
    }

    private PageBean<RowRecord> preciseByHandle(int type, RowRecord model, int start, int pageSize,
                                                Integer companyId, Integer shipId, Integer[] cityIds, Integer[] courierIds) {
        List<Integer> ids = handleService.getRecordIdsByCourierIdAndType(courierIds[0], courierIds[1]);
        ids = recordService.flitIds(model, companyId, shipId, ids.toArray(Integer[]::new), type);
        if (ids.isEmpty()) {
            return new PageBean<>(0, new ArrayList<>(), null, "no found");
        }
        return preciseByRecord(ids, model, start, pageSize, cityIds, courierIds);
    }

    private PageBean<RowRecord> preciseByTransit(int type, RowRecord model, int start, int pageSize,
                                                 Integer companyId, Integer shipId, Integer[] cityIds, Integer[] courierIds) {
        List<Integer> ids = transitService.getRecordIdByDateAndTax(model);
        ids = recordService.flitIds(model, companyId, shipId, ids.toArray(Integer[]::new), type);
        if (ids.isEmpty()) {
            return new PageBean<>(0, new ArrayList<>(), null, "no found");
        }
        return preciseByRecord(ids, model, start, pageSize, cityIds, courierIds);
    }


    private PageBean<RowRecord> preciseByRecord(List<Integer> ids, RowRecord model, int start, int pageSize,
                                                Integer[] cityIds, Integer[] courierIds) {
        int count = 0;
        System.out.println("record id num:" + ids.size());
        List<RowRecord> rows = new ArrayList<>();
        for (Integer id : ids) {
            List<Transit> transits = transitService.getTransitsByRecordId(id);
            if (!transitService.isCorrectTransits(transits, model, cityIds)) {
                continue;
            }
            List<Handle> handles = handleService.getHandlesByRecordId(id);
            if (!handleService.isCorrectHandle(handles, model, courierIds)) {
                continue;
            }
            RowRecord rowRecord = rowRecordService.formRowRecord(recordService.getRecordById(id), transits, handles, model);
            if (rowRecord == null) {
                continue;
            }
            if (count >= (start - 1) * pageSize && rows.size() < pageSize) {
                rows.add(rowRecord);
            }
            count++;
        }
        PageBean<RowRecord> res = new PageBean<>();
        res.setRows(rows);
        res.setTotalCount(count);
        return res;
    }

    public Result<Map<String, Integer>> getContainerAdvance(Container container) {
        Map<String, Integer> time = containerService.getOldestContainer(container);
        Result<Map<String, Integer>> res = new Result<>();
        res.setData(time);
        res.setCode("0");
        return res;
    }

    public Result<Map<String, Integer>> getGreatestCourier(String city, String company) {
        Map<String, Integer> data = courierService.getGreatestCourier(city, company);
        Result<Map<String, Integer>> res = new Result<>();
        res.setData(data);
        res.setCode("0");
        return res;
    }

    public Result<String> getResult3ByList(String[] companies) {
        return null;
    }

    public Result<Map<String, Double>> getResult3() {
        Map<String, Double> data = transitService.getResult3();
        Result<Map<String, Double>> res = new Result<>();
        res.setData(data);
        res.setCode("0");
        return res;
    }
}

