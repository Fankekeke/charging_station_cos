package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.ComplaintInfo;
import cc.mrbird.febs.cos.service.IComplaintInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/complaint-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ComplaintInfoController {

    private final IComplaintInfoService complaintInfoService;

    /**
     * 分页获取投诉记录
     *
     * @param page          分页对象
     * @param complaintInfo 投诉记录
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<ComplaintInfo> page, ComplaintInfo complaintInfo) {
        return R.ok(complaintInfoService.queryComplaintPage(page, complaintInfo));
    }

    /**
     * 获取ID获取审核详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(complaintInfoService.getById(id));
    }

    /**
     * 获取投诉记录列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(complaintInfoService.list());
    }

    /**
     * 新增投诉记录
     *
     * @param complaintInfo 投诉记录
     * @return 结果
     */
    @PostMapping
    public R save(ComplaintInfo complaintInfo) {
        return R.ok(complaintInfoService.save(complaintInfo));
    }

    /**
     * 修改投诉记录
     *
     * @param complaintInfo 投诉记录
     * @return 结果
     */
    @PutMapping
    public R edit(ComplaintInfo complaintInfo) {
        return R.ok(complaintInfoService.updateById(complaintInfo));
    }

    /**
     * 删除投诉记录
     *
     * @param ids ids
     * @return 投诉记录
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(complaintInfoService.removeByIds(ids));
    }
}
