package com.mainline.magic.scheduler.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mainline.magic.scheduler.config.McpProperties;
import com.mainline.magic.scheduler.dto.Terms;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CommonUtils {
	
	public static final String success = "02";
	public static final String start = "03";
	public static final String end = "04";
	public static final String fail = "98";
	public static final String makeFail = "99";
	public static final String jobName = "MainLineJob";
	public static final String jobGroup = "MainLineJobGroup";
	public static final String defaultUser = "SYSTEM";
	public static final String msgStr = "msg";
	public static final String dataStr = "data";
	public static final String codeStr = "code";
	public static final String LI = "LI";
	public static final String GI = "GI";
	private static Gson gson = new Gson();
	
	@Autowired
	private McpProperties properties;
	
	
	public boolean checkBoolean(String str) {
		return Boolean.valueOf(str != null ? str : "false");
	}
	
	public String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public Terms setDefaultUser(Terms terms) {
		if(terms.getCreator() == null || terms.getCreator().trim().length() == 0) {
			terms.setCreator(defaultUser);
		}
		
		if(terms.getUpdator() == null || terms.getUpdator().trim().length() == 0) {
			terms.setUpdator(defaultUser);
		}
		return terms;
	}
	
	public String getDateToString(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	public String toJsonArrayString(List<?> list) {
		if(list != null) {
			JsonArray array = gson.fromJson(gson.toJson(list), JsonArray.class);
			return gson.toJson(array);
		}else {
			return "";
		}
	}
	
	public String getResponseJson(String msg, Object obj) {
		JsonObject object = new JsonObject();
		object.addProperty(msgStr, msg);
		if(obj != null) {
			if(obj instanceof List) {
				object.add(dataStr, 	gson.fromJson(gson.toJson(obj), JsonArray.class));	
			}else {
				object.add(dataStr, 	gson.fromJson(gson.toJson(obj), JsonObject.class));	
			}
		}
		return gson.toJson(object);
	}
	
	public String getResponseJson(String msg, String code, Object obj) {
		JsonObject object = new JsonObject();
		object.addProperty(msgStr, msg);
		object.addProperty(codeStr, code);
		if(obj != null) {
			if(obj instanceof List) {
				object.add(dataStr, 	gson.fromJson(gson.toJson(obj), JsonArray.class));	
			}else {
				object.add(dataStr, 	gson.fromJson(gson.toJson(obj), JsonObject.class));	
			}
		}
		return gson.toJson(object);
	}
	
	public boolean isInsuranceTypeLI() {
		if(LI.equals(properties.getInsuranceType())) {
			return true;
		}else {
			return false;
		}
	}
	
}
