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
	ScheduleDao cronScheduleDao;
	
	public int getConTrigger(){
		return cronScheduleDao.getConTrigger();
	}
	
	public List<Terms> getTermsJob(){
		return cronScheduleDao.getTermsJob();
	}
	
	public List<Map<String, Object>> getSchedulerState(Long date){
		return cronScheduleDao.getSchedulerState(date);
	}

}
