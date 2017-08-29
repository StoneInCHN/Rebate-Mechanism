<template>
	<div>
		<mt-header class="title" title="支付结果"></mt-header>
		<div class="content">
			<div class="info" v-if="paidSuccess">
				<img src="../assets/images/operationSuccess.png" alt="11">
				<p>恭喜你，支付成功！</p>
			</div>
			<div class="info" v-else>
				<img src="../assets/images/operationFailed.png" alt="11">
				<p>很遗憾，支付失败！</p>
			</div>
			<mt-button size="large" type="primary" plain @click.native="returnAndroid">返回商户</mt-button>
		</div>
	</div>
</template>
<script>
export default {
  name: 'card',
  computed: {
    paidSuccess () {
      return this.$store.getters.getPaidResultState
    }
  },
  methods: {
    returnAndroid () {
      window.Android.show()
    }
  }
}
</script>
<style scoped>
.content{
	padding: 20px;
}
.info{
	height: 200px;
	text-align: center;
}
.info img {
	height: 120px;
}
</style>