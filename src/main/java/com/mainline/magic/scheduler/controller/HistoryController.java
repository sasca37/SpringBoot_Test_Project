package com.mainline.magic.scheduler.controller;

import java.util.List;

import com.mainline.magic.scheduler.dto.Criteria;
import com.mainline.magic.scheduler.dto.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
		return "/test";
	}
	

	@GetMapping("/test4")
	public String test3(Criteria cri, Model model) throws Exception {

		// 페이징 전체 글 개수
		int boardListCnt = mcpTermsService.boardListCnt(cri);

		// 페이징 객체
		Paging paging = new Paging();
		paging.setCri(cri);
		log.info(cri);
		paging.setTotalCounting(boardListCnt);

		List<McpTerms> list = mcpTermsService.boardList(cri);
		model.addAttribute("list", list);
		model.addAttribute("paging", paging);
		model.addAttribute("cri", cri);
		model.addAttribute("boardListCnt", boardListCnt);
		log.info("list"+list);
		log.info("cri status : "+cri);
		return "/test4";
	}

}
