const app = getApp();
let http = require('../../../utils/request')
Page({
	data: {
		StatusBar: app.globalData.StatusBar,
		CustomBar: app.globalData.CustomBar,
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
		defaultAddress: 1,
		addressId: 0,
	},
	onLoad: function (option) {
		this.setData({ addressId: option.addressId })
		this.getAddressInfo(option.addressId)
	},
	getAddressInfo(addressId) {
		http.get('addressInfoById', { addressId }).then((r) => {
			this.setData({
				name: r.data.contactPerson,
				address: r.data.address,
				phone: r.data.contactMethod,
				province: r.data.province,
				city: r.data.city,
				area: r.data.area,
				houseNumber: r.data.houseNumber,
				latitude: r.data.latitude,
				longitude: r.data.longitude,
				lnglat: r.data.longitude + ', ' + r.data.latitude,
				defaultAddress: r.data.defaultAddress
			})
		})
	},
	delete() {
		let that = this
		wx.showModal({
			title: '提示',
			content: '确定要删除吗？',
			success: function (sm) {
				if (sm.confirm) {
					http.get('address/delete', { addressId: that.data.addressId }).then((r) => {
						wx.showToast({
							title: '删除成功',
							icon: 'success',
							duration: 2000
						})
						setTimeout(() => {
							wx.redirectTo({
								url: '/pages/user/addresss/index'
							});
						}, 1000)
					})
				} else if (sm.cancel) {
					console.log('取消')
				}
			}
		})
	},
	edit() {
		wx.getStorage({
			key: 'userInfo',
			success: (res) => {
				let data = { contactPerson: this.data.name, contactMethod: this.data.phone, address: this.data.address, defaultAddress: this.data.defaultAddress, userId: res.data.id, id: this.data.addressId, province: this.data.province, city: this.data.city, area: this.data.area, houseNumber: this.data.houseNumber, latitude: this.data.latitude, longitude: this.data.longitude }
				this.addressEdit(data)
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
	getNameValue(e) {
		this.setData({ name: e.detail.value })
	},
	getAddressValue(e) {
		this.setData({ address: e.detail.value })
	},
	getPhoneValue(e) {
		this.setData({ phone: e.detail.value })
	},
	addressEdit(address) {
		http.post('addressEdit', address).then((r) => {
			wx.showToast({
				title: '修改成功',
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
