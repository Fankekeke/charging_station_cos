package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.PaymentRecord;
import cc.mrbird.febs.cos.service.IPaymentRecordService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/payment-record")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentRecordController {

    private final IPaymentRecordService paymentRecordService;

    /**
     * 分页获取缴费信息
     *
     * @param page          分页对象
     * @param paymentRecord 缴费信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<PaymentRecord> page, PaymentRecord paymentRecord) {
        return R.ok(paymentRecordService.queryPaymentPage(page, paymentRecord));
    }

    /**
     * 获取ID获取审核详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(paymentRecordService.getById(id));
    }

    /**
     * 获取缴费信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(paymentRecordService.list());
    }

    /**
     * 新增缴费信息
     *
     * @param paymentRecord 缴费信息
     * @return 结果
     */
    @PostMapping
    public R save(PaymentRecord paymentRecord) {
        return R.ok(paymentRecordService.save(paymentRecord));
    }

    /**
     * 修改缴费信息
     *
     * @param paymentRecord 缴费信息
     * @return 结果
     */
    @PutMapping
    public R edit(PaymentRecord paymentRecord) {
        return R.ok(paymentRecordService.updateById(paymentRecord));
    }

    /**
     * 删除缴费信息
     *
     * @param ids ids
     * @return 缴费信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(paymentRecordService.removeByIds(ids));
    }
}
