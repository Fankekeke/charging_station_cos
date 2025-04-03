const app = getApp();
let http = require('../../../utils/request')
Page({
    data: {
        StatusBar: app.globalData.StatusBar,
        CustomBar: app.globalData.CustomBar,
        TabbarBot: app.globalData.tabbar_bottom,
        TabCur: 1,
        scrollLeft: 0,
        SortMenu: [{
            id: 1,
            name: "等待接单"
        }, {
            id: 2,
            name: "正在配送"
        }, {
            id: 3,
            name: "配送完成"
        }],
        userInfo: null,
        orderListCopy: [],
        orderList: [],
        myOrderList: [],
        show: false,
        value: 3,
        remarks: '',
        orderId: null,
        rate: 5,
        fileList: [],
        complaintShow: false,
        complaintRemarks: '',
    },
    onLoad: function (options) {
        wx.getStorage({
            key: 'userInfo',
            success: (res) => {
                this.setData({
                    userInfo: res.data
                })
                this.getOrderListByUserId(res.data.id)
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
    afterRead(event) {
        const { file } = event.detail;
        let that = this
        // 当设置 mutiple 为 true 时, file 为数组格式，否则为对象格式
        wx.uploadFile({
          url: 'http://127.0.0.1:9527/file/fileUpload', // 仅为示例，非真实的接口地址
          filePath: file.url,
          name: 'avatar',
          success(res) {
            // 上传完成需要更新 fileList
            const { fileList = [] } = that.data;
            fileList.push({ ...file, url: res.data });
            that.setData({ fileList });
          },
        });
      },
    evaluateChange(event) {
        this.setData({
          rate: event.detail,
        });
        console.log(this.data.rate)
      },
    getOrderByUserId(userId) {
        http.get('getOrderByUserId', {
            userId
        }).then((r) => {
            r.data.forEach(item => {
                if (item.images) {
                    item.image = item.images.split(',')[0]
                }
                item.days = this.timeFormat(item.createDate)
            });
            this.setData({
                myOrderList: r.data
            })
        })
    },
    onChange: function (e) {
        this.setData({
            value: e.detail
        })
    },
    evaluation: function (e) {
        this.setData({
            show: true,
            orderId: e.currentTarget.dataset['index']
        })
    },
    complaint: function (e) {
        this.setData({
            complaintShow: true,
            orderId: e.currentTarget.dataset['index']
        })
    },
    receipt: function (e) {
        wx.showModal({
            title: '提示',
            content: '确定要运输完成吗？',
            success: (sm) => {
                if (sm.confirm) {
                    http.get('receipt', {
                        orderId: e.currentTarget.dataset['index']
                    }).then((r) => {
                        wx.showToast({
                            title: '运输结束',
                            icon: 'success',
                            duration: 1000
                        })
                        setTimeout(() => {
                            this.getOrderListByUserId(this.data.userInfo.id)
                        }, 1000)
                    })
                } else if (sm.cancel) {
                    console.log('用户点击取消')
                }
            }
        })
    },
    complaintSubmit () {
        if (this.data.complaintRemarks != '') {
            http.get('complaintAdd', {
                orderId: this.data.orderId,
                content: this.data.complaintRemarks,
                userId: this.data.userInfo.id
            }).then((r) => {
                this.setData({
                    complaintShow: false
                })
                wx.showToast({
                    title: '投诉提交成功',
                    icon: 'success',
                    duration: 1000
                })
                setTimeout(() => {
                    this.getOrderListByUserId(this.data.userInfo.id)
                }, 1000)
            })
        } else {
            wx.showToast({
                title: '请填写投诉内容',
                icon: 'none',
                duration: 1000
            })
        }
    },
    evaluationSubmit: function (e) {
        let that = this
        if (this.data.remarks != '') {
            let images = []
            this.data.fileList.forEach(item => {
                images.push(item.url)
            });
            http.post('evaluationAdd', {
                orderId: this.data.orderId,
                overScore: this.data.rate,
                content: this.data.remarks,
                userId: this.data.userInfo.id,
                images: images.length !== 0 ? images.join(',') : null
            }).then((r) => {
                that.setData({
                    show: false
                })
                wx.showToast({
                    title: '评价成功',
                    icon: 'success',
                    duration: 1000
                })
                setTimeout(() => {
                    this.getOrderListByUserId(this.data.userInfo.id)
                }, 1000)
            })
        } else {
            wx.showToast({
                title: '请填写评价内容',
                icon: 'none',
                duration: 1000
            })
        }
    },
    onComplaintClose() {
        this.setData({
            complaintShow: false
        })
    },
    onClose: function () {
        this.setData({
            show: false
        })
    },
    timeFormat(time) {
        var nowTime = new Date();
        var day = nowTime.getDate();
        var hours = parseInt(nowTime.getHours());
        var minutes = nowTime.getMinutes();
        // 开始分解付入的时间
        var timeday = time.substring(8, 10);
        var timehours = parseInt(time.substring(11, 13));
        var timeminutes = time.substring(14, 16);
        var d_day = Math.abs(day - timeday);
        var d_hours = hours - timehours;
        var d_minutes = Math.abs(minutes - timeminutes);
        if (d_day <= 1) {
            switch (d_day) {
                case 0:
                    if (d_hours == 0 && d_minutes > 0) {
                        return d_minutes + '分钟前';
                    } else if (d_hours == 0 && d_minutes == 0) {
                        return '1分钟前';
                    } else {
                        return d_hours + '小时前';
                    }
                    break;
                case 1:
                    if (d_hours < 0) {
                        return (24 + d_hours) + '小时前';
                    } else {
                        return d_day + '天前';
                    }
                    break;
            }
        } else if (d_day > 1 && d_day < 10) {
            return d_day + '天前';
        } else {
            return time;
        }
    },
    getOrderListByUserId(userId) {
        http.get('getOrderListByUserId', {
            userId
        }).then((r) => {
            r.data.forEach(item => {
                console.log(item.images)
                if (item.images) {
                    item.image = item.images.split(',')[0]
                }
                item.days = this.timeFormat(item.createDate)
            });
            
            let orderList = []
            r.data.forEach(item => {
                if (item.status == 1) {
                    orderList.push(item)
                }
            });
            this.setData({
                orderList: orderList,
                orderListCopy: r.data
            })
        })
    },
    tabSelect(e) {
        console.log(e.currentTarget.dataset.id);
        this.setData({
            TabCur: e.currentTarget.dataset.id,
            scrollLeft: (e.currentTarget.dataset.id - 1) * 60
        })
        // if (e.currentTarget.dataset.id == 0) {
        //     this.setData({
        //         orderList: this.data.orderListCopy
        //     })
        // }
        let orderList = []
            this.data.orderListCopy.forEach(item => {
                if (item.status == e.currentTarget.dataset.id) {
                    orderList.push(item)
                }
            });
            this.setData({
                orderList
            })
            console.log(this.data.orderList)

        // if (e.currentTarget.dataset.id == 1) {
        //     let orderList = []
        //     this.data.orderListCopy.forEach(item => {
        //         if (item.status == 1) {
        //             orderList.push(item)
        //         }
        //     });
        //     this.setData({
        //         orderList
        //     })
        // }
        // if (e.currentTarget.dataset.id == 2) {
        //     let orderList = []
        //     this.data.orderListCopy.forEach(item => {
        //         if (item.orderStatus == 3) {
        //             orderList.push(item)
        //         }
        //     });
        //     this.setData({
        //         orderList
        //     })
        // }
    }
});