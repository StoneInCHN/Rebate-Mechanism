package org.rebate.controller;

import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.EndUser;
import org.rebate.entity.LeBeanRecord;
import org.rebate.entity.commonenum.CommonEnum;
import org.rebate.json.base.BasePssRequest;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.service.EndUserService;
import org.rebate.service.LeBeanRecordService;
import org.rebate.utils.TokenGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Controller - pss商城接口
 * 
 *
 */
@Controller("pssController")
@RequestMapping("/pss")
public class PssController extends MobileBaseController {

    @Resource(name = "endUserServiceImpl")
    private EndUserService endUserService;
    @Resource(name = "leBeanRecordServiceImpl")
    private LeBeanRecordService leBeanRecordService;

    /**
     * 打开pss
     * 验证token是否合法
     *
     * @return
     */
    @RequestMapping(value = "/validateToken", method = RequestMethod.POST)
    public @ResponseBody
    ResponseMultiple<Map<String, Object>> validateToken(@RequestBody BasePssRequest request) {

        ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

        Long userId = request.getUserId();
        String token = request.getToken();

        // 验证登录token
        String userToken = endUserService.getEndUserToken(userId);
        if (!TokenGenerator.isValiableToken(token, userToken)) {
            response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
            response.setDesc(Message.error("rebate.user.token.timeout").getContent());
            return response;
        }

        response.setCode(CommonAttributes.SUCCESS);
        response.setDesc("token验证成功");
        return response;
    }

    /**
     * 通过userid获取
     *
     * @return
     */
    @RequestMapping(value = "/getLeBeanAmount", method = RequestMethod.POST)
    public @ResponseBody
    ResponseMultiple<Map<String, Object>> getLeBeanAmount(@RequestBody BasePssRequest request) {

        ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

        Long userId = request.getUserId();

        EndUser user = endUserService.find(userId);

        if(user==null){
            response.setDesc("用户不合法");
            response.setCode(CommonAttributes.FAIL_LOGIN);
            return response;
        }
        response.setDesc(user.getPrePayLeBean().toString());
        response.setCode(CommonAttributes.SUCCESS);
        return response;
    }

    /**
     * 乐豆操作接口
     *  type 0:预扣除（不影响真正乐豆数量） 1:扣除乐豆（cur乐豆）2：增加预扣除 (支付失败)
     *  amount 数量
     * @return
     */
    @RequestMapping(value = "/doLeBeanAmount", method = RequestMethod.POST)
    public @ResponseBody
    ResponseMultiple<Map<String, Object>> doLeBeanAmount(@RequestBody BasePssRequest request) {

        ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();

        Long userId = request.getUserId();
        BigDecimal amount = request.getAmount();
        Integer type = request.getType();

        EndUser user = endUserService.find(userId);

        if(user==null){
            response.setDesc("用户不合法");
            response.setCode(CommonAttributes.FAIL_LOGIN);
            return response;
        }

        BigDecimal curLeBean = user.getCurLeBean();

        //预扣除
        //pss开始支付到支付回调
        if(type==0){
            BigDecimal prePayLeBean = user.getPrePayLeBean();
            BigDecimal afterPayLeBean = prePayLeBean.subtract(amount);
            if(afterPayLeBean.compareTo(new BigDecimal(0))<0){
                response.setDesc("乐豆预扣除失败，预扣除数量不足");
                response.setCode(CommonAttributes.FAIL_PRE_PAY_LEBEAN);
                return response;
            }
            user.setPrePayLeBean(afterPayLeBean);
            endUserService.update(user);
        }else if(type==1){
            //扣除乐豆 不需要更新预扣除乐豆
            curLeBean = curLeBean.subtract(amount);
            if(curLeBean.compareTo(new BigDecimal(0))<0){
                response.setDesc("乐豆扣除失败");
                response.setCode(CommonAttributes.FAIL_CUR_PAY_LEBEAN);
                return response;
            }
            user.setCurLeBean(curLeBean);
            endUserService.update(user);
            //生产乐豆消费记录
            LeBeanRecord leBeanRecord = new LeBeanRecord();
            leBeanRecord.setAmount(amount);
            leBeanRecord.setEndUser(user);
            leBeanRecord.setType(CommonEnum.LeBeanChangeType.CONSUME);
            leBeanRecord.setUserCurLeBean(curLeBean);
            leBeanRecord.setRemark("pss商城消费");
            leBeanRecordService.save(leBeanRecord);
        }else{
            //增加预扣除 (支付失败)
            BigDecimal prePayLeBean = curLeBean.add(amount);
            user.setPrePayLeBean(prePayLeBean);
            endUserService.update(user);
        }

        response.setDesc(user.getCurLeBean().toString());
        response.setCode(CommonAttributes.SUCCESS);
        return response;
    }
}
