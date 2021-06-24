package com.mainline.magic.scheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HistoryController {

	
	@GetMapping("/test")
	public String test() {
		return "test";
	}
}
