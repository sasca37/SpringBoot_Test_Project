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

@Component
public class CommonUtils {
	public static final String httpSuccess = "00";
	public static final String httpError = "100";
	public static final String httpFail = "99";
	
	// success : 작업 요청을 받았다 
	public static final String success = "02";
	//약관 제작 시작
	public static final String start = "03";
	//약관 제작 완료
	public static final String end = "04";
	//실패 
	public static final String fail = "98";
	//약관 제작이 실패 
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
	
	
	/**
	 * String true, false 를 boolean type으로 변환용
	 * @param str
	 * @return
	 */
	public boolean checkBoolean(String str) {
		return Boolean.valueOf(str != null ? str : "false");
	}
	
	/**
	 * uuid 생성용
	 * @return
	 */
	public String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * Terms 기본 유저 세팅 : default SYSTEM
	 * @param terms
	 * @return
	 */
	public Terms setDefaultUser(Terms terms) {
		if(terms.getCreator() == null || terms.getCreator().trim().length() == 0) {
			terms.setCreator(defaultUser);
		}
		
		if(terms.getUpdator() == null || terms.getUpdator().trim().length() == 0) {
			terms.setUpdator(defaultUser);
		}
		return terms;
	}
	
	/**
	 * Date 문자 변환용
	 * @param date
	 * @param pattern
	 * @return
	 */
	public String getDateToString(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	

	/**
	 * json string을 object로 변환
	 * @param <T>
	 * @param json
	 * @param clazz
	 * @return
	 */
	public  <T>T fromJsonObject(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}
	
	/**
	 * obj 를 json string 으로 변환
	 * @param obj
	 * @return
	 */
	public String toJsonString(Object obj) {
		return gson.toJson(obj);
	}
	
	/**
	 * list를 json string 으로 변환
	 * @param list
	 * @return
	 */
	public String toJsonArrayString(List<?> list) {
		if(list != null) {
			JsonArray array = gson.fromJson(gson.toJson(list), JsonArray.class);
			return gson.toJson(array);
		}else {
			return "";
		}
	}
	
	/**
	 * api 결과 반환용 메소드 code X
	 * @param msg
	 * @param obj
	 * @return
	 */
	public String getResponseJson(String msg, Object obj) {
		JsonObject object = new JsonObject();
		object.addProperty(msgStr, msg);
		if(obj != null) {
			if(obj instanceof List) {
				object.add(dataStr, 	gson.fromJson(gson.toJson(obj), JsonArray.class));	
			}else if(obj instanceof String) {
				object.addProperty(dataStr, (String)obj);
			}else {
				object.add(dataStr, 	gson.fromJson(gson.toJson(obj), JsonObject.class));	
			}
		}
		return gson.toJson(object);
	}
	

	
	/**
	 * api 결과 변환용 메소드 code O
	 * @param msg
	 * @param code
	 * @param obj
	 * @return
	 */
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
	
	/**
	 * 생보 손보 체크용
	 * @return
	 */
	public boolean isInsuranceTypeLI() {
		if(LI.equals(properties.getInsuranceType())) {
			return true;
		}else {
			return false;
		}
	}
	
}
