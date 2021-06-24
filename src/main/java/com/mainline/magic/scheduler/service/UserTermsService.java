package com.mainline.magic.scheduler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mainline.magic.scheduler.dao.UserTermsDao;
import com.mainline.magic.scheduler.dto.Publishing;

@Service
public class UserTermsService {

	@Autowired
	private UserTermsDao dao;

	public List<Publishing> getLIListForCodes(String date, List<String> list) {
		return dao.getLIListForCodes(date, list);
	}
	public Publishing getGIListForCode(String date, String code) {
		return dao.getGIListForCode(date, code);
	}
	
	
}
