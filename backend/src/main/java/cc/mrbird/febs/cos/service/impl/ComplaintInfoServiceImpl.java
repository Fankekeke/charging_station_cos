package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.ComplaintInfoMapper;
import cc.mrbird.febs.cos.entity.ComplaintInfo;
import cc.mrbird.febs.cos.service.IComplaintInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK fan1ke2ke@gmail.com
 */
@Service
public class ComplaintInfoServiceImpl extends ServiceImpl<ComplaintInfoMapper, ComplaintInfo> implements IComplaintInfoService {

    /**
     * 分页获取投诉记录
     *
     * @param page          分页对象
     * @param complaintInfo 投诉记录
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryComplaintPage(Page<ComplaintInfo> page, ComplaintInfo complaintInfo) {
        return baseMapper.queryComplaintPage(page, complaintInfo);
    }

    /**
     * 查询用户投诉信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> queryComplaintList(Integer userId) {
        return baseMapper.queryComplaintList(userId);
    }
}
