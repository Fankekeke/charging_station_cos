package cc.mrbird.febs.cos.controller;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.dao.NotifyInfoMapper;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebController {

    private final IUserInfoService userInfoService;

    private final IComplaintInfoService complaintInfoService;

    private final IBulletinInfoService bulletinInfoService;

    private final IDiscountInfoService discountInfoService;

    private final IPostInfoService postInfoService;

    private final IReplyInfoService replyInfoService;

    private final IMessageInfoService messageInfoService;

    private final IParkOrderInfoService orderInfoService;

    private final IEvaluateInfoService evaluationService;

    private final NotifyInfoMapper mobileInfoMapper;

    private final INotifyInfoService notifyInfoService;

    private final IPaymentRecordService paymentRecordService;

    private final IStaffInfoService staffInfoService;

    private final IExchangeInfoService exchangeInfoService;

    private final ISpaceInfoService spaceInfoService;

    private final ISpaceStatusInfoService spaceStatusInfoService;

    private final IPharmacyInfoService pharmacyInfoService;

    private final IMemberInfoService memberInfoService;

    private final IRuleInfoService ruleInfoService;

    private final IMemberRecordInfoService memberRecordInfoService;

    private final IMailService mailService;

    private final TemplateEngine templateEngine;

    /**
     * File 转MultipartFile
     *
     * @param file
     * @return
     */
    public MultipartFile getMultipartFile(File file) {
        FileInputStream fileInputStream = null;
        MultipartFile multipartFile = null;
        try {
            fileInputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartFile;
    }

    @PostMapping("/userAdd")
    public R userAdd(@RequestBody UserInfo user) throws Exception {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        url += "?appid=wx76a6577665633a86";//自己的appid
        url += "&secret=78070ccedf3f17b272b84bdeeca28a2e";//自己的appSecret
        url += "&js_code=" + user.getOpenId();
        url += "&grant_type=authorization_code";
        url += "&connect_redirect=1";
        String res = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000)
                .setRedirectsEnabled(false).build();
        httpget.setConfig(requestConfig);
        response = httpClient.execute(httpget);
        HttpEntity responseEntity = response.getEntity();
        System.out.println("响应状态为:" + response.getStatusLine());
        if (responseEntity != null) {
            res = EntityUtils.toString(responseEntity);
            System.out.println("响应内容长度为:" + responseEntity.getContentLength());
            System.out.println("响应内容为:" + res);
        }
        // 释放资源
        httpClient.close();
        response.close();
        String openid = JSON.parseObject(res).get("openid").toString();
        System.out.println("openid" + openid);
        int count = userInfoService.count(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getOpenId, openid));
        if (count > 0) {
            return R.ok(userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getOpenId, openid)));
        } else {
            user.setOpenId(openid);
            user.setCreateDate(DateUtil.formatDateTime(new Date()));
            user.setName(user.getUserName());
            user.setCode("UR-" + System.currentTimeMillis());
            // 图片上传
            byte[] bytes = HttpUtil.downloadBytes(user.getAvatar());
            MultipartFile multipartFile = new MockMultipartFile("xxx.jpg", "xxx.jpg", ".jpg", bytes);
//            MultipartFile cMultiFile = new MockMultipartFile("file", file.getName(), null, new FileInputStream(file));
//
            // 1定义要上传文件 的存放路径
            String localPath = "G:/Project/20250321充电桩小程序/db";
            // 2获得文件名字
            String fileName = multipartFile.getName();
            // 2上传失败提示
            String warning = "";
            String newFileName = cc.mrbird.febs.common.utils.FileUtil.upload(multipartFile, localPath, fileName);
            if (newFileName != null) {
                //上传成功
                warning = newFileName;
            } else {
                warning = "上传失败";
            }
            System.out.println(warning);
            user.setImages(warning);
            userInfoService.save(user);
            return R.ok(user);
        }
    }

    @RequestMapping("/openid")
    public R getUserInfo(@RequestParam(name = "code") String code) throws Exception {
        System.out.println("code" + code);
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        url += "?appid=wx9fffb151ded22005";//自己的appid
        url += "&secret=9666e94c91361e7de4d3a6d09a23402f";//自己的appSecret
        url += "&js_code=" + code;
        url += "&grant_type=authorization_code";
        url += "&connect_redirect=1";
        String res = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);    //GET方式
        CloseableHttpResponse response = null;
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000)
                .setRedirectsEnabled(false).build();
        httpget.setConfig(requestConfig);
        response = httpClient.execute(httpget);
        HttpEntity responseEntity = response.getEntity();
        System.out.println("响应状态为:" + response.getStatusLine());
        if (responseEntity != null) {
            res = EntityUtils.toString(responseEntity);
            System.out.println("响应内容长度为:" + responseEntity.getContentLength());
            System.out.println("响应内容为:" + res);
        }
        // 释放资源
        httpClient.close();
        response.close();
        String openid = JSON.parseObject(res).get("openid").toString();
        System.out.println("openid" + openid);
        return R.ok(openid);
    }

    @GetMapping("/subscription")
    public R subscription(@RequestParam("taskCode") String taskCode) throws Exception {
        LinkedHashMap<String, Object> tokenParam = new LinkedHashMap<String, Object>() {
            {
                put("grant_type", "client_credential");
                put("appid", "wx76a6577665633a86");
                put("secret", "78070ccedf3f17b272b84bdeeca28a2e");
            }
        };
        String tokenResult = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token", tokenParam);
        String token = JSON.parseObject(tokenResult).get("access_token").toString();
        LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>() {
            {
                put("thing1", new HashMap<String, Object>() {
                    {
                        put("value", "张三");
                    }
                });
                put("character_string3", new HashMap<String, Object>() {
                    {
                        put("value", taskCode);
                    }
                });
                put("time4", new HashMap<String, Object>() {
                    {
                        put("value", DateUtil.formatDateTime(new Date()));
                    }
                });
                put("thing5", new HashMap<String, Object>() {
                    {
                        put("value", "若查看详细内容，请登录小程序");
                    }
                });
            }
        };
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + token;
        LinkedHashMap<String, Object> subscription = new LinkedHashMap<String, Object>() {
            {
                put("touser", "oeDfR5zqxQD3EEA3uPT836qnauSc");
                put("template_id", "Z27pBK1n9WnKNtQ_fo7TC-nUJUlOQ9KVJk6LIgp0nH8");
                put("data", data);
            }
        };
        String result = HttpUtil.post(url, JSONUtil.toJsonStr(subscription));
        return R.ok(result);
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return 结果
     */
    @GetMapping("/selectUserInfo")
    public R selectUserInfo(@RequestParam("userId") Integer userId) {
        return R.ok(userInfoService.getById(userId));
    }

    /**
     * 用户信息更新
     *
     * @param userInfo 用户信息
     * @return 结果
     */
    @PostMapping("/editUserInfo")
    public R editUserInfo(@RequestBody UserInfo userInfo) {
        return R.ok(userInfoService.updateById(userInfo));
    }


    /**
     * 进入小程序主页信息
     *
     * @return 结果
     */
    @GetMapping("/home/user")
    public R home(BigDecimal longitude, BigDecimal latitude, Integer userId) {
        return R.ok(orderInfoService.queryHomeByUserId(longitude, latitude, userId));
    }

    /**
     * 获取店铺详情
     *
     * @param shopId 店铺ID
     * @return 结果
     */
    @GetMapping("/getShopDetail")
    public R getShopDetail(Integer shopId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("shopInfo", null);
                put("goodsList", spaceStatusInfoService.querySpaceListByShopId(shopId));
            }
        };
        PharmacyInfo pharmacyInfo = pharmacyInfoService.getById(shopId);
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, pharmacyInfo.getUserId()));
        pharmacyInfo.setUserInfoId(userInfo.getId());
        result.put("shopInfo", pharmacyInfo);
        return R.ok(result);
    }

    /**
     * 获取充电桩详情
     *
     * @param spaceId 充电桩ID
     * @return 结果
     */
    @GetMapping("/getGoodsDetail")
    public R getGoodsDetail(Integer spaceId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("spaceInfo", spaceStatusInfoService.getGoodsDetail(spaceId));
            }
        };
        // 获取所属商家
        SpaceInfo spaceInfo = spaceInfoService.getById(spaceId);

        PharmacyInfo pharmacyInfo = pharmacyInfoService.getById(spaceInfo.getPharmacyId());
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, pharmacyInfo.getUserId()));
        pharmacyInfo.setUserInfoId(userInfo.getId());
        result.put("shopInfo", pharmacyInfo);
        return R.ok(result);
    }

    /**
     * 根据用户获取消息信息
     *
     * @param userId 用户ID1
     * @return 结果
     */
    @GetMapping("/queryMessageByUser")
    public R queryMessageByUser(@RequestParam("userId") Integer userId) {
        return R.ok(mobileInfoMapper.selectList(Wrappers.<NotifyInfo>lambdaQuery().eq(NotifyInfo::getUserId, userId)));
    }

    /**
     * 删除消息信息
     *
     * @param messageId 消息ID
     * @return 结果
     */
    @GetMapping("/deleteMessage")
    public R deleteMessage(@RequestParam("messageId") Integer messageId) {
        return R.ok(notifyInfoService.update(Wrappers.<NotifyInfo>lambdaUpdate().set(NotifyInfo::getDelFlag, 1).eq(NotifyInfo::getId, messageId)));
    }

    /**
     * 查询用户投诉信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/queryComplaintListById")
    public R queryComplaintListById(@RequestParam Integer userId) {
        return R.ok(complaintInfoService.queryComplaintList(userId));
    }

    /**
     * 获取贴子信息
     *
     * @return 结果
     */
    @GetMapping("/getPostList")
    public R getPostList() {
        return R.ok(postInfoService.getPostList());
    }

    /**
     * 根据贴子编号获取详细信息
     *
     * @param postId
     * @return 结果
     */
    @GetMapping("/getPostInfoById")
    public R getPostInfoById(@RequestParam Integer postId) {
        return R.ok(postInfoService.getPostInfoById(postId));
    }

    /**
     * 、
     * 贴子回复
     *
     * @return 结果
     */
    @PostMapping("/replyPost")
    public R replyPost(@RequestBody ReplyInfo replyInfo) {
        replyInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(replyInfoService.save(replyInfo));
    }

    /**
     * 添加贴子
     *
     * @param postInfo
     * @return 结果
     */
    @PostMapping("/postAdd")
    public R postAdd(@RequestBody PostInfo postInfo) {
        postInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(postInfoService.save(postInfo));
    }

    /**
     * 获取公告信息
     *
     * @return 结果
     */
    @GetMapping("/getBulletinList")
    public R getBulletinList() {
        return R.ok(bulletinInfoService.list());
    }

    /**
     * 查询消息信息
     *
     * @param userId
     * @return 结果
     */
    @GetMapping("/messageListById")
    public R messageListById(@RequestParam Integer userId) {
        return R.ok(messageInfoService.messageListById(userId));
    }

    /**
     * 查找聊天记录
     *
     * @param takeUser
     * @param sendUser
     * @return 结果
     */
    @GetMapping("/getMessageDetail")
    public R getMessageDetail(@RequestParam(value = "takeUser") Integer takeUser, @RequestParam(value = "sendUser") Integer sendUser, @RequestParam(value = "userId") Integer userId) {
        if (takeUser.equals(userId)) {
            messageInfoService.update(Wrappers.<MessageInfo>lambdaUpdate().set(MessageInfo::getTaskStatus, 1)
                    .eq(MessageInfo::getTakeUser, takeUser).eq(MessageInfo::getSendUser, sendUser));
        } else {
            messageInfoService.update(Wrappers.<MessageInfo>lambdaUpdate().set(MessageInfo::getTaskStatus, 1)
                    .eq(MessageInfo::getTakeUser, sendUser).eq(MessageInfo::getSendUser, takeUser));
        }
        return R.ok(messageInfoService.getMessageDetail(takeUser, sendUser));
    }

    /**
     * 消息回复
     *
     * @param messageInfo
     * @return 结果
     */
    @PostMapping("/messageReply")
    public R messageReply(@RequestBody MessageInfo messageInfo) {
        messageInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        messageInfo.setTaskStatus(0);
        return R.ok(messageInfoService.save(messageInfo));
    }

    /**
     * 进入小程序主页信息
     *
     * @return 结果
     */
    @GetMapping("/home")
    public R home() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("postInfo", postInfoService.getPostListHot());
        return R.ok(result);
    }

    /**
     * 计算订单价格
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PostMapping("/calculateAmountResult")
    public R calculateAmountResult(@RequestBody ParkOrderInfo orderInfo) throws FebsException {
        return R.ok(orderInfoService.getPriceTotal(orderInfo));
    }

    /**
     * 添加订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PostMapping("/addOrder")
    @Transactional(rollbackFor = Exception.class)
    public R addOrder(@RequestBody ParkOrderInfo orderInfo) {
        orderInfo.setCode("ORD-" + System.currentTimeMillis());
        orderInfo.setStartDate(DateUtil.formatDateTime(new Date()));
        orderInfo.setStatus("-1");

        // 获取充电桩信息
        SpaceInfo spaceInfo = spaceInfoService.getById(orderInfo.getSpaceId());
        orderInfo.setPrice(spaceInfo.getPrice());
        orderInfo.setPharmacyId(spaceInfo.getPharmacyId());
        // 获取用户信息
        UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
        // 添加通知
        NotifyInfo notifyInfo = new NotifyInfo(userInfo.getCode(), 0, DateUtil.formatDateTime(new Date()), userInfo.getName());
        notifyInfo.setContent("你好【" + orderInfo.getCode() + "】，此订单已开始， 等待充满后请结束");
        notifyInfo.setUserId(userInfo.getId());
        notifyInfoService.save(notifyInfo);

        return R.ok(orderInfoService.save(orderInfo));
    }

    /**
     * 订单结算
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    @PostMapping("/overOrder")
    @Transactional(rollbackFor = Exception.class)
    public R overOrder(@RequestBody ParkOrderInfo orderInfo) {
        orderInfo.setCode("ORD-" + System.currentTimeMillis());
        orderInfo.setStartDate(DateUtil.formatDateTime(new Date()));
        orderInfo.setPayDate(DateUtil.formatDateTime(new Date()));
        orderInfo.setStatus("1");
        // 获取用户信息
        UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
        // 添加通知
        NotifyInfo notifyInfo = new NotifyInfo(userInfo.getCode(), 0, DateUtil.formatDateTime(new Date()), userInfo.getName());
        notifyInfo.setContent("你好【" + orderInfo.getCode() + "】，此订单已付款");
        notifyInfo.setUserId(userInfo.getId());
        notifyInfoService.save(notifyInfo);

        // 添加付款记录
        PaymentRecord paymentInfo = new PaymentRecord();
        paymentInfo.setAmount(orderInfo.getAfterOrderPrice());
        paymentInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        paymentInfo.setUserCode(userInfo.getCode());
        paymentInfo.setOrderCode(orderInfo.getCode());
        paymentRecordService.save(paymentInfo);

        // 判断是否使用优惠券
        if (orderInfo.getDiscountId() != null) {
            // 更新优惠券状态
            DiscountInfo discountInfo = discountInfoService.getOne(Wrappers.<DiscountInfo>lambdaQuery().eq(DiscountInfo::getId, orderInfo.getDiscountId()));
            discountInfo.setStatus("1");
            discountInfoService.updateById(discountInfo);
        }

        return R.ok(orderInfoService.save(orderInfo));
    }

    /**
     * 获取订单详情
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @GetMapping("/queryOrderDetail")
    public R queryOrderDetail(Integer orderId) {
        ParkOrderInfo orderInfo = orderInfoService.getById(orderId);
//        return R.ok(orderInfoService.selectOrderDetail(orderInfo.getId()));
        return R.ok();
    }

    /**
     * 根据商家获取充电桩信息
     *
     * @param pharmacyId 商家ID
     * @return 结果
     */
    @GetMapping("/querySpaceById")
    public R querySpaceById(Integer pharmacyId) {
        return R.ok(spaceInfoService.selectFreeSpace(pharmacyId));
    }

    /**
     * 添加评价信息
     *
     * @param evaluation
     * @return 结果
     */
    @PostMapping("/evaluationAdd")
    public R evaluationAdd(@RequestBody EvaluateInfo evaluation) {
        ParkOrderInfo orderInfo = orderInfoService.getById(evaluation.getOrderId());
        // 获取用户编号
        UserInfo userInfo = userInfoService.getById(orderInfo.getUserId());
        evaluation.setOrderId(orderInfo.getId());
        evaluation.setOrderCode(orderInfo.getCode());
        evaluation.setUserId(userInfo.getId());
        evaluation.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(evaluationService.save(evaluation));
    }

    /**
     * 添加投诉信息
     *
     * @return 结果
     */
    @GetMapping("/complaintAdd")
    public R complaintAdd(Integer userId, Integer orderId, String content) {
        ParkOrderInfo orderInfo = orderInfoService.getById(orderId);
        // 获取用户编号
        ComplaintInfo complaintInfo = new ComplaintInfo();
        complaintInfo.setContent(content);
        complaintInfo.setUserId(userId);
        complaintInfo.setOrderCode(orderInfo.getCode());
        complaintInfo.setStatus("0");
        complaintInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        complaintInfo.setStaffId(orderInfo.getPharmacyId());
        return R.ok(complaintInfoService.save(complaintInfo));
    }

    /**
     * 运输结束回调
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @GetMapping("/receipt")
    public R receipt(Integer orderId) {
//        return R.ok(orderInfoService.auditOrderFinish(orderId));
        return R.ok();
    }

    /**
     * 获取可用优惠券
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/queryUseDiscountByUserId")
    public R queryUseDiscountByUserId(Integer userId, BigDecimal amount) {
        return R.ok(discountInfoService.queryUseDiscountByUserId(userId, amount));
    }

    /**
     * 获取用户所有订单
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/getOrderListByUserId")
    public R getOrderListByUserId(Integer userId) {
//        return R.ok(orderInfoService.queryOrderByUserId(userId));
        return R.ok();
    }


    /**
     * 根据状态用户ID获取优惠券信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/queryDiscountSortByUserId")
    public R queryDiscountSortByUserId(Integer userId) {
        return R.ok(discountInfoService.queryDiscountSortByUserId(userId));
    }

    /**
     * 根据用户获取贴子信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/getPostByUser")
    public R getPostByUser(@RequestParam("userId") Integer userId) {
        return R.ok(postInfoService.getPostByUser(userId));
    }

    /**
     * 删除贴子信息
     *
     * @param postId 贴子ID
     * @return 结果
     */
    @GetMapping("/deletePost")
    public R deletePost(@RequestParam("postId") Integer postId) {
        return R.ok(postInfoService.removeById(postId));
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/getUserInfoById")
    public R getUserInfoById(Integer userId) {
        return R.ok(userInfoService.getById(userId));
    }

    /**
     * 兑换优惠券
     *
     * @param userId 用户ID
     * @param type   类型
     * @return 结果
     */
    @GetMapping("/exchange")
    @Transactional(rollbackFor = Exception.class)
    public R exchange(Integer userId, Integer type) {
        DiscountInfo discountInfo = new DiscountInfo();
        UserInfo userInfo = userInfoService.getById(userId);

        ExchangeInfo exchangeInfo = new ExchangeInfo();
        exchangeInfo.setUserId(userId);
        exchangeInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        if (type == 1) {
            discountInfo.setCode("DC-" + System.currentTimeMillis());
            discountInfo.setStatus("0");
            discountInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
            discountInfo.setDiscountPrice(BigDecimal.valueOf(100));
            discountInfo.setThreshold(BigDecimal.valueOf(500));
            discountInfo.setUserId(userId);
            discountInfo.setCouponName("500-100满减券");
            discountInfo.setContent("500-100满减券");
            discountInfo.setType("1");
            userInfo.setIntegral(NumberUtil.sub(userInfo.getIntegral(), BigDecimal.valueOf(300)));
            exchangeInfo.setMaterialId(1);
            exchangeInfo.setIntegral(new BigDecimal(300));
        } else {
            discountInfo.setCode("DC-" + System.currentTimeMillis());
            discountInfo.setStatus("0");
            discountInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
            discountInfo.setRebate(BigDecimal.valueOf(8));
            discountInfo.setUserId(userId);
            discountInfo.setCouponName("八折无门槛优惠券");
            discountInfo.setContent("八折无门槛优惠券");
            discountInfo.setType("2");
            userInfo.setIntegral(NumberUtil.sub(userInfo.getIntegral(), BigDecimal.valueOf(200)));
            exchangeInfo.setMaterialId(2);
            exchangeInfo.setIntegral(new BigDecimal(200));
        }
        exchangeInfoService.save(exchangeInfo);
        discountInfoService.save(discountInfo);
        return R.ok(userInfoService.updateById(userInfo));

    }

    /**
     * 根据用户获取会员信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping("/queryMemberByUserId")
    public R queryMemberByUserId(Integer userId) {
        // 返回信息
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        // 用户信息
        UserInfo userInfo = userInfoService.getById(userId);
        result.put("user", userInfo);

        // 公告信息
        List<BulletinInfo> bulletinInfoList = bulletinInfoService.list(Wrappers.<BulletinInfo>lambdaQuery().eq(BulletinInfo::getType, 1));
        result.put("bulletin", bulletinInfoList);
        // 会员信息
        List<MemberInfo> memberInfos = memberInfoService.list(Wrappers.<MemberInfo>lambdaQuery().eq(MemberInfo::getUserId, userInfo.getId()));
        if (CollectionUtil.isNotEmpty(memberInfos)) {
            for (MemberInfo memberInfo : memberInfos) {
                if (DateUtil.isIn(new Date(), DateUtil.parseDateTime(memberInfo.getStartDate()), DateUtil.parseDateTime(memberInfo.getEndDate()))) {
                    RuleInfo ruleInfo = ruleInfoService.getById(memberInfo.getMemberLevel());
                    memberInfo.setRuleName(ruleInfo.getName());
                    result.put("member", memberInfo);
                    return R.ok(result);
                }
            }
        } else {
            result.put("member", null);
        }
        return R.ok(result);
    }

    /**
     * 获取会员价格
     *
     * @return 结果
     */
    @GetMapping("/queryMemberList")
    public R queryMemberList() {
        return R.ok(ruleInfoService.list());
    }

    /**
     * 购买会员
     *
     * @return
     * @throws AlipayApiException
     */
    @GetMapping(value = "/member")
    public R alipayMember(String totalAmount, Integer ruleId, Integer userId) throws AlipayApiException {
        // 获取用户信息
        UserInfo userInfo = userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getId, userId));

        // 会员订单
        MemberRecordInfo memberRecordInfo = new MemberRecordInfo();
        memberRecordInfo.setStatus("1");
        memberRecordInfo.setMemberId(ruleId);
        memberRecordInfo.setCode("MEM-" + System.currentTimeMillis());
        memberRecordInfo.setUserId(userInfo.getId());
        memberRecordInfo.setPrice(new BigDecimal(totalAmount));
        memberRecordInfo.setPayDate(DateUtil.formatDateTime(new Date()));

        memberRecordInfoService.save(memberRecordInfo);

        // 会员信息
        RuleInfo ruleInfo = ruleInfoService.getById(memberRecordInfo.getMemberId());

        // 添加会员信息
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.setUserId(userInfo.getId());
        memberInfo.setMemberLevel(ruleInfo.getId().toString());
        memberInfo.setPayDate(DateUtil.formatDateTime(new Date()));
        memberInfo.setStartDate(DateUtil.formatDateTime(new Date()));
        memberInfo.setEndDate(DateUtil.formatDateTime(DateUtil.offsetDay(new Date(), ruleInfo.getDays())));
        memberInfo.setPrice(memberRecordInfo.getPrice());
        memberInfoService.save(memberInfo);

        // 发送消息
        NotifyInfo notifyInfo = new NotifyInfo();
        notifyInfo.setUserId(userInfo.getId());
        notifyInfo.setContent("您好，您已缴费会员成功，会员截至 " + memberInfo.getEndDate() + "");
        notifyInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        notifyInfoService.save(notifyInfo);

        if (StrUtil.isNotEmpty(userInfo.getEmail())) {
            Context context = new Context();
            context.setVariable("today", DateUtil.formatDate(new Date()));
            context.setVariable("custom", userInfo.getName() + "您好，您已缴费会员成功，会员截至 " + memberInfo.getEndDate() + "");
            String emailContent = templateEngine.process("registerEmail", context);
            mailService.sendHtmlMail(userInfo.getEmail(), DateUtil.formatDate(new Date()) + "会员提示", emailContent);
        }
        return R.ok(true);
    }
}
