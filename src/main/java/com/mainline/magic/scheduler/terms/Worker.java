package com.mainline.magic.scheduler.terms;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

/**
 * 실제 약관 제작을 하는 Runnable
 * @author mainline
 *
 */
@Slf4j
public class Worker extends BaseWorker implements Runnable{
	private Terms terms;
	private UserTermsService termsService;
	private SchedulerService schedulerService;
	
	public Worker(Terms terms, UserTermsService termsService, SchedulerService schedulerService) {
		this.terms = terms;
		this.termsService = termsService;
		this.schedulerService = schedulerService;
		
	}
	
	/**
	 * 상태 코드 업데이트
	 * @param terms
	 * @return
	 */
	private int updateStatus(Terms terms) {
		return schedulerService.updateTermsStatus(terms);
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
				String[] codes = terms.getCode().split(",");
				List<String> list = Arrays.asList(codes);
				List<Publishing> publishings = termsService.getLIListForCodes(terms.getContractDate(), list);
				product = BaseUserTerms.makeProductInfoLI(publishingToMtsPublishingDto(publishings));
			} else {
				Publishing publishing = termsService.getGIListForCode(terms.getContractDate(), terms.getCode());
				product = BaseUserTerms.makeProductInfoGI(publishingToMtsPublishingDto(publishing), terms.getCode());
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
								log.error("Worker errorProcess key : {} ", key, t);
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
			
			log.error("Worker run terms : {} ", terms.toString(), e);
			try {
				File f = createErrorDir(terms.getMergeId());
				createErrorFile(f, AbstractUserTerms.errorToStr(e));
			}catch(Exception ee) {
				log.error("Worker run createErrorFile ", ee);
			}
		} 
	}
}
