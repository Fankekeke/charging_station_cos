package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.ParkOrderInfo;
import cc.mrbird.febs.cos.entity.PharmacyInfo;
import cc.mrbird.febs.cos.service.IParkOrderInfoService;
import cc.mrbird.febs.cos.service.IPharmacyInfoService;
import cc.mrbird.febs.system.service.UserService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/stock/pharmacy-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PharmacyInfoController {

    private final IPharmacyInfoService pharmacyInfoService;

    private final UserService userService;

    private final IParkOrderInfoService orderInfoService;

    /**
     * 分页获取商家信息
     *
     * @param page         分页对象
     * @param pharmacyInfo 商家信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<PharmacyInfo> page, PharmacyInfo pharmacyInfo) {
        return R.ok(pharmacyInfoService.selectPharmacyPage(page, pharmacyInfo));
    }

    /**
     * 查询本月订单数量排行
     *
     * @return 结果
     */
    @GetMapping("/order/rank/{type}")
    public R selectOrderRank(@PathVariable("type") Integer type) {
        return R.ok(pharmacyInfoService.selectOrderRank(type));
    }

    /**
     * 统计数据查询
     *
     * @return 结果
     */
    @GetMapping("/selectOrderDays")
    public R selectOrderDays() {
        // todo 接口有问题
        return R.ok(new HashMap<String, Object>(16) {
            {
                put("orderPriceDays", pharmacyInfoService.selectOrderPriceDays());
                put("orderNumDays", pharmacyInfoService.selectOrderNumDays());
            }
        });
    }

    /**
     * 获取详情信息
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(pharmacyInfoService.getById(id));
    }

    /**
     * 获取详情信息
     *
     * @param pharmacyId 商家ID
     * @return 结果
     */
    @GetMapping("/queryDetail")
    public R queryDetail(@RequestParam("pharmacyId") Integer pharmacyId) {
        // 返回结果
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("pharmacy", null);
                put("order", null);
            }
        };
        PharmacyInfo pharmacyInfo = pharmacyInfoService.getOne(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getUserId, pharmacyId));
        result.put("pharmacy", pharmacyInfo);
        // 查询订单信息
//        result.put("order", orderInfoService.list(Wrappers.<ParkOrderInfo>lambdaQuery().eq(ParkOrderInfo::getId, pharmacyInfo.getId())));
        return R.ok(result);
    }

    /**
     * 获取信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(pharmacyInfoService.list());
    }

    /**
     * 新增商家信息
     *
     * @param pharmacyInfo 商家信息
     * @return 结果
     */
    @PostMapping
    public R save(PharmacyInfo pharmacyInfo) throws Exception {
        pharmacyInfo.setCode("PM-" + System.currentTimeMillis());
        pharmacyInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        userService.registPharmacy(pharmacyInfo.getCode(), "1234qwer", pharmacyInfo);
        return R.ok(true);
    }

    /**
     * 修改商家信息
     *
     * @param pharmacyInfo 商家信息
     * @return 结果
     */
    @PutMapping
    public R edit(PharmacyInfo pharmacyInfo) {
        return R.ok(pharmacyInfoService.updateById(pharmacyInfo));
    }

    /**
     * 删除商家信息
     *
     * @param ids ids
     * @return 商家信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(pharmacyInfoService.removeByIds(ids));
    }

}
