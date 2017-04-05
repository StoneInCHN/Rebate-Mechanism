package org.rebate.service;



public interface MailService {

  /**
   * 发送邮件
   * 
   * @param smtpFromMail 发件人邮箱
   * @param smtpHost SMTP服务器地址
   * @param smtpPort SMTP服务器端口
   * @param smtpUsername SMTP用户名
   * @param smtpPassword SMTP密码
   * @param toMail 收件人邮箱
   * @param subject 主题
   * @param message 邮件内容
   * @param async 是否异步
   */
  void send(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername,
      String smtpPassword, String toMail, String subject, String message, boolean async);


  /**
   * 发送邮件(异步)
   * 
   * @param toMail 收件人邮箱
   * @param subject 主题
   * @param message 邮件内容
   */
  void send(String toMail, String subject, String message);



}
