package com.example.testdemo.service;

import com.alibaba.druid.util.StringUtils;
import com.example.testdemo.component.Result;
import com.example.testdemo.entity.Record;
import com.example.testdemo.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataInsertService {
    private static final String FORMAT = "Item Name,Item Type,Item Price,Retrieval City,Retrieval Start Time,Retrieval Courier,Retrieval Courier Gender,Retrieval Courier Phone Number,Retrieval Courier Age,Delivery Finished Time,Delivery City,Delivery Courier,Delivery Courier Gender,Delivery Courier Phone Number,Delivery Courier Age,Item Export City,Item Export Tax,Item Export Time,Item Import City,Item Import Tax,Item Import Time,Container Code,Container Type,Ship Name,Company Name,Log Time";
    private static final String DELIMITER = ",";
    private static final long M = 1024L * 1024;
    private static final int DEFAULT_THREAD_NUM = 10;
    private static final int DEFAULT_THREAD_CAPACITY = 50000;

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


    /**
     * 插入数据库的接口
     *
     * @param root 文件路径
     * @return
     */
    @Transactional
    public Result<?> insertToDataBase(String root) {
        long begin = System.currentTimeMillis();
        String line = "";
        try (BufferedReader br = Files.newBufferedReader(Paths.get(root), Charset.forName("GBK"))) {
            // 按行读取
            line = br.readLine();
            if (!line.equals(FORMAT)) {
                System.out.println("csv 文件格式不正确！");
                return Result.error("103", "csv 文件格式不正确！");
            }
            long length = new File(root).length();
            System.out.println("文件大小为" + length + "b,约合" + (length / M) + "mb");
            insertBySingleLine(br);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(line);
            return Result.error("203", e.getMessage());
        }
        long end = System.currentTimeMillis();
        System.out.println("操作用时: " + (end - begin) + "ms");
        return Result.success("操作用时: " + (end - begin) + "ms");
    }

    /**
     * 从上到下按行插入
     *
     * @param br 缓冲流
     * @throws IOException
     * @throws ParseException
     */
    public void insertBySingleLine(BufferedReader br) throws IOException, ParseException {
        String line;
        int cnt = 1;
        while ((line = br.readLine()) != null) {
            if (cnt % 10000 == 0) {
                System.out.println("当前已处理条数:" + cnt);
            }
            cnt++;
            insertByString(line);
        }
    }

    public void insertByString(String line) throws ParseException {
        String[] columns = line.split(DELIMITER);
        int[] ids = insertCommon(columns);
        if (!StringUtils.isEmpty(columns[9])) {
            insertFinished(columns, ids);
        } else if (!StringUtils.isEmpty(columns[20])) {
            insertUnDelivered(columns, ids);
        } else if (!StringUtils.isEmpty(columns[23])) {
            insertExported(columns, ids);
        } else {
            insertUnExported(columns, ids);
        }
    }

    /**
     * 插入共有的列
     *
     * @throws ParseException 空行异常
     */
    public int[] insertCommon(String[] columns) {
        final int companyId = companyService.add(columns[24]);
        final int city1Id = cityService.add(new City(columns[3], null));
        final int cityEx = cityService.add(new City(columns[15], null));
        Courier courier1 = new Courier(columns[7], columns[5], Integer.parseInt(columns[8]), "男".equals(columns[6]) ? 1 : 2, companyId, city1Id, null);
        final int c1 = courierService.add(courier1);
        return new int[]{city1Id, cityEx, c1, companyId};
    }

    /**
     * 插入未出口的订单
     *
     * @throws ParseException 空行异常
     */
    public void insertUnExported(String[] columns, int[] ids) throws ParseException {
        final int recordId = recordService.add(new Record(new Timestamp(new SimpleDateFormat("yyyy/MM/dd hh:mm").parse(columns[25]).getTime()),
                columns[0], columns[1], Long.parseLong(columns[2]), ids[3], null, null, null,1));
        handleService.add(new Handle(1, new SimpleDateFormat("yyyy/MM/dd").parse(columns[4]), recordId, ids[2], null));
        transitService.add(new Transit(1, new SimpleDateFormat("yyyy/MM/dd").parse(columns[4]), null, ids[0], recordId, null));
        transitService.add(new Transit(2, null, Double.parseDouble(columns[16]), ids[1], recordId, null));
    }

    /**
     * 插入刚出口的订单
     *
     * @throws ParseException 空行异常
     */
    public void insertExported(String[] columns, int[] ids) throws ParseException {
        cityService.add(new City(columns[10], null));
        final int cityIn = cityService.add(new City(columns[18], null));
        final int containerId = containerService.add(new Container(columns[21], columns[22], null));
        final int shipId = shipService.add(new Ship(columns[23], ids[3], null));
        final int recordId = recordService.add(new Record(new Timestamp(new SimpleDateFormat("yyyy/MM/dd hh:mm").parse(columns[25]).getTime()),
                columns[0], columns[1], Long.parseLong(columns[2]), ids[3], containerId, shipId, null,2));
        handleService.add(new Handle(1, new SimpleDateFormat("yyyy/MM/dd").parse(columns[4]), recordId, ids[2], null));
        transitService.add(new Transit(1, new SimpleDateFormat("yyyy/MM/dd").parse(columns[4]), null, ids[0], recordId, null));
        transitService.add(new Transit(2, null, Double.parseDouble(columns[16]), ids[1], recordId, null));
        transitService.add(new Transit(3, null, Double.parseDouble(columns[19]), cityIn, recordId, null));
    }

    /**
     * 插入未配送的订单
     *
     * @throws ParseException 空行异常
     */
    public void insertUnDelivered(String[] columns, int[] ids) throws ParseException {
        final int city2Id = cityService.add(new City(columns[10], null));
        final int cityIn = cityService.add(new City(columns[18], null));
        final int containerId = containerService.add(new Container(columns[21], columns[22], null));
        final int shipId = shipService.add(new Ship(columns[23], ids[3], null));
        final int recordId = recordService.add(new Record(new Timestamp(new SimpleDateFormat("yyyy/MM/dd hh:mm").parse(columns[25]).getTime()),
                columns[0], columns[1], Long.parseLong(columns[2]), ids[3], containerId, shipId, null,3));
        handleService.add(new Handle(1, new SimpleDateFormat("yyyy/MM/dd").parse(columns[4]), recordId, ids[2], null));
        transitService.add(new Transit(1, new SimpleDateFormat("yyyy/MM/dd").parse(columns[4]), null, ids[0], recordId, null));
        transitService.add(new Transit(2, new SimpleDateFormat("yyyy/MM/dd").parse(columns[17]), Double.parseDouble(columns[16]), ids[1], recordId, null));
        transitService.add(new Transit(3, new SimpleDateFormat("yyyy/MM/dd").parse(columns[20]), Double.parseDouble(columns[19]), cityIn, recordId, null));
        transitService.add(new Transit(4, null, null, city2Id, recordId, null));
    }

    /**
     * 插入已完成的订单
     *
     * @throws ParseException 空行异常
     */
    public void insertFinished(String[] columns, int[] ids) throws ParseException {
        final int city2Id = cityService.add(new City(columns[10], null));
        final int cityIn = cityService.add(new City(columns[18], null));
        final int containerId = containerService.add(new Container(columns[21], columns[22], null));
        final int shipId = shipService.add(new Ship(columns[23], ids[3], null));
        Courier courier2 = new Courier(columns[13], columns[11], Integer.parseInt(columns[14]), "男".equals(columns[12]) ? 1 : 2, ids[3], city2Id, null);
        final int c2 = courierService.add(courier2);
        final int recordId = recordService.add(new Record(new Timestamp(new SimpleDateFormat("yyyy/MM/dd hh:mm").parse(columns[25]).getTime()),
                columns[0], columns[1], Long.parseLong(columns[2]), ids[3], containerId, shipId, null,4));
        handleService.add(new Handle(1, new SimpleDateFormat("yyyy/MM/dd").parse(columns[4]), recordId, ids[2], null));
        handleService.add(new Handle(4, new SimpleDateFormat("yyyy/MM/dd").parse(columns[9]), recordId, c2, null));
        transitService.add(new Transit(1, new SimpleDateFormat("yyyy/MM/dd").parse(columns[4]), null, ids[0], recordId, null));
        transitService.add(new Transit(2, new SimpleDateFormat("yyyy/MM/dd").parse(columns[17]), Double.parseDouble(columns[16]), ids[1], recordId, null));
        transitService.add(new Transit(3, new SimpleDateFormat("yyyy/MM/dd").parse(columns[20]), Double.parseDouble(columns[19]), cityIn, recordId, null));
        transitService.add(new Transit(4, new SimpleDateFormat("yyyy/MM/dd").parse(columns[9]), null, city2Id, recordId, null));

    }


    @Transactional
    public synchronized Result<?> insertToDataBaseByThread(String path) {
        long begin = System.currentTimeMillis();
        String line = "";
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path), Charset.forName("GBK"))) {
            line = br.readLine();
            if (!line.equals(FORMAT)) {
                System.out.println("csv 文件格式不正确！");
                return Result.error("103", "csv 文件格式不正确！");
            }
            long length = new File(path).length();
            System.out.println("文件大小为" + length + "b,约合" + (length / M) + "mb");
            List<String> list = new ArrayList<>(DEFAULT_THREAD_CAPACITY);
            List<MyThread> threads = new ArrayList<>();
            int cnt = 0;
            while ((line = br.readLine()) != null) {
                list.add(line);
                if (list.size() == DEFAULT_THREAD_CAPACITY) {
                    MyThread thread = new MyThread(new ArrayList<>(list), this, ++cnt);
                    thread.start();
                    list.clear();
                    threads.add(thread);
                }
            }
            for (MyThread thread : threads) {
                thread.join();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(line);
            return Result.error("203", e.getMessage());
        }
        long end = System.currentTimeMillis();
        System.out.println("操作用时: " + (end - begin) + "ms");
        return Result.success("操作用时: " + (end - begin) + "ms");
    }

    @Transactional
    public Result<?> deleteAll() {
        long start = System.currentTimeMillis();
        transitService.deleteAll();
        handleService.deleteAll();
        recordService.deleteAll();
        courierService.deleteAll();
        shipService.deleteAll();
        companyService.deleteAll();
        cityService.deleteAll();
        containerService.deleteAll();
        long end = System.currentTimeMillis();
        return Result.success("用时：" + (end - start) + "ms");
    }

}

class MyThread extends Thread {
    DataInsertService dataInsertService;
    List<String> recordsList;
    int threadNum;

    public MyThread(List<String> recordsList, DataInsertService dataInsertService, int threadNum) {
        this.recordsList = recordsList;
        this.dataInsertService = dataInsertService;
        this.threadNum = threadNum;
    }

    @Override
    public void run() {
        for (String s : recordsList) {
            try {
                dataInsertService.insertByString(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("线程%d已结束，处理数据量%d\n", threadNum, recordsList.size());
    }
}