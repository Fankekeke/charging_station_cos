package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.entity.PharmacyInfo;
import cc.mrbird.febs.cos.entity.PharmacyOrderRank;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IPharmacyInfoService extends IService<PharmacyInfo> {

    /**
     * 分页获取商家信息
     *
     * @param page         分页对象
     * @param pharmacyInfo 商家信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectPharmacyPage(Page<PharmacyInfo> page, PharmacyInfo pharmacyInfo);

    /**
     * 查询本月订单数量排行
     *
     * @param type 1.按订单数 2.按交易金额
     * @return 结果
     */
    List<PharmacyOrderRank> selectOrderRank(Integer type);

    /**
     * 查询近十天内各家订单收益统计
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectOrderPriceDays();

    /**
     * 查询近十天内各家订单数量统计
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectOrderNumDays();

}
