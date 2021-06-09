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

import com.mainline.magic.scheduler.config.McpProperties;
import com.mainline.magic.scheduler.dto.Terms;
import com.mainline.magic.scheduler.service.SchedulerService;
import com.mainline.magic.scheduler.utils.CommonUtils;
import com.mainline.magic.scheduler.utils.McpHttpUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisallowConcurrentExecution
public class CronJob extends QuartzJobBean {

	@Autowired
	SchedulerService schedulerService;
	
	@Autowired
	private McpProperties mcpProperteis;

	/**
	 * 요청 작업을 분배해서 본인 포함 다은 스케쥴러 서버에 작업을 분배하여 준다.
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			log.info("================= CronJob =================");
			// 단일 모드 동작 여부
			List<Terms> list = schedulerService.getTerms();
			int total = list.size();
			if(total == 0) {
				Thread.sleep(2000);
				log.info("job count : " + total);
				return;
			}
			log.info("스케쥴러 갯수 가져온다.");
			// 10초를 빼준다.
			List<Map<String, Object>> schedulers = schedulerService
					.getSchedulerState(System.currentTimeMillis() - 10000);
			int schedulerCnt = schedulers.size();

			log.info("schedulerCnt :"+ schedulerCnt);
			// 스케쥴러 정보가 없거나 단일이면 빠져 나간다.
			if (schedulerCnt == 0) {
				Thread.sleep(1000);
				return;
			}
			
			if(mcpProperteis.getLoadbalancePath() != null) {
				log.info("로드 벨런싱 주소로 던진다");
				l4Loadbalance(list);
			}else {
				loadbalance(list, schedulers);
			}
			
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 상태 코드 업데이트 
	 * @param list
	 */
	private void updateTermsStatus(List<Terms> list, String status) {
		for (Terms terms : list) {
			terms.setStatus(status);
			updateTermsStatus(terms);
		}
	}
	/**
	 * 상태 코드 없데이트 
	 * @param terms
	 */
	private void updateTermsStatus(Terms terms) {
		schedulerService.updateTermsStatus(terms);
	}
	
	/**
	 * L4 로드밸런싱 path 정보가 있다면 해당 경로로 작업정보를 보낸다.
	 * @param list
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void l4Loadbalance(List<Terms> list)  throws InterruptedException, IOException {
		String url = mcpProperteis.getLoadbalancePath();
		log.info(url);
		for(Terms terms : list) {
			terms.setStatus(CommonUtils.success);
			updateTermsStatus(terms);
			if(McpHttpUtils.addJobPost(url, terms)) {
				log.info("http addJobPost success");
			}else {
				terms.setStatus(CommonUtils.fail);
				updateTermsStatus(terms);
			}
		}
	}
	
	
	/**
	 * L4 로드밸런싱 path가 없은때 Instance_name 정보로 작업을 분배하여 던져 준다.
	 * @param list
	 * @param schedulers
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void loadbalance(List<Terms> list, List<Map<String, Object>> schedulers)  throws InterruptedException, IOException {
		int total = list.size();
		int schedulerCnt = schedulers.size();
		int remainder = total % schedulerCnt;
		List<List<Terms>> partition = ListUtils.partition(list, total / schedulerCnt);
		List<Terms> result = new ArrayList<Terms>();
		String url = null; 
		for (int i = 0; i < partition.size(); i++) {
			if (remainder == 0) {
				url = schedulers.get(i).get("INSTANCE_NAME").toString();
				// DB 상태 변경
				updateTermsStatus(partition.get(i), CommonUtils.success);
				if(McpHttpUtils.addJobPost(url, partition.get(i))) {
					log.info("http addJobPost success");
				}else {
					// 전송 실패로 표시
					updateTermsStatus(partition.get(i), CommonUtils.fail);
				}
			} else {
				// 마지막과 마지막 전꺼를 합치기위해
				if (schedulerCnt - 1 == i || schedulerCnt == i) {
					// 마지막일때 이전꺼에 추가하여 보내준다.
					if (schedulerCnt == i) {
						for (int j = 0; j < partition.get(i).size(); j++) {
							result.add(partition.get(i).get(j));
						}
						url = schedulers.get(i - 1).get("INSTANCE_NAME").toString();
						// DB 상태 변경
						updateTermsStatus(result, CommonUtils.success);
						if(McpHttpUtils.addJobPost(url, result)) {
							log.info("http addJobPost success");
						}else {
							// 전송 실패로 표시
							updateTermsStatus(result, CommonUtils.fail);
						}
						result.clear();
					} else {
						result.addAll(partition.get(i));
					}
				} else {
					result.addAll(partition.get(i));
					url = schedulers.get(i).get("INSTANCE_NAME").toString();
					// DB 상태 변경
					updateTermsStatus(result, CommonUtils.success);
					if(McpHttpUtils.addJobPost(url, result)) {
						log.info("http addJobPost success");
					}else {
						// 전송 실패로 표시
						updateTermsStatus(result, CommonUtils.fail);
					}
					result.clear();
				}
			}
		}
	}
}
