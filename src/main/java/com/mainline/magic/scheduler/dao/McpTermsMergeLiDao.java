package com.mainline.magic.scheduler.dao;

import java.util.List;


import com.mainline.magic.scheduler.dto.Criteria;
import com.mainline.magic.scheduler.dto.SearchCriteria;
import org.apache.ibatis.annotations.Mapper;

import com.mainline.magic.scheduler.dto.McpTerms;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface McpTermsMergeLiDao {
// DB에 쿼리 던지는 MyBatis Mapper 클래스 생성 단계 ( 인터페이스, 매퍼 분리) 
	
	
	
//	Publishing getPublishingOne(@Param("versionId") String versionId);
//	int deletePublishing(@Param("versionId") String versionId);
//	int deletePublishingCode(@Param("versionId") String versionId);
//	int deletePublishingCondition(@Param("versionId") String versionId);
//	int insertPublishing(Publishing publishing);
//	int insertPublishingCondition(@Param("versionId") String versionId, @Param("conditions") String conditions);
//	int insertPublishingCode(@Param("versionId") String versionId, @Param("codes") String codes);
//	int updateSaleEndDate(@Param("versionId") String versionId, @Param("saleEndDate") String saleEndDate);
	
	 List<McpTerms> boardList(Criteria cri) throws Exception;
	 int boardListCnt(Criteria cri) throws Exception;

	 List<McpTerms> list(SearchCriteria scri) throws Exception;
	 int listCount( SearchCriteria scri) throws Exception;

}
