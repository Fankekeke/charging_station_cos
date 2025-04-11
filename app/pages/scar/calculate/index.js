const app = getApp();
let http = require('../../../utils/request')
Page({
    data: {
        StatusBar: app.globalData.StatusBar,
        CustomBar: app.globalData.CustomBar,
        TabbarBot: app.globalData.tabbar_bottom,
        orderType: 0,
        orderIds: [],
        userData: null,
        memberData: null,
        goods: null,
        shopInfo: null,
        addressInfo: null,
        commodityId: null,
        orderList: [],
        allPrice: 0,
        fileList: [],
        userInfo: null,
        startPoint: {
            show: false,
            id: null,
            address: '',
            point: null
        },
        endPoint: {
            show: false,
            id: null,
            address: '',
            point: null
        },
        vehicle: {
            show: false,
            label: '小型车',
            value: '2',
            options: ['大型车', '中型车', '小型车']
        },
        vehicleData: [],
        goodsType: {
            show: false,
            label: '',
            value: null,
            options: [{
                id: 1,
                text: '文件'
            }, {
                id: 2,
                text: '食品'
            }, {
                id: 3,
                text: '蛋糕'
            }, {
                id: 4,
                text: '数码'
            }, {
                id: 5,
                text: '证件'
            }, {
                id: 6,
                text: '药品'
            }, {
                id: 7,
                text: '海鲜'
            }, {
                id: 8,
                text: '鲜花'
            }, {
                id: 9,
                text: '服饰'
            }, {
                id: 10,
                text: '其他'
            }]
        },
        staff: {
            show: false,
            label: '一个',
            value: '1',
            options: ['不需要', '一个', '两个', '三个']
        },
        elevator: {
            show: false,
            label: '有',
            value: '1',
            options: ['无', '有']
        },
        remark: {
            show: false,
            value: ''
        },
        orderId: null,
        appointmentTime: '',
        appointmentTimeShow: false,
        calculateAmountInfo: null,
        discount: {
            show: false,
            label: '',
            value: '',
            options: []
        },
        orderInfo: null,
        weight: '默认30分钟',
        width: null,
        height: null
    },
    onLoad: function (options) {
        wx.getStorage({
            key: 'userInfo',
            success: (res) => {
                console.log(options.orderId)
                this.setData({
                    userInfo: res.data,
                    orderId: options.orderId
                })
                this.queryOrderDetailById(options.orderId, -1);
                //this.getUserAddress(res.data.id)
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
    queryVehicleListById(userId) {
        http.get('queryVehicleLessListById', {
            userId: userId
        }).then((r) => {
            this.setData({
                vehicleData: r.data
            })
        })
    },
    queryOrderDetailById(orderId, discountId) {
        http.get('queryOrderDetailById', {
            orderId: orderId,
            discountId
        }).then((r) => {
            r.shopInfo.image = r.shopInfo.images.split(',')[0]
            let discountInfos = r.orderInfo.discountInfos
            if (discountInfos) {
                discountInfos.forEach(item => {
                    item.text = item.couponName
                })
            }
            this.setData({
                userData: r.user,
                memberData: r.member,
                goods: r.spaceInfo,
                shopInfo: r.shopInfo,
                orderInfo: r.orderInfo,
                'discount.options': discountInfos
            })
        })
    },
    queryMemberByUserId(userId) {
        http.get('queryMemberByUserId', {
            userId: userId
        }).then((r) => {
            this.setData({
                userData: r.user,
                memberData: r.member
            })
        })
    },
    getGoodsDetail(spaceId) {
        http.get('getGoodsDetail', {
            spaceId: spaceId
        }).then((r) => {
            console.log(r.spaceInfo)
            r.shopInfo.image = r.shopInfo.images.split(',')[0]
            this.setData({
                goods: r.spaceInfo,
                shopInfo: r.shopInfo
            })
        })
    },
    calculateAmountResult() {
        http.post('orderPay', this.data.orderInfo).then((r) => {
            wx.showLoading({
                title: '正在模拟支付',
            })
            setTimeout(() => {
                wx.showToast({
                    title: '支付成功',
                    icon: 'success',
                    duration: 1000
                })
                setTimeout(() => {
                    wx.switchTab({
                        url: '/pages/user/index/index'
                    });
                }, 1000)
            }, 1000)
        })
    },
    afterRead(event) {
        const {
            file
        } = event.detail;
        let that = this
        // 当设置 mutiple 为 true 时, file 为数组格式，否则为对象格式
        wx.uploadFile({
            url: 'http://127.0.0.1:9527/file/fileUpload', // 仅为示例，非真实的接口地址
            filePath: file.url,
            name: 'avatar',
            success(res) {
                // 上传完成需要更新 fileList
                const {
                    fileList = []
                } = that.data;
                fileList.push({
                    ...file,
                    url: res.data
                });
                that.setData({
                    fileList
                });
            },
        });
    },
    queryUseDiscountByUserId(userId, amount) {
        http.get('queryUseDiscountByUserId', {
            userId,
            amount
        }).then((r) => {
            r.data.forEach(item => {
                item.text = item.couponName
            })
            this.setData({
                'discount.options': r.data
            })
        })
    },
    openPopup(e) {
        console.log(e.currentTarget.dataset.type)
        if (e.currentTarget.dataset.type == 1) {
            this.setData({
                'goodsType.show': true,
            })
        }
        if (e.currentTarget.dataset.type == 5) {
            this.setData({
                'discount.show': true,
            })
        }
    },
    onClose(e) {
        if (e.currentTarget.dataset.type == 1) {
            this.setData({
                'goodsType.show': false,
            })
        } else if (e.currentTarget.dataset.type == 2) {
            this.setData({
                'staff.show': false,
            })
        } else if (e.currentTarget.dataset.type == 3) {
            this.setData({
                'elevator.show': false,
            })
        } else if (e.currentTarget.dataset.type == 4) {
            this.setData({
                appointmentTimeShow: false,
            })
        } else if (e.currentTarget.dataset.type == 5) {
            this.setData({
                'discount.show': false,
            })
        }
    },
    onRemarkChange(e) {
        this.setData({
            'remark.value': e.detail,
        })
    },
    formChange(e) {
        if (e.currentTarget.dataset.type == 1) {
            this.setData({
                'weight': e.detail,
            })
        } else if (e.currentTarget.dataset.type == 2) {
            this.setData({
                'width': e.detail
            })
        } else if (e.currentTarget.dataset.type == 3) {
            this.setData({
                'height': e.detail
            })
        }
    },
    onChange(e) {
        if (e.currentTarget.dataset.type == 1) {
            this.setData({
                'goodsType.label': e.detail.value.text,
                'goodsType.value': e.detail.value.id,
                'goodsType.show': false
            })
        } else if (e.currentTarget.dataset.type == 2) {
            this.setData({
                'staff.label': e.detail.value,
                'staff.value': e.detail.index,
                'staff.show': false
            })
        } else if (e.currentTarget.dataset.type == 3) {
            this.setData({
                'elevator.label': e.detail.value,
                'elevator.value': e.detail.index,
                'elevator.show': false
            })
        } else if (e.currentTarget.dataset.type == 5) {
            this.setData({
                'discount.label': e.detail.value.couponName,
                'discount.value': e.detail.value.id,
                'discount.show': false
            })
            this.queryOrderDetailById(this.data.orderId, e.detail.value.id)
        }
    },
    onConfirm(event) {
        this.setData({
            appointmentTimeShow: false,
            appointmentTime: this.formatDate(event.detail),
        });
    },
    formatDate(date) {
        date = new Date(date);
        return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
    },
    getUserAddress(userId) {
        http.get('selDefaultAddress', {
            userId
        }).then((r) => {
            this.setData({
                addressInfo: r.data
            })
        })
    },
    goodsDetail(commodityId) {
        http.get('goodsDetail', {
            commodityId
        }).then((r) => {
            let order = r
            let allPrice = order.price
            order.image = order.images.split(',')[0]
            this.setData({
                orderList: [order],
                allPrice: allPrice.toFixed(2)
            })
        })
    },
    getOrderList(orderIds) {
        http.get('selOrderListByOrderIds', {
            ids: orderIds.join(',')
        }).then((r) => {
            let allPrice = 0
            r.data.forEach(item => {
                item.image = item.images.split(',')[0]
                allPrice += item.price
            });
            this.setData({
                orderList: r.data,
                allPrice: allPrice.toFixed(2)
            })
        })
    },
    submit() {
        if (this.data.fileList.length === 0) {
            wx.showToast({
                title: '请上传货物图片',
                icon: 'none',
                duration: 2000
            })
            return false
        }
        wx.showLoading({
            title: '正在模拟支付',
        })
        setTimeout(() => {
            let images = []
            this.data.fileList.forEach(item => {
                images.push(item.url)
            });
            let data = {
                userId: this.data.userInfo.id,
                total: this.data.calculateAmountInfo.orderPrice,
                goodsType: this.data.goodsType.value,
                weight: this.data.weight,
                height: this.data.height,
                width: this.data.width,
                startAddressId: this.data.startPoint.id,
                endAddressId: this.data.endPoint.id,
                discountId: this.data.discount.value,
                kilometre: this.data.calculateAmountInfo.kilometre,
                distributionPrice: this.data.calculateAmountInfo.distributionPrice,
                weightPrice: this.data.calculateAmountInfo.weightPrice,
                orderPrice: this.data.calculateAmountInfo.orderPrice,
                afterOrderPrice: this.data.calculateAmountInfo.afterOrderPrice,
                integral: this.data.calculateAmountInfo.afterOrderPrice,
                remark: this.data.remark.value,
                images: images.length !== 0 ? images.join(',') : null
            }
            http.post('addOrder', data).then((r) => {
                wx.showToast({
                    title: '支付成功',
                    icon: 'success',
                    duration: 1000
                })
                setTimeout(() => {
                    wx.navigateBack({
                        changed: true
                    });
                }, 1000)
            })
            wx.hideLoading()
        }, 1000)
    }
});