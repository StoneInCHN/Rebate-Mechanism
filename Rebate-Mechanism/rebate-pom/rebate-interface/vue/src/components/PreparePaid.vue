<template>
	<div class="prepare">
		<mt-header >
		  <router-link to="/bankList" slot="left">
		    <mt-button icon="back">返回</mt-button>
		  </router-link>
		</mt-header>
		<div class="info">
			<h3>向 {{goodsName}} 支付</h3>
			<h3>{{amount}}￥</h3>
		</div>
		<div class="content">
			<mt-cell title="银行名称" :value="cardItem.bankName" ></mt-cell>
			<mt-cell title="银行卡号" :value="cardItem.cardNo" ></mt-cell>
			<mt-cell title="银行卡类型" :value="type" ></mt-cell>
			<mt-cell  v-if="showHelp" class="help" title="支付帮助?"></mt-cell>
			<mt-button size="large" type="danger" @click="handleClick">选择此卡进行支付</mt-button>
		</div>
	</div>
</template>
<script>
export default {
  name: 'preparePaid',
  data () {
    return {
      showHelp: false,
      amount: this.$store.state.amount,
      goodsName: this.$store.state.goodsName,
      cardItem: this.$store.getters.getSelectedCard
    }
  },
  computed: {
    type () {
      return (this.cardItem.cardType === '1') ? '信用卡' : '储蓄卡'
    }
  },
  methods: {
    handleClick () {
      let datas = {
        userId: this.$store.getters.getUserId,
        token: this.$store.getters.getToken,
        contractId: this.cardItem.contractId,
        orderSn: this.$store.state.orderSn,
        amount: this.amount,
        clientIP: this.$store.state.clientIP,
        goodsName: this.goodsName
      }
      console.log(datas)
      this.$api.rpmQuickPayInit(datas)
         .then(res => {
           console.log(res)
           if (res.code === '0000') {
             this.$store.dispatch('updateToken', {token: res.token})
             this.$router.push('paid')
           } else {
             this.$toast(res.desc)
           }
         })
          .catch(error => {
            console.log(error)
          })
    }
  }
}
</script>
<style scoped>
	.prepare{
		background-color: #F1EFF4;
	}
	.content{
		padding: 5px
	}
	.info{
		height: 100px;
		padding: 20px;
	}
	.info h3{
		text-align: center;
	}
	p{
		text-align: center;
	}
	.mint-cell{
		margin-top: 1px;
	}
	.help{
   		text-align:right;
   		color: #97becc;
	}
	.help .mint-cell-text{
		font-size:10px;
	}
</style>