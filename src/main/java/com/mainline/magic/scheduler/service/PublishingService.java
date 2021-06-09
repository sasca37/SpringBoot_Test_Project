package com.mainline.magic.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainline.magic.scheduler.dao.PublishingDao;
import com.mainline.magic.scheduler.dto.Publishing;

@Service
public class PublishingService {
	
	@Autowired
	private PublishingDao publishingDao;
	
	public Publishing getPublishingOne(String versionId) {
		return publishingDao.getPublishingOne(versionId);
	}
	
	public int insertPublishing(Publishing publishing) {
		return publishingDao.insertPublishing(publishing);
	}
	
	public int deletePublishing(String versionId) {
		return publishingDao.deletePublishing(versionId);
	}

}
