package com.mainline.magic.scheduler.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Publishing implements Serializable{
	
	private static final long serialVersionUID = 4700210301733013515L;
	
	private String versionId;
	private String productCode;
	private String saleStartDate;
	private String saleEndDate;
	private String path;
	private String created;
	private String creator;
	private String codes;
	private String conditions;
	
}
