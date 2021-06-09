package com.mainline.magic.scheduler.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainline.magic.scheduler.config.McpProperties;
import com.mainline.magic.scheduler.dao.SchedulerDao;
import com.mainline.magic.scheduler.dto.Terms;
import com.mainline.magic.scheduler.utils.CommonUtils;

@Service
public class SchedulerService {
	
	@Autowired
	private SchedulerDao scheduleDao;
	
	
	@Autowired
	private CommonUtils commonUtils;
	
	public List<Map<String, Object>> getSchedulerState(Long date){
		return scheduleDao.getSchedulerState(date);
	}
	
	public int updateTermsStatus(Terms terms) {
		if(commonUtils.isInsuranceTypeLI()) {
			return scheduleDao.updateTermsLIStatus(terms);
		}else {
			return scheduleDao.updateTermsGIStatus(terms);
		}
	}
	
	public List<Terms> getTerms(){
		if(commonUtils.isInsuranceTypeLI()) {
			return scheduleDao.getTermsLI();
		}else {
			return scheduleDao.getTermsGI();
		}
	}

	public int insertTerms(Terms terms) {
		if(commonUtils.isInsuranceTypeLI()) {
			return scheduleDao.insertTermsLI(terms);
		}else {
			return scheduleDao.insertTermsGI(terms);
		}
	}
	
	public List<Terms> termsVerification(List<Terms> list) {
		if(commonUtils.isInsuranceTypeLI()) {
			return scheduleDao.termsVerificationLI(list);
		}else {
			return scheduleDao.termsVerificationGI(list);
		}
	}
}
