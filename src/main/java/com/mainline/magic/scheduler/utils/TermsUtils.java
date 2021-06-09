package com.mainline.magic.scheduler.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mainline.magic.scheduler.config.McpProperties;
import com.mainline.magic.scheduler.dto.Terms;
import com.mainline.magic.scheduler.service.SchedulerService;
import com.mainline.magicterms.clause.prop.PropertyConstance;
import com.mainline.magicterms.poc.customize.terms.AbstractUserTerms;
import com.mainline.magicterms.poc.customize.terms.BaseUserTerms;
import com.mainline.magicterms.poc.customize.terms.ProductInfo;
import com.mainline.magicterms.userterms.batch.BatchProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TermsUtils {

	private final McpProperties mcpProperties;

	private final SchedulerService schedulerService;

	private ExecutorService executorService;

	@Autowired
	public TermsUtils(McpProperties mcpProperties, SchedulerService schedulerService) {
		this.mcpProperties = mcpProperties;
		this.schedulerService = schedulerService;
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
	 * 약관 제작 실행 Runnable 실행
	 * @param terms
	 */
	public void executor(Terms terms) {
		executorService.submit(new Worker(terms));
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
	private int updateStatus(Terms terms) {
		return schedulerService.updateTermsStatus(terms);
	}

	class Worker implements Runnable {
		private Terms terms;

		public Worker(Terms terms) {
			this.terms = terms;
		}
		
		private File createErrorDir(String key) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String date = sdf.format(new Date());
			
			File f = new File(BatchProperties.getMtsResultPath(), date);
			if(!f.exists()) {
				f.mkdir();
			}
			
			File file = new File(f, key + ".txt");
			return file;
		}
		
		private void createErrorFile(File f, String errMsg) throws Exception{
			OutputStreamWriter os = null;
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(f);
				os = new OutputStreamWriter(fos, "UTF-8");
				
				os.write(errMsg);
				os.flush();
			}finally {
				if(fos != null) {
					try {
						fos.close();
					}catch(Exception e) {}
				}
				if(os != null) {
					try {
						os.close();
					}catch(Exception e) {}
				}
			}
		}

		@Override
		public void run() {
			// 진행중 상태변경
			terms.setStatus(CommonUtils.start);
			log.info(Thread.currentThread().getName());
			boolean isSuccess[] = {true};
			try {
				File mtsDir = new File(BatchProperties.getMtsPath());

				ProductInfo product = null;

				if (PropertyConstance.isInsuranceTypeLI()) {
					product = BaseUserTerms.makeProductInfoLI(terms.getCode());
				} else {
					product = BaseUserTerms.makeProductInfoGI(terms.getId(), terms.getCode());
				}

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String date = format.format(new Date());
				
				final String key = terms.getMergeId();

				BaseUserTerms bt = new BaseUserTerms(null, product.getProductName(),
						PropertyConstance.getDocumentType(), PropertyConstance.getInsuranceType()) {
							@Override
							public void deleteTempFile(File[] files, String name) {
								super.deleteTempFile(files, key + ".pdf");
							}

							@Override
							public void errorProcess(Throwable t) {
								super.errorProcess(t);
								
								isSuccess[0] = false;
								try {
									release();
									File f = createErrorDir(key);
									createErrorFile(f, AbstractUserTerms.errorToStr(t));
								}catch(Exception e) {
									e.printStackTrace();
								}
							}
				};
				
				bt.OUTPUT_DIR = new File(BatchProperties.getMtsResultPath() + File.separator + date + File.separator
						+ terms.getMergeId());
				terms.setPath(bt.OUTPUT_DIR.getAbsolutePath());
				bt.init();
				bt.setResultFileName(terms.getMergeId());
				bt.setMtsDir(mtsDir);
				bt.setInfo(product);
				bt.merge();
				
				//완료 상태변경
				if(isSuccess[0]) {
					terms.setStatus(CommonUtils.end);
					updateStatus(terms);
				}else {
					terms.setStatus(CommonUtils.makeFail);
					updateStatus(terms);
				}

			} catch (Exception e) {
				terms.setStatus(CommonUtils.makeFail);
				updateStatus(terms);
				// 에러 파일 생성
				e.printStackTrace();
				try {
					File f = createErrorDir(terms.getMergeId());
					createErrorFile(f, AbstractUserTerms.errorToStr(e));
				}catch(Exception ee) {
					ee.printStackTrace();
				}
			} 
		}
	}
}
