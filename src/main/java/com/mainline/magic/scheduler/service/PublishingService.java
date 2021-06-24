package com.mainline.magic.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainline.magic.scheduler.dao.PublishingDao;
import com.mainline.magic.scheduler.dto.Publishing;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PublishingService {
	
	@Autowired
	private PublishingDao publishingDao;
	
	
	public Publishing getPublishingOne(String versionId) {
		return publishingDao.getPublishingOne(versionId);
	}
	
	public int insertPublishing(Publishing publishing) throws Exception{
		
		String codes = publishing.getCodes();
		if(codes != null && !"null".equals(codes) && codes.length() > 0) {
			if(codes.contains(",")) {
				String[] codeStr = codes.split(",");
				for(String code : codeStr) {
					 insertPublishingCode(publishing.getVersionId(), code);
				}
			}else {
				 insertPublishingCode(publishing.getVersionId(), codes);
			}
		}
		
		String coditions = publishing.getConditions();
		log.info("codes : "+ codes);
		log.info("coditions : "+ coditions);
		if(coditions != null && !"null".equals(coditions) && coditions.length() > 0) {
			
			if(coditions.contains(",")) {
				String[] coditionStr = coditions.split(",");
				for(String condition : coditionStr) {
					 insertPublishingCondition(publishing.getVersionId(), condition);
				}
			}else {
				insertPublishingCondition(publishing.getVersionId(), coditions);
			}
		}
		
		return publishingDao.insertPublishing(publishing);
	}
	
	public int insertPublishingCondition(String versionId, String conditions) throws Exception {
		return publishingDao.insertPublishingCondition(versionId, conditions);
	}
	
	public int insertPublishingCode(String versionId, String codes) throws Exception {
		return publishingDao.insertPublishingCode(versionId, codes);
	}
	
	public int updateSaleEndDate(String versionId, String saleEndDate) {
		return publishingDao.updateSaleEndDate(versionId, saleEndDate);
	}
	
	public int deletePublishing(String versionId) throws Exception{
		deletePublishingCode(versionId);
		deletePublishingCondition(versionId);
		return publishingDao.deletePublishing(versionId);
	}
	
	public int deletePublishingCode(String versionId) throws Exception{
		return publishingDao.deletePublishingCode(versionId);
	}
	
	public int deletePublishingCondition(String versionId) throws Exception {
		return publishingDao.deletePublishingCondition(versionId);
	}
	

}
