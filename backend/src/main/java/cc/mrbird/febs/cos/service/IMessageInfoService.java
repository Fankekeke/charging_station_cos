package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.MessageInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK fan1ke2ke@gmail.com
 */
public interface IMessageInfoService extends IService<MessageInfo> {

    /**
     * 分页获取消息信息
     *
     * @param page        分页对象
     * @param messageInfo 消息信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> queryMessagePage(Page<MessageInfo> page, MessageInfo messageInfo);

    /**
     * 查询消息信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> messageListById(Integer userId);

    /**
     * 查找聊天记录
     *
     * @param takeUser 发送者
     * @param sendUser 接收人
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> getMessageDetail(Integer takeUser, Integer sendUser);

    /**
     * 根据用户编号获取联系人
     *
     * @param userId 用户编号
     * @param flag
     * @return 结果
     */
    List<UserInfo> selectContactPerson(Integer userId, Integer flag);

    /**
     * 查询聊天记录
     *
     * @param expertCode
     * @param enterpriseCode
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectChatList(String expertCode, String enterpriseCode);
}
