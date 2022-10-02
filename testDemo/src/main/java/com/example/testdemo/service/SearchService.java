package com.example.testdemo.service;

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.druid.util.StringUtils;
import com.example.testdemo.component.PageBean;
import com.example.testdemo.component.RowRecord;
import com.example.testdemo.entity.*;
import com.example.testdemo.entity.Record;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.testdemo.service.HandleService.HANDLE_SIZE;
import static com.example.testdemo.service.TransitService.TRANSIT_SIZE;

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
        if ("item".equals(item)) {
            return batchItemClass(type, condition, start, pageSize);
        }
        return new PageBean<>();
    }

    public PageBean<RowRecord> batchItemClass(int type, String condition, int start, int pageSize) {

        return null;
    }

    public PageBean<RowRecord> handlePreciseSearch(int type, RowRecord model, int start, int pageSize) {
        Company company = new Company(model.getCompany(), null);
        Integer companyId = companyService.getSearchId(company.getName());
        if (companyId != null && companyId == -1) {
            return new PageBean<>(0, new ArrayList<>());
        }
        Ship ship = new Ship(model.getShip(), companyId, null);
        Integer shipId = shipService.getSearchId(ship);
        if (shipId != null && shipId == -1) {
            return new PageBean<>(0, new ArrayList<>());
        }

        Container container = new Container(model.getContainerCode(), model.getContainerType(), null);
        Integer containerId = containerService.getId(container);

        Integer c1 = cityService.getSearchId(new City(model.getRetrievalCity(), null));
        Integer c2 = cityService.getSearchId(new City(model.getExportCity(), null));
        Integer c3 = cityService.getSearchId(new City(model.getImportCity(), null));
        Integer c4 = cityService.getSearchId(new City(model.getDeliveryCity(), null));
        if ((c1 != null && c1 == -1) || (c2 != null && c2 == -1)
                || (c3 != null && c3 == -1) || (c4 != null && c4 == -1)) {
            return new PageBean<>(0, new ArrayList<>());
        }
        Integer[] cityIds = new Integer[]{c1, c2, c3, c4};

        Courier courier1 = new Courier(
                model.getRetrievalCourierPhone(), model.getRetrievalCourier(), null, model.getRetrievalCourierGender(), companyId, c1, null);
        Courier courier2 = new Courier(
                model.getDeliveryCourierPhone(), model.getDeliveryCourier(), null, model.getDeliveryCourierGender(), companyId, c4, null);
        Integer cid1 = courierService.getSearchId(courier1);
        Integer cid2 = courierService.getSearchId(courier2);
        if ((cid1 != null && cid1 == -1) || (cid2 != null && cid2 == -1)) {
            return new PageBean<>(0, new ArrayList<>());
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
        } else {
            return preciseByTransit(type, model, start, pageSize, companyId, shipId, cityIds, courierIds);
        }
    }

    private PageBean<RowRecord> preciseByHandle(int type, RowRecord model, int start, int pageSize,
                                                Integer companyId, Integer shipId, Integer[] cityIds, Integer[] courierIds) {
        List<Integer> ids = handleService.getRecordIdsByCourierIdAndType(courierIds[0], courierIds[1]);
        ids = recordService.flitIds(model, companyId, shipId, ids.toArray(Integer[]::new), type);
        return preciseByRecord(ids, model, start, pageSize, cityIds, courierIds);
    }

    private PageBean<RowRecord> preciseByTransit(int type, RowRecord model, int start, int pageSize,
                                                 Integer companyId, Integer shipId, Integer[] cityIds, Integer[] courierIds) {
        List<Integer> ids = transitService.getRecordIdByDateAndTax(model);
        System.out.println(ids);
        ids = recordService.flitIds(model, companyId, shipId, ids.toArray(Integer[]::new), type);
        return preciseByRecord(ids, model, start, pageSize, cityIds, courierIds);
    }


    private PageBean<RowRecord> preciseByRecord(List<Integer> ids, RowRecord model, int start, int pageSize,
                                                Integer[] cityIds, Integer[] courierIds) {
        int count = 0;
        System.out.println("record num:" + ids.size());
        List<RowRecord> rows = new ArrayList<>();
        for (Integer id : ids) {
            List<Transit> transits = transitService.getTransitsByRecordId(id);
            while (transits.size() < TRANSIT_SIZE) {
                transits.add(null);
            }
            if (!transitService.isCorrectTransits(transits, model, cityIds)) {
                continue;
            }
            List<Handle> handles = handleService.getHandlesByRecordId(id);
            while (handles.size() < HANDLE_SIZE) {
                handles.add(null);
            }
            if (!handleService.isCorrectHandle(handles, model, courierIds)) {
                continue;
            }
            if (count > (start - 1) * pageSize && rows.size() < pageSize) {
                rows.add(rowRecordService.formRowRecord(recordService.getRecordById(id), transits, handles));
            }
            count++;
        }
        PageBean<RowRecord> res = new PageBean<>();
        res.setRows(rows);
        res.setTotalCount(count);
        return res;
    }
}

