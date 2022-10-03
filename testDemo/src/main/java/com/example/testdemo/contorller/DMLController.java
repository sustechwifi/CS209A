package com.example.testdemo.contorller;

import com.example.testdemo.component.Result;
import com.example.testdemo.service.DeleteService;
import com.example.testdemo.service.RecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;

@RestController
@RequestMapping("/dml")
public class DMLController {

    @Resource
    DeleteService deleteService;

    @Resource
    RecordService recordService;

    @PostMapping("/deleteByIds")
    public Result<?> deleteByRecords(@RequestBody Integer[] ids) {
        System.out.println(Arrays.toString(ids));
        return deleteService.deleteByRecordIds(ids);
    }

    @GetMapping("/")
    public Result<?> getCount() {
        return recordService.getCount();
    }
}
