const app = getApp();
let http = require('../../../utils/request')
Page({
	data: {
		StatusBar: app.globalData.StatusBar,
		CustomBar: app.globalData.CustomBar,
		TabbarBot: app.globalData.tabbar_bottom,
		hidden: true,
		region: ['重庆市', '重庆市', '江北区'],
		name: '',
		phone: '',
		province: '',
		city: '',
		area: '',
		houseNumber: '',
		latitude: '',
		longitude: '',
		lnglat: '',
		address: '',
		defaultAddress: 1
	},
	onLoad: function (option) {
		// wx.getSetting({
		// 	success: res => {
		// 		if (!res.authSetting['scope.userInfo']) {
		// 			wx.redirectTo({
		// 				url: '/pages/auth/auth'
		// 			})
		// 		}
		// 	}
		// });
	},
	RegionChange: function (e) {
		this.setData({
			region: e.detail.value
		})
	},
	getProvinceValue(e) {
		this.setData({ province: e.detail.value })
	},
	getCityValue(e) {
		this.setData({ city: e.detail.value })
	},
	getAreaValue(e) {
		this.setData({ area: e.detail.value })
	},
	getHouseNumberValue(e) {
		this.setData({ houseNumber: e.detail.value })
	},
	getLnglatValue() {
		const _this = this;
		wx.chooseLocation({
			success(res) {
				_this.setData({
					latitude: res.latitude,
					longitude: res.longitude,
					lnglat: res.longitude + ', ' + res.latitude
				})
			},
			fail(e) {
				console.log(e);
			}
		})
	},
	add() {
		wx.getStorage({
			key: 'userInfo',
			success: (res) => {
				let data = { contactPerson: this.data.name, contactMethod: this.data.phone, address: this.data.address, defaultAddress: this.data.defaultAddress, userId: res.data.id, province: this.data.province, city: this.data.city, area: this.data.area, houseNumber: this.data.houseNumber, latitude: this.data.latitude, longitude: this.data.longitude }
				this.addressAdd(data)
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
	getNameValue(e) {
		this.setData({ name: e.detail.value })
	},
	getAddressValue(e) {
		this.setData({ address: e.detail.value })
	},
	getPhoneValue(e) {
		this.setData({ phone: e.detail.value })
	},
	addressAdd(address) {
		http.post('addressAdd', address).then((r) => {
			wx.showToast({
				title: '添加成功',
				icon: 'success',
				duration: 2000
			})
			setTimeout(() => {
				wx.redirectTo({
					url: '/pages/user/addresss/index'
				});
			}, 1000)
		})
	},
	switch1Change(event) {
		let defaultAddress = event.detail.value ? 1 : 0
		this.setData({ defaultAddress })
	}
});
