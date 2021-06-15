package com.mainline.magic.scheduler.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mainline.magic.scheduler.dto.Publishing;

@Mapper
public interface PublishingDao {
	Publishing getPublishingOne(@Param("versionId") String versionId);
	int deletePublishing(@Param("versionId") String versionId);
	int deletePublishingCode(@Param("versionId") String versionId);
	int deletePublishingCondition(@Param("versionId") String versionId);
	int insertPublishing(Publishing publishing);
	int insertPublishingCondition(@Param("versionId") String versionId, @Param("conditions") String conditions);
	int insertPublishingCode(@Param("versionId") String versionId, @Param("codes") String codes);
	
}
