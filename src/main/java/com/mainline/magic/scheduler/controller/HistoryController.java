package com.mainline.magic.scheduler.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mainline.magic.scheduler.dto.McpTerms;
import com.mainline.magic.scheduler.service.McpTermsService;

import jdk.internal.org.jline.utils.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Controller
public class HistoryController {

	@Autowired
	McpTermsService mcpTermsService;
	@GetMapping("/test")
	public String test() {
		return "/test";
	}
	
//	@GetMapping("/test2")
//	public String test2() {
//		return "/test2";
//	}
//	
	@GetMapping("/test3")
	public @ResponseBody List<McpTerms> selectAll() {
		List<McpTerms> allMembers = mcpTermsService.selectList();
		log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2");
		return allMembers;
	}
}
