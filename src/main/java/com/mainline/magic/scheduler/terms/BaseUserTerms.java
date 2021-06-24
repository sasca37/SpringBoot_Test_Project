package com.mainline.magic.scheduler.terms;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

import com.mainline.magicterms.clause.prop.PropertyConstance;
import com.mainline.magicterms.factory.DocumentFileType;
import com.mainline.magicterms.poc.customize.terms.AbstractUserTerms;
import com.mainline.magicterms.poc.customize.terms.FileInfo;
import com.mainline.magicterms.poc.customize.terms.PageLayout.Paper;
import com.mainline.magicterms.poc.customize.terms.ProductInfo;
import com.mainline.magicterms.poc.customize.terms.TermsAttachedInfo;
import com.mainline.magicterms.poc.customize.terms.TermsEtcFileInfo;
import com.mainline.magicterms.poc.customize.terms.TermsFileInfo;
import com.mainline.magicterms.poc.customize.terms.ToCPage;
import com.mainline.magicterms.userterms.batch.MtsPublishingDto;

import kr.dazzle.google.gson.JsonArray;
import kr.dazzle.google.gson.JsonObject;
import kr.dazzle.google.gson.JsonParser;
import kr.dazzle.util.EnUtility;

public class BaseUserTerms extends AbstractUserTerms{

	public BaseUserTerms(String id, String name, String documentType, String insuarnceType) {
		super(id, name, documentType, insuarnceType, "gulim.ttc", DocumentFileType.DOCX);
	}
	public BaseUserTerms(String id, String name, String documentType, String insuarnceType, DocumentFileType type) {
		super(id, name, documentType, insuarnceType, "gulim.ttc", type);
	}

	@Override
	public void mergeFile() throws Exception{
	}

	@Override
	public Paper getPaper() {
		return Paper.A4;
	}

	@Override
	public Object getComposite() {
		return null;
	}

	@Override
	public void afterPublishing(File f) throws Exception{
		
	}

	@Override
	public void settingFstBeforeToc(PDFMergerUtility util, ToCPage header, TermsEtcFileInfo[] infos) throws Exception {
		if(PropertyConstance.isInsuranceTypeGI()) {
			if(infos != null && infos.length != 0) {
				for(TermsEtcFileInfo info : infos) {
					util.addSource(info.getFile());
				}
			}
		}
	}

	@Override
	public void settingFstAfterToc(PDFMergerUtility util, ToCPage header, TermsEtcFileInfo[] infos) throws Exception {
		if(PropertyConstance.isInsuranceTypeLI()) {
			if(infos != null && infos.length != 0) {
				for(TermsEtcFileInfo info : infos) {
					util.addSource(info.getFile());
					header.getFrontPageCount(info.getFile().getAbsolutePath());
				}
			}
		}
	}
	
