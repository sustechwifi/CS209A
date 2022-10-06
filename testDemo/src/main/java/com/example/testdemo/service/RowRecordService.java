package com.example.testdemo.service;

import com.example.testdemo.component.RowRecord;
import com.example.testdemo.entity.*;
import com.example.testdemo.entity.Record;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RowRecordService {

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


    public RowRecord formRowRecord(Record record, List<Transit> transits, List<Handle> handles, RowRecord model) {
        Courier c1 = courierService.formCourier(courierService.getCourierById(handles.get(0).getCourierId()));
        if (courierService.checkAgeAndGender(c1, model.getRetrievalCourierAge(),
                model.getRetrievalCourierGender())) {
            return null;
        }
        Courier c2 = courierService.formCourier(courierService.getCourierById(handles.get(1).getCourierId()));
        if (courierService.checkAgeAndGender(c2, model.getDeliveryCourierAge(),
                model.getDeliveryCourierGender())) {
            return null;
        }
        Container container = containerService.formContainer(
                containerService.getContainerById(record.getContainerId()));
        return new RowRecord(
                record.getId(),
                record.getItemName(),
                record.getItemClass(),
                record.getItemPrice(),
                transits.get(0).getTime(),
                cityService.getCityName(transits.get(0).getCityId()),
                c1.getName(),
                c1.getGender(),
                c1.getPhoneNumber(),
                c1.getAge(),
                transits.get(3) == null ? null : transits.get(3).getTime(),
                transits.get(3) == null ? null : cityService.getCityName(transits.get(3).getCityId()),
                c2.getName(),
                c2.getGender(),
                c2.getPhoneNumber(),
                c2.getAge(),
                transits.get(1) == null ? null : cityService.getCityName(transits.get(1).getCityId()),
                transits.get(1) == null ? null : transits.get(1).getTax(),
                transits.get(1) == null ? null : transits.get(1).getTime(),
                transits.get(2) == null ? null : cityService.getCityName(transits.get(2).getCityId()),
                transits.get(2) == null ? null : transits.get(2).getTax(),
                transits.get(2) == null ? null : transits.get(2).getTime(),
                container.getCode(),
                container.getType(),
                shipService.getShipById(record.getShipId()),
                companyService.getName(record.getCompanyId()),
                record.getLogTime()
        );
    }
}
