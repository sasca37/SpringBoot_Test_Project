package com.mainline.magic.scheduler.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Terms implements Serializable{
	
	private static final long serialVersionUID = 6595851701569884701L;
//	private String seq;
//	private String PolicyNum;
//	private String ProuctCode;
//	private String InsuranceCode;
//	private Date startDate;
//	private int state;
//	private String productName;

	private String mergeId;
	private String id;
	private String code;
	private String status;
	private String creator;
	private String created;
	private String updator;
	private String updated;
}
