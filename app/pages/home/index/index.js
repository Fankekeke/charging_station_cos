const app = getApp();
let http = require('../../../utils/request')
Page({
	data: {
		StatusBar: app.globalData.StatusBar,
		CustomBar: app.globalData.CustomBar,
		hidden: true,
		current: 0,
		lines: 0,
		swiperlist: [{
			id: 0,
			url: 'http://www.chahtq.com/Upload/banner/banner1.jpg',
			type: 1
		}, {
			id: 1,
			url: 'http://www.chahtq.com/Upload/banner/banner2.jpg',
			type: 2
		}, {
			id: 2,
			url: 'http://news.cjn.cn/zjjjdpd/jtys_20051/202407/W020240709574295671398.jpg',
			type: 3
		}],
		iconList: [{
			id: 1,
			icon: 'questionfill',
			color: 'red',
			name: '公告',
			type: 1
		}, {
			id: 3,
			icon: 'shopfill',
			color: 'yellow',
			name: '论坛',
			type: 3
		}],
		Headlines: [{
			id: 1,
			title: "配送员入驻",
			type: 1
		}, {
			id: 2,
			title: "配送大作战",
			type: 2
		}],
		shopInfo: [],
		postInfo: [],
		orderList: [],
		commodityHot: [],
		keys: '',
		videosrc: "http://wxsnsdy.tc.qq.com/105/20210/snsdyvideodownload?filekey=30280201010421301f0201690402534804102ca905ce620b1241b726bc41dcff44e00204012882540400&bizid=1023&hy=SH&fileparam=302c020101042530230204136ffd93020457e3c4ff02024ef202031e8d7f02030f42400204045a320a0201000400",
		addressList: [],
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
		userInfo: null,
		// 新增地图相关数据
		latitude: 39.813911,  // 默认纬度
		longitude: 116.496507,  // 默认经度
		markers: []  // 标记点数组
	},
	onLoad: function () {
		this.home()
	},
	onMarkerTap(e) {
		console.log(e)
		const markerId = e.markerId;
		const marker = this.data.markers.find(m => m.id === markerId);
		const order = this.data.orderList.find(o => o.id === markerId);

		if (order) {
			wx.navigateTo({
				url: `/pages/shop/index/index?shopId=${order.id}`
			});
		}
	},
	onShow() {
		wx.getStorage({
			key: 'userInfo',
			success: (res) => {
				this.setData({
					userInfo: res.data
				})
			},
			fail: res => {

			}
		})
	},
	getUserAddress(userId) {
		http.get('selDefaultAddress', {
			userId
		}).then((r) => {
			r.data.forEach(item => {
				item.text = item.address
			})
			this.setData({
				addressList: r.data
			})
		})
	},
	onClose(e) {
		if (e.currentTarget.dataset.type == 1) {
			this.setData({
				'startPoint.show': false,
			})
		} else if (e.currentTarget.dataset.type == 2) {
			this.setData({
				'endPoint.show': false,
			})
		}
	},
	onChange(e) {
		if (e.currentTarget.dataset.type == 1) {
			this.setData({
				'startPoint.address': e.detail.value.address,
				'startPoint.id': e.detail.value.id,
				'startPoint.show': false
			})
		} else if (e.currentTarget.dataset.type == 2) {
			this.setData({
				'endPoint.address': e.detail.value.address,
				'endPoint.id': e.detail.value.id,
				'endPoint.show': false
			})
		}
	},
	/**
	 * 选择位置
	 */
	startChooseLocation() {
		const _this = this;
		wx.chooseLocation({
			success(res) {
				console.log(res)
				_this.setData({
					['startPoint.startAddress']: res.address,
					['startPoint.point']: {
						latitude: res.latitude,
						longitude: res.longitude
					},
				})
			},
			fail(e) {
				console.log(e);
			}
		})
	},
	/**
	 * 选择位置
	 */
	endChooseLocation() {
		const _this = this;
		wx.chooseLocation({
			success(res) {
				console.log(res)
				_this.setData({
					['endPoint.endAddress']: res.address,
					['endPoint.point']: {
						latitude: res.latitude,
						longitude: res.longitude
					},
				})
			},
			fail(e) {
				console.log(e);
			}
		})
	},
	openPopup(e) {
		if (e.currentTarget.dataset.type == 1) {
			this.setData({
				'startPoint.show': true,
			})
		} else if (e.currentTarget.dataset.type == 2) {
			this.setData({
				'endPoint.show': true,
			})
		}
	},
	submit() {
		console.log(this.data.startPoint)
		console.log(this.data.endPoint)
		if (this.data.startPoint.id == null || this.data.endPoint.id == null) {
			wx.showToast({
				title: '请选择地址',
				icon: 'error',
				duration: 1000
			})
			return false;
		}
		let param = {
			startPoint: this.data.startPoint,
			endPoint: this.data.endPoint
		}
		wx.navigateTo({
			url: '/pages/scar/order/index?address=' + JSON.stringify(param)
		});
	},
	shopDeatil(e) {
		wx.navigateTo({
			url: '/pages/shop/index/index?shopId=' + e.currentTarget.dataset.shopid + ''
		});
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
	home() {
		let that = this;
		wx.getLocation({
			type: 'wgs84',
			success(res) {
				console.log(res);
				const latitude = res.latitude;
				const longitude = res.longitude;

				// 设置当前定位点
				that.setData({
					latitude: latitude,
					longitude: longitude
				});

				http.get('home/user', {
					latitude,
					longitude,
					userId: that.data.userInfo.id
				}).then((r) => {
					r.postInfo.forEach(item => {
						if (item.images) {
							item.image = item.images.split(',')[0];
						}
						item.days = that.timeFormat(item.createDate);
					});

					r.orderList.forEach(item => {
						if (item.images) {
							item.image = item.images.split(',')[0];
						}
					});

					// 处理充电桩标记点
					const markers = r.orderList.map(item => ({
						id: item.id,
						latitude: item.latitude,
						longitude: item.longitude,
						title: item.name,
						callout: {
							content: `${item.name}\n地址: ${item.address}\n距离: ${item.kilometre}千米`,
							display: 'ALWAYS',
							fontSize: 12,
							borderRadius: 5,
							borderWidth: 1,
							borderColor: '#ccc',
							bgColor: '#fff',
							padding: 10,
							boxShadow: '0 2px 6px rgba(0, 0, 0, 0.1)'
						},
						iconPath: '/img/充电桩.png',
						width: 40,
						height: 40,
						anchor: { x: 0.5, y: 1 }, // 设置锚点，让图标底部中间对准坐标点
					}));

					that.setData({
						postInfo: r.postInfo,
						orderList: r.orderList,
						markers: markers
					});
				});
			},
			fail(err) {
				console.error('获取位置失败:', err);
				// 如果获取位置失败，使用默认位置
				http.get('home/user', {
					latitude: that.data.latitude,
					longitude: that.data.longitude,
					userId: that.data.userInfo.id
				}).then((r) => {
					r.postList.forEach(item => {
						if (item.images) {
							item.image = item.images.split(',')[0];
						}
						item.days = that.timeFormat(item.createDate);
					});

					r.orderList.forEach(item => {
						if (item.images) {
							item.image = item.images.split(',')[0];
						}
					});

					// 处理充电桩标记点
					const markers = r.orderList.map(item => ({
						id: item.id,
						latitude: item.latitude,
						longitude: item.longitude,
						title: item.name,
						callout: {
							content: item.name + '\n' + item.address,
							display: 'ALWAYS'
						},
						iconPath: '/img/marker.png',
						width: 30,
						height: 30
					}));

					that.setData({
						postInfo: r.postInfo,
						orderList: r.orderList,
						markers: markers
					});
				});
			}
		});
	},
	postDetail(event) {
		wx.navigateTo({
			url: '/pages/coupon/detail/index?postId=' + event.currentTarget.dataset.postid + ''
		});
	},
	swiperchange: function (e) {
		this.setData({
			current: e.detail.current
		});
	},
	swipclick: function (e) {
		let that = this;
		var swip = that.data.swiperlist[that.data.current];
		console.log(swip);
		if (swip.type === 1) {
			wx.navigateTo({
				url: '/pages/home/doc/index?id=' + swip.id
			});
		}
	},
	lineschange: function (e) {
		this.setData({
			lines: e.detail.current
		});
	},
	linesclick: function (e) {
		let that = this;
		var swip = that.data.Headlines[that.data.current];
		console.log(swip);
		if (swip.type === 1) {
			wx.navigateTo({
				url: '/pages/home/doc/index?id=' + swip.id
			});
		}
	},
	itemckcred: function (e) {
		let that = this;
		var item = e.currentTarget.dataset;
		console.log(item.index, item.itemtype)
		if (item.itemtype === 1) {
			wx.navigateTo({
				url: '/pages/home/bulletin/index'
			});
		}
		if (item.itemtype === 2) {
			wx.navigateTo({
				url: '/pages/home/groom/index'
			});
		}
		if (item.itemtype === 3) {
			wx.navigateTo({
				url: '/pages/coupon/index/index'
			});
		}
		if (item.itemtype === 4) {
			wx.navigateTo({
				url: '/pages/home/search/index?key=生鲜'
			});
		}
	},
	getKeysValue: function (e) {
		this.setData({
			keys: e.detail.value
		})
	},
	search: function () {
		wx.navigateTo({
			url: '/pages/home/search/index?key=' + this.data.keys + ''
		});
	}
});
