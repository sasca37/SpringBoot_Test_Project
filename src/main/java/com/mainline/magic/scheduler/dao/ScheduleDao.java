package com.mainline.magic.scheduler.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mainline.magic.scheduler.dto.Terms;

@Mapper
public interface ScheduleDao {
	int getConTrigger();
	List<Terms> getTermsJob();
	List<Map<String, Object>> getSchedulerState(@Param("date") Long date);
	int setSchedulerState(Terms terms);
}
