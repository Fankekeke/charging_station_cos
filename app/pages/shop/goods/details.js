const app = getApp();
let http = require('../../../utils/request')
Page({
    data: {
        StatusBar: app.globalData.StatusBar + 6,
        TabbarBot: app.globalData.tabbar_bottom,
        swiperlist: [],
        goods: null,
        commoditId: null,
        startAddress: null,
        endAddress: null,
        user: null,
        evaluation: [],
        shopInfo: null,
        staffInfo: null
    },
    onLoad: function (options) {
        console.log(options.commoditId)
        this.setData({
            commoditId: options.commoditId
        })
        this.getGoodsDetail(options.commoditId)
    },
    takePhone() {
        wx.makePhoneCall({
            phoneNumber: this.data.shopInfo.phone
        })
    },
    getGoodsDetail(commodityId) {
        http.get('getGoodsDetail', {
            spaceId: commodityId
        }).then((r) => {
            console.log(r.spaceInfo)
            r.shopInfo.image = r.shopInfo.images.split(',')[0]
            this.setData({
                swiperlist: r.spaceInfo.images.split(','),
                goods: r.spaceInfo,
                shopInfo: r.shopInfo
            })
        })
    },
    getEvaluationByGoods(commodityId) {
        http.get('getEvaluationByGoods', {
            commodityId
        }).then((r) => {
            this.setData({
                evaluation: r.data
            })
        })
    },
    shopDetail() {
        let that = this
        wx.getLocation({
            type: 'gcj02', // 默认为 wgs84 返回 gps 坐标，gcj02 返回可用于 wx.openLocation 的坐标
            success: function (res) {
                wx.openLocation({
                    latitude: Number(that.data.shopInfo.latitude), // 纬度，范围为-90~90，负数表示南纬
                    longitude: Number(that.data.shopInfo.longitude), // 经度，范围为-180~180，负数表示西经
                    scale: 28, // 缩放比例
                    name: that.data.shopInfo.address,
                    address: that.data.shopInfo.address
                })
            }
        })
    },
    message() {
        wx.getStorage({
            key: 'userInfo',
            success: (res) => {
                http.get('getUserInfoById', {
                    userId: res.data.id
                }).then((r) => {
                    wx.navigateTo({
                        url: '/pages/user/detail/index?takeUser=' + this.data.shopInfo.userInfoId + '&sendUser=' + res.data.id + '&otherName=' + this.data.shopInfo.name + ''
                    });
                })
            },
            fail: res => {
                wx.showToast({
                    title: '请先进行登录',
                    icon: 'none',
                    duration: 2000
                })
            }
        })
    },
    cartView() {
        wx.switchTab({
            url: '/pages/scar/index/index'
        })
    },
    addCart() {
        if (this.data.goods.onPut == 0) {
            wx.showToast({
                title: '该商品已下架',
                icon: 'none',
                duration: 2000
            })
        } else {
            wx.getStorage({
                key: 'userInfo',
                success: (res) => {
                    http.get('selUserCartByGoodsId', {
                        userId: res.data.id,
                        commodityId: this.data.goods.id
                    }).then((r) => {
                        if (r.data != 0) {
                            wx.showToast({
                                title: '商品已在购物车',
                                icon: 'none',
                                duration: 2000
                            })
                        } else {
                            let data = {
                                userId: res.data.id,
                                commodityId: this.data.goods.id,
                                price: this.data.goods.price,
                            }
                            http.post('addGoodsCart', data).then((r) => {
                                wx.showToast({
                                    title: '添加购物车成功',
                                    icon: 'success',
                                    duration: 2000
                                })
                            })
                        }
                    })
                },
                fail: res => {
                    wx.showToast({
                        title: '请先进行登录',
                        icon: 'none',
                        duration: 2000
                    })
                }
            })
        }
    },
    reserveGoods() {
        if (this.data.goods.status != 0) {
            wx.showToast({
                title: '当前充电桩已被使用',
                icon: 'none',
                duration: 1000
            })
            return false
        }
        wx.getStorage({
            key: 'userInfo',
            success: (res) => {
                wx.navigateTo({
                    url: '/pages/scar/order/index?spaceId=' + this.data.commoditId
                });
            },
            fail: res => {
                wx.showToast({
                    title: '请先进行登录',
                    icon: 'none',
                    duration: 2000
                })
            }
        })
    },
    buyGoods() {
        if (this.data.goods.status != 0) {
            wx.showToast({
                title: '当前充电桩已被使用',
                icon: 'none',
                duration: 1000
            })
            return false
        }
        wx.getStorage({
            key: 'userInfo',
            success: (res) => {
                http.get('getUserInfoById', {
                    userId: res.data.id
                }).then((r) => {
                    if (r.data.type == null || r.data.type == 1) {
                        wx.showToast({
                            title: '请先申请成为配送员',
                            icon: 'none',
                            duration: 1000
                        })
                    } else if (r.data.type != null && r.data.type == 2) {
                        http.get('checkOrder', {
                            orderId: this.data.goods.id,
                            staffId: res.data.id
                        }).then((r) => {
                            if (r.data == null) {
                                wx.showToast({
                                    title: '请先设置默认收货地址',
                                    icon: 'none',
                                    duration: 1000
                                })
                            } else {
                                wx.navigateBack({
                                    delta: 1
                                })
                            }
                        })
                    }
                })
            },
            fail: res => {
                wx.showToast({
                    title: '请先进行登录',
                    icon: 'none',
                    duration: 2000
                })
            }
        })
    }
});