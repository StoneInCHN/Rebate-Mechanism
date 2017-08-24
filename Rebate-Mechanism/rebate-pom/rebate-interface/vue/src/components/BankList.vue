<template>
	<div>
		<mt-header class="title" :title="title">
		  <mt-button  slot="right" @click="handleClick">添加</mt-button>
		</mt-header>
		<div class="content">
			<card v-for="cardItem in cardLists" :cardItem="cardItem" :key="cardItem.contractId"></card>
		</div>
	</div>
</template>
<script>
import Card from '@/components/Card'
export default {
  name: 'bankList',
  data () {
    return {
      title: '我的银行卡'
    }
  },
  components: {
    Card
  },
  created () {
    this.getCardList()
  },
  computed: {
    datas () {
      return {
        userId: this.$store.getters.getUserId,
        token: this.$store.getters.getToken
      }
    },
    cardLists () {
      return this.$store.getters.getCardLists
    }
  },
  methods: {
    getCardList () {
      this.$api.rpmMemberCardList(this.datas)
         .then(res => {
           console.log(res)
           if (res.code === '0000') {
             this.$store.dispatch('updateToken', {token: res.token})
             if (res.msg.cardList) {
               this.$store.dispatch('updateCardLists', {cardLists: res.msg.cardList})
             } else {
               this.$router.push('addBankCard')
             }
           } else if (res.code === '0008') {
             this.$router.push('404')
           } else if (res.code === '3000') {
             this.$router.push('404')
           } else {
             this.$router.push('addBankCard')
           }
         })
          .catch(error => {
            console.log(error)
          })
    },
    handleClick () {
      this.$router.push('addBankCard')
    }
  }
}
</script>
<style scoped>
	.content{
		padding: 10px;
	}
</style>