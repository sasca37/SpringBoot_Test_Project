package com.mainline.magic.scheduler.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

@DisallowConcurrentExecution
public class InsuranceJob extends QuartzJobBean {
	
	private static final Logger logger = LoggerFactory.getLogger(InsuranceJob.class);


	
	/**
	 * 실제 약관 생성을 위한 작업을 진행한다.
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		logger.info("작업이 시잔한다.");
		for(int i = 0; i < 20; i++) {
			try {
				Thread.sleep(1000);
				System.out.println(i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JobDataMap map = context.getMergedJobDataMap();
		System.out.println(map.get("jobInfo"));
	
		logger.info("작업이 끝.");
	}

}
