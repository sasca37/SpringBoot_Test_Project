package com.mainline.magic.scheduler.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.mainline.magic.scheduler.dto.Criteria;
import com.mainline.magic.scheduler.dto.Paging;
import com.mainline.magic.scheduler.dto.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.mainline.magic.scheduler.dto.McpTerms;
import com.mainline.magic.scheduler.service.McpTermsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	/*@GetMapping("/test2")
	public String test2(Criteria cri, Model model) throws Exception {
		log.info("cri : "+cri);
		// 페이징 전체 글 개수
		int boardListCnt = mcpTermsService.boardListCnt();

		// 페이징 객체
		Paging paging = new Paging();
		paging.setCri(cri);
		paging.setTotalCounting(boardListCnt);

		List<McpTerms> list = mcpTermsService.boardList(cri);
		model.addAttribute("list", list);
		model.addAttribute("paging", paging);
		model.addAttribute("cri", cri);
		model.addAttribute("boardListCnt", boardListCnt);
		log.info("list"+list);
		log.info("paging"+paging);
		return "/test2";
	}*/

	@GetMapping("/test4")
	public String test3(Criteria cri, Model model) throws Exception {
		// 날짜
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c1 = Calendar.getInstance();
		String today = sdf.format(c1.getTime());
		log.info("today: "+today);
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


	@GetMapping("/search")
	public String searchPost(Criteria cri, RedirectAttributes redirectAttributes){
		//log.info("redirect cri: "+cri);
		/*cri.setContract_date(cri.getContract_date());
		cri.setRegistration_num(cri.getRegistration_num());
		cri.setStatus(cri.getStatus());
		cri.setCreated_start(cri.getCreated_start());
		cri.setCreated_end(cri.getCreated_end());*/
		redirectAttributes.addAttribute("status", cri.getStatus());
		/*redirectAttributes.addAttribute("contract_date", cri.getContract_date());
		redirectAttributes.addAttribute("registration_num", cri.getRegistration_num());

		redirectAttributes.addAttribute("created_start", cri.getCreated_start());
		redirectAttributes.addAttribute("created_end", cri.getCreated_end());*/

		return "redirect:/MagicScheduler/test3";
	}

}
