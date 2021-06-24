package com.mainline.magic.scheduler.utils;

import java.io.IOException;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.mainline.magic.scheduler.dto.Terms;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class McpHttpUtils {


	@Autowired
	private CommonUtils utils;
	
	private String addJobs = "/add/jobs";
	private String addJob = "/add/job";
	
	
	/**
	 * 여러건의 작업정보를 보낸다.
	 * @param url
	 * @param list
	 * @return
	 * @throws IOException
	 */
	public boolean addJobPost(String url, List<Terms> list) {
		boolean result = false;
		try{
			final CloseableHttpClient httpclient = HttpClients.createDefault();
			final HttpPost httppost = new HttpPost("http://" + url + addJobs);
			httppost.addHeader("Accept", "application/json");
			httppost.addHeader("content-type",ContentType.APPLICATION_JSON);
			
			httppost.setEntity(new StringEntity(utils.toJsonArrayString(list)));
			final CloseableHttpResponse response = httpclient.execute(httppost);
			final HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				result = response.getCode() == HttpStatus.SC_OK;
				log.info("Response status ok : " + result);
			}
			EntityUtils.consume(resEntity);

		}catch (Exception e) {
			log.error("addJobPost list url : {} terms {}",url,utils.toJsonArrayString(list),e);
			return result;
		}
		return result;
	}

	/**
	 * 단건의 작업정보를 보낸다.
	 * @param url
	 * @param terms
	 * @return
	 * @throws IOException
	 */
	public boolean addJobPost(String url, Terms terms)  {
		boolean result = false;
		try{
			final CloseableHttpClient httpclient = HttpClients.createDefault();
			final HttpPost httppost = new HttpPost("http://" + url + addJob);
			httppost.addHeader("Accept", "application/json");
			httppost.addHeader("content-type",ContentType.APPLICATION_JSON);
			Gson gson = new Gson();
			httppost.setEntity(new StringEntity(gson.toJson(terms)));
			final CloseableHttpResponse response = httpclient.execute(httppost);
			final HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				result = response.getCode() == HttpStatus.SC_OK;
				log.info("Response status ok : " + result);
			}
			EntityUtils.consume(resEntity);
		}catch (Exception e) {
			log.error("addJobPost terms url : {} terms {}",url,terms,e);
			return result;
		}
		return result;
	}
	

	/**
	 * @param url
	 * @param body
	 * @return
	 */
	public String httpPost(String url, String jsonBody)  {
		String result = null;
		try{
			final CloseableHttpClient httpclient = HttpClients.createDefault();
			final HttpPost httppost = new HttpPost(url);
			httppost.addHeader("Accept", "application/json");
			httppost.addHeader("content-type",ContentType.APPLICATION_JSON);
			httppost.setEntity(new StringEntity(jsonBody));
			final CloseableHttpResponse response = httpclient.execute(httppost);
			final HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				boolean flag = response.getCode() == HttpStatus.SC_OK;
				if(flag) {
					return  EntityUtils.toString(resEntity);
				}else {
					log.info("httpPost  url : {}, body {}, response code : {}",url,jsonBody, response.getCode());
				}
			}
			EntityUtils.consume(resEntity);
		}catch (Exception e) {
			log.error("httpPost  url : {} body {}",url,jsonBody,e);
			return result;
		}
		return result;
	}
}
