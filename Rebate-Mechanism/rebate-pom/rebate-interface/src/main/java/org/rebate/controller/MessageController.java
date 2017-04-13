package org.rebate.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.rebate.aspect.UserParam.CheckUserType;
import org.rebate.aspect.UserValidCheck;
import org.rebate.beans.CommonAttributes;
import org.rebate.beans.Message;
import org.rebate.controller.base.MobileBaseController;
import org.rebate.entity.EndUser;
import org.rebate.entity.MessageInfo;
import org.rebate.entity.MsgEndUser;
import org.rebate.framework.paging.Page;
import org.rebate.framework.paging.Pageable;
import org.rebate.json.base.BaseRequest;
import org.rebate.json.base.BaseResponse;
import org.rebate.json.base.PageResponse;
import org.rebate.json.base.ResponseMultiple;
import org.rebate.json.request.MsgRequest;
import org.rebate.service.EndUserService;
import org.rebate.service.MessageInfoService;
import org.rebate.service.MsgEndUserService;
import org.rebate.utils.FieldFilterUtils;
import org.rebate.utils.TokenGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * Controller - 消息中心
 * 
 * @author huyong
 *
 */
@Controller("MessageController")
@RequestMapping("/message")
public class MessageController extends MobileBaseController {

  @Resource(name = "endUserServiceImpl")
  private EndUserService endUserService;


  @Resource(name = "messageInfoServiceImpl")
  private MessageInfoService messageInfoService;

  @Resource(name = "msgEndUserServiceImpl")
  private MsgEndUserService msgEndUserService;



  /**
   * 获取消息列表
   * 
   * @param req
   * @return
   */
  @RequestMapping(value = "/getMsgList", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody ResponseMultiple<Map<String, Object>> getMsgList(@RequestBody BaseRequest req) {
    ResponseMultiple<Map<String, Object>> response = new ResponseMultiple<Map<String, Object>>();
    Integer pageSize = req.getPageSize();
    Integer pageNumber = req.getPageNumber();
    String token = req.getToken();
    Long userId = req.getUserId();
    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    Pageable pageable = new Pageable();
    pageable.setPageNumber(pageNumber);
    pageable.setPageSize(pageSize);
    Page<MessageInfo> msgs = messageInfoService.findMsgByUser(userId, pageable);

    PageResponse pageInfo = new PageResponse();
    pageInfo.setPageNumber(pageNumber);
    pageInfo.setPageSize(pageSize);
    pageInfo.setTotal((int) msgs.getTotal());

    Integer count = 0; // 未读消息数
    EndUser endUser = endUserService.find(userId);
    if (endUser != null) {
      for (MsgEndUser msgEndUser : endUser.getMsgEndUsers()) {
        if (!msgEndUser.getIsRead()) {
          count++;
        }
      }
    }

    String[] propertys = {"id", "messageTitle", "messageContent"};
    String[] msgUserPro = {"isRead", "createDate"};
    List<Map<String, Object>> result =
        FieldFilterUtils.filterCollectionMap(propertys, msgs.getContent());
    for (Map<String, Object> map : result) {
      MessageInfo messageInfo = messageInfoService.find((long) map.get("id"));
      MsgEndUser msgEndUser = msgEndUserService.findMsgEndUserByUserAndMsg(endUser, messageInfo);
      Map<String, Object> msgUser = FieldFilterUtils.filterEntityMap(msgUserPro, msgEndUser);
      map.putAll(msgUser);
    }
    response.setMsg(result);
    response.setPage(pageInfo);

    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setDesc(count.toString());
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

  /**
   * 读取消息
   * 
   * @param req
   * @return
   */
  @RequestMapping(value = "/readMessage", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody BaseResponse readMessage(@RequestBody MsgRequest req) {
    BaseResponse response = new BaseResponse();
    String token = req.getToken();
    Long userId = req.getUserId();
    Long msgId = req.getMsgId();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    EndUser endUser = endUserService.find(userId);
    MessageInfo msg = messageInfoService.find(msgId);
    MsgEndUser msgEndUser = msgEndUserService.findMsgEndUserByUserAndMsg(endUser, msg);
    msgEndUser.setIsRead(true);
    msgEndUserService.update(msgEndUser);
    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }


  /**
   * 清除消息
   * 
   * @param req
   * @return
   */
  @RequestMapping(value = "/deleteMsgs", method = RequestMethod.POST)
  @UserValidCheck(userType = CheckUserType.ENDUSER)
  public @ResponseBody BaseResponse deleteMsgs(@RequestBody MsgRequest req) {
    BaseResponse response = new BaseResponse();

    String token = req.getToken();
    Long userId = req.getUserId();
    Long[] msgIds = req.getMsgIds();

    // 验证登录token
    String userToken = endUserService.getEndUserToken(userId);
    if (!TokenGenerator.isValiableToken(token, userToken)) {
      response.setCode(CommonAttributes.FAIL_TOKEN_TIMEOUT);
      response.setDesc(Message.error("rebate.user.token.timeout").getContent());
      return response;
    }

    EndUser endUser = endUserService.find(userId);
    if ((msgIds == null || msgIds.length == 0)
        && (endUser.getMsgEndUsers() != null && endUser.getMsgEndUsers().size() > 0)) {
      List<MsgEndUser> msgEndUsers = new ArrayList<MsgEndUser>();
      msgEndUsers.addAll(endUser.getMsgEndUsers());
      msgIds = new Long[msgEndUsers.size()];
      for (int i = 0; i < msgEndUsers.size(); i++) {
        MessageInfo msg = msgEndUsers.get(i).getMessage();
        if (msg != null) {
          msgIds[i] = msg.getId();
        }
      }
    }

    for (Long msgId : msgIds) {
      MessageInfo msg = messageInfoService.find(msgId);
      MsgEndUser msgEndUser = msgEndUserService.findMsgEndUserByUserAndMsg(endUser, msg);
      endUser.getMsgEndUsers().remove(msgEndUser);
      msg.getMsgUser().remove(msgEndUser);
      msgEndUserService.delete(msgEndUser);
    }

    String newtoken = TokenGenerator.generateToken(token);
    endUserService.createEndUserToken(newtoken, userId);
    response.setToken(newtoken);
    response.setCode(CommonAttributes.SUCCESS);
    return response;
  }

}
