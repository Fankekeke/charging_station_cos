package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.SpaceStatusInfo;
import cc.mrbird.febs.cos.dao.SpaceStatusInfoMapper;
import cc.mrbird.febs.cos.service.ISpaceStatusInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Service
public class SpaceStatusInfoServiceImpl extends ServiceImpl<SpaceStatusInfoMapper, SpaceStatusInfo> implements ISpaceStatusInfoService {

    /**
     * 分页获取充电桩状态信息
     *
     * @param page            分页对象
     * @param spaceStatusInfo 充电桩状态信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectSpacePage(Page<SpaceStatusInfo> page, SpaceStatusInfo spaceStatusInfo) {
        return baseMapper.selectSpacePage(page, spaceStatusInfo);
    }

    /**
     * 获取充电桩列表
     *
     * @param shopId 商铺ID
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> querySpaceListByShopId(Integer shopId) {
        return baseMapper.querySpaceListByShopId(shopId);
    }

    /**
     * 获取充电桩详情
     *
     * @param spaceId 充电桩ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> getGoodsDetail(Integer spaceId) {
        return baseMapper.getGoodsDetail(spaceId);
    }

    /**
     * 获取充电桩状态图
     *
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectStatusCheck() {
        return baseMapper.selectStatusCheck();
    }
}
