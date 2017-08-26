import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    init: true,
    cellPhoneNum: '15902823856',
    password: 'bOrA1KqdhpV+nhsi+r9dixFGSxcIMB3dJBCYYw2BKPvK52TeGke/lpzhJIHym+Lnv24sgXkKnx5DrlLvJwPN4HMpafTXLRO7QS0TfsBXzWO/iKRwPVzsR9uUWo85UY7dVGTa7vg8vNA3BdgEdavw3LnG0X6y2n9VwwTdfvTvpbA=',
    token: '',
    userId: '',
    title: '绑定银行卡',
    amount: '',
    clientIP: '',
    goodsName: '',
    orderSn: '',
    cardLists: [],
    selectedCard: Object,
    paidResultState: Boolean
  },
  mutations: {
    setInit (state, {init}) {
      state.init = init
    },
    setCellPhoneNum (state, {cellPhoneNum}) {
      state.cellPhoneNum = cellPhoneNum
    },
    setUserId (state, {userId}) {
      state.userId = userId
    },
    setAmount (state, {amount}) {
      state.amount = amount
    },
    setGoodsName (state, {goodsName}) {
      state.goodsName = goodsName
    },
    setClientIP (state, {clientIP}) {
      state.clientIP = clientIP
    },
    setOrderSn (state, {orderSn}) {
      state.orderSn = orderSn
    },
    updateToken (state, {token}) {
      state.token = token
    },
    updateCardLists (state, {cardLists}) {
      state.cardLists = cardLists
    },
    updateSelectedCard (state, {selectedCard}) {
      state.selectedCard = selectedCard
    },
    updatePaidResultState (state, {paidResultState}) {
      state.paidResultState = paidResultState
    }
  },
  getters: {
    getInit (state) {
      return state.init
    },
    getCellPhoneNum (state) {
      return state.cellPhoneNum
    },
    getPassword (state) {
      return state.password
    },
    getTitle (state) {
      return state.title
    },
    getUserId (state) {
      return state.userId
    },
    getAmount (state) {
      return state.amount
    },
    getClientIP (state) {
      return state.clientIP
    },
    getGoodsName (state) {
      return state.goodsName
    },
    getOrderSn (state) {
      return state.orderSn
    },
    getToken (state) {
      return state.token
    },
    getCardLists (state) {
      return state.cardLists
    },
    getSelectedCard (state) {
      return state.selectedCard
    },
    getPaidResultState (state) {
      return state.paidResultState
    }
  },
  actions: {
    setInit (context, payload) {
      context.commit('setInit', payload)
    },
    setCellPhoneNum (context, payload) {
      context.commit('setCellPhoneNum', payload)
    },
    setUserId (context, payload) {
      context.commit('setUserId', payload)
    },
    setAmount (context, payload) {
      context.commit('setAmount', payload)
    },
    setClientIP (context, payload) {
      context.commit('setClientIP', payload)
    },
    setGoodsName (context, payload) {
      context.commit('setGoodsName', payload)
    },
    setOrderSn (context, payload) {
      context.commit('setOrderSn', payload)
    },
    updateToken (context, payload) {
      context.commit('updateToken', payload)
    },
    updateCardLists (context, payload) {
      context.commit('updateCardLists', payload)
    },
    updateSelectedCard (context, payload) {
      context.commit('updateSelectedCard', payload)
    },
    updatePaidResultState (context, payload) {
      context.commit('updatePaidResultState', payload)
    }
  }
})
