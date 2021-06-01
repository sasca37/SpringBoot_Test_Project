package com.mainline.magic.scheduler.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainline.magic.scheduler.config.McpProperties;
import com.mainline.magic.scheduler.dao.SchedulerDao;
import com.mainline.magic.scheduler.dto.Terms;

@Service
public class SchedulerService {
	
	@Autowired
	private SchedulerDao scheduleDao;
	
	@Autowired
	private McpProperties properties;
	
	public List<Map<String, Object>> getSchedulerState(Long date){
		return scheduleDao.getSchedulerState(date);
	}
	
	public int updateTermsStatus(Terms terms) {
		if("LI".equals(properties.getInsuranceType())) {
			return scheduleDao.updateTermsLIStatus(terms);
		}else {
			return scheduleDao.updateTermsGIStatus(terms);
		}
	}
	
	public List<Terms> getTermsLi(){
		return scheduleDao.getTermsLi();
	}

	public int insertTerms(Terms terms) {
		if("LI".equals(properties.getInsuranceType())) {
			return scheduleDao.insertTermsLI(terms);
		}else {
			return scheduleDao.insertTermsGI(terms);
		}
	}
}
