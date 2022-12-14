package com.example.testdemo.contorller;

import com.example.testdemo.component.Result;
import com.example.testdemo.component.RowRecord;
import com.example.testdemo.service.DataInsertService;
import com.example.testdemo.service.DeleteService;
import com.example.testdemo.service.RecordService;
import com.example.testdemo.service.UpdateService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @Resource
    DataInsertService dataInsertService;

    @Resource
    UpdateService updateService;

    @PostMapping("/deleteByIds")
    public Result<?> deleteByRecords(@RequestBody Integer[] ids) {
        long a = System.currentTimeMillis();
        System.out.println(Arrays.toString(ids));
        if (ids.length == 0) {
            return Result.error("203", "no id selected");
        }
        long b = System.currentTimeMillis();
        System.out.println("用时：" + (b - a) + "ms");
        return deleteService.deleteByRecordIds(ids);
    }

    @PostMapping("/insert")
    public Result<?> insert(@RequestBody InsertBody body) {
        System.out.println(body);
        return dataInsertService.insertByRange(body.getInsertPath(), body.getLineStart(), body.getLineEnd());
    }

    @PostMapping("/updateBatch/{type}")
    public Result<?> updateBatch(@RequestBody RowRecord record, @PathVariable("type") int type) {
        System.out.println(record);
        long a = System.currentTimeMillis();
        record.setRecordId(45498);
        if (type == 0) {
            return Result.error("505", "请选择检索类型");
        }
        System.out.println("更新用时" + (System.currentTimeMillis() - a) + "ms");
        return updateService.updateBatch(record, type);
    }

    @GetMapping("/")
    public Result<?> getCount() {
        return recordService.getCount();
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class InsertBody {
    private String insertPath;
    private int lineStart;
    private int lineEnd;

    @Override
    public String toString() {
        return "InsertBody{" +
                "insertPath='" + insertPath + '\'' +
                ", lineStart=" + lineStart +
                ", lineEnd=" + lineEnd +
                '}';
    }
}