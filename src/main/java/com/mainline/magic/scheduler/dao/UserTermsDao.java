package com.mainline.magic.scheduler.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mainline.magic.scheduler.dto.Publishing;

@Mapper
public interface UserTermsDao {
	List<Publishing> getLIListForCodes(@Param("date") String date, @Param("list") List<String> list);
	Publishing getGIListForCode(@Param("date") String date, @Param("code") String code);
}
