package com.mainline.magic.scheduler.service;

import java.util.List;

import com.mainline.magic.scheduler.dto.Criteria;
import com.mainline.magic.scheduler.dto.SearchCriteria;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainline.magic.scheduler.dao.McpTermsMergeLiDao;
import com.mainline.magic.scheduler.dto.McpTerms;

@Log4j2
@Service
public class McpTermsService {
	
	@Autowired
	private McpTermsMergeLiDao mcpTermsMergeLiDao;
	

	/*public int boardListCnt(@Param("status") String status) throws Exception {
		return mcpTermsMergeLiDao.boardListCnt(status);
	}*/
	public int boardListCnt(Criteria cri) throws Exception {
		return mcpTermsMergeLiDao.boardListCnt(cri);
	}
	public List<McpTerms> boardList(Criteria cri) throws Exception {
		return mcpTermsMergeLiDao.boardList(cri);
	}


	public List<McpTerms> list(SearchCriteria scri) throws Exception {
		return mcpTermsMergeLiDao.list(scri);
	}

	public int listCount(SearchCriteria scri) throws Exception {
		return mcpTermsMergeLiDao.listCount(scri);
	}


}
