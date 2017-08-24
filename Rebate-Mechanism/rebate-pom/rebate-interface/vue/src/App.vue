<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'app',
  data () {
    return {
      dataFlag: false
    }
  },
  created () {
    this.logon()
  },
  methods: {
    logon () {
      let loginDatas = {
        cellPhoneNum: this.$store.getters.getCellPhoneNum,
        password: this.$store.getters.getPassword
      }
      this.$api.login(loginDatas)
         .then(res => {
           console.log(res)
           if (res.code === '0000') {
             this.$store.dispatch('setCellPhoneNum', {cellPhoneNum: res.msg.cellPhoneNum})
             this.$store.dispatch('setUserId', {userId: res.msg.id})
             this.$store.dispatch('updateToken', {token: res.token})
             this.$router.push('bankList')
           } else {
             this.$messagebox(res.desc)
           }
         })
          .catch(error => {
            console.log(error)
          })
    },
    analysisSearch () {
      let url = window.location.search
      let theRequest = {}
      if (url.indexOf('?') !== -1) {
        let str = url.substr(1)
        let strs = str.split('&')
        for (var i = 0; i < strs.length; i++) {
          theRequest[strs[i].split('=')[0]] = unescape(strs[i].split('=')[1])
        }
      }
      console.log(theRequest)
      let params = ['userId', 'token', 'amount', 'clientIP', 'goodsName', 'orderSn']
      for (let index in params) {
        console.log(theRequest[params[index]])
        let param = params[index]
        if (!theRequest[param]) {
          this.dataFlag = true
          break
        } else {
          switch (param) {
            case 'userId':
              this.$store.dispatch('setUserId', {userId: theRequest[param]})
              break
            case 'token':
              this.$store.dispatch('updateToken', {token: theRequest[param]})
              break
            case 'amount':
              this.$store.dispatch('setAmount', {amount: theRequest[param]})
              break
            case 'clientIP':
              this.$store.dispatch('setClientIP', {clientIP: theRequest[param]})
              break
            case 'goodsName':
              this.$store.dispatch('setGoodsName', {goodsName: theRequest[param]})
              break
            case 'orderSn':
              this.$store.dispatch('setOrderSn', {orderSn: theRequest[param]})
              break
            default:
          }
        }
      }
      if (!this.dataFlag) {
        window.location.search = ''
        this.$router.push('bankList')
      } else {
        this.$router.push('404')
      }
    }
  }
}
</script>

<style>
body{
  margin:0;
  padding: 0;
}
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}
.icon {
   width: 1em; height: 1em;
   vertical-align: -0.15em;
   fill: currentColor;
   overflow: hidden;
}
</style>
