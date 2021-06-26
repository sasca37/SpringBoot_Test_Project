package com.mainline.magic.scheduler.service;

import java.util.List;
import java.util.Map;

import com.mainline.magic.scheduler.dto.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainline.magic.scheduler.dao.McpTermsMergeLiDao;
import com.mainline.magic.scheduler.dto.McpTerms;

@Service
public class McpTermsService {
	
	@Autowired
	private McpTermsMergeLiDao mcpTermsMergeLiDao;
	
	public List<McpTerms> selectList(){
		return mcpTermsMergeLiDao.selectAll();
	}

	public List<McpTerms> selectLimit(){
		return mcpTermsMergeLiDao.selectLimit();
	}

	public int boardListCnt() throws Exception {
		return mcpTermsMergeLiDao.boardListCnt();
	}

	public List<Map<String, Object>> boardList(Criteria cri) throws Exception {
		return mcpTermsMergeLiDao.boardList(cri);
	}





}
