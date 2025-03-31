package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.ParkOrderInfoMapper;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkOrderInfoServiceImpl extends ServiceImpl<ParkOrderInfoMapper, ParkOrderInfo> implements IParkOrderInfoService {

    private final IUserInfoService userInfoService;
    private final IVehicleInfoService vehicleInfoService;
    private final ISpaceInfoService spaceInfoService;
    private final IBulletinInfoService bulletinInfoService;

    private final IMemberInfoService memberInfoService;

    private final IDiscountInfoService discountInfoService;

    /**
     * 分页获取订单信息
     *
     * @param page          分页对象
     * @param parkOrderInfo 订单信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<ParkOrderInfo> page, ParkOrderInfo parkOrderInfo) {
        return baseMapper.selectOrderPage(page, parkOrderInfo);
    }

    /**
     * 查询主页信息
     *
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> homeData() {
        // 管理员展示信息
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        // 本月收益
        BigDecimal incomeMonth = baseMapper.selectIncomeMonth();
        // 本月工单
        Integer workOrderMonth = baseMapper.selectWorkOrderMonth();
        // 本年收益
        BigDecimal incomeYear = baseMapper.selectIncomeYear();
        // 本年工单
        Integer workOrderYear = baseMapper.selectWorkOrderYear();
        // 客户数量
        Integer userNum = userInfoService.count();
        // 车辆数量
        Integer staffNum = vehicleInfoService.count();
        // 充电桩数量
        Integer roomNum = spaceInfoService.count();
        // 总收益
        BigDecimal amount = baseMapper.selectAmountPrice();
        // 十天内缴费记录
        List<LinkedHashMap<String, Object>> paymentRecord = baseMapper.selectPaymentRecord();
        // 十天内工单数量
        List<LinkedHashMap<String, Object>> orderRecord = baseMapper.selectOrderRecord();
        result.put("incomeMonth", incomeMonth);
        result.put("workOrderMonth", workOrderMonth);
        result.put("incomeYear", incomeYear);
        result.put("workOrderYear", workOrderYear);
        result.put("totalOrderNum", userNum);
        result.put("staffNum", staffNum);
        result.put("roomNum", roomNum);
        result.put("totalRevenue", amount);
        result.put("paymentRecord", paymentRecord);
        result.put("orderRecord", orderRecord);

        // 公告信息
        List<BulletinInfo> bulletinList = bulletinInfoService.list();
        result.put("bulletin", bulletinList);
        return result;
    }

    @Override
    public ParkOrderInfo getPriceTotal(ParkOrderInfo orderInfo) throws FebsException {
        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getId, orderInfo.getUserId()));
        if (userInfo == null) {
            throw new FebsException("用户信息错误");
        }

        orderInfo.setEndDate(DateUtil.formatDateTime(new Date()));
        orderInfo.setUseDiscount(false);

        // 获取价格规则
        SpaceInfo spaceInfo = spaceInfoService.getById(orderInfo.getSpaceId());
        orderInfo.setPrice(spaceInfo.getPrice());

        // 获取用户会员信息
        List<MemberInfo> memberInfos = memberInfoService.list(Wrappers.<MemberInfo>lambdaQuery().eq(MemberInfo::getUserId, userInfo.getId()));
        boolean isMember = false;
        if (CollectionUtil.isNotEmpty(memberInfos)) {
            for (MemberInfo memberInfo : memberInfos) {
                if (DateUtil.isIn(new Date(), DateUtil.parseDateTime(memberInfo.getStartDate()), DateUtil.parseDateTime(memberInfo.getEndDate()))) {
                    isMember = true;
                }
            }
        }

        orderInfo.setEndDate(DateUtil.formatDateTime(new Date()));
        // 充电总时常
        long totalTime = DateUtil.between(DateUtil.parseDateTime(orderInfo.getStartDate()), DateUtil.parseDateTime(orderInfo.getEndDate()), DateUnit.MINUTE);
        orderInfo.setTotalTime(BigDecimal.valueOf(totalTime));
        // 总价格
        if (isMember) {
            orderInfo.setTotalPrice(BigDecimal.ZERO);
            orderInfo.setStatus("-1");
        } else {
            BigDecimal unit = NumberUtil.div(orderInfo.getTotalTime(), new BigDecimal(60), 2);
            orderInfo.setTotalPrice(NumberUtil.mul(unit, orderInfo.getPrice()));
        }

        orderInfo.setAfterOrderPrice(orderInfo.getTotalPrice());

        // 是否有优惠券
        if (orderInfo.getDiscountId() != null && !isMember) {
            DiscountInfo discountInfo = discountInfoService.getById(orderInfo.getDiscountId());
            // 满减
            if ("2".equals(discountInfo.getType())) {
                orderInfo.setAfterOrderPrice(NumberUtil.mul(orderInfo.getTotalPrice(), NumberUtil.mul(discountInfo.getRebate(), BigDecimal.valueOf(0.1))));
                orderInfo.setDiscountAmount(NumberUtil.sub(orderInfo.getTotalPrice(), orderInfo.getAfterOrderPrice()));
            }
            // 折扣
            if ("1".equals(discountInfo.getType()) && orderInfo.getTotalPrice().compareTo(discountInfo.getThreshold()) >= 0) {
                orderInfo.setAfterOrderPrice(NumberUtil.sub(orderInfo.getTotalPrice(), discountInfo.getDiscountPrice()));
                orderInfo.setDiscountAmount(NumberUtil.sub(orderInfo.getTotalPrice(), orderInfo.getAfterOrderPrice()));
            }
        } else {
            orderInfo.setDiscountAmount(BigDecimal.ZERO);
        }

        // 判断是有可用优惠券
        List<DiscountInfo> discountInfoList = discountInfoService.list(Wrappers.<DiscountInfo>lambdaQuery().eq(DiscountInfo::getUserId, userInfo.getId()).eq(DiscountInfo::getStatus, "0"));
        if (CollectionUtil.isNotEmpty(discountInfoList)) {
            List<DiscountInfo> discount1s = discountInfoList.stream().filter(e -> "2".equals(e.getType())).collect(Collectors.toList());
            List<DiscountInfo> discount2s = discountInfoList.stream().filter(e -> "1".equals(e.getType()) && orderInfo.getTotalPrice().compareTo(e.getThreshold()) >= 0).collect(Collectors.toList());

            discount1s.addAll(discount2s);
            orderInfo.setDiscountInfos(discount1s);

            boolean discountCheck = (discountInfoList.stream().anyMatch(e -> "2".equals(e.getType())) || discountInfoList.stream().anyMatch(e -> "1".equals(e.getType()) && orderInfo.getTotalPrice().compareTo(e.getThreshold()) >= 0));
            orderInfo.setUseDiscount(discountCheck);
        } else {
            orderInfo.setDiscountInfos(Collections.emptyList());
        }
        return orderInfo;
    }

    /**
     * 数据统计
     *
     * @param checkDate 选择日期
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectStatistics(String checkDate) {
        if (StrUtil.isEmpty(checkDate)) {
            checkDate = DateUtil.formatDate(new Date());
        }

        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        // 获取当前月份及当前月份
        String year = StrUtil.toString(DateUtil.year(DateUtil.parseDate(checkDate)));
        String month = StrUtil.toString(DateUtil.month(DateUtil.parseDate(checkDate)) + 1);

        // 本月收益
        List<LinkedHashMap<String, Object>> priceByMonth = baseMapper.selectPriceByMonth(year, month, checkDate);
        result.put("priceByMonth", priceByMonth);

        // 本月订单
        List<LinkedHashMap<String, Object>> orderNumByMonth = baseMapper.selectOrderNumByMonth(year, month, checkDate);
        result.put("orderNumByMonth", orderNumByMonth);

        // 类型销量统计
        List<LinkedHashMap<String, Object>> typeOrderNumRateByMonth = baseMapper.selectTypeRateByMonth(year, month);
        result.put("typeOrderNumRateByMonth", typeOrderNumRateByMonth);

        // 类型销售统计
        List<LinkedHashMap<String, Object>> typePriceRateByMonth = baseMapper.selectTypePriceRateByMonth(year, month);
        result.put("typePriceRateByMonth", typePriceRateByMonth);

        return result;
    }
}