	public static Map<String, Object> getMtsInfo(File f) throws Exception{
		Map<String, Object> result = new HashMap<>();
		
		File infoFile = new File(f.getAbsolutePath(), "termsFileList.txt");
		
       	InputStream stream = null;
    	try {
    		stream = new FileInputStream(infoFile);
        	
    		String objStr = ""; 
        	byte[] b = new byte[stream.available()];
        	stream.read(b);
			
			StringTokenizer st = new StringTokenizer(new String(b, "UTF-8"), "\r\n");
			List<TermsEtcFileInfo> etcInfos = new ArrayList<>();
			List<TermsFileInfo> infos = new ArrayList<>();
			List<TermsAttachedInfo> attachedInfos = new ArrayList<>();
			
			while(st.hasMoreTokens()) {
				String next = st.nextToken().trim();
				objStr += next;
			}
			
			JsonParser parser = new JsonParser();
			JsonObject jo = (JsonObject)parser.parse(new StringReader(objStr));
			
			String insuranceTypeCode = FileInfo.getJsonValue(jo, ProductInfo.INSURANCE_TYPE_CODE);
			String codeEtc = FileInfo.getJsonValue(jo, ProductInfo.CODE_ETC);
			
//    			if(!isSaleDateChk(saleDate)) {
//					return null;
//				}
			
			Object etcObj = jo.get(ProductInfo.ETC_FILE_INFO);
			
			if(etcObj != null) {
				JsonArray etcJo = (JsonArray)etcObj;
				for(int i = 0 ; i < etcJo.size(); i++) {
					JsonObject o = (JsonObject)etcJo.get(i);
					TermsEtcFileInfo info = TermsEtcFileInfo.parseTermsEtcFileList(o.toString());
					info.setFile(new File(f.getAbsolutePath(), info.getFileName()));
					etcInfos.add(info);
				}
			}
			
			JsonArray fileJo = (JsonArray)jo.get(ProductInfo.FILE_INFO);
			for(int i = 0 ; i < fileJo.size(); i++) {
				JsonObject o = (JsonObject)fileJo.get(i);
				TermsFileInfo info = TermsFileInfo.parseTermsFileList(o.toString());
				File file = new File(f.getAbsolutePath(), info.getFileName());
				info.setPath(file.getAbsolutePath());
				infos.add(info);
			}
			
			Object attachedObj = jo.get("attachedInfos");
			if(attachedObj != null) {
				JsonArray attachedJo = (JsonArray)attachedObj;
				for(int i = 0 ; i < attachedJo.size(); i++) {
					JsonObject o = (JsonObject)attachedJo.get(i);
					TermsAttachedInfo info = TermsAttachedInfo.parseTermsFileList(o.toString());
					File file = new File(f.getAbsolutePath(), info.getFileName());
					info.setPath(file.getAbsolutePath());
					attachedInfos.add(info);
				}
			}
			
			result.put(ProductInfo.NAME, FileInfo.getJsonValue(jo, ProductInfo.NAME));
			result.put(ProductInfo.BOOK_TYPE, FileInfo.getJsonValue(jo, ProductInfo.BOOK_TYPE));
			result.put(ProductInfo.FONT, FileInfo.getJsonValue(jo, ProductInfo.FONT));
			result.put(ProductInfo.PAPER, FileInfo.getJsonValue(jo, ProductInfo.PAPER));
			result.put(ProductInfo.CODE, FileInfo.getJsonValue(jo, ProductInfo.CODE));
			result.put(ProductInfo.INSURANCE_TYPE_CODE, insuranceTypeCode);
			result.put(ProductInfo.CODE_ETC, codeEtc);
			result.put(ProductInfo.ETC_FILE_INFO, etcInfos.toArray(new TermsEtcFileInfo[etcInfos.size()]));
			result.put(ProductInfo.FILE_INFO, infos.toArray(new TermsFileInfo[infos.size()]));
			result.put(ProductInfo.ATTACHED_INFO, attachedInfos.toArray(new TermsAttachedInfo[attachedInfos.size()]));
    	}finally{
    		try {
				stream.close();
			}catch(Exception e) {}
    	}
		
		return result;
	}
	
	public static boolean isSaleDateChk(String saleDate) throws Exception{
		if(EnUtility.isBlank(saleDate)) {
			return false;
		}
		
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
		Date sDate = fm.parse(saleDate);
		Date nowDate = new Date();
		
		int compareResult = sDate.compareTo(nowDate);
		
		return compareResult > 0 || compareResult == 0;
	}
	
	public static ProductInfo makeProductInfoLI(List<MtsPublishingDto> list) throws Exception{
		String mtsName = "";
		String bookletType = "";
		String font = "";
		String layout = "";
		String prodCode = "";
		String insuranceTypeCode = "";
		String codeEtc = "";
		
		List<TermsEtcFileInfo> etcFileInfo = new ArrayList<>();
		List<TermsFileInfo> fileInfo = new ArrayList<>();
		
		int count = 0;
		
		//���� mts���
		List<Map<String, Object>> infoList = new ArrayList<>();
		
		
		//mts�� �������
		for(MtsPublishingDto dto : list) {
			File f = new File(dto.getPath());
			Map<String, Object> infoMap = getMtsInfo(f);
			infoList.add(infoMap);
		}
		
		for(Map<String, Object> infoMap : infoList) {
			if(count == 0) {
				mtsName = infoMap.get(ProductInfo.NAME).toString();
				bookletType = infoMap.get(ProductInfo.BOOK_TYPE).toString();
				font = infoMap.get(ProductInfo.FONT).toString();
				layout = infoMap.get(ProductInfo.PAPER).toString();
				prodCode = infoMap.get(ProductInfo.CODE).toString();
				insuranceTypeCode = infoMap.get(ProductInfo.INSURANCE_TYPE_CODE).toString();
				codeEtc = infoMap.get(ProductInfo.CODE_ETC).toString();
				
				Object etcObj = infoMap.get(ProductInfo.ETC_FILE_INFO);
				if(etcObj != null) {
					TermsEtcFileInfo[] etcInfos = (TermsEtcFileInfo[])etcObj;
					etcFileInfo.addAll(new ArrayList<TermsEtcFileInfo>(Arrays.asList(etcInfos)));
				}
			}
			
			Object fileObj = infoMap.get(ProductInfo.FILE_INFO);
			
			if(fileObj != null) {
				TermsFileInfo[] fileInfos = (TermsFileInfo[])fileObj;
				fileInfo.addAll(new ArrayList<TermsFileInfo>(Arrays.asList(fileInfos)));
			}
			
			count++;
		}
		
		ProductInfo info = new ProductInfo(mtsName, getBookletType(bookletType), font, layout, prodCode, insuranceTypeCode, codeEtc, null, null, null, null);
		info.setEtcFileInfos(etcFileInfo.toArray(new TermsEtcFileInfo[etcFileInfo.size()]));
		info.setFileInfos(fileInfo.toArray(new TermsFileInfo[fileInfo.size()]));
		return info;
	}
	
