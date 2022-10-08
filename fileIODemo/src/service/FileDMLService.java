package service;

import component.Result;
import component.RowRecord;

import java.util.List;

/**
 * File 文件的 dml 接口
 * 返回 restful 风格的数据包装
 * <p>
 * class Result<T> {
 * private String code;   响应状态：0 为正常，若抛出异常请给出状态码如，404等
 * private String msg;    附加信息：若正常执行为 运行时间，否则为 exception.getMessage()
 * private T data;        返回值结果
 * }
 */
public interface FileDMLService {

    /**
     * 根据路径存 csv
     *
     * @param path .csv文件路径
     * @return 仅返回运行时间
     */
    Result<?> insertFromFile(String path);

    /**
     * 根据路径存 csv 的前line行
     *
     * @param path .csv文件路径
     * @param line 行数
     * @return 仅返回运行时间
     */
    Result<?> insertFromFile(String path, int line);

    /**
     * 返回有效记录总数
     *
     * @param path .csv文件路径
     * @return 有效 record 的总行数
     * 调用方式：
     * int count = new FileDMLServiceImpl().getCount(path).getData();
     */
    Result<Integer> getCount(String path);


    /**
     * 从给定id 删除订单信息
     *
     * @param path .csv文件路径
     * @param ids  给定的 record_id数组
     * @return 无返回，仅需要运行时间
     */
    Result<?> deleteByRecordId(String path, Integer[] ids);


    /**
     * 用 record 中的信息按类型 批量更新记录
     * @param record 仅包含 ship,container,company,delivery courier,transit 的信息
     * @param type  三个取值 {1，2，3} 分别代表 未出口，未进口，未配送
     * @return 返回改动的 record id
     */
    Result<List<Integer>> updateBatch(RowRecord record, int type);

}
