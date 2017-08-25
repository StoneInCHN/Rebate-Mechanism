<template>
	<div class="addBankCard">
		<mt-header class="title" :title="title">
		  <router-link to="/bankList" slot="left">
		    <mt-button icon="back">返回</mt-button>
		  </router-link>
		</mt-header>
		<div class="content">
				<mt-field label="卡号" placeholder="请输入卡号" v-model="cardNo" :state="cardNoState"></mt-field>
				<mt-field label="银行卡预留手机号" type="tel" placeholder="请输入预留手机号" v-model="phoneNum" :state="phoneNumState"></mt-field>
				<mt-field label="身份证号" placeholder="身份证号" v-model="idNo" :state="idNoState"></mt-field>
				<mt-field label="银行卡开户名" placeholder="银行卡开户名" v-model="userName" :state="userNameState"></mt-field>
				<mt-button size="large" type="danger" @click="handleClick" :disabled="addState">同意协议并绑定</mt-button>
				<mt-cell  class="agreement" title="《九派快捷支付服务协议》" to='protocol'></mt-cell>
		</div>
	</div>
</template>
<script>
export default {
  name: 'addBankCard',
  data () {
    return {
      validation: [],
      cardNo: '',
      userName: '',
      phoneNum: '',
      idNo: '',
      cardNoValidate: false,
      phoneNumValidate: false,
      idNoValidate: false,
      userNameValidate: false
    }
  },
  methods: {
    handleClick () {
      this.$api.rpmBindCard(this.datas)
         .then(res => {
           console.log(res)
           if (res.code === '0000') {
             this.$router.push('bankList')
           } else {
             this.$toast(res.desc)
           }
         })
          .catch(error => {
            console.log(error)
          })
    },
    isCardNo () {
      let reg = /^(\d{16}|\d{19})$/
      return reg.test(this.cardNo)
    },
    isIdNo () {
      let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
      return reg.test(this.idNo)
    },
    isPhoneNum () {
      let reg = /^1[3|4|5|7|8][0-9]{9}$/
      return reg.test(this.phoneNum)
    }
  },
  computed: {
    title () {
      return this.$store.getters.getTitle
    },
    datas () {
      return {
        cardNo: this.cardNo,
        userName: this.userName,
        cellPhoneNum: this.phoneNum,
        idNo: this.idNo,
        orderSn: '20170820200129222',
        userId: this.$store.getters.getUserId,
        token: this.$store.getters.getToken
      }
    },
    cardNoState () {
      return this.cardNoValidate ? 'success' : 'error'
    },
    phoneNumState () {
      return this.phoneNumValidate ? 'success' : 'error'
    },
    idNoState () {
      console.log()
      return this.idNoValidate ? 'success' : 'error'
    },
    userNameState () {
      return this.userNameValidate ? 'success' : 'error'
    },
    addState () {
      return !(this.cardNoValidate && this.phoneNumValidate && this.idNoValidate && this.userNameValidate)
    }
  },
  watch: {
    cardNo (value) {
      if (!value || !this.isCardNo()) {
        this.cardNoValidate = false
      } else {
        this.cardNoValidate = true
      }
    },
    idNo (value) {
      if (!value || !this.isIdNo()) {
        this.idNoValidate = false
      } else {
        this.idNoValidate = true
      }
    },
    userName (value) {
      if (!value) {
        this.userNameValidate = false
      } else {
        this.userNameValidate = true
      }
    },
    phoneNum (value) {
      if (!value || !this.isPhoneNum()) {
        this.phoneNumValidate = false
      } else {
        this.phoneNumValidate = true
      }
    }
  }
}
</script>
<style scoped>
	.addBankCard{
		background-color: #F1EFF4;
	}
	.title{
      text-align: center;
	}
	.content{
		padding: 5px;
	}
	.mint-cell{
		margin-top: 1px;
	}
	.mint-button{
		margin-top: 10px
	}
	.agreement{
		text-align: right;
    	background-color: #f1eff4;
	}
	.agreement .mint-cell-text{
		font-size: 10px
	}
</style>