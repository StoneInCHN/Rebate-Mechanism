import Vue from 'vue'
import Router from 'vue-router'
import AddBankCard from '@/components/AddBankCard'
import BankList from '@/components/BankList'
import PreparePaid from '@/components/PreparePaid'
import Paid from '@/components/Paid'
import Protocol from '@/components/Protocol'
import PaidResult from '@/components/PaidResult'
import ErrorPage from '@/components/ErrorPage'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/addBankCard',
      name: 'AddBankCard',
      component: AddBankCard
    },
    {
      path: '/bankList',
      name: 'BankList',
      component: BankList
    },
    {
      path: '/preparePaid',
      name: 'PreparePaid',
      component: PreparePaid
    },
    {
      path: '/paid',
      name: 'Paid',
      component: Paid
    },
    {
      path: '/protocol',
      name: 'Protocol',
      component: Protocol
    },
    {
      path: '/paidResult',
      name: 'PaidResult',
      component: PaidResult
    },
    {
      path: '/404',
      name: 'ErrorPage',
      component: ErrorPage
    }
  ]
})
