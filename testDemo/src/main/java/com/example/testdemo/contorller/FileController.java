package com.example.testdemo.contorller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.example.testdemo.component.Result;
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
    public Result<?> uploadLocal(@RequestBody String path) {
        System.out.println("请等待---");
        return dataInsertService.insertToDataBase(path);
    }

    @DeleteMapping("/deleteAll")
    public Result<?> deleteAll() {
        return dataInsertService.deleteAll();
    }

}
