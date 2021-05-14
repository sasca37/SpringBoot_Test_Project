package com.mainline.magic.scheduler.dto;

import java.io.Serializable;
import java.sql.Date;

import lombok.Data;

@Data
public class Terms implements Serializable{
	
	private static final long serialVersionUID = 6595851701569884701L;
	private String seq;
	private String PolicyNum;
	private String ProuctCode;
	private String InsuranceCode;
	private Date startDate;
	private int state;
	private String productName;
}
