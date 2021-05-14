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
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import com.google.gson.Gson;
import com.mainline.magic.scheduler.dto.Terms;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class McpHttpUtils {

	public static void addJobPost(String url, List<Terms> list) throws IOException {
		try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
			final HttpPost httppost = new HttpPost("http://" + url + "/addJob");
			List<NameValuePair> nvps = new ArrayList<>();
			httppost.addHeader("Accept", "application/json");
			httppost.addHeader("content-type",ContentType.APPLICATION_JSON);
			Gson gson = new Gson();
			httppost.setEntity(new StringEntity(gson.toJson(list)));
			try (final CloseableHttpResponse response = httpclient.execute(httppost)) {
				final HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					log.info("Response content length: " + resEntity.getContentLength());
				}
				EntityUtils.consume(resEntity);
			}
		}
	}
}
