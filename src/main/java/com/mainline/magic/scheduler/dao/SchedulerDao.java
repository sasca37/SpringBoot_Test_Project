package com.mainline.magic.scheduler.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mainline.magic.scheduler.dto.Terms;

@Mapper
public interface SchedulerDao {
	List<Map<String, Object>> getSchedulerState(@Param("date") Long date);
	int updateTermsLIStatus(Terms terms);
	int updateTermsGIStatus(Terms terms);
	List<Terms> getTermsLI();
	List<Terms> getTermsGI();
	int insertTermsLI(Terms terms);
	int insertTermsGI(Terms terms);
	List<Terms> termsVerificationLI(@Param("list") List<Terms> list);
	List<Terms> termsVerificationGI(@Param("list") List<Terms> list);
}
