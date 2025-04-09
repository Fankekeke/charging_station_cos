package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.MessageInfoMapper;
import cc.mrbird.febs.cos.entity.MessageInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IMessageInfoService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK fan1ke2ke@gmail.com
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageInfoServiceImpl extends ServiceImpl<MessageInfoMapper, MessageInfo> implements IMessageInfoService {

    private final IUserInfoService userInfoService;

    /**
     * 分页获取消息信息
     *
     * @param page        分页对象
     * @param messageInfo 消息信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> queryMessagePage(Page<MessageInfo> page, MessageInfo messageInfo) {
        return baseMapper.queryMessagePage(page, messageInfo);
    }

    /**
     * 查询消息信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> messageListById(Integer userId) {
        return baseMapper.messageListById(userId);
    }

    /**
     * 查找聊天记录
     *
     * @param takeUser 发送者
     * @param sendUser 接收人
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> getMessageDetail(Integer takeUser, Integer sendUser) {
        return baseMapper.getMessageDetail(takeUser, sendUser);
    }

    /**
     * 根据用户编号获取联系人
     *
     * @param userId 用户编号
     * @param flag
     * @return 结果
     */
    @Override
    public List<UserInfo> selectContactPerson(Integer userId, Integer flag) {
        List<MessageInfo> messageList = this.list(Wrappers.<MessageInfo>lambdaQuery().eq(MessageInfo::getSendUser, userId).or().eq(MessageInfo::getTakeUser, userId));
        if (CollectionUtil.isEmpty(messageList)) {
            return Collections.emptyList();
        }

        // 保存用户ID
        List<Integer> userIdList = new ArrayList<>();
        // 获取联系人
        for (MessageInfo messageInfo : messageList) {
            if (messageInfo.getTakeUser().equals(userId)) {
                userIdList.add(messageInfo.getSendUser());
            } else {
                userIdList.add(messageInfo.getTakeUser());
            }
        }
        // 获取用户信息
        List<UserInfo> userInfoList = userInfoService.list(Wrappers.<UserInfo>lambdaQuery().in(UserInfo::getId, userIdList));
        return userInfoList;
    }

    /**
     * 查询聊天记录
     *
     * @param expertCode
     * @param enterpriseCode
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectChatList(String expertCode, String enterpriseCode) {
        return baseMapper.selectChatList(expertCode, enterpriseCode);
    }

}
