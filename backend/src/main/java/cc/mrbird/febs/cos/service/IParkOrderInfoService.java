package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.entity.ParkOrderInfo;
import cc.mrbird.febs.cos.entity.PharmacyInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface IParkOrderInfoService extends IService<ParkOrderInfo> {

    /**
     * 分页获取订单信息
     *
     * @param page          分页对象
     * @param parkOrderInfo 订单信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<ParkOrderInfo> page, ParkOrderInfo parkOrderInfo);

    /**
     * 获取订单列表
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> queryOrderList(Integer userId);

    /**
     * 查询主页信息
     *
     * @return 结果
     */
    LinkedHashMap<String, Object> homeData();

    /**
     * 获取订单付款信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    ParkOrderInfo getPriceTotal(ParkOrderInfo orderInfo) throws FebsException;

    /**
     * 数据统计
     *
     * @param checkDate 选择日期
     * @return 结果
     */
    LinkedHashMap<String, Object> selectStatistics(String checkDate);

    /**
     * 员工获取推荐订单
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 结果
     */
    List<PharmacyInfo> queryOrderRecommend(BigDecimal longitude, BigDecimal latitude);

    /**
     * 根据用户ID获取主页信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    LinkedHashMap<String, Object> queryHomeByUserId(BigDecimal longitude, BigDecimal latitude, Integer userId);
}
