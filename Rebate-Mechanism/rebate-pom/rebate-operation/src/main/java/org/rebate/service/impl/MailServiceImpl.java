package org.rebate.service.impl;

import java.io.IOException;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.rebate.beans.Setting;
import org.rebate.service.MailService;
import org.rebate.utils.SettingUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Service - 邮件
 */
@Service("mailServiceImpl")
public class MailServiceImpl implements MailService {

  @Resource(name = "javaMailSender")
  private JavaMailSenderImpl javaMailSender;
  @Resource(name = "taskExecutor")
  private TaskExecutor taskExecutor;


  /**
   * 添加邮件发送任务
   * 
   * @param mimeMessage MimeMessage
   */
  private void addSendTask(final MimeMessage mimeMessage) {
    try {
      taskExecutor.execute(new Runnable() {
        public void run() {
          javaMailSender.send(mimeMessage);
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void send(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername,
      String smtpPassword, String toMail, String subject, String message, boolean async) {
    Assert.hasText(smtpFromMail);
    Assert.hasText(smtpHost);
    Assert.notNull(smtpPort);
    Assert.hasText(smtpUsername);
    Assert.hasText(smtpPassword);
    Assert.hasText(toMail);
    Assert.hasText(subject);

    try {
      Setting setting = SettingUtils.get();
      javaMailSender.setHost(smtpHost);
      javaMailSender.setPort(smtpPort);
      javaMailSender.setUsername(smtpUsername);
      javaMailSender.setPassword(smtpPassword);
      MimeMessage mimeMessage = javaMailSender.createMimeMessage();
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");
      mimeMessageHelper.setFrom(MimeUtility.encodeWord(setting.getSiteName()) + " <" + smtpFromMail
          + ">");
      mimeMessageHelper.setSubject(subject);
      String[] toMails = toMail.split(",");
      mimeMessageHelper.setTo(toMails);
      mimeMessageHelper.setText(message);
      if (async) {
        addSendTask(mimeMessage);
      } else {
        javaMailSender.send(mimeMessage);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (MessagingException e) {
      e.printStackTrace();
    }

  }


  public void send(String toMail, String subject, String message) {
    Setting setting = SettingUtils.get();
    send(setting.getSmtpFromMail(), setting.getSmtpHost(), setting.getSmtpPort(),
        setting.getSmtpUsername(), setting.getSmtpPassword(), toMail, subject, message, true);
  }


}
