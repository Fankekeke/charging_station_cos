package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/reserve-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReserveInfoController {

    private final IReserveInfoService reserveInfoService;

    private final ISpaceStatusInfoService spaceStatusInfoService;

    private final IParkOrderInfoService parkOrderInfoService;

    private final IMailService mailService;

    private final TemplateEngine templateEngine;

    private final IMessageInfoService messageInfoService;

    private final IVehicleInfoService vehicleInfoService;

    private final IUserInfoService userInfoService;

    private final INotifyInfoService notifyInfoService;

    /**
     * 分页获取充电桩预约信息
     *
     * @param page        分页对象
     * @param reserveInfo 充电桩预约信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<ReserveInfo> page, ReserveInfo reserveInfo) {
        return R.ok(reserveInfoService.selectReservePage(page, reserveInfo));
    }

    /**
     * 充电桩预约信息详情
     *
     * @param id 充电桩预约ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(reserveInfoService.getById(id));
    }

    /**
     * 充电桩预约信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(reserveInfoService.list());
    }

    /**
     * 新增充电桩预约信息
     *
     * @param reserveInfo 充电桩预约信息
     * @return 结果
     */
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public R save(ReserveInfo reserveInfo) throws FebsException {
        // 判断此车辆是否可预约
        List<ParkOrderInfo> orderInfoList = parkOrderInfoService.list(Wrappers.<ParkOrderInfo>lambdaQuery().eq(ParkOrderInfo::getVehicleId, reserveInfo.getVehicleId()).eq(ParkOrderInfo::getStatus, "0"));
        if (CollectionUtil.isNotEmpty(orderInfoList)) {
            throw new FebsException("此车辆正在充电中或有未缴费订单");
        }

        // 是否正在预约中
        List<ReserveInfo> reserveInfoList = reserveInfoService.list(Wrappers.<ReserveInfo>lambdaQuery().eq(ReserveInfo::getVehicleId, reserveInfo.getVehicleId()).eq(ReserveInfo::getStatus, "1"));
        if (CollectionUtil.isNotEmpty(reserveInfoList)) {
            throw new FebsException("此车辆已预约");
        }

        reserveInfo.setStartDate(DateUtil.formatDateTime(new Date()));
        // 预约结束时间
        reserveInfo.setEndDate(DateUtil.formatDateTime(DateUtil.offsetMinute(new Date(), 30)));
        reserveInfo.setStatus("1");

        // 充电桩状态更新
        SpaceStatusInfo statusInfo = spaceStatusInfoService.getOne(Wrappers.<SpaceStatusInfo>lambdaQuery().eq(SpaceStatusInfo::getSpaceId, reserveInfo.getSpaceId()));
        statusInfo.setStatus("-1");
        spaceStatusInfoService.updateById(statusInfo);

        // 车辆信息
        VehicleInfo vehicleInfo = vehicleInfoService.getById(reserveInfo.getVehicleId());

        // 用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getId, vehicleInfo.getUserId()));

        // 发送消息
        NotifyInfo notifyInfo = new NotifyInfo();
        notifyInfo.setUserId(userInfo.getId());
        notifyInfo.setContent("您好，您预定充电桩已预定成功，截至"+reserveInfo.getEndDate()+"失效");
        notifyInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        notifyInfoService.save(notifyInfo);

        if (StrUtil.isNotEmpty(userInfo.getEmail())) {
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", userInfo.getName() + " 您好，您的"+vehicleInfo.getVehicleNumber()+"预定充电桩已预定成功，截至"+reserveInfo.getEndDate()+"失效");
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(userInfo.getEmail(), DateUtil.formatDate(new Date()) + "预定提示", emailContent);
        }

        return R.ok(reserveInfoService.save(reserveInfo));
    }

    /**
     * 修改充电桩预约信息
     *
     * @param reserveInfo 充电桩预约信息
     * @return 结果
     */
    @PutMapping
    public R edit(ReserveInfo reserveInfo) {
        return R.ok(reserveInfoService.updateById(reserveInfo));
    }

    /**
     * 删除充电桩预约信息
     *
     * @param ids ids
     * @return 充电桩预约信息
     */
    @DeleteMapping("/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        // 获取预约信息
        List<ReserveInfo> reserveInfoList = (List<ReserveInfo>) reserveInfoService.listByIds(ids);
        for (ReserveInfo reserveInfo : reserveInfoList) {
            if (DateUtil.isIn(new Date(), DateUtil.parseDateTime(reserveInfo.getStartDate()), DateUtil.parseDateTime(reserveInfo.getEndDate()))) {
                spaceStatusInfoService.update(Wrappers.<SpaceStatusInfo>lambdaUpdate().set(SpaceStatusInfo::getStatus, 0).eq(SpaceStatusInfo::getId, reserveInfo.getSpaceId()));
            }
        }
        return R.ok(reserveInfoService.removeByIds(ids));
    }
}
