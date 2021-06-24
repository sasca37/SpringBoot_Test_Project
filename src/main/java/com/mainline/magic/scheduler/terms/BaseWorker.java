package com.mainline.magic.scheduler.terms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mainline.magic.scheduler.dto.Publishing;
import com.mainline.magicterms.userterms.batch.BatchProperties;
import com.mainline.magicterms.userterms.batch.MtsPublishingDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseWorker {
	/**
	 * @param key
	 * @return
	 */
	protected File createErrorDir(String key) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date());
		
		File f = new File(BatchProperties.getMtsResultPath(), date);
		if(!f.exists()) {
			f.mkdir();
		}
		
		File file = new File(f, key + ".txt");
		return file;
	}
	
	/**
	 * @param f
	 * @param errMsg
	 * @throws Exception
	 */
	protected void createErrorFile(File f, String errMsg) throws Exception{
		OutputStreamWriter os = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			os = new OutputStreamWriter(fos, "UTF-8");
			
			os.write(errMsg);
			os.flush();
		}finally {
			try {
				if(fos != null) {
					fos.close();
				}
				if(os != null) {
					os.close();
				}
			}catch(Exception e) {
				log.error("createErrorFile method ",e);
			}
		}
	}
	
	protected List<MtsPublishingDto> publishingToMtsPublishingDto(List<Publishing> publishings) {
		List<MtsPublishingDto> list = new ArrayList<MtsPublishingDto>();
		for(Publishing publishing : publishings) {
			list.add(publishingToMtsPublishingDto(publishing));
		}
		return list;
	}
	
	protected MtsPublishingDto publishingToMtsPublishingDto(Publishing publishing) {
		MtsPublishingDto dto = new MtsPublishingDto();
		dto.setVersionId(publishing.getVersionId());
		dto.setProductCode(publishing.getProductCode());
		dto.setSaleStartDate(publishing.getSaleStartDate());
		dto.setSaleEndDate(publishing.getSaleEndDate());
		dto.setPath(publishing.getPath());
		return dto;
	}
}
