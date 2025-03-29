package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/space-status-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpaceStatusInfoController {

    private final ISpaceStatusInfoService spaceStatusInfoService;

    private final IReserveInfoService reserveInfoService;

    private final IMailService mailService;

    private final TemplateEngine templateEngine;

    private final IMessageInfoService messageInfoService;

    private final IVehicleInfoService vehicleInfoService;

    private final IUserInfoService userInfoService;

    private final INotifyInfoService notifyInfoService;

    /**
     * 分页获取充电桩状态信息
     *
     * @param page            分页对象
     * @param spaceStatusInfo 充电桩状态信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<SpaceStatusInfo> page, SpaceStatusInfo spaceStatusInfo) {
        return R.ok(spaceStatusInfoService.selectSpacePage(page, spaceStatusInfo));
    }

    /**
     * 获取充电桩状态图
     *
     * @return 结果
     */
    @GetMapping("/status/list")
    public R selectStatusCheck() {
        return R.ok(spaceStatusInfoService.selectStatusCheck());
    }

    /**
     * 充电桩状态信息详情
     *
     * @param id 充电桩状态ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(spaceStatusInfoService.getById(id));
    }

    /**
     * 充电桩状态信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(spaceStatusInfoService.list());
    }

    /**
     * 新增充电桩状态信息
     *
     * @param spaceStatusInfo 充电桩状态信息
     * @return 结果
     */
    @PostMapping
    public R save(SpaceStatusInfo spaceStatusInfo) {
        return R.ok(spaceStatusInfoService.save(spaceStatusInfo));
    }

    /**
     * 修改充电桩状态信息
     *
     * @param spaceStatusInfo 充电桩状态信息
     * @return 结果
     */
    @PutMapping
    public R edit(SpaceStatusInfo spaceStatusInfo) {
        return R.ok(spaceStatusInfoService.updateById(spaceStatusInfo));
    }

    /**
     * 删除充电桩状态信息
     *
     * @param ids ids
     * @return 充电桩状态信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(spaceStatusInfoService.removeByIds(ids));
    }

    /**
     * 定时任务30秒执行 状态更新
     */
    @Scheduled(fixedRate = 30000)
    public void scheduledVehicleStatusTask() {
        // 获取当前充电桩预约状态
        List<ReserveInfo> reserveInfos = reserveInfoService.list(Wrappers.<ReserveInfo>lambdaQuery().eq(ReserveInfo::getStatus,"1"));
        System.out.println("======== 状态更新");
        for (ReserveInfo reserveInfo : reserveInfos) {
            if (!DateUtil.isIn(new Date(), DateUtil.parseDateTime(reserveInfo.getStartDate()), DateUtil.parseDateTime(reserveInfo.getEndDate()))) {
                spaceStatusInfoService.update(Wrappers.<SpaceStatusInfo>lambdaUpdate().set(SpaceStatusInfo::getStatus, 0).eq(SpaceStatusInfo::getId, reserveInfo.getSpaceId()));
                reserveInfo.setStatus("0");
                reserveInfoService.updateById(reserveInfo);

                // 车辆信息
                VehicleInfo vehicleInfo = vehicleInfoService.getById(reserveInfo.getVehicleId());

                // 用户信息
                UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getId, vehicleInfo.getUserId()));
                // 发送消息
                NotifyInfo notifyInfo = new NotifyInfo();
                notifyInfo.setUserId(userInfo.getId());
                notifyInfo.setContent("您好，您的预约订单超过30分钟预定充电桩失效");
                notifyInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
                notifyInfoService.save(notifyInfo);

                if (StrUtil.isNotEmpty(userInfo.getEmail())) {
                    Context context = new Context();
                    context.setVariable("today", DateUtil.formatDate(new Date()));
                    context.setVariable("custom", userInfo.getName() + " 您好，您的"+vehicleInfo.getVehicleNumber()+"超过30分钟预定充电桩失效");
                    String emailContent = templateEngine.process("registerEmail", context);
                    mailService.sendHtmlMail(userInfo.getEmail(), DateUtil.formatDate(new Date()) + "预定提示", emailContent);
                }
            }
        }
    }
}
