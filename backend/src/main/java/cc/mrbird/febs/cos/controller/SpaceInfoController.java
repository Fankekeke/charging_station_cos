package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.SpaceInfo;
import cc.mrbird.febs.cos.entity.SpaceStatusInfo;
import cc.mrbird.febs.cos.service.ISpaceInfoService;
import cc.mrbird.febs.cos.service.ISpaceStatusInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@RestController
@RequestMapping("/cos/space-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpaceInfoController {

    private final ISpaceInfoService spaceInfoService;

    private final ISpaceStatusInfoService spaceStatusInfoService;

    /**
     * 分页获取充电桩信息
     *
     * @param page      分页对象
     * @param spaceInfo 充电桩信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<SpaceInfo> page, SpaceInfo spaceInfo) {
        return R.ok(spaceInfoService.selectSpacePage(page, spaceInfo));
    }

    /**
     * 充电桩信息详情
     *
     * @param id 充电桩ID
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(spaceInfoService.getById(id));
    }

    /**
     * 充电桩信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(spaceInfoService.list());
    }

    /**
     * 查询空闲充电桩
     *
     * @return 结果
     */
    @GetMapping("/selectFreeSpace/list")
    public R selectFreeSpace() {
        return R.ok(spaceInfoService.selectFreeSpace(null));
    }

    /**
     * 新增充电桩信息
     *
     * @param spaceInfo 充电桩信息
     * @return 结果
     */
    @PostMapping
    public R save(SpaceInfo spaceInfo) {
        spaceInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        spaceInfo.setCode("SP-" + System.currentTimeMillis());

        spaceInfoService.save(spaceInfo);
        // 添加充电桩状态
        SpaceStatusInfo spaceStatusInfo = new SpaceStatusInfo();
        spaceStatusInfo.setStatus("0");
        spaceStatusInfo.setSpaceId(spaceInfo.getId());

        return R.ok(spaceStatusInfoService.save(spaceStatusInfo));
    }

    /**
     * 修改充电桩信息
     *
     * @param spaceInfo 充电桩信息
     * @return 结果
     */
    @PutMapping
    public R edit(SpaceInfo spaceInfo) {
        return R.ok(spaceInfoService.updateById(spaceInfo));
    }

    /**
     * 删除充电桩信息
     *
     * @param ids ids
     * @return 充电桩信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(spaceInfoService.removeByIds(ids));
    }
}
