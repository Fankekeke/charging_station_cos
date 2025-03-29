package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.dao.ParkOrderInfoMapper;
import cc.mrbird.febs.cos.entity.BulletinInfo;
import cc.mrbird.febs.cos.service.IBulletinInfoService;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.PharmacyInfoMapper;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PharmacyInfoServiceImpl extends ServiceImpl<PharmacyInfoMapper, PharmacyInfo> implements IPharmacyInfoService {


    private final PharmacyInfoMapper pharmacyInfoMapper;

    private final IBulletinInfoService bulletinInfoService;

    private final ParkOrderInfoMapper orderInfoMapper;


    /**
     * 分页获取商家信息
     *
     * @param page     分页对象
     * @param pharmacyInfo 商家信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectPharmacyPage(Page<PharmacyInfo> page, PharmacyInfo pharmacyInfo) {
        return baseMapper.selectPharmacyPage(page, pharmacyInfo);
    }

    /**
     * 查询本月订单数量排行
     *
     * @param type 1.按订单数 2.按交易金额
     * @return 结果
     */
    @Override
    public List<PharmacyOrderRank> selectOrderRank(Integer type) {
        // 所有商家信息
        List<PharmacyInfo> pharmacyInfoList = this.list(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1));
        // 本月订单数据
        List<ParkOrderInfo> goodsOrderInfoList = baseMapper.selectOrderByMonth(null);
        if (CollectionUtil.isEmpty(goodsOrderInfoList) || CollectionUtil.isEmpty(pharmacyInfoList)) {
            return Collections.emptyList();
        }
        Map<Integer, List<ParkOrderInfo>> orderMap = goodsOrderInfoList.stream().collect(Collectors.groupingBy(ParkOrderInfo::getPharmacyId));
        List<PharmacyOrderRank> result = new ArrayList<>();
        pharmacyInfoList.forEach(e -> {
            PharmacyOrderRank pharmacyOrderRank = new PharmacyOrderRank(e.getId(), e.getName(), 0, BigDecimal.ZERO);
            List<ParkOrderInfo> goodsOrderInfoItemList = orderMap.get(e.getId());
            if (CollectionUtil.isNotEmpty(goodsOrderInfoItemList)) {
                pharmacyOrderRank.setOrderNum(goodsOrderInfoItemList.size());
                BigDecimal totalPrice = goodsOrderInfoItemList.stream().map(ParkOrderInfo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
                pharmacyOrderRank.setTotalPrice(totalPrice);
            }
            result.add(pharmacyOrderRank);
        });
        // 排序
        if (type == 0) {
            return result.stream().sorted(Comparator.comparing(PharmacyOrderRank::getOrderNum)).collect(Collectors.toList());
        } else {
            return result.stream().sorted(Comparator.comparing(PharmacyOrderRank::getTotalPrice)).collect(Collectors.toList());
        }
    }

    /**
     * 查询近十天内各家订单收益统计
     *
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectOrderPriceDays() {
        // 正在营业供应商
        List<PharmacyInfo> pharmacyInfoList = this.list(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1));
        if (CollectionUtil.isEmpty(pharmacyInfoList)) {
            return Collections.emptyList();
        }
        // 返回数据
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        pharmacyInfoList.forEach(e -> {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>() {
                {
                    put("name", e.getName());
                    put("value", baseMapper.selectOrderPriceWithinDays(e.getId()));
                }
            };
            result.add(item);
        });
        return result;
    }

    /**
     * 查询近十天内各家订单数量统计
     *
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectOrderNumDays() {
        // 正在营业供应商
        List<PharmacyInfo> pharmacyInfoList = this.list(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1));
        if (CollectionUtil.isEmpty(pharmacyInfoList)) {
            return Collections.emptyList();
        }
        // 返回数据
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        pharmacyInfoList.forEach(e -> {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>() {
                {
                    put("name", e.getName());
                    put("value", baseMapper.selectOrderNumWithinDays(e.getId()));
                }
            };
            result.add(item);
        });
        return result;
    }


}
