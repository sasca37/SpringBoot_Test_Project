package com.mainline.magic.scheduler;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mainline.magic.scheduler.dto.Publishing;
import com.mainline.magic.scheduler.service.PublishingService;
import com.mainline.magic.scheduler.utils.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PublishingTest {

	@Autowired
	private PublishingService service;
	
	@Autowired
	private CommonUtils utils;
	
	@Test
	public void insertPublishingTest() {
		Publishing publishing = null;
		try {
			publishing = new Publishing();
			String uuid = utils.getUUID();
			publishing.setVersionId(uuid);
			log.info("version id : "+ uuid);
			uuid = utils.getUUID();
			publishing.setProductCode(uuid);
			log.info("product code : "+ uuid);
			Date date = new Date();
			publishing.setSaleStartDate(utils.getDateToString(date, "yyyy-MM-dd HH:mm:ss"));
			publishing.setSaleEndDate(utils.getDateToString(date, "yyyy-MM-dd HH:mm:ss"));
			publishing.setPath("C:///////");
			publishing.setCreator("홍길동");
			
			String json = utils.toJsonString(publishing);
			Publishing p = utils.fromJsonObject(json, Publishing.class);
			log.info(" publishing dto : " + p.toString());
			
			int cnt = service.insertPublishing(publishing);
			log.info(" insert count : "+ cnt);
			getPublishing(publishing.getVersionId());
			deletePublishing(publishing.getVersionId());
		}catch(Exception e) {
			log.error("insertPublishingTest {}",publishing, e);
		}
	}
	
	public void deletePublishing(String versionId) {
		try {
			int cnt = service.deletePublishing(versionId);
			log.info(" delete count : "+ cnt);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void getPublishing(String versionId) {
		Publishing publishing = service.getPublishingOne(versionId);
		log.info(" getPublishing : "+ publishing.toString());
	}
	

}
