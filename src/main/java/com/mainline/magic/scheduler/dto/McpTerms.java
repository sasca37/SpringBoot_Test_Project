package com.mainline.magic.scheduler.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class McpTerms {

	private String mergeId; //기본키
	private String code; //보종코드
	private String status; //상태코드
	private String creator; //생성자
	private String created; //생성일자
	private String updator; //생성자
	private String updated; //업데이트 일자
	private String path; //경로
	private String contractDate; //계약 일자
	private String productCode; //상품 코드
	private int registrationNum; //접수 번호


}
