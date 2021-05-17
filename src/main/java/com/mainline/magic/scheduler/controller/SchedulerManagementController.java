package com.mainline.magic.scheduler.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.quartz.Scheduler;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SchedulerManagementController {
	

	@Autowired
    private Scheduler scheduler;
	
	@Autowired
	private McpProperties properties;
	
	@RequestMapping(value = "/addJobs", method = RequestMethod.POST)
	public ResponseEntity<?> addScheduleJobs(@RequestBody  List<Terms> jobInfo) {
		try {
			System.out.println("addJob          :" + jobInfo.size());
			for(Terms terms  : jobInfo) {
				// 약관 제작 실행
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("Job created successfully", HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/addJob", method = RequestMethod.POST)
	public ResponseEntity<?> addScheduleJob(@RequestBody  Terms terms) {
		try {
			System.out.println("addJob          :" + terms);
	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>("Job created successfully", HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/file", method = RequestMethod.POST)
	public ResponseEntity<?> fileUpload(@RequestPart MultipartFile file, @RequestPart String comment) {
		String fileName = "";
		try {
			File dir = new File(properties.getDownloadPath());
			
			if(!dir.exists()) {
				log.info("make download directory : "+dir.mkdirs());
				log.info(dir.getAbsolutePath());
			}

			ZipInputStream inputStream = new ZipInputStream(file.getInputStream());
			Path path = Paths.get(properties.getDownloadPath()+File.separator+file.getOriginalFilename());
		
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
		}
		return new ResponseEntity<>( fileName + " file Upload successfully", HttpStatus.OK);
	}
}
