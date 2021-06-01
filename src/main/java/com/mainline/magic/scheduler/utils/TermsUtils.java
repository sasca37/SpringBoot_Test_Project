package com.mainline.magic.scheduler.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mainline.magic.scheduler.config.McpProperties;
import com.mainline.magic.scheduler.dto.Terms;
import com.mainline.magic.scheduler.service.SchedulerService;
import com.mainline.magicterms.clause.prop.PropertyConstance;
import com.mainline.magicterms.poc.customize.terms.BaseUserTerms;
import com.mainline.magicterms.poc.customize.terms.ProductInfo;
import com.mainline.magicterms.userterms.batch.BatchProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TermsUtils {
	
	private final McpProperties mcpProperties ;
	
	private final SchedulerService schedulerService ;
	
	private ExecutorService executorService;
	
	
	@Autowired
	public TermsUtils(McpProperties mcpProperties,SchedulerService schedulerService) {
		this.mcpProperties = mcpProperties;
		this.schedulerService = schedulerService;
		setProperties();
		setThreadSize();
	}
	
	private void setThreadSize() {
		// thread 숫자 조정
		if(mcpProperties.getThreadSize() != null) {
			executorService = Executors.newFixedThreadPool(Integer.parseInt(mcpProperties.getThreadSize()));
		}else {
			// 서버의 프로세스의 절반으로 세팅
			executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/2);
		}
	}
	
	private void setProperties() {
		System.setProperty(mcpProperties.getInsuranceTypeKey(), mcpProperties.getInsuranceType());
		System.setProperty(mcpProperties.getMagictermsDocumentKey(), mcpProperties.getMagictermsDocumentType());
		
		// 폴더 경로 주입 및 폴더 생성
		Properties props = new Properties();
		BatchProperties.getInstance().setProperties(props);
		BatchProperties.getInstance().setMtsPath(mcpProperties.getMtsPath());
		BatchProperties.getInstance().setMtsResultPath(mcpProperties.getMtsResultPath());
		
		File dir = new File(mcpProperties.getMtsPath());
		if(!dir.exists()) {
			log.info("Mts Directory Make Is " + dir.mkdirs());
		}
		
		dir = new File(mcpProperties.getMtsResultPath());
		if(!dir.exists()) {
			log.info("MtsResult Directory Make Is " + dir.mkdirs());
		}
	}
	
	public void executor(Terms terms) {
		if(executorService == null) {
			setThreadSize();
		}
		executorService.submit(new Worker(terms));
	}
	
//	public void makeTerms(Terms terms) {
//		//진행중 상태변경
//		terms.setStatus(commonUtils.start);
//		
//		try {
//			File mtsDir = new File(BatchProperties.getMtsPath());
//			
//			ProductInfo product = null;
//			
//			if(PropertyConstance.isInsuranceTypeLI()) {
//				product = BaseUserTerms.makeProductInfoLI(terms.getCode());
//			}else {
//				product = BaseUserTerms.makeProductInfoGI(terms.getId(), terms.getCode());
//			}
//			
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//			String date = format.format(new Date());
//			
//			BaseUserTerms bt = new BaseUserTerms(null, product.getProductName(), PropertyConstance.getDocumentType(), PropertyConstance.getInsuranceType());
//			bt.OUTPUT_DIR = new File(BatchProperties.getMtsResultPath()+File.separator+date+File.separator+terms.getMergeId());
//			bt.init();
//			
//			bt.setResultFileName(terms.getMergeId());
//			bt.setMtsDir(mtsDir);
//			bt.setInfo(product);
//			bt.merge();
//			
//			terms.setStatus(commonUtils.end);
//			updateStatus(terms);
//		}catch(Exception e) {
//			terms.setStatus(commonUtils.makeFail);
//			updateStatus(terms);
//			//에러 파일 생성
//			e.printStackTrace();
//		}
//	}
	
	private int updateStatus(Terms terms) {
		return schedulerService.updateTermsStatus(terms);
	}
	
	class Worker implements Runnable{
		private Terms terms;
		
		public Worker(Terms terms) {
			super();
			this.terms = terms;
		}

		@Override
		public void run() {
			//진행중 상태변경
			terms.setStatus(CommonUtils.start);
			log.info(Thread.currentThread().getName());
			try {
				File mtsDir = new File(BatchProperties.getMtsPath());
				
				ProductInfo product = null;
				
				if(PropertyConstance.isInsuranceTypeLI()) {
					product = BaseUserTerms.makeProductInfoLI(terms.getCode());
				}else {
					product = BaseUserTerms.makeProductInfoGI(terms.getId(), terms.getCode());
				}
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String date = format.format(new Date());
				
				BaseUserTerms bt = new BaseUserTerms(null, product.getProductName(), PropertyConstance.getDocumentType(), PropertyConstance.getInsuranceType());
				bt.OUTPUT_DIR = new File(BatchProperties.getMtsResultPath()+File.separator+date+File.separator+terms.getMergeId());
				bt.init();
				bt.setResultFileName(terms.getMergeId());
				bt.setMtsDir(mtsDir);
				bt.setInfo(product);
				bt.merge();
				
				terms.setStatus(CommonUtils.end);
				updateStatus(terms);
			}catch(Exception e) {
				terms.setStatus(CommonUtils.makeFail);
				updateStatus(terms);
				//에러 파일 생성
				e.printStackTrace();
			}// TODO Auto-generated method stub
			
		}
		
	}
}
