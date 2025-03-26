package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.NotifyInfo;
import cc.mrbird.febs.cos.service.INotifyInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/notify-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotifyInfoController {

    private final INotifyInfoService notifyInfoService;

    /**
     * 分页获取消息通知信息
     *
     * @param page       分页对象
     * @param notifyInfo 消息通知信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<NotifyInfo> page, NotifyInfo notifyInfo) {
        return R.ok(notifyInfoService.queryNotifyPage(page, notifyInfo));
    }

    /**
     * 获取ID获取审核详情
     *
     * @param id 主键
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(notifyInfoService.getById(id));
    }

    /**
     * 获取消息通知信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(notifyInfoService.list());
    }

    /**
     * 新增消息通知信息
     *
     * @param notifyInfo 消息通知信息
     * @return 结果
     */
    @PostMapping
    public R save(NotifyInfo notifyInfo) {
        return R.ok(notifyInfoService.save(notifyInfo));
    }

    /**
     * 修改消息通知信息
     *
     * @param notifyInfo 消息通知信息
     * @return 结果
     */
    @PutMapping
    public R edit(NotifyInfo notifyInfo) {
        return R.ok(notifyInfoService.updateById(notifyInfo));
    }

    /**
     * 删除消息通知信息
     *
     * @param ids ids
     * @return 消息通知信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(notifyInfoService.removeByIds(ids));
    }
}
