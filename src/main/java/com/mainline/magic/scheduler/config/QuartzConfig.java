package com.mainline.magic.scheduler.config;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.mainline.magic.scheduler.job.CronJob;
import com.mainline.magic.scheduler.utils.JobUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Configuration
public class QuartzConfig {

	private final String jobName = "LoopJobChecker";
	private final String jobGroup = "CronJobGroup";

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
    private Scheduler scheduler;
	
	@Autowired
	private McpProperties mcpProperteis;


	@Bean
	public SchedulerFactoryBean schedulerFactory() throws SchedulerException, UnknownHostException {
		log.info("SchedulerFactoryBean created!");
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		schedulerFactoryBean.setJobFactory(jobFactory);
		schedulerFactoryBean.setTransactionManager(transactionManager);
		schedulerFactoryBean.setDataSource(dataSource);
		schedulerFactoryBean.setOverwriteExistingJobs(true);
		schedulerFactoryBean.setAutoStartup(true);
		schedulerFactoryBean.setQuartzProperties(quartzProperties());
		return schedulerFactoryBean;
	}

	@Bean
	public Properties quartzProperties() {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/application.properties"));

		Properties properties = null;
		try {
			propertiesFactoryBean.afterPropertiesSet();
			properties = propertiesFactoryBean.getObject();
		} catch (Exception e) {
			log.warn("Cannot load quartz.properties");
		}
		return properties;
	}

	// initMethod 아직 이해 안됨 찾아봐야겠땅..;; 언제 init을 한다는건지..;;;
	@Bean(initMethod = "init")
	public void quartzStarter() throws Exception {
		
		
		// 스케쥴러 내역을 비울지 확인한다.
		if(Boolean.valueOf(mcpProperteis.getClear() != null ? mcpProperteis.getClear() : "false" )) {
			scheduler.clear();
		}
		
		// 마스터 스케쥴러인저 확인한다.
		if(Boolean.valueOf(mcpProperteis.getMaster() != null ? mcpProperteis.getMaster() : "false" )) {
			List<Trigger> list = JobUtils.getJobs(scheduler, jobGroup);
			scheduler.start();
			// 최초 무한 루프 job 관리용 cron job을 생성한다.
			if(list.size() == 0) {
				// cron job 등록
				Date date  = JobUtils.createCronJob(jobName, jobGroup, CronJob.class, scheduler, null);
			}else {
				log.info("현재 등록된 작업이 있습니다. : "+ list.size());
			}
		}else {
			log.info("이서버는 마스터가 아님");
		}
	}
}
