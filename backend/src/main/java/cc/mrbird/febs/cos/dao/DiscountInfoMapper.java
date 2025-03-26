package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.DiscountInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface DiscountInfoMapper extends BaseMapper<DiscountInfo> {

    /**
     * 分页获取优惠券信息
     *
     * @param page         分页对象
     * @param discountInfo 优惠券信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDiscountPage(Page<DiscountInfo> page, @Param("discountInfo") DiscountInfo discountInfo);

    /**
     * 根据用户ID获取优惠券信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> queryDiscountByUserId(@Param("userId") Integer userId);
}
