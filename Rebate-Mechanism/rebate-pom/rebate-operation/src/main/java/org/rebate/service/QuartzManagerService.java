package org.rebate.service;

import javax.annotation.Resource;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/** 
 * @Description: 定时任务管理类 
 *  
 * @ClassName: QuartzManager 
 * @Copyright: Copyright (c) 2014 
 *  
 * @author Comsys-LZP 
 * @date 2014-6-26 下午03:15:52 
 * @version V2.0 
 */  
@Service
@Transactional(readOnly = true)
public class QuartzManagerService {  
    @Resource  
    private StdScheduler  quartzScheduler; 
    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";  
    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";  
  
    /** 
     * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名 
     *  
     * @param jobName 
     *            任务名 
     * @param cls 
     *            任务 
     * @param time 
     *            时间设置，参考quartz说明文档 
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2014 
     *  
     * @author Comsys-LZP 
     * @date 2014-6-26 下午03:47:44 
     * @version V2.0 
     */  
    @SuppressWarnings("unchecked")  
    public void addJob(String jobName, Class<? extends Job> cls, String cronExpression) {  
        try {  
//            JobDetail jobDetail = new JobDetail(jobName, JOB_GROUP_NAME, cls);// 任务名，任务组，任务执行类  
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName,JOB_GROUP_NAME).build();
            // 触发器  
//            CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// 触发器名,触发器组  
//            trigger.setCronExpression(time);// 触发器时间设定  
            
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, TRIGGER_GROUP_NAME)
                    .withSchedule(scheduleBuilder.withMisfireHandlingInstructionDoNothing()).build();
            quartzScheduler.scheduleJob(jobDetail, trigger);  
            // 启动  
            if (!quartzScheduler.isShutdown()) {  
            	quartzScheduler.start();  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * @Description: 添加一个定时任务 
     *  
     * @param jobName 
     *            任务名 
     * @param jobGroupName 
     *            任务组名 
     * @param triggerName 
     *            触发器名 
     * @param triggerGroupName 
     *            触发器组名 
     * @param jobClass 
     *            任务 
     * @param time 
     *            时间设置，参考quartz说明文档 
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2014 
     *  
     * @author Comsys-LZP 
     * @date 2014-6-26 下午03:48:15 
     * @version V2.0 
     */  
    @SuppressWarnings("unchecked")  
    public  void addJob(String jobName, String jobGroupName,  
            String triggerName, String triggerGroupName, Class<? extends Job> jobClass,  
            String cronExpression) {  
        try {  
//            JobDetail jobDetail = new JobDetail(jobName, jobGroupName, jobClass);// 任务名，任务组，任务执行类  
//            // 触发器  
//            CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);// 触发器名,触发器组  
//            trigger.setCronExpression(time);// 触发器时间设定
            
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName,jobGroupName).build();
            // 触发器  
            
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroupName)
            		 .withSchedule(scheduleBuilder.withMisfireHandlingInstructionDoNothing()).build();
            quartzScheduler.scheduleJob(jobDetail, trigger);  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名) 
     *  
     * @param jobName 
     * @param time 
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2014 
     *  
     * @author Comsys-LZP 
     * @date 2014-6-26 下午03:49:21 
     * @version V2.0 
     */  
    @SuppressWarnings("unchecked")  
    public  void modifyJobTime(String jobName, String cronExpression) {  
        try {  
//            CronTrigger trigger = (CronTrigger) sched.getTrigger(jobName,TRIGGER_GROUP_NAME);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName,TRIGGER_GROUP_NAME)
                    .withSchedule(scheduleBuilder).build();
            
            if (trigger == null) {  
                return;  
            }  
            String oldTime = trigger.getCronExpression();  
            if (!oldTime.equalsIgnoreCase(cronExpression)) {  
                JobDetail jobDetail = quartzScheduler.getJobDetail(new JobKey(jobName,JOB_GROUP_NAME));  
                Class objJobClass = jobDetail.getJobClass();  
                removeJob(jobName);  
                addJob(jobName, objJobClass, cronExpression);  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * @Description: 修改一个任务的触发时间 
     *  
     * @param triggerName 
     * @param triggerGroupName 
     * @param time 
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2014 
     *  
     * @author Comsys-LZP 
     * @date 2014-6-26 下午03:49:37 
     * @version V2.0 
     */  
    public  void modifyJobTime(String triggerName,  
            String triggerGroupName, String cronExpression) {  
        try {  
//            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerName,triggerGroupName);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName,triggerGroupName)
                    .withSchedule(scheduleBuilder).build();
            if (trigger == null) {  
                return;  
            }  
            String oldTime = trigger.getCronExpression();  
            if (!oldTime.equalsIgnoreCase(cronExpression)) {  
            	CronTriggerImpl ct = (CronTriggerImpl) trigger;  
                // 修改时间  
                ct.setCronExpression(cronExpression);  
                // 重启触发器  
                quartzScheduler.resumeTrigger(new TriggerKey(triggerName, triggerGroupName));  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名) 
     *  
     * @param jobName 
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2014 
     *  
     * @author Comsys-LZP 
     * @date 2014-6-26 下午03:49:51 
     * @version V2.0 
     */  
    public  void removeJob(String jobName) {  
        try {  
        	quartzScheduler.pauseTrigger(new TriggerKey(jobName, TRIGGER_GROUP_NAME));// 停止触发器  
        	quartzScheduler.unscheduleJob(new TriggerKey(jobName, TRIGGER_GROUP_NAME));// 移除触发器  
        	quartzScheduler.deleteJob(new JobKey(jobName, JOB_GROUP_NAME));// 删除任务  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    /**
     * 暂停job
     * @param jobName
     */
    public  void pauseJob(String jobName) {  
        try {  
        	quartzScheduler.pauseJob(new JobKey(jobName, TRIGGER_GROUP_NAME));
        	quartzScheduler.pauseTrigger(new TriggerKey(jobName, TRIGGER_GROUP_NAME));
        } catch (SchedulerException e) { 
        	e.printStackTrace();
//            throw new RuntimeException(e);  
        }  
    }
    
    public void resumeJob(String jobName){
    	try {
			quartzScheduler.resumeJob(new JobKey(jobName, TRIGGER_GROUP_NAME));
			quartzScheduler.resumeTrigger(new TriggerKey(jobName, TRIGGER_GROUP_NAME));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
    }
    /** 
     * @Description: 移除一个任务 
     *  
     * @param jobName 
     * @param jobGroupName 
     * @param triggerName 
     * @param triggerGroupName 
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2014 
     *  
     * @author Comsys-LZP 
     * @date 2014-6-26 下午03:50:01 
     * @version V2.0 
     */  
    public  void removeJob(String jobName, String jobGroupName,  
            String triggerName, String triggerGroupName) {  
        try {  
        	quartzScheduler.pauseTrigger(new TriggerKey(triggerName, triggerGroupName));// 停止触发器  
        	quartzScheduler.unscheduleJob(new TriggerKey(triggerName, triggerGroupName));// 移除触发器  
        	quartzScheduler.deleteJob(new JobKey(jobName, jobGroupName));// 删除任务  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    
    /** 
     * @Description:启动所有定时任务 
     *  
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2014 
     *  
     * @author Comsys-LZP 
     * @date 2014-6-26 下午03:50:18 
     * @version V2.0 
     */  
    public  void startJobs() {  
        try {  
        	quartzScheduler.start();  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
    /** 
     * @Description:关闭所有定时任务 
     *  
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2014 
     *  
     * @author Comsys-LZP 
     * @date 2014-6-26 下午03:50:26 
     * @version V2.0 
     */  
    public  void shutdownJobs() {  
        try {  
            if (!quartzScheduler.isShutdown()) {  
            	quartzScheduler.shutdown();  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    } 
    
//    public List<SysJob> retrieveJobs(List<SysJob> sysJobs) {
//		for (SysJob sysJob : sysJobs) {    
//		    List<? extends Trigger> triggers;
//			try {
////				JobDetail jobDetail = quartzScheduler.getJobDetail(new JobKey(sysJob.getJobName(),JOB_GROUP_NAME));
//				triggers = quartzScheduler.getTriggersOfJob(new JobKey(sysJob.getJobName(),JOB_GROUP_NAME));
//				   for (Trigger trigger : triggers) {   
//				        //触发器当前状态    
//				    	if (trigger.getKey().getName().equals(sysJob.getJobName())) {
//				    		 Trigger.TriggerState triggerState = quartzScheduler.getTriggerState(trigger.getKey());    
//						     sysJob.setJobStatus(triggerState.name());
//						     break;
//						}
//				    }    
//			} catch (SchedulerException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		  
//		 
//		  
//		}    
//		return sysJobs;    
//    }
}  
