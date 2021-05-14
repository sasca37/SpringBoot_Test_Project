package com.mainline.magic.scheduler.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.mainline.magic.scheduler.dto.Terms;
import com.mainline.magic.scheduler.service.SchedulerService;
import com.mainline.magic.scheduler.utils.McpHttpUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisallowConcurrentExecution
public class CronJob extends QuartzJobBean {

	@Autowired
	SchedulerService schedulerService;

	/**
	 * 요청 작업을 분배해서 본인 포함 다은 스케쥴러 서버에 작업을 분배하여 준다.
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			log.info("================= CronJob =================");
			List<Terms> list = schedulerService.getTermsJob();
			int total = list.size();

			if(total == 0) {
				Thread.sleep(1000);
				log.info("job count : " + total);
				return;
			}
			// 10초를 빼준다.
			List<Map<String, Object>> schedulers = schedulerService
					.getSchedulerState(System.currentTimeMillis() - 10000);
			int schedulerCnt = schedulers.size();

			// 스케쥴러 정보가 없거나 단일이면 빠져 나간다.
			if (schedulerCnt == 0 || schedulerCnt == 1) {
				Thread.sleep(1000);
				return;
			}
			int remainder = total % schedulerCnt;
			List<List<Terms>> partition = ListUtils.partition(list, total / schedulerCnt);
			List<Terms> result = new ArrayList<Terms>();
			for (int i = 0; i < partition.size(); i++) {
				if (remainder == 0) {
					result.addAll(partition.get(i));
					System.out.println(i + " 해당 job size : " + result.size());
					String url = schedulers.get(i).get("INSTANCE_NAME").toString();
					// DB 상태 변경
					setSchedulerState(result);
					McpHttpUtils.addJobPost(url, result);
					result.clear();
				} else {
					// 마지막과 마지막 전꺼를 합치기위해
					if (schedulerCnt - 1 == i || schedulerCnt == i) {
						// 마지막일때 이전꺼에 추가하여 보내준다.
						if (schedulerCnt == i) {
							for (int j = 0; j < partition.get(i).size(); j++) {
								result.add(partition.get(i).get(j));
							}
							System.out.println("마지막 job size :" + result.size());
							String url = schedulers.get(i - 1).get("INSTANCE_NAME").toString();
							// DB 상태 변경
							setSchedulerState(result);
							McpHttpUtils.addJobPost(url, result);
							result.clear();
						} else {
							result.addAll(partition.get(i));
						}
					} else {
						result.addAll(partition.get(i));
						System.out.println(i + " 해당 job size : " + result.size());
						String url = schedulers.get(i).get("INSTANCE_NAME").toString();
						// DB 상태 변경
						setSchedulerState(result);
						McpHttpUtils.addJobPost(url, result);
						result.clear();
					}
				}
			}

			Thread.sleep(1000);
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setSchedulerState(List<Terms> list) {
		for (Terms terms : list) {
			terms.setState(1);
			schedulerService.setSchedulerState(terms);
		}
	}
}
