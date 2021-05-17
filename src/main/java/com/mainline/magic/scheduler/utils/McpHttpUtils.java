package com.mainline.magic.scheduler.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import com.google.gson.Gson;
import com.mainline.magic.scheduler.dto.Terms;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class McpHttpUtils {


	/**
	 * 여러건의 작업정보를 보낸다.
	 * @param url
	 * @param list
	 * @return
	 * @throws IOException
	 */
	public static boolean addJobPost(String url, List<Terms> list) throws IOException {
		boolean result = false;
		try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
			final HttpPost httppost = new HttpPost("http://" + url + "/addJobs");
			httppost.addHeader("Accept", "application/json");
			httppost.addHeader("content-type",ContentType.APPLICATION_JSON);
			Gson gson = new Gson();
			httppost.setEntity(new StringEntity(gson.toJson(list)));
			try (final CloseableHttpResponse response = httpclient.execute(httppost)) {
				final HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = response.getCode() == HttpStatus.SC_OK;
					log.info("Response content length: " + resEntity.getContentLength());
					log.info("Response status ok : " + result);
					
				}
				EntityUtils.consume(resEntity);
			}
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
	public static boolean addJobPost(String url, Terms terms) throws IOException {
		boolean result = false;
		try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
			final HttpPost httppost = new HttpPost("http://" + url + "/addJob");
			httppost.addHeader("Accept", "application/json");
			httppost.addHeader("content-type",ContentType.APPLICATION_JSON);
			Gson gson = new Gson();
			httppost.setEntity(new StringEntity(gson.toJson(terms)));
			try (final CloseableHttpResponse response = httpclient.execute(httppost)) {
				final HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = response.getCode() == HttpStatus.SC_OK;
					log.info("Response content length: " + resEntity.getContentLength());
					log.info("Response status ok : " + result);
				}
				EntityUtils.consume(resEntity);
			}
		}
		return result;
	}
}
