<template>
	<div class="paid">
		<div class="info">
			<h3>向 {{goodsName}} 支付</h3>
      <h3>{{amount}}￥</h3>
		</div>
		<div class="content">
      <mt-cell title="订单金额" :value="amount" ></mt-cell>
      <mt-cell title="商户名称" :value="goodsName"></mt-cell>
			<mt-cell title="银行名称" :value="cardItem.bankName" ></mt-cell>
			<mt-cell title="银行卡号" :value="cardItem.cardNo" ></mt-cell>
      <mt-cell title="银行卡类型" :value="type" ></mt-cell>
			<mt-cell title="手机号" :value="cardItem.bankMobileNo" ></mt-cell>
			<mt-field label="验证码" placeholder="输入验证码" v-model="captcha">
				<mt-button type="primary" @click.native="handleCaptcha" :disabled="disabled">{{captchaMsg}}</mt-button>
			</mt-field>
			<mt-button class='btn-paid' size="large" type="danger" @click.native="handleClick" :disabled="submitBtnStatus">立即支付</mt-button>
		</div>
	</div>
</template>
<script>
export default {
  name: 'paid',
  data () {
    return {
      amount: this.$store.state.amount,
      goodsName: this.$store.state.goodsName,
      captcha: '',
      disabled: false,
      captchaMsg: '获取验证码',
      cardItem: this.$store.getters.getSelectedCard,
      submitBtnStatus: true
    }
  },
  created () {
    this.$toast('短信验证码已发送,注意查收')
    this.disabledCaptchaBtn()
  },
  computed: {
    type () {
      return (this.cardItem.cardType === '1') ? '信用卡' : '储蓄卡'
    }
  },
  watch: {
    captcha (newValue) {
      this.submitBtnStatus = !newValue
    }
  },
  methods: {
    handleClick () {
      let datas = {
        userId: this.$store.getters.getUserId,
        token: this.$store.getters.getToken,
        contractId: this.cardItem.contractId,
        orderSn: this.$store.state.orderSn,
        amount: this.$store.state.amount,
        clientIP: this.$store.state.clientIP,
        goodsName: this.$store.state.goodsName,
        checkCode: this.captcha
      }
      this.$api.rpmQuickPayCommit(datas)
         .then(res => {
           if (res.code === '0000') {
             this.$store.dispatch('updateToken', {token: res.token})
             this.$store.dispatch('updatePaidResultState', {paidResultState: true})
             this.$router.push('paidResult')
           } else {
             this.$store.dispatch('updatePaidResultState', {paidResultState: false})
             this.$toast(res.desc)
           }
         })
          .catch(error => {
            console.log(error)
          })
    },
    handleCaptcha () {
      this.disabled = true
      let datas = {
        userId: this.$store.getters.getUserId,
        token: this.$store.getters.getToken,
        contractId: this.cardItem.contractId
      }
      this.$api.rpmQuickPaySms(datas)
         .then(res => {
           if (res.code === '0000') {
             this.$store.dispatch('updateToken', {token: res.token})
           } else {
             console.log(res.desc)
           }
         })
          .catch(error => {
            console.log(error)
          })
      this.disabledCaptchaBtn()
    },
    disabledCaptchaBtn () {
      let _this = this
      let sec = 60
      _this.disabled = true
      for (let i = 0; i <= 60; i++) {
        window.setTimeout(function () {
          if (sec !== 0) {
            _this.captchaMsg = '获取验证码(' + sec + ')'
            sec--
          } else {
            sec = 60
            _this.disabled = false
            _this.captchaMsg = '获取验证码'
          }
        }, i * 1000)
      }
    }
  }
}
</script>
<style scoped>
	.paid{
		background-color: #F1EFF4;
	}
	.btn-paid{
		margin-top: 10px;
	}
	.info{
		height: 80px;
		padding: 20px;
	}
  .info h3{
    text-align: center;
  }
	.content{
		padding: 5px
	}
	.mint-cell{
		margin-top: 1px;
	}
</style>