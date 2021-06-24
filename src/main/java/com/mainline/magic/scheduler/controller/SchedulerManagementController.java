package com.mainline.magic.scheduler.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
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

import com.mainline.magic.scheduler.config.McpProperties;
import com.mainline.magic.scheduler.dto.Publishing;
import com.mainline.magic.scheduler.dto.Terms;
import com.mainline.magic.scheduler.service.PublishingService;
import com.mainline.magic.scheduler.service.SchedulerService;
import com.mainline.magic.scheduler.terms.TermsExecutor;
import com.mainline.magic.scheduler.utils.CommonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SchedulerManagementController {
	
	@Autowired
	private McpProperties properties;

	@Autowired
	private CommonUtils utils;
	
	@Autowired
	private TermsExecutor termsExecutor;
	
	@Autowired
	private SchedulerService schedulerService;
	
	@Autowired
	private PublishingService publishingService;
		
	

	@RequestMapping(value = "/add/jobs", method = RequestMethod.POST)
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
						return new ResponseEntity<>(utils.getResponseJson("create data failed", CommonUtils.httpFail), HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
				// 동기 식인지 체크
				if(utils.checkBoolean(properties.getSyncFlag())) {
					Future<Terms> future = termsExecutor.callWorkerExecutor(terms);
					t = future.get();
					list.add(t);
				}else {
					termsExecutor.workerExecutor(terms);
					t = new Terms();
					t.setMergeId(terms.getMergeId());
					t.setCode(terms.getCode());
					list.add(t);
				}
			}
		} catch (Exception e) {
			log.error("addScheduleJobs Api Terms {} ",t.toString(), e);
			return new ResponseEntity<>(utils.getResponseJson("Job created failed", CommonUtils.httpError), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(utils.getResponseJson("Job created successfully",CommonUtils.httpSuccess, list) , HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/add/job", method = RequestMethod.POST)
	public ResponseEntity<?> addScheduleJob(@RequestBody  Terms terms) {
		try {
			log.info("========================addScheduleJob========================");
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
					return new ResponseEntity<>(utils.getResponseJson("create data failed", CommonUtils.httpFail), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			
			// 동기 식인지 체크
			if(utils.checkBoolean(properties.getSyncFlag())) {
				Future<Terms> future = termsExecutor.callWorkerExecutor(terms);
				terms = future.get();
				log.info("result : " + terms.toString());
			}else{
				termsExecutor.workerExecutor(terms);
			}

		} catch (Exception e) {
			log.error("addScheduleJob Api Terms {} ",terms.toString(), e);
			return new ResponseEntity<>(utils.getResponseJson("Job created failed", CommonUtils.httpError), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(utils.getResponseJson("Job created successfully", CommonUtils.httpSuccess) , HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/update/sale-end-date", method = RequestMethod.POST)
	public ResponseEntity<?> updateSaleEndDate(@RequestPart String versionId, @RequestPart String saleEndDate) {
		try {
			int result = publishingService.updateSaleEndDate(versionId, saleEndDate);
			if(result <= 0) {
				return new ResponseEntity<>(utils.getResponseJson("sale end date update failed", CommonUtils.httpFail), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}catch (Exception e) {
			log.error("updateSaleEndDate Api versionId :  {} , saleEndDate {} ",versionId,saleEndDate, e);
			return new ResponseEntity<>(utils.getResponseJson("sale end date update Error failed", CommonUtils.httpError), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(utils.getResponseJson("sale end date update successfully", CommonUtils.httpSuccess) , HttpStatus.OK);
	}
	
	@RequestMapping(value = "/terms-verification", method = RequestMethod.POST)
	public ResponseEntity<?> 	termsVerification(@RequestBody  List<Terms> listTerms) {
		List<Terms> list = null;
		try {
			list = schedulerService.termsVerification(listTerms);
		}catch(Exception e) {
			log.error("terms-verification Api Terms List {} ", utils.toJsonArrayString(listTerms), e);
			return new ResponseEntity<>(utils.getResponseJson("List lookup failed", CommonUtils.httpError), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(utils.getResponseJson("List lookup successfully", CommonUtils.httpSuccess, list) , HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/mts-file-upload", method = RequestMethod.POST)
	public ResponseEntity<?> mtsFileUpload(@RequestPart MultipartFile file, @RequestPart String comment) {
		log.info("======================== Call Method mtsFileUpload ========================");
		try {
			String dirPath = null;
			File dir = new File(properties.getMtsPath());
			
			if(!dir.exists()) {
				log.info("make download directory : "+dir.mkdirs());
				log.info(dir.getAbsolutePath());
			}

			ZipInputStream inputStream = new ZipInputStream(file.getInputStream());
//			Path path = Paths.get(properties.getDownloadPath()+File.separator+file.getOriginalFilename());
			Path path = Paths.get(properties.getMtsPath()+File.separator+file.getOriginalFilename());
		
			for (ZipEntry entry; (entry = inputStream.getNextEntry()) != null;) {
				Path resolvedPath = path.resolve(entry.getName());
				if(dirPath == null) {
					dirPath = resolvedPath.toString();
				}
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
			
			// 해당 데이터를 DB에 적재
			if(comment != null) {
				log.info(comment);
				Publishing publishing =  utils.fromJsonObject(comment, Publishing.class);
				Publishing p = publishingService.getPublishingOne(publishing.getVersionId());
				if(p != null) {
					int cnt = publishingService.deletePublishing(p.getVersionId());
					if(cnt > 0 ) {
						publishing.setPath(dirPath);
						cnt = publishingService.insertPublishing(publishing);
						log.info("publishing data insert count : "+ cnt);
					}else {
						log.info("publishing data delete fail");
					}
				}else {
					publishing.setPath(dirPath);
					int cnt = publishingService.insertPublishing(publishing);
					log.info("publishing data insert count : "+ cnt);
				}
			}
		} catch (Exception e) {
			log.error("mtsFileUpload Api OriginalFilename : {}  , comment : {}",file.getOriginalFilename(), comment, e);
			return new ResponseEntity<>(utils.getResponseJson(" file Upload failed", CommonUtils.httpError), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(utils.getResponseJson("ile Upload successfully", CommonUtils.httpSuccess) , HttpStatus.OK);
	}
}
