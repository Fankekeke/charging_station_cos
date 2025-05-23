package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.ReserveInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
public interface IReserveInfoService extends IService<ReserveInfo> {

    /**
     * 分页获取充电桩预约信息
     *
     * @param page        分页对象
     * @param reserveInfo 充电桩预约信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectReservePage(Page<ReserveInfo> page, ReserveInfo reserveInfo);

    /**
     * 获取预约列表
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> queryReserveList(Integer userId);
}
