![](https://img.shields.io/badge/License-MIT-blue.svg)

![](https://img.shields.io/badge/Version-1.0.0-brightgreen.svg)

![](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)

# 基于 SpringBoot + Vue 的充电桩预约小程序 (Charging Station Reservation Mini-Program)

本项目是一套面向新能源汽车用户的智慧充电预约小程序平台，采用 SpringBoot + Vue 前后端分离架构，支持用户端小程序、商家端管理后台与平台管理员后台协同工作。平台围绕“找桩—预约—充电—结算—评价”完整流程设计，提供充电桩状态监控、在线预约、订单管理、会员体系、优惠券、积分兑换、社区论坛及数据统计等功能，适用于社区、园区、商场及公共充电场站的充电运营管理场景。

#### 安装环境
️**注意**：请确保您的开发环境已安装以下依赖。

JAVA 环境 

Node.js环境 [https://nodejs.org/en/] 选择14.17

Yarn 打开cmd， 输入npm install -g yarn !!!必须安装完毕nodejs

Mysql 数据库 一定要把账户和密码记住

redis

Idea 编译器

WebStorm OR VScode 编译器

#### 采用技术及功能

后端：SpringBoot、MybatisPlus、MySQL、Redis、
前端：Vue、Apex、Antd、Axios

平台前端：vue(框架) + vuex(全局缓存) + rue-router(路由) + axios(请求插件) + apex(图表)  + antd-ui(ui组件) + 百度地图 API + 微信小程序

平台后台：springboot(框架) + redis(缓存中间件) + shiro(权限中间件) + mybatisplus(orm) + restful风格接口 + mysql(数据库)

开发环境：windows10 or windows7 ， vscode or webstorm ， idea + lambok

---

## 核心特性

- **三端协同运营**：覆盖管理员、商家、用户三类角色，实现充电预约、订单处理、设备管理与用户服务全流程闭环。
- **充电桩状态监控**：实时展示充电桩空闲、占用、故障等状态，支持商家与管理员对设备信息进行维护。
- **智能预约充电**：支持用户在线选择充电桩、设置充电时间并提交预约，减少现场排队与资源空置。
- **会员与营销体系**：支持会员购买、会员价格、优惠券发放、积分兑换等运营功能，提升用户活跃度。
- **多维度数据统计**：提供订单统计、缴费记录、充电桩使用情况、用户行为等数据分析能力，辅助运营决策。
- **社区互动与反馈**：内置社区论坛、订单评价、投诉记录与用户消息通知，完善用户沟通与服务监督机制。
---

## 功能模块概览

### 管理员端

管理员端负责平台整体运营、设备监管、订单审核与数据分析：

**平台运营与用户管理**

- **系统公告**：发布平台通知、充电政策调整、维护公告等信息。
- **用户管理 / 会员管理**：管理注册用户资料、会员等级、账户状态及会员价格规则。
- **商家管理**：审核入驻商家资质，维护商家信息与运营状态。
- **员工管理**：管理平台运营人员账号、角色权限与操作日志。
- **用户消息 / 消息通知**：向用户或商家推送订单提醒、活动通知及系统公告。

**充电桩与订单管理**

- **充电桩管理**：维护充电桩基本信息、所属商家、位置、功率、收费标准等。
- **充电桩状态**：实时监控充电桩空闲、使用中、故障、离线等状态。
- **订单管理 / 会员订单**：查看全平台充电订单、会员订单及订单流转状态。
- **充电预约**：管理平台预约记录，处理预约冲突、超时释放与异常订单。
- **车辆管理**：维护用户绑定车辆信息，支持车牌、车型等基础数据管理。

**财务、营销与数据看板**

- **缴费记录**：查询用户充电缴费、会员购买、积分兑换等交易明细。
- **优惠券管理**：创建优惠券、设置使用规则并监控发放与核销情况。
- **积分兑换 / 物品积分**：配置积分商品、兑换规则及用户积分流水。
- **订单评价 / 投诉记录**：查看用户评价与投诉内容，跟踪处理进度。
- **数据统计**：分析订单量、充电收入、充电桩利用率、用户活跃度等核心指标。
- **社区论坛**：管理社区帖子、评论及违规内容，维护社区秩序。

### 商家端

商家端面向充电桩运营商家，提供设备维护、订单处理与用户沟通能力：

**商家基础管理**

- **商家注册登录 / 密码修改**：支持商家账号注册、登录与安全设置。
- **商家信息**：维护商家名称、联系方式、服务范围、营业时间等基础信息。

**充电桩与预约管理**

- **充电桩管理**：添加、编辑、下架所属充电桩，设置充电价格与服务规则。
- **充电桩状态**：查看设备运行状态，及时发现故障桩与离线设备。
- **充电预约 / 订单管理**：接收用户预约与充电订单，确认订单状态并安排充电服务。

**用户互动与评价管理**

- **评价管理**：查看用户对充电服务的评价，优化服务质量。
- **用户沟通**：回复用户咨询、处理订单问题与售后反馈。
- **员工管理**：管理商家内部员工账号，分配设备维护与订单处理权限。

### 用户端

用户端以微信小程序为主要入口，提供便捷的充电预约与服务体验：

**账户与个人中心**

- **小程序账户注册登录**：支持手机号注册、登录与账号安全维护。
- **个人信息修改**：更新姓名、联系方式、头像等个人资料。
- **消息通知**：接收预约成功、订单状态变更、优惠券到账等系统消息。

**充电预约与订单服务**

- **充电预约**：查看附近充电桩，选择充电时段并提交预约订单。
- **我的订单**：查看待预约、充电中、已完成等订单状态与费用明细。
- **订单评价**：对已完成的充电服务进行评分与文字反馈。
- **订单投诉**：针对异常订单、设备故障或服务问题提交投诉。

**车辆、会员与社区**

- **车辆管理**：绑定个人车辆信息，方便后续充电与订单记录查询。
- **会员购买**：查看会员权益并在线购买会员，享受优惠价格。
- **我的优惠券**：领取、查看和使用平台或商家发放的充电优惠券。
- **积分兑换**：使用积分兑换充电券、服务权益或平台礼品。
- **社区论坛回复**：参与社区讨论，回复帖子，分享充电经验与使用反馈。

---

## 项目结构

```text
pharmacy-dispensing-system/
├── backend/            # 后端 API 服务
│   ├── src/
│   │   ├── controllers/  # 控制器
│   │   ├── models/       # 数据模型
│   │   ├── routes/       # 路由
│   │   └── services/     # 业务逻辑
│   └── package.json
├── frontend/           # 前端 Web 应用
│   ├── src/
│   │   ├── views/      # 页面视图
│   │   ├── components/ # 公共组件
│   │   └── store/      # 状态管理
│   └── package.json
├── docs/               # 项目文档与 API 接口说明
├── LICENSE
└── README.md
```
---


#### 前台启动方式
安装所需文件 yarn install 
运行 yarn run dev

#### 默认后台账户密码
[管理员]
admin
123456

[员工]
fank
123456

#### 项目截图
|  |  |
|---------------------|---------------------|
| ![](./assets/image%20(1).png) | ![](./assets/image%20(2).png) |
| ![](./assets/image%20(3).png) | ![](./assets/image%20(4).png) |
| ![](./assets/image%20(5).png) | ![](./assets/image%20(6).png) |
| ![](./assets/image%20(7).png) | ![](./assets/image%20(8).png) |
| ![](./assets/image%20(9).png) | ![](./assets/image%20(10).png) |
| ![](./assets/image%20(11).png) | ![](./assets/image%20(12).png) |
| ![](./assets/image%20(13).png) | ![](./assets/image%20(14).png) |
| ![](./assets/image%20(15).png) | ![](./assets/image%20(16).png) |
| ![](./assets/image%20(17).png) | ![](./assets/image%20(18).png) |
| ![](./assets/image%20(19).png) | | 

| ![](./assets/app_image%20(1).png) | ![](./assets/app_image%20(2).png) |
| ![](./assets/app_image%20(3).png) | ![](./assets/app_image%20(4).png) |
| ![](./assets/app_image%20(5).png) | ![](./assets/app_image%20(6).png) |
| ![](./assets/app_image%20(7).png) | ![](./assets/app_image%20(8).png) |
| ![](./assets/app_image%20(9).png) | ![](./assets/app_image%20(10).png) |
| ![](./assets/app_image%20(11).png) | ![](./assets/app_image%20(12).png) |
| ![](./assets/app_image%20(13).png) | ![](./assets/app_image%20(14).png) |

#### 演示视频

暂无

#### 获取方式

Email: fan1ke2ke@gmail.com

WeChat: `Storm_Berserker`

`附带部署与讲解服务，因为要恰饭资源非免费，伸手党勿扰，谢谢理解😭`

> 1.项目纯原创，不做二手贩子 2.一次购买终身有效 3.项目讲解持续到答辩结束 4.非常负责的答辩指导 5.**黑奴价格**

> 项目部署调试不好包退！功能逻辑没讲明白包退！

<div align=center><img align="center" src="./assets/product_word.png" alt="fankekeke" /></div>

#### 其它资源
[2026年-答辩顺利通过-客户评价🀄](https://berserker287.github.io/2026/06/29/2026%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2025年-答辩顺利通过-客户评价🍜](https://berserker287.github.io/2025/06/18/2025%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2024年-答辩顺利通过-客户评价👻](https://berserker287.github.io/2024/06/06/2024%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2023年-答辩顺利通过-客户评价🐢](https://berserker287.github.io/2023/06/14/2023%E5%B9%B4%E7%AD%94%E8%BE%A9%E9%A1%BA%E5%88%A9%E9%80%9A%E8%BF%87/)

[2022年-答辩通过率100%-客户评价🐣](https://berserker287.github.io/2022/05/25/%E9%A1%B9%E7%9B%AE%E4%BA%A4%E6%98%93%E8%AE%B0%E5%BD%95/)

[毕业答辩导师提问的高频问题](https://berserker287.github.io/2023/06/13/%E6%AF%95%E4%B8%9A%E7%AD%94%E8%BE%A9%E5%AF%BC%E5%B8%88%E6%8F%90%E9%97%AE%E7%9A%84%E9%AB%98%E9%A2%91%E9%97%AE%E9%A2%98/)

[50个高频答辩问题-技术篇](https://berserker287.github.io/2023/06/13/50%E4%B8%AA%E9%AB%98%E9%A2%91%E7%AD%94%E8%BE%A9%E9%97%AE%E9%A2%98-%E6%8A%80%E6%9C%AF%E7%AF%87/)

[计算机毕设答辩时都会问到哪些问题？](https://www.zhihu.com/question/31020988)

[计算机专业毕业答辩小tips](https://zhuanlan.zhihu.com/p/145911029)

#### 接JAVAWEB毕设，纯原创，价格公道，诚信第一

`网站建设、小程序、H5、APP、各种系统 选题+开题报告+任务书+程序定制+安装调试+项目讲解+论文+答辩PPT`

More info: [悲伤的橘子树](https://berserker287.github.io/)

<div align="center"><img align="center" src="./assets/chat_qr_code.png" alt="fankekeke" width=250 /></div>

<p><img align="center" src="./assets/wechat_group.png" alt="fankekeke" /></p>

**感谢查看**
