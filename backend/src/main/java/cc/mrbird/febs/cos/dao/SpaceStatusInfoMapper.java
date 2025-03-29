package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.SpaceStatusInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface SpaceStatusInfoMapper extends BaseMapper<SpaceStatusInfo> {

    /**
     * 分页获取充电桩状态信息
     *
     * @param page            分页对象
     * @param spaceStatusInfo 充电桩状态信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectSpacePage(Page<SpaceStatusInfo> page, @Param("spaceStatusInfo") SpaceStatusInfo spaceStatusInfo);

    /**
     * 获取充电桩状态图
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectStatusCheck();
}
