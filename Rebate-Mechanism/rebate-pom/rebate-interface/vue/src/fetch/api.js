import axios from 'axios'
import {Indicator, MessageBox} from 'mint-ui'
// 超时时间
axios.defaults.timeout = 5000
//
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8'
// http请求拦截器
axios.interceptors.request.use(config => {
  // Indicator
  Indicator.open({
    text: '加载中...',
    spinnerType: 'fading-circle'
  })
  return config
}, error => {
  Indicator.close()
  MessageBox('加载超时')
  return Promise.reject(error)
})
// http响应拦截器
axios.interceptors.response.use(data => {
  // 响应成功关闭loading
  Indicator.close()
  return data
}, error => {
  Indicator.close()
  MessageBox('加载失败')
  return Promise.reject(error)
})

export function fetch (url, params) {
  return new Promise((resolve, reject) => {
    axios.post(url, params)
      .then(response => {
        resolve(response.data)
      }, err => {
        reject(err)
      })
      .catch((error) => {
        reject(error)
      })
  })
}

export default{
  /**
   * 登陆
   */
  login (params) {
    return fetch('/rebate-interface/endUser/login.jhtml', params)
  },
  /**
   * 银行卡绑定
   */
  rpmBindCard (params) {
    return fetch('/rebate-interface/jiupaiPay/rpmBindCard.jhtml', params)
  },
  /**
   *查询用户绑卡信息
   */
  rpmMemberCardList (params) {
    return fetch('/rebate-interface/jiupaiPay/rpmMemberCardList.jhtml', params)
  },
  /**
   *查询支持绑卡的银行列表
   */
  rpmBankList (params) {
    return fetch('/rebate-interface/jiupaiPay/rpmBankList.jhtml', params)
  },
  /**
   *用户解绑银行卡
   */
  rpmUnbindCard (params) {
    return fetch('/rebate-interface/jiupaiPay/rpmUnbindCard.jhtml', params)
  },
  /**
   *快捷支付发起(九派验证短信)
   */
  rpmQuickPayInit (params) {
    return fetch('/rebate-interface/jiupaiPay/rpmQuickPayInit.jhtml', params)
  },
  /**
   * 获取短信验证码
   */
  rpmQuickPaySms (params) {
    return fetch('/rebate-interface/jiupaiPay/rpmQuickPaySms.jhtml', params)
  },
  /**
   *快捷支付提交(九派验证短信)
   */
  rpmQuickPayCommit (params) {
    return fetch('/rebate-interface/jiupaiPay/rpmQuickPayCommit.jhtml', params)
  }
}
