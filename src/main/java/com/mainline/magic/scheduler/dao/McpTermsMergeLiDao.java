package com.mainline.magic.scheduler.dao;

import java.util.List;


import com.mainline.magic.scheduler.dto.Criteria;
import org.apache.ibatis.annotations.Mapper;

import com.mainline.magic.scheduler.dto.McpTerms;

@Mapper
public interface McpTermsMergeLiDao {

	 List<McpTerms> boardList(Criteria cri) throws Exception;
	 int boardListCnt(Criteria cri) throws Exception;
	 String threeMonthAgo() throws Exception;
	 String threeMonthLater() throws Exception;
}
