const app = getApp();
let http = require('../../../utils/request')
Page({
    data: {
        StatusBar: app.globalData.StatusBar,
        CustomBar: app.globalData.CustomBar,
        TabbarBot: app.globalData.tabbar_bottom,
        orderInfo: null,
        merchant: null,
        user: null,
        staff: [],
        commoditId: null,
        startAddress: null,
        endAddress: null,
        vehicle: null
    },
    onLoad: function (options) {
        this.getOrderDetail(options.orderId)
    },
    message(event) {
        wx.getStorage({
            key: 'userInfo',
            success: (res) => {
                wx.navigateTo({
                    url: '/pages/user/detail/index?takeUser=' + event.currentTarget.dataset.shopid + '&sendUser=' + res.data.id + '&otherName=' + event.currentTarget.dataset.shopname + ''
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
    takePhone() {
        wx.makePhoneCall({
            phoneNumber: this.data.user.phone
          })
    },
    getOrderDetail(orderId) {
        http.get('queryOrderDetail', {
            orderId: orderId
        }).then((r) => {
            let order = r.order
            if (order.images) {
                order.imageList = order.images.split(',')
            }
            this.setData({
                orderInfo: order,
                startAddress: r.startAddress,
                endAddress: r.endAddress,
                user: r.user
            })
        })
    }
});
