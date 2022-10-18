package com.example.testdemo.contorller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.example.testdemo.component.Result;
import com.example.testdemo.fileIO.*;
import com.example.testdemo.service.DataInsertService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    DataInsertService dataInsertService;

    /*状态  检索     出关      入关     配送   备注
     * 未出关  1       0       0        0    没有ship,container
     * 未入关  1       1       0        0    没有import time
     * 未配送  1       1       1        0    没有deliver快递员
     * 已完成  1       1       1        1
     */

    @CrossOrigin
    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID();
        String root = System.getProperty("user.dir") + "\\testDemo\\files\\" + flag + "_" + originalFilename;
        try {
            FileUtil.writeBytes(file.getBytes(), root);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("201", e.getMessage());
        }
        return dataInsertService.insertToDataBase(root);
    }

    @PostMapping("/uploadLocal")
    public Result<?> uploadLocal(@RequestBody UploadBody body) {
        System.out.println("请等待---");
        if (body.isThread()) {
            System.out.println("已开启多线程插入");
            return dataInsertService.insertToDataBaseByThread(body.getPath());
        } else {
            return dataInsertService.insertToDataBase(body.getPath());
        }
    }

    @DeleteMapping("/deleteAll")
    public Result<?> deleteAll() {
        return dataInsertService.deleteAll();
    }


    @GetMapping("/uploadByBatch")
    public Result<?> insertByBatch() {
        long a = System.currentTimeMillis();
        try {
            DBLoaderMain db = (DBLoaderMain)
                    Class.forName("com.example.testdemo.fileIO.DBLoaderMain")
                            .getDeclaredConstructor().newInstance();
            db.dataInsert();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long b = System.currentTimeMillis();
        System.out.println("用时:" + (b - a) + "ms");
        return Result.success();
    }

    @GetMapping("/fileIO")
    public Result<String> addToFile() {
        long a = System.currentTimeMillis();
        FileLoaderMain fm = new FileLoaderMain();
        fm.main();
        long b = System.currentTimeMillis();
        return Result.success("time cost:" + (b - a) + "ms");
    }

    @GetMapping("/find")
    public Result<String> query(@RequestParam String itemName) {
        long a = System.currentTimeMillis();
        FileOperation fm = new FileOperation();
        try {
            fm.queryTest(itemName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long b = System.currentTimeMillis();
        System.out.println("time cost:" + (b - a) + "ms");
        return Result.success("time cost:" + (b - a) + "ms");
    }

    @GetMapping("/dml")
    public Result<String> dmlTest() {
        long a = System.currentTimeMillis();
        FileOperationTest fm = new FileOperationTest();
        try {
            fm.main();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long b = System.currentTimeMillis();
        return Result.success("time cost:" + (b - a) + "ms");
    }

    @GetMapping("/task1")
    public Result<String> task1() {
        System.out.println("begin");
        long a = System.currentTimeMillis();
        AdvancedFileQuery a1 = new AdvancedFileQuery();
        try {
            a1.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        a1.advancedQuery1();
        long b = System.currentTimeMillis();
        System.out.println("time cost:" + (b - a) + "ms");
        return Result.success("time cost:" + (b - a) + "ms");
    }

    @GetMapping("/task2")
    public Result<String> task2() {
        System.out.println("begin");
        long a = System.currentTimeMillis();
        AdvancedFileQuery a2 = new AdvancedFileQuery();
        try {
            a2.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        a2.advancedQuery2();
        long b = System.currentTimeMillis();
        System.out.println("time cost:" + (b - a) + "ms");
        return Result.success("time cost:" + (b - a) + "ms");
    }

    @GetMapping("/task3")
    public Result<String> task3() {
        System.out.println("begin");
        long a = System.currentTimeMillis();
        AdvancedFileQuery a3 = new AdvancedFileQuery();
        try {
            a3.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        a3.advancedQuery2();
        long b = System.currentTimeMillis();
        System.out.println("time cost:" + (b - a) + "ms");
        return Result.success("time cost:" + (b - a) + "ms");
    }

}

class UploadBody {
    private String path;
    private boolean thread;

    public String getPath() {
        return path;
    }

    public boolean isThread() {
        return thread;
    }
}