package com.mainline.magic.scheduler.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mainline.magic.scheduler.config.McpProperties;
import com.mainline.magic.scheduler.dto.Publishing;
import com.mainline.magic.scheduler.dto.Terms;
import com.mainline.magic.scheduler.service.PublishingService;
import com.mainline.magic.scheduler.service.SchedulerService;
import com.mainline.magic.scheduler.utils.CommonUtils;
import com.mainline.magic.scheduler.utils.TermsUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SchedulerManagementController {
	
	@Autowired
	private McpProperties properties;

	@Autowired
	private CommonUtils utils;
	
	@Autowired
	private TermsUtils termsUtils;
	
	@Autowired
	private SchedulerService schedulerService;
	
	@Autowired
	private PublishingService publishingService;
		
	
	@RequestMapping(value = "/add-jobs", method = RequestMethod.POST)
	public ResponseEntity<?> addScheduleJobs(@RequestBody  List<Terms> listTerms) {
		List<Terms> list = new ArrayList<Terms>();
		Terms t = null;
		try {
			for(Terms terms : listTerms) {
				t = terms;
				// api 연동 방식
				if(!utils.checkBoolean(properties.getQuartzDb())) {
					// history 관리를 위해서 DB에 날라온 정보를 insert 해준다.
					
					// 실제 사용할때는 증권번호가 날라올것이다.
					if(terms.getMergeId() == null || terms.getMergeId().trim().length() == 0) {
						terms.setMergeId(utils.getUUID());
					}
					
					terms.setStatus(CommonUtils.success);
					terms = utils.setDefaultUser(terms);
					int result = schedulerService.insertTerms(terms);
					if(result <= 0) {
						return new ResponseEntity<>(utils.getResponseJson("create data failed", null), HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
				termsUtils.executor(terms);
				t = new Terms();
				t.setMergeId(terms.getMergeId());
				t.setCode(terms.getCode());
				list.add(t);
			}
		} catch (Exception e) {
			log.error("addScheduleJobs Api Terms {} ",t.toString(), e);
			return new ResponseEntity<>(utils.getResponseJson("Job created failed", null), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(utils.getResponseJson("Job created successfully", list) , HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/add-job", method = RequestMethod.POST)
	public ResponseEntity<?> addScheduleJob(@RequestBody  Terms terms) {
		try {
			// api 연동 방식
			if(!utils.checkBoolean(properties.getQuartzDb())) {
				// 실제 사용할때는 증권번호가 날라올것이다.
				if(terms.getMergeId() == null || terms.getMergeId().trim().length() == 0) {
					terms.setMergeId(utils.getUUID());
				}
				terms.setStatus(CommonUtils.success);
				terms = utils.setDefaultUser(terms);
				// 데이터 추가
				int result = schedulerService.insertTerms(terms);
				if(result <= 0) {
					return new ResponseEntity<>(utils.getResponseJson("create data failed", null), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			termsUtils.executor(terms);
		} catch (Exception e) {
			log.error("addScheduleJob Api Terms {} ",terms.toString(), e);
			return new ResponseEntity<>(utils.getResponseJson("Job created failed", null), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(utils.getResponseJson("Job created successfully", terms) , HttpStatus.OK);
	}
	
	@RequestMapping(value = "/terms-verification", method = RequestMethod.POST)
	public ResponseEntity<?> 	termsVerification(@RequestBody  List<Terms> listTerms) {
		List<Terms> list = null;
		try {
			list = schedulerService.termsVerification(listTerms);
		}catch(Exception e) {
			log.error("terms-verification Api Terms List {} ", utils.toJsonArrayString(listTerms), e);
			return new ResponseEntity<>(utils.getResponseJson("List lookup failed", null), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(utils.getResponseJson("List lookup successfully", list) , HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/mts-file-upload", method = RequestMethod.POST)
	public ResponseEntity<?> mtsFileUpload(@RequestPart MultipartFile file, @RequestPart String comment) {
		try {
			File dir = new File(properties.getMtsPath());
			
			// 해당 데이터를 DB에 적재
			if(comment != null) {
				Publishing publishing =  utils.fromJsonObject(comment, Publishing.class);
				Publishing p = publishingService.getPublishingOne(publishing.getVersionId());
				if(p != null) {
					int cnt = publishingService.deletePublishing(p.getVersionId());
					if(cnt > 0 ) {
						cnt = publishingService.insertPublishing(publishing);
						log.info("publishing data insert count : "+ cnt);
					}else {
						log.info("publishing data delete fail");
					}
				}else {
					int cnt = publishingService.insertPublishing(publishing);
					log.info("publishing data insert count : "+ cnt);
				}
			}
			
			if(!dir.exists()) {
				log.info("make download directory : "+dir.mkdirs());
				log.info(dir.getAbsolutePath());
			}

			ZipInputStream inputStream = new ZipInputStream(file.getInputStream());
//			Path path = Paths.get(properties.getDownloadPath()+File.separator+file.getOriginalFilename());
			Path path = Paths.get(properties.getMtsPath()+File.separator+file.getOriginalFilename());
		
			for (ZipEntry entry; (entry = inputStream.getNextEntry()) != null;) {
				Path resolvedPath = path.resolve(entry.getName());
				if (!entry.isDirectory()) {
					if(!resolvedPath.getParent().toFile().exists()) {
						Files.createDirectories(resolvedPath.getParent());
					}
					log.info("copy file size :"+Files.copy(inputStream, resolvedPath,StandardCopyOption.REPLACE_EXISTING));
				} else {
					if(!resolvedPath.getParent().toFile().exists()) {
						Files.createDirectories(resolvedPath.getParent());
					}
				}
			}
		} catch (Exception e) {
			log.error("mtsFileUpload Api OriginalFilename {} ",file.getOriginalFilename(), e);
			return new ResponseEntity<>(" file Upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(" file Upload successfully", HttpStatus.OK);
	}
}
