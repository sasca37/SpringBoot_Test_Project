package com.mainline.magic.scheduler.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
import com.mainline.magic.scheduler.dto.Terms;
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
	private CommonUtils commonUtils;
	
	@Autowired
	private TermsUtils termsUtils;
	
	@Autowired
	private SchedulerService schedulerService;
		
	
	@RequestMapping(value = "/add-jobs", method = RequestMethod.POST)
	public ResponseEntity<?> addScheduleJobs(@RequestBody  List<Terms> listTerms) {
		try {
			for(Terms terms : listTerms) {
				// api 연동 방식
				if(!commonUtils.checkBoolean(properties.getQuartzDb())) {
					// history 관리를 위해서 DB에 날라온 정보를 insert 해준다.
					terms.setMergeId(commonUtils.getUUID());
					terms.setStatus(CommonUtils.success);
					terms = commonUtils.setDefaultUser(terms);
					int result = schedulerService.insertTerms(terms);
					if(result <= 0) {
						return new ResponseEntity<>("create data failed", HttpStatus.INTERNAL_SERVER_ERROR);
					}

				}
//				termsUtils.makeTerms(terms);
				termsUtils.executor(terms);
				log.info("termsUtils.executor");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Job create failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Job created successfully", HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/add-job", method = RequestMethod.POST)
	public ResponseEntity<?> addScheduleJob(@RequestBody  Terms terms) {
		try {
			// api 연동 방식
			if(!commonUtils.checkBoolean(properties.getQuartzDb())) {
				terms.setMergeId(commonUtils.getUUID());
				terms.setStatus(CommonUtils.success);
				terms = commonUtils.setDefaultUser(terms);
				// 데이터 추가
				int result = schedulerService.insertTerms(terms);
				if(result <= 0) {
					return new ResponseEntity<>("create data failed", HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
//			termsUtils.makeTerms(terms);
			termsUtils.executor(terms);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Job created failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("Job created successfully", HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/mts-file-upload", method = RequestMethod.POST)
	public ResponseEntity<?> fileUpload(@RequestPart MultipartFile file, @RequestPart String comment) {
		try {
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
				if (!entry.isDirectory()) {
					Files.createDirectories(resolvedPath.getParent());
					log.info("copy file size :"+Files.copy(inputStream, resolvedPath));
				} else {
					Files.createDirectories(resolvedPath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(" file Upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(" file Upload successfully", HttpStatus.OK);
	}
}
