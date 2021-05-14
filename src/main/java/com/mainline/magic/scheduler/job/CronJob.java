package com.mainline.magic.scheduler.job;

import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mainline.magic.scheduler.utils.JobUtils;

@DisallowConcurrentExecution
public class CronJob extends QuartzJobBean {
	
	private static final Logger logger = LoggerFactory.getLogger(CronJob.class);
	private final String jobName = "LoopJob";
	private final String jobGroup = "LoopJobGroup";

	/**
	 *Loop Job이 살아있는지 확인한다. 만약에 사라졌으면 새로 등록을 해준다.
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		Scheduler scheduler = context.getScheduler(); 
		List<Trigger> list = JobUtils.getJobs(scheduler, jobGroup);
		// 등록된것이 없다면 Loop Job을 등록해준다.
		if(list.size() == 0) {
			try {
				Date date = JobUtils.createJob(jobName, jobGroup, LoopJob.class, scheduler);
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			logger.info("정상적으로 작업이 등록되어 있습니다.");
		}
	}

}
