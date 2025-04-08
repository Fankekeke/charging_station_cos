package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.SpaceStatusInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface ISpaceStatusInfoService extends IService<SpaceStatusInfo> {

    /**
     * 分页获取充电桩状态信息
     *
     * @param page            分页对象
     * @param spaceStatusInfo 充电桩状态信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectSpacePage(Page<SpaceStatusInfo> page, SpaceStatusInfo spaceStatusInfo);

    /**
     * 获取充电桩列表
     *
     * @param shopId 商铺ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> querySpaceListByShopId(Integer shopId);

    /**
     * 获取充电桩详情
     *
     * @param spaceId 充电桩ID
     * @return 结果
     */
    LinkedHashMap<String, Object> getGoodsDetail(Integer spaceId);

    /**
     * 获取充电桩状态图
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectStatusCheck();
}
