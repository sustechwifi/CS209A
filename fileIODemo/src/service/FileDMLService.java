package service;

import component.Result;
import entity.*;

import java.util.List;

/**
 * File 文件的 dml 接口
 * 返回 restful 风格的数据包装
 *
 * class Result<T> {
 *     private String code;   响应状态：0 为正常，若抛出异常请给出状态码如，404等
 *     private String msg;    附加信息：若正常执行为 运行时间，否则为 exception.getMessage()
 *     private T data;        返回值结果
 *}
 */
public interface FileDMLService {

    /**
     * 根据路径存 csv
     * @param path .csv文件路径
     * @return 仅返回运行时间
     */
    Result<?> insertFromFile(String path);

    /**
     * 根据路径存 csv 的前line行
     * @param path .csv文件路径
     * @param line 行数
     * @return 仅返回运行时间
     */
    Result<?> insertFromFile(String path,int line);

    /**
     * 返回有效记录总数
     * @param path .csv文件路径
     * @return  有效 record 的总行数
     * 调用方式：
     * int count = new FileDMLServiceImpl().getCount(path).getData();
     */
    Result<Integer> getCount(String path);



    /**
     * 从给定id 删除订单信息
     * @param path .csv文件路径
     * @param ids 给定的 record_id数组
     * @return 无返回，仅需要运行时间
     */
    Result<?> deleteByRecordId(String path, Integer[] ids);



    /**
     * 将所有未出口的订单 用 ship, container, transit的信息 更新为已出口
     * @param path .csv文件路径
     * @param ship 给定船只  包含 name 的信息
     * @param container 给定的集装箱  包含 code,type 的信息
     * @param transit    包含出口时间，出口城市，关税的信息
     * @return 返回改动的所有  record_id，并更新 logTime的时间为当前时间
     */
    Result<List<Integer>> updateUnExported(String path, Ship ship, Container container, Transit transit);



    /**
     * 将所有未出口的订单 用 ship, container, transit的信息 更新为已出口
     * @param path .csv文件路径
     * @param transit    包含进口时间，进口城市，关税的信息
     * @return 返回改动的所有  record_id，并更新 logTime的时间为当前时间
     */
    Result<List<Integer>> updateExported(String path, Transit transit);


    /**
     * 将所有未进口的订单 用transit中的信息
     * @param path  .csv文件路径
     * @param courier 配送快递员个人信息
     * @param transit 配送完成时间，城市
     * @return 返回改动的所有  record_id，并更新 logTime的时间为当前时间
     */
    Result<List<Integer>> updateUnDelivery(String path, Courier courier, Transit transit);

}
