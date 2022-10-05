package com.example.testdemo.service;

import com.example.testdemo.component.Result;
import com.example.testdemo.component.RowRecord;
import com.example.testdemo.entity.Courier;
import com.example.testdemo.entity.Ship;
import com.example.testdemo.entity.Transit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;

@Service
public class UpdateService {

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

    public Result<?> updateToExported(Ship ship, Container container) {
        return null;
    }

    public Result<?> updateToUnDelivery(Transit transit) {
        return null;
    }

    public Result<?> updateToFinished(Transit transit, Courier courier) {
        return null;
    }

    public Result<?> updateBatch(RowRecord record, int type) {
        return null;
    }
}
