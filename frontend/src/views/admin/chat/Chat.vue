<template>
  <a-row style="width: 100%;" :gutter="20">
    <a-col :span="4">
      <a-menu mode="inline" style="width: 100%;margin-top: 30px" v-if="contactList.length !== 0">
        <a-menu-item :key="index" style="height: 80px;margin: 0 auto;text-align: center;line-height: 80px" v-for="(item, index) in contactList" @click="onChange(item)">
          <a-avatar
            :size="46"
            shape="square"
            icon="user"
            :src="'http://127.0.0.1:9527/imagesWeb/' + item.images.split(',')[0]"
          />
          <span style="margin-left: 25px;font-size: 15px">
            {{ item.name }}-{{ item.deptName }}
          </span>
        </a-menu-item>
      </a-menu>
      <div style="text-align: center;margin-top: 50px" v-else>
        <a-icon type="meh" theme="twoTone" style="font-size: 50px;"/>
        <p style="font-size: 20px;margin-top: 30px">暂无联系人</p>
      </div>
    </a-col>
    <a-col :span="20">
      <div style="background:#ECECEC; padding:30px">
        <a-card>
          <a-list
            class="comment-list"
            item-layout="horizontal"
            :data-source="chatList"
          >
            <a-list-item slot="renderItem" slot-scope="item, index">
              <a-comment style="margin-left: 25px" :author="(checkUserName(item))" :avatar="'http://127.0.0.1:9527/imagesWeb/' + (checkUserImages(item))">
                <p slot="content">
                  {{ item.content }}
                </p>
                <a-tooltip slot="datetime" :title="item.createDate">
                  <span>{{ item.createDate }}</span>
                </a-tooltip>
              </a-comment>
            </a-list-item>
          </a-list>
          <a-comment v-if="currentItem != null">
            <div slot="content">
              <a-form-item>
                <a-textarea :rows="4" v-model="contentValue"/>
              </a-form-item>
              <a-form-item>
                <a-button html-type="submit" type="primary" @click="handleSubmit">
                  Add Comment
                </a-button>
              </a-form-item>
            </div>
          </a-comment>
        </a-card>
      </div>
    </a-col>
  </a-row>
</template>

<script>
import {mapState} from 'vuex'
export default {
  name: 'Chat',
  computed: {
    ...mapState({
      multipage: state => state.setting.multipage,
      user: state => state.account.user
    })
  },
  data () {
    return {
      chatList: [],
      contactList: [],
      staffList: [],
      contentValue: '',
      currentItem: null
    }
  },
  mounted () {
    this.selectContactPerson()
    // this.queryStaffList()
  },
  methods: {
    checkUserName (item) {
      console.log(item.sendUser + '---' + item.takeUser)
      if (item.sendUserId == this.user.userId) {
        return item.sendUserName
      }
      if (item.takeUserId == this.user.userId) {
        return item.sendUserName
      }
      if (item.sendUserId == null) {
        return item.sendUserName
      }
      if (item.takeUserId == null) {
        return item.takeUserName
      }
    },
    checkUserImages (item) {
      if (item.sendUserId == this.user.userId) {
        return item.sendUserAvatar.split(',')[0]
      }
      if (item.takeUserId == this.user.userId) {
        return item.sendUserAvatar.split(',')[0]
      }
      if (item.sendUserId == null) {
        return item.sendUserAvatar.split(',')[0]
      }
      if (item.takeUserId == null) {
        return item.takeUserAvatar.split(',')[0]
      }
    },
    queryStaffList () {
      this.$get(`/cos/staff-info/queryStaffList/staff`, {
        staffId: this.user.userId
      }).then((r) => {
        this.contactList = r.data.data
      })
    },
    selectContactPerson () {
      this.$get(`/cos/message-info/contact/person`, {
        userId: this.user.userId,
        flag: 1
      }).then((r) => {
        this.contactList = r.data.data
      })
    },
    onChange (item) {
      this.currentItem = item
      this.$get(`/cos/message-info/getMessageDetail`, {
        takeUser: this.user.userId,
        sendUser: item.id
      }).then((r) => {
        this.chatList = r.data.data
      })
    },
    handleSubmit () {
      if (this.contentValue === '') {
        this.$message.error('请输入消息')
        return false
      }
      this.$post(`/cos/message-info`, {
        sendUser: this.user.userId,
        takeUser: this.currentItem.id,
        content: this.contentValue
      }).then((r) => {
        this.contentValue = ''
        this.onChange(this.currentItem)
      })
    }
  }
}
</script>
<style scoped>

</style>
