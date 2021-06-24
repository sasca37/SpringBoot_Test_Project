package com.mainline.magic.scheduler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainline.magic.scheduler.dao.McpTermsMergeLiDao;
import com.mainline.magic.scheduler.dto.McpTerms;

@Service
public class McpTermsService {
	
	@Autowired()
	private McpTermsMergeLiDao mcpTermsMergeLiDao;
	
	public List<McpTerms> selectList(){
		return mcpTermsMergeLiDao.selectAll();
	}

	
	
}
