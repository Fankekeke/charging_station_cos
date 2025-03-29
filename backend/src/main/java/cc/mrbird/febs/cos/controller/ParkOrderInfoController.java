package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/park-order-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkOrderInfoController {

    private final IParkOrderInfoService parkOrderInfoService;

    private final IUserInfoService userInfoService;

    private final IMemberInfoService memberInfoService;

    private final ISpaceInfoService spaceInfoService;

    private final IReserveInfoService reserveInfoService;

    private final ISpaceStatusInfoService spaceStatusInfoService;

    private final IMailService mailService;

    private final TemplateEngine templateEngine;

    private final IMessageInfoService messageInfoService;

    private final IVehicleInfoService vehicleInfoService;

    private final INotifyInfoService notifyInfoService;

    /**
     * 分页获取订单信息
     *
     * @param page          分页对象
     * @param parkOrderInfo 订单信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<ParkOrderInfo> page, ParkOrderInfo parkOrderInfo) {
        return R.ok(parkOrderInfoService.selectOrderPage(page, parkOrderInfo));
    }

    /**
     * 订单驶出结算
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    @GetMapping("/order/over")
    @Transactional(rollbackFor = Exception.class)
    public R orderOver(String orderCode) {
        // 获取订单信息
        ParkOrderInfo orderInfo = parkOrderInfoService.getOne(Wrappers.<ParkOrderInfo>lambdaQuery().eq(ParkOrderInfo::getCode, orderCode));

        // 车辆信息
        VehicleInfo vehicleInfo = vehicleInfoService.getById(orderInfo.getVehicleId());

        // 获取用户会员信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getId, vehicleInfo.getUserId()));
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
            orderInfo.setStatus("1");
        } else {
            BigDecimal unit = NumberUtil.div(orderInfo.getTotalTime(), new BigDecimal(60), 2);
            orderInfo.setTotalPrice(NumberUtil.mul(unit, orderInfo.getPrice()));
        }

        // 发送消息
        NotifyInfo notifyInfo = new NotifyInfo();
        notifyInfo.setUserId(userInfo.getId());
        notifyInfo.setContent("您好，您的充电已结束，请前往订单缴费");
        notifyInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        notifyInfoService.save(notifyInfo);

        if (StrUtil.isNotEmpty(userInfo.getEmail())) {
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", userInfo.getName() + " 您好，您的车辆 " + vehicleInfo.getVehicleNumber() + "已驶出，请前往订单缴费");
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(userInfo.getEmail(), DateUtil.formatDate(new Date()) + "充电桩订单提示", emailContent);
        }

        // 充电桩状态变更
        SpaceStatusInfo spaceStatusInfo = spaceStatusInfoService.getOne(Wrappers.<SpaceStatusInfo>lambdaQuery().eq(SpaceStatusInfo::getSpaceId, orderInfo.getSpaceId()));
        spaceStatusInfo.setStatus("0");
        spaceStatusInfoService.updateById(spaceStatusInfo);
        return R.ok(parkOrderInfoService.updateById(orderInfo));
    }

    /**
     * 查询主页信息
     *
     * @return 结果
     */
    @GetMapping("/home/data")
    public R homeData() {
        return R.ok(parkOrderInfoService.homeData());
    }

    /**
     * 数据统计
     *
     * @param checkDate 选择日期
     * @return 结果
     */
    @GetMapping("/statistics")
    public R selectRoomStatistics(@RequestParam(value = "checkDate", required = false) String checkDate) {
        return R.ok(parkOrderInfoService.selectStatistics(checkDate));
    }

    /**
     * 订单信息详情
     *
     * @param id 订单ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(parkOrderInfoService.getById(id));
    }

    /**
     * 订单信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(parkOrderInfoService.list());
    }

    /**
     * 新增订单信息
     *
     * @param parkOrderInfo 订单信息
     * @return 结果
     */
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public R save(ParkOrderInfo parkOrderInfo) throws FebsException {
        // 校验车辆是否可以添加订单
        List<ParkOrderInfo> orderInfoList = parkOrderInfoService.list(Wrappers.<ParkOrderInfo>lambdaQuery().eq(ParkOrderInfo::getVehicleId, parkOrderInfo.getVehicleId()).eq(ParkOrderInfo::getStatus, "0"));
        if (CollectionUtil.isNotEmpty(orderInfoList)) {
            throw new FebsException("此车辆正在充电中或有未缴费订单");
        }

        // 是否正在预约中
        List<ReserveInfo> reserveInfoList = reserveInfoService.list(Wrappers.<ReserveInfo>lambdaQuery().eq(ReserveInfo::getVehicleId, parkOrderInfo.getVehicleId()).eq(ReserveInfo::getStatus, "1"));
        if (CollectionUtil.isNotEmpty(reserveInfoList)) {
            List<Integer> ids = reserveInfoList.stream().map(ReserveInfo::getId).collect(Collectors.toList());

            List<Integer> spaceIds = reserveInfoList.stream().map(ReserveInfo::getSpaceId).collect(Collectors.toList());
            reserveInfoService.update(Wrappers.<ReserveInfo>lambdaUpdate().set(ReserveInfo::getStatus, "0").in(ReserveInfo::getId, ids));
            // 更新预约充电桩状态
            spaceStatusInfoService.update(Wrappers.<SpaceStatusInfo>lambdaUpdate().set(SpaceStatusInfo::getStatus, "0").in(SpaceStatusInfo::getSpaceId, spaceIds));
        }

        // 充电桩信息
        SpaceInfo spaceInfo = spaceInfoService.getById(parkOrderInfo.getSpaceId());

        // 更新充电桩状态
        spaceStatusInfoService.update(Wrappers.<SpaceStatusInfo>lambdaUpdate().set(SpaceStatusInfo::getStatus, "1").eq(SpaceStatusInfo::getSpaceId, spaceInfo.getId()));

        // 车辆信息
        VehicleInfo vehicleInfo = vehicleInfoService.getById(parkOrderInfo.getVehicleId());
        // 用户信息
        UserInfo userInfo = userInfoService.getById(vehicleInfo.getUserId());

        // 发送消息
        NotifyInfo notifyInfo = new NotifyInfo();
        notifyInfo.setUserId(userInfo.getId());
        notifyInfo.setContent("您好，您的订单 “" + parkOrderInfo.getCode() + "”已生成，请注意查看");
        notifyInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        notifyInfoService.save(notifyInfo);

        if (StrUtil.isNotEmpty(userInfo.getEmail())) {
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", userInfo.getName() + " 您好。您的车辆 " + vehicleInfo.getVehicleNumber() + " 订单已生成，请注意查看");
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(userInfo.getEmail(), DateUtil.formatDate(new Date()) + "充电桩订单提示", emailContent);
        }

        // 订单信息
        parkOrderInfo.setCode("ORD-" + System.currentTimeMillis());
        parkOrderInfo.setPrice(spaceInfo.getPrice());
        parkOrderInfo.setStatus("0");
        parkOrderInfo.setStartDate(DateUtil.formatDateTime(new Date()));
        return R.ok(parkOrderInfoService.save(parkOrderInfo));
    }

    /**
     * 修改订单信息
     *
     * @param parkOrderInfo 订单信息
     * @return 结果
     */
    @PutMapping
    public R edit(ParkOrderInfo parkOrderInfo) {
        return R.ok(parkOrderInfoService.updateById(parkOrderInfo));
    }

    /**
     * 删除订单信息
     *
     * @param ids ids
     * @return 订单信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(parkOrderInfoService.removeByIds(ids));
    }
}
