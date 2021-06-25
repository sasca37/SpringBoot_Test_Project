package com.mainline.magic.scheduler.controller;

import java.util.List;
import java.util.Map;

import com.mainline.magic.scheduler.dto.Criteria;
import com.mainline.magic.scheduler.dto.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mainline.magic.scheduler.dto.McpTerms;
import com.mainline.magic.scheduler.service.McpTermsService;
import lombok.extern.log4j.Log4j2;
@Log4j2
@Controller
@RequestMapping("/MagicScheduler")
public class HistoryController {

	@Autowired
	McpTermsService mcpTermsService;


	@GetMapping("/test")
	public String test() {
		log.info("@@!!!!");
		return "/test";
	}
	
	@GetMapping("/test2")
	public String test2(Criteria cri, Model model ) throws Exception {
		List<McpTerms> list = mcpTermsService.selectList();
		List<McpTerms> list2 = mcpTermsService.selectLimit();
		model.addAttribute("list", list);
		log.info("list 값 : "+list);
		model.addAttribute("list2", list2);

		// 페이징

		// 전체 글 개수
		int boardListCnt = mcpTermsService.boardListCnt();

		// 페이징 객체
		Paging paging = new Paging();
		paging.setCri(cri);
		paging.setTotalCount(boardListCnt);

		List<Map<String, Object>> boardList = mcpTermsService.boardList(cri);

		model.addAttribute("boardList", boardList);
		model.addAttribute("paging", paging);

		return "/test2";
	}

	@GetMapping("/test3")
	public String test3() {
		return "/test3";
	}



	@GetMapping("/tt")
	public @ResponseBody List<McpTerms> selectAll() {
		List<McpTerms> list = mcpTermsService.selectLimit();
		log.info("selectLimit 실행");
		return list;

	}
}
