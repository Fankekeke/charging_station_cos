package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DiscountInfo;
import cc.mrbird.febs.cos.dao.DiscountInfoMapper;
import cc.mrbird.febs.cos.service.IDiscountInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class DiscountInfoServiceImpl extends ServiceImpl<DiscountInfoMapper, DiscountInfo> implements IDiscountInfoService {

    /**
     * 分页获取优惠券信息
     *
     * @param page         分页对象
     * @param discountInfo 优惠券信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectDiscountPage(Page<DiscountInfo> page, DiscountInfo discountInfo) {
        return baseMapper.selectDiscountPage(page, discountInfo);
    }

    /**
     * 根据状态用户ID获取优惠券信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> queryDiscountSortByUserId(Integer userId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("notUse", Collections.emptyList());
                put("used", Collections.emptyList());
            }
        };
        List<LinkedHashMap<String, Object>> list = baseMapper.queryDiscountByUserId(userId);
        if (CollectionUtil.isEmpty(list)) {
            return result;
        }
        List<LinkedHashMap<String, Object>> notUseList = new ArrayList<>();
        List<LinkedHashMap<String, Object>> usedList = new ArrayList<>();
        for (LinkedHashMap<String, Object> mapItem : list) {
            String status = (String) mapItem.get("status");
            if (StrUtil.isEmpty(status)) {
                continue;
            }
            switch (status) {
                case "0":
                    notUseList.add(mapItem);
                    break;
                case "1":
                    usedList.add(mapItem);
                    break;
                default:
            }
        }
        result.put("notUse", notUseList);
        result.put("used", usedList);
        return result;
    }


    /**
     * 获取可用优惠券
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public List<DiscountInfo> queryUseDiscountByUserId(Integer userId, BigDecimal amount) {
        // 获取用户可用优惠券信息
        List<DiscountInfo> discountInfoList = this.list(Wrappers.<DiscountInfo>lambdaQuery().eq(DiscountInfo::getUserId, userId).eq(DiscountInfo::getStatus, "0"));
        if (CollectionUtil.isEmpty(discountInfoList)) {
            return discountInfoList;
        }
        // 过滤出不符合条件的优惠券
        List<DiscountInfo> resultList = new ArrayList<>();
        for (DiscountInfo discount : discountInfoList) {
            if ("2".equals(discount.getType())) {
                resultList.add(discount);
            } else {
                if (discount.getThreshold().compareTo(amount) > 0) {
                    continue;
                }
                resultList.add(discount);
            }
        }
        return resultList;
    }
}
