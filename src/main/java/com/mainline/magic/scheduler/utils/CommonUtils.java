package com.mainline.magic.scheduler.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.mainline.magic.scheduler.dto.Terms;

@Component
public class CommonUtils {
	
	public static final String success = "02";
	public static final String start = "03";
	public static final String end = "04";
	public static final String fail = "98";
	public static final String makeFail = "99";
	public static final String jobName = "MainLineJob";
	public static final String jobGroup = "MainLineJobGroup";
	
	public boolean checkBoolean(String str) {
		return Boolean.valueOf(str != null ? str : "false");
	}
	
	public String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public Terms setDefaultUser(Terms terms) {
		if(terms.getCreator() == null || terms.getCreator().trim().length() == 0) {
			terms.setCreator("SYSTEM");
		}
		
		if(terms.getUpdator() == null || terms.getUpdator().trim().length() == 0) {
			terms.setUpdator("SYSTEM");
		}
		return terms;
	}
	
	public String getDateToString(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
}
