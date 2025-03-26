package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.DiscountInfo;
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
public interface IDiscountInfoService extends IService<DiscountInfo> {

    /**
     * 分页获取优惠券信息
     *
     * @param page         分页对象
     * @param discountInfo 优惠券信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDiscountPage(Page<DiscountInfo> page, DiscountInfo discountInfo);

    /**
     * 根据状态用户ID获取优惠券信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    LinkedHashMap<String, Object> queryDiscountSortByUserId(Integer userId);

    /**
     * 获取可用优惠券
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<DiscountInfo> queryUseDiscountByUserId(Integer userId, BigDecimal amount);
}
