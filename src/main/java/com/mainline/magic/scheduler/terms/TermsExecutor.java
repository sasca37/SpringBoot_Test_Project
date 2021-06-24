package com.mainline.magic.scheduler.terms;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mainline.magic.scheduler.config.McpProperties;
import com.mainline.magic.scheduler.dto.Publishing;
import com.mainline.magic.scheduler.dto.Terms;
import com.mainline.magic.scheduler.service.SchedulerService;
import com.mainline.magic.scheduler.service.UserTermsService;
import com.mainline.magic.scheduler.utils.CommonUtils;
import com.mainline.magicterms.clause.prop.PropertyConstance;
import com.mainline.magicterms.poc.customize.terms.AbstractUserTerms;
import com.mainline.magicterms.poc.customize.terms.BaseUserTerms;
import com.mainline.magicterms.poc.customize.terms.ProductInfo;
import com.mainline.magicterms.userterms.batch.BatchProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TermsExecutor {

	private final McpProperties mcpProperties;

	private final SchedulerService schedulerService;
	
	private final UserTermsService termsService;

	private ExecutorService executorService;

	@Autowired
	public TermsExecutor(McpProperties mcpProperties, SchedulerService schedulerService, UserTermsService termsService) {
		this.mcpProperties = mcpProperties;
		this.schedulerService = schedulerService;
		this.termsService = termsService;
		setProperties();
		setThreadSize();
	}

	/**
	 * thread size 설정
	 */
	private void setThreadSize() {
		// thread 숫자 조정
		if (mcpProperties.getThreadSize() != null) {
			executorService = Executors.newFixedThreadPool(Integer.parseInt(mcpProperties.getThreadSize()));
		} else {
			// 서버의 프로세스의 절반으로 세팅
			int threadSize = Runtime.getRuntime().availableProcessors() / 2;
			executorService = Executors.newFixedThreadPool(threadSize == 0 ? 1 : threadSize);
		}
	}

	/**
	 * 약관 제작에 필요한 property 설정
	 */
	private void setProperties() {
		System.setProperty(mcpProperties.getInsuranceTypeKey(), mcpProperties.getInsuranceType());
		System.setProperty(mcpProperties.getMagictermsDocumentKey(), mcpProperties.getMagictermsDocumentType());

		// 폴더 경로 주입 및 폴더 생성
		Properties props = new Properties();
		BatchProperties.getInstance().setProperties(props);
		BatchProperties.getInstance().setMtsPath(mcpProperties.getMtsPath());
		BatchProperties.getInstance().setMtsResultPath(mcpProperties.getMtsResultPath());

		File dir = new File(mcpProperties.getMtsPath());
		if (!dir.exists()) {
			log.info("Mts Directory Make Is " + dir.mkdirs());
		}

		dir = new File(mcpProperties.getMtsResultPath());
		if (!dir.exists()) {
			log.info("MtsResult Directory Make Is " + dir.mkdirs());
		}
	}

	
	/**
	 * 약관 제작 실행 Worker 실행
	 * @param terms
	 */
	public Future<Terms> callWorkerExecutor(Terms terms) {
		return executorService.submit(new CallWorker(terms,termsService,schedulerService));
	}
	
	/**
	 * 약관 제작 실행 CallWorker 실행
	 * @param terms
	 */
	public void workerExecutor(Terms terms) {
		executorService.submit(new Worker(terms,termsService,schedulerService));
	}
	
	/**
	 * 어플리케이션 종료시 thread 종료
	 */
	@PreDestroy
	public void shutdown() {
		if (executorService != null) {
			executorService.shutdown(); 
			try {
				if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
					executorService.shutdownNow(); 
					if (!executorService.awaitTermination(60, TimeUnit.SECONDS))
						System.err.println("executorService did not terminate");
				}
			} catch (InterruptedException ie) {
				executorService.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
	}
	/**
	 * 상태 코드 업데이트
	 * @param terms
	 * @return
	 */
	private int updateStatus(Terms terms) {
		return schedulerService.updateTermsStatus(terms);
	}

}