	public static ProductInfo makeProductInfoGI(MtsPublishingDto dto, String codeStr) throws Exception{
		String mtsName = "";
		String bookletType = "";
		String font = "";
		String layout = "";
		
		List<TermsEtcFileInfo> etcFileInfo = new ArrayList<>();
		List<TermsFileInfo> fileInfo = new ArrayList<>();
		List<TermsAttachedInfo> attachedInfo = new ArrayList<>();
		
		Map<String, String> chkMap = new HashMap<String, String>();
		
		if(EnUtility.isBlank(codeStr)) {
			throw new Exception("not code");
		}
		
		String[] codes = null;
		if(codeStr.contains(",")) {
			codes = codeStr.split(",");
		}else {
			codes = new String[] {codeStr};
		}
		
		File termsDir = new File(dto.getPath());
		Map<String, Object> infoMap = getMtsInfo(termsDir);
		
		mtsName = infoMap.get(ProductInfo.NAME).toString();
		bookletType = infoMap.get(ProductInfo.BOOK_TYPE).toString();
		font = infoMap.get(ProductInfo.FONT).toString();
		layout = infoMap.get(ProductInfo.PAPER).toString();
		
		Object etcObj = infoMap.get(ProductInfo.ETC_FILE_INFO);
		if(etcObj != null) {
			TermsEtcFileInfo[] etcInfos = (TermsEtcFileInfo[])etcObj;
			for(TermsEtcFileInfo info: etcInfos) {
				info.setFile(new File(termsDir.getAbsolutePath(), info.getFileName()));
			}
			etcFileInfo.addAll(new ArrayList<TermsEtcFileInfo>(Arrays.asList(etcInfos)));
		}
		
		for(String code : codes) {
			Object fileObj = infoMap.get(ProductInfo.FILE_INFO);
			if(fileObj != null) {
				TermsFileInfo[] fileInfos = (TermsFileInfo[])fileObj;
				for(TermsFileInfo info: fileInfos) {
					//�㺸�ڵ� ����
					
					boolean isAdd = false;
					System.out.println(info.getCodeEtc() + " : " + code);
					if(containsCode(info.getInsuranceTypeCode(), code)) {
						isAdd = true;
					}else if(containsCode(info.getCodeEtc(), code) || info.getCodeEtc().equals(ALL)) {
						isAdd = true;
					}		
					
					if(isAdd) {
						if(!chkMap.containsKey(info.getTitle())) {
							File f = new File(termsDir.getAbsolutePath(), info.getFileName());
							info.setPath(f.getAbsolutePath());
							fileInfo.add(info);
							chkMap.put(info.getTitle(), "");
						}
					}
				}
			}
		}
		
		Object attachedObj = infoMap.get(ProductInfo.ATTACHED_INFO);
		if(attachedObj != null) {
			TermsAttachedInfo[] infos = (TermsAttachedInfo[])attachedObj;
			for(TermsAttachedInfo info: infos) {
				File f = new File(termsDir.getAbsolutePath(), info.getFileName());
				info.setPath(f.getAbsolutePath());
			}
			attachedInfo.addAll(new ArrayList<TermsAttachedInfo>(Arrays.asList(infos)));
		}
		
		ProductInfo info = new ProductInfo(mtsName, getBookletType(bookletType), font, layout);
		info.setEtcFileInfos(etcFileInfo.toArray(new TermsEtcFileInfo[etcFileInfo.size()]));
		info.setFileInfos(fileInfo.toArray(new TermsFileInfo[fileInfo.size()]));
		info.setAttachedInfos(attachedInfo.toArray(new TermsAttachedInfo[attachedInfo.size()]));
		return info;
	}
	
}
