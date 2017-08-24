<template>
	<div class="mint-cell-wrapper card">
	    <div class="mint-cell-title" @click="handleSelect">
	      	<span class="mint-cell-text">{{cardItem.bankName}}</span>
          	<span class="mint-cell-label" >{{cardItem.cardNo}}</span>
	    </div>
	    <div class="mint-cell-value">
	      	<mt-button size="small" plain type="danger" @click.native="handleDelete">删除</mt-button>
	    </div>
	</div>
</template>
<script>
export default {
  name: 'card',
  props: ['cardItem'],
  computed: {
  },
  methods: {
    handleSelect () {
      this.$store.dispatch('updateSelectedCard', {selectedCard: this.cardItem})
      this.$router.push('preparePaid')
    },
    handleDelete () {
      this.$messagebox.confirm('确定要解除该银行卡的绑定?').then(action => {
        let datas = {
          userId: this.$store.getters.getUserId,
          token: this.$store.getters.getToken,
          contractId: this.cardItem.contractId
        }
        this.$api.rpmUnbindCard(datas)
         .then(res => {
           console.log(res)
           if (res.code === '0000') {
             this.$store.dispatch('updateToken', {token: res.token})
             window.location.reload()
           } else {
             console.log(res.desc)
           }
         })
          .catch(error => {
            console.log(error)
          })
        this.$messagebox('解除绑定')
      })
    }
  }
}
</script>
<style scoped>
	.card{
		padding: 10px;
		margin:10px 0;
		border-radius: 5px;
		border:1px solid #b1b2b3; 
	}
	.card span {
		word-break: break-all;
	}
</style>