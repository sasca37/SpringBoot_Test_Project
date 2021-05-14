package com.mainline.magic.scheduler.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainline.magic.scheduler.dao.ScheduleDao;
import com.mainline.magic.scheduler.dto.Terms;

@Service
public class SchedulerService {
	
	@Autowired
	ScheduleDao scheduleDao;
	
	public int getConTrigger(){
		return scheduleDao.getConTrigger();
	}
	
	public List<Terms> getTermsJob(){
		return scheduleDao.getTermsJob();
	}
	
	public List<Map<String, Object>> getSchedulerState(Long date){
		return scheduleDao.getSchedulerState(date);
	}
	
	public int setSchedulerState(Terms terms) {
		return scheduleDao.setSchedulerState(terms);
	}

}
