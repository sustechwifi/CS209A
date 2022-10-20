package com.example.testdemo.service;

import com.example.testdemo.component.Result;
import com.example.testdemo.component.RowRecord;
import com.example.testdemo.entity.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


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


    public void updateToExported(Transit t, int shipId, int containerId) {
        transitService.update(t);
        recordService.updateToExported(t.getRecordId(), shipId, containerId);
    }

    public void updateToUnDelivery(Transit transit) {
        transitService.update(transit);
        recordService.updateToUnDelivery(transit.getRecordId());
    }

    public void updateToFinished(Transit transit, Courier courier) {
        int cid = courierService.add(courier);
        Handle h = new Handle(4, transit.getTime(), transit.getRecordId(), cid, null);
        handleService.update(h);
        transitService.update(transit);
        recordService.updateToFinished(transit.getRecordId());
    }

    public Result<?> updateBatch(RowRecord record, int type) {
        int companyId = companyService.add(record.getCompany());
        int shipId = shipService.add(new Ship(record.getShip(), companyId, null));
        int containerId = containerService.add(new Container(record.getContainerCode(), record.getContainerType(), null));
        int c2 = cityService.add(new City(record.getExportCity(), null));
        int c3 = cityService.add(new City(record.getImportCity(), null));
        int c4 = cityService.add(new City(record.getDeliveryCity(), null));
        Courier courier = new Courier(record.getDeliveryCourierPhone(), record.getDeliveryCourier()
                , record.getDeliveryCourierAge(), record.getDeliveryCourierGender(), companyId, c4, null);
        if (type == 1) {
            List<Integer> getIds = recordService.getAllUnExported();
            for (Integer id : getIds) {
                System.out.println(id);
                recordService.updateToFinished(id);
                Transit t2 = new Transit(2, record.getExportDate(), record.getExportTax(), c2,
                        id, null);
                Transit t3 = new Transit(3, record.getImportDate(), record.getImportTax(), c3,
                        id, null);
                Transit t4 = new Transit(4, record.getDeliveryDate(), null, c4,
                        id, null);
                updateToExported(t2, shipId, containerId);
                updateToUnDelivery(t3);
                updateToFinished(t4, courier);
            }
        } else if (type == 2) {
            List<Integer> getIds = recordService.getAllExported();
            for (Integer id : getIds) {
                System.out.println(id);
                recordService.updateToFinished(id);
                Transit t3 = new Transit(3, record.getImportDate(), record.getImportTax(), c3,
                        id, null);
                Transit t4 = new Transit(4, record.getDeliveryDate(), null, c4,
                        id, null);
                updateToUnDelivery(t3);
                updateToFinished(t4, courier);
            }
        } else if (type == 3) {
            List<Integer> getIds = recordService.getAllUnFinished();
            for (Integer id : getIds) {
                System.out.println(id);
                recordService.updateToFinished(id);
                Transit t = new Transit(4, record.getDeliveryDate(), null, c4,
                        id, null);
                updateToFinished(t, courier);
            }
        } else {
            return null;
        }
        return Result.success();
    }
}
