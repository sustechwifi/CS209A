package service;

import component.PageBean;
import component.Result;
import component.RowRecord;
import entity.*;
import entity.Record;

import java.util.List;

/**
 * 文件查询的 FileIO接口
 * 返回 restful 风格的数据包装
 *
 * class Result<T> {
 *     private String code;   响应状态：0 为正常，若抛出异常请给出状态码如，404等
 *     private String msg;    附加信息：若正常执行为 运行时间，否则为 exception.getMessage()
 *     private T data;        返回值结果
 *}
 */
public interface FileDQLService {

    /**
     * 查询item的所在记录信息，为了简化问题，保证 record的 item_name 不为空
     * 此方法主要作为 一般任务 和sql的对比演示
     * @param path .csv 文件路径
     * @param record 订单相关信息
     * @return 返回整行信息，和运行时间
     */
    Result<RowRecord> getRowRecordsByRecord(String path, Record record);

    /**
     * 根据 时间，城市，关税等信息查找订单
     * 此方法主要作为 一般任务 和sql的对比演示
     * @param path
     * @param transit 订单相关信息，简单起见，保证 time 不为空,注意 type可以有多种类型，其他字段可能为空
     * @param begin 分页查询的起始页
     * @param pageSize 分页查询的页大小
     * @return 返回PageBean<List<RowRecord>> 包装对象以及运行时间
     */
    Result<PageBean<List<RowRecord>>> getRowRecordsByDate(String path,Transit transit,int begin,int pageSize);


    /**
     * 找到所有 未出口的订单id，（即ship,和 container为空的订单）
     * 此方法主要为 dml的语句使用，不上传到前端
     * @param path .csv 文件路径
     * @return ids
     */
    List<Integer> getAllUnExported(String path);
    List<Integer> getAllExported(String path);
    List<Integer> getAllUnDelivery(String path);




    /**
     * 以下是高级任务
     * 1.找到给定集装箱服务了几年
     * 2.找到 给定公司名下 在给定城市里 派件最多的快递员
     * 3.找到 城市 使得 总出口成本最低 (?)
     */

    /**
     * 找到指定集装箱 的服务时间
     * @param path
     * @param container 可以由 type 或 code 指定
     * @return 对应集装箱 的 服务时间
     */
    Result<?> getTimeServedBy(String path, Container container);

    /**
     * 找到 给定公司名下 在给定城市里 派件最多的快递员
     * @param path
     * @param company
     * @param city
     * @return
     */
    Result<Courier> getCourier(String path, Company company, City city);

    /**
     * 找到 城市 使得 总出口成本最低 (?)
     * @param path
     * @return
     */
    Result<City> getCity(String path);

}
