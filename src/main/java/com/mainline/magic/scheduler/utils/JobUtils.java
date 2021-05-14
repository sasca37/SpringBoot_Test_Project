package com.mainline.magic.scheduler.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.google.gson.Gson;
import com.mainline.magic.scheduler.dto.Terms;

public class JobUtils {
	
	/**
	 * JobDetail 생성
	 * @param jobName
	 * @param JobGroup
	 * @param job
	 * @return
	 */
	public static JobDetail createJobDetail(String jobName,String JobGroup, Class<? extends QuartzJobBean> job) {
		return JobBuilder.newJob(job).withIdentity(new JobKey(jobName, JobGroup))
				// 작업 실패시 재작업 설정
				.requestRecovery().build();
	}
	
	/**
	 * fired trigger를 생성한다.
	 * @param jobKey
	 * @return
	 */
	public static Trigger createTrigger(JobDetail jobDetail) {
		return TriggerBuilder.newTrigger().forJob(jobDetail).build();
	}
	
	/**
	 * cron trigger를 생성한다.
	 * @param jobDetail
	 * @param cron
	 * @return
	 */
	public static Trigger createCronTrigger(JobDetail jobDetail, String cron) {
		String cronExpression = cron != null && cron.length() != 0? cron : "0/01 * * * * ?";
		return TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
				.forJob(jobDetail.getKey()).build();
	}
	

	/**
	 * 약관 제작 Trigger 생성한다.
	 * @param jobDetail
	 * @param terms
	 * @return
	 */
	public static Trigger createTrigger(JobDetail jobDetail, Terms terms) {
		return TriggerBuilder.newTrigger().withIdentity(terms.getUuid(), terms.getJobGroup()).forJob(jobDetail).build();
	}
	
	
	/**
	 * fired job 등록
	 * @param jobName
	 * @param jobGroup
	 * @param job
	 * @param scheduler
	 * @return
	 * @throws Exception
	 */
	public static Date createJob( String jobName, String jobGroup, Class<? extends QuartzJobBean> job, Scheduler scheduler) throws SchedulerException {
		JobDetail detail = createJobDetail(jobName, jobGroup, job);
		Trigger trigger = createTrigger(detail);
		return scheduler.scheduleJob(detail, trigger);
	}
	

	/**
	 * cron Job 등록
	 * @param jobName
	 * @param jobGroup
	 * @param job
	 * @param scheduler
	 * @param cron
	 * @return
	 * @throws Exception
	 */
	public static Date createCronJob( String jobName, String jobGroup, Class<? extends QuartzJobBean> job, Scheduler scheduler, String cron) throws SchedulerException {
		JobDetail detail = createJobDetail(jobName, jobGroup, job);
		Trigger trigger = createCronTrigger(detail, cron);
		return scheduler.scheduleJob(detail, trigger);
	}
	


	/**
	 * 해당 JobGroup의 trigger 정보를 가져온다.
	 * @param scheduler
	 * @param jobGroup
	 * @return
	 */
	public static List<Trigger> getJobs(Scheduler scheduler, String jobGroup) {
		List<Trigger> list = new ArrayList<>();
		try {
			Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupContains(jobGroup));
			for(JobKey jobKey : jobKeys) {
				TriggerKey triggerKey = TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup());
				list.add(scheduler.getTrigger(triggerKey));
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
