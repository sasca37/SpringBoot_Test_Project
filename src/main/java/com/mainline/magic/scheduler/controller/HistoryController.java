package com.mainline.magic.scheduler.controller;

import java.util.List;
import java.util.Map;

import com.mainline.magic.scheduler.dto.Criteria;
import com.mainline.magic.scheduler.dto.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
		return "/test";
	}
	
	@GetMapping("/test2")
	public String test2(Criteria cri, Model model ) throws Exception {
		/*List<McpTerms> list = mcpTermsService.selectList();
		List<McpTerms> list2 = mcpTermsService.selectLimit();
		model.addAttribute("list", list);
		log.info("list 값 : "+list);*/
		/*model.addAttribute("list2", list2);*/

		// 페이징

		// 전체 글 개수
		int boardListCnt = mcpTermsService.boardListCnt();

		// 페이징 객체
		Paging paging = new Paging();
		log.info("paging_cri"+cri);
		paging.setCri(cri);
		paging.setTotalCounting(boardListCnt);

		List<Map<String, Object>> list = mcpTermsService.boardList(cri);
		log.info("list_cri"+cri);
		model.addAttribute("list", list);
		model.addAttribute("paging", paging);
		model.addAttribute("cri", cri);
		model.addAttribute("boardListCnt", boardListCnt);
		log.info("list"+list);
		log.info("paging"+paging);
		return "/test2";
	}

	@GetMapping("/test3")
	public String test3() {
		return "/test3";
	}

	@GetMapping("/search")
	public void search(){
		log.info("search_get start");
	}

	@PostMapping("/search")
	public String searchPost(){
		log.info("redirect start");
		return "redirect:/MagicScheduler/test2";
	}

	/*@GetMapping("/tt")
	public @ResponseBody List<McpTerms> selectAll() {
		List<McpTerms> list = mcpTermsService.selectLimit();
		log.info("selectLimit 실행");
		return list;

	}*/
}
