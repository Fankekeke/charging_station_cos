### 基于SpringBoot + Vue的充电站 充电桩 车辆充电小程序.

#### 安装环境

JAVA 环境 

Node.js环境 [https://nodejs.org/en/] 选择14.17

Yarn 打开cmd， 输入npm install -g yarn !!!必须安装完毕nodejs

Mysql 数据库 [https://blog.csdn.net/qq_40303031/article/details/88935262] 一定要把账户和密码记住

redis

Idea 编译器 [https://blog.csdn.net/weixin_44505194/article/details/104452880]

WebStorm OR VScode 编译器 [https://www.jianshu.com/p/d63b5bae9dff]

#### 采用技术及功能

后端：SpringBoot、MybatisPlus、MySQL、Redis、
前端：Vue、Apex、Antd、Axios

平台前端：vue(框架) + vuex(全局缓存) + rue-router(路由) + axios(请求插件) + apex(图表)  + antd-ui(ui组件)

平台后台：springboot(框架) + redis(缓存中间件) + shiro(权限中间件) + mybatisplus(orm) + restful风格接口 + mysql(数据库)

开发环境：windows10 or windows7 ， vscode or webstorm ， idea + lambok


#### 前台启动方式
安装所需文件 yarn install 
运行 yarn run dev

#### 默认后台账户密码
[管理员]
admin
1234qwer

###### 管理员：
系统公告 、会员管理 、用户消息 、订单管理 、充电预约 、会员价格 、充电桩管理 、充电桩状态 、用户管理 、车辆管理 、数据统计 、会员订单 、员工管理 、投诉记录 、优惠券管理 、订单评价 、积分兑换 、物品积分 、缴费记录 、商家管理 、社区论坛、数据统计。

###### 商家：
商家注册登录、密码修改、订单管理 、充电预约 、充电桩管理 、充电桩状态 、评价管理 、商家信息 、员工管理 、用户沟通。

###### 用户：
小程序账户注册登录、个人信息修改、消息通知、充电预约、会员购买、我的优惠券、我的订单、车辆管理、订单评价、订单投诉、社区论坛回复、积分兑换。

#### 项目截图
暂无

|  |  |
|---------------------|---------------------|
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/4027bde4-c2ee-4f29-8d80-1a9a4b9cbbfb.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/1ba115ea-f741-47ac-a93d-77b22aec00eb.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/3538f9af-2a18-4481-adc3-322f62327036.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/dc815d6b-0666-4983-bb29-09bcd04f5349.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/668f2aa0-cf8b-493b-b9b8-8a11e2a786ec.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/da876246-abc1-4a2d-8330-144bc3ee4158.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/458d8474-8e64-441b-a456-4d9ab1f8848f.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/c2dda804-288b-4627-9eed-a958c2f9f421.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/65d81932-c17c-44b9-879a-7f8587ec6bb4.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/ba7bcfd2-bdd6-4794-a8cf-1641fabd27f6.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/60f35a99-c107-416d-bfee-2074c79952d2.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/b15bcb6b-4682-4ace-9339-ce47e92afab7.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/5e9ac82b-ef57-473f-809b-25f5935857b9.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/a7ccc275-5925-4c2e-90a6-adee830fe470.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/4f1e4aa5-e56d-4cde-9291-dff809ff5e16.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/18192a0f-fb0c-4f04-989e-67c180c05b21.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/2dae35c6-0985-4fd7-a310-b424b93130e1.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/7696f88d-7ad2-44c6-ba66-47f67698dda8.png) |

|  |  |
|---------------------|---------------------|
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/02582d0b-ac29-4935-81ca-cdfba3c0673c.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/9a63d43c-5b26-4e04-a3cf-5245176b3c44.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/91ac2d3e-cf18-4e68-b0f4-739f485d49e7.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/fbce5e05-3226-4778-8ed7-a19227806dfc.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/64df715b-f446-4cf1-925f-9f3336e6fbdf.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/f8f357c2-ceca-4b91-8f23-afe4ccb2a6d3.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/055a79cb-fe68-4790-bffd-0e9757509607.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/ee5c5a40-18a8-4953-949e-6a1a7bb8a38e.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/54fdc238-5307-4afb-a1fb-184a5633a789.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/6327002d-44e3-4a2f-b5bc-300120c9b538.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/49cb965f-5827-435b-809a-23aae1a39b3f.png) | ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/05316c95-4b1f-4e3d-ba99-dec0c5155978.png) |
| ![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/img/43a2e582-2bbf-486f-8e7a-16eb94b54ce2.png) |  |

![](https://fank-bucket-oss.oss-cn-beijing.aliyuncs.com/work/936e9baf53eb9a217af4f89c616dc19.png)

#### 演示视频

暂无

#### 获取方式

Email: fan1ke2ke@gmail.com

WeChat: `Storm_Berserker`

`附带部署与讲解服务，因为要恰饭资源非免费，伸手党勿扰，谢谢理解😭`

> 1.项目纯原创，不做二手贩子 2.一次购买终身有效 3.项目讲解持续到答辩结束 4.非常负责的答辩指导 5.黑奴价格

> 项目部署调试不好包退！功能逻辑没讲明白包退！

#### 其它资源

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

