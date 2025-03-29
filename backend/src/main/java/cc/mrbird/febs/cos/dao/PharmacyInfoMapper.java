package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.ParkOrderInfo;
import cc.mrbird.febs.cos.entity.PharmacyInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface PharmacyInfoMapper extends BaseMapper<PharmacyInfo> {

    /**
     * 分页获取商家信息
     *
     * @param page     分页对象
     * @param pharmacyInfo 商家信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectPharmacyPage(Page<PharmacyInfo> page, @Param("pharmacyInfo") PharmacyInfo pharmacyInfo);

    /**
     * 本月订单信息
     *
     * @param merchantId 商家ID
     * @return 结果
     */
    List<ParkOrderInfo> selectOrderByMonth(@Param("merchantId") Integer merchantId);

    /**
     * 十天内订单数量统计
     *
     * @param merchantId 商家ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectOrderNumWithinDays(@Param("merchantId") Integer merchantId);

    /**
     * 十天内订单收益统计
     *
     * @param merchantId 商家ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectOrderPriceWithinDays(@Param("merchantId") Integer merchantId);
}
