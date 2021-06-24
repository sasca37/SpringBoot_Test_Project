package com.mainline.magic.scheduler.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class McpTerms {
	private String merge_id; //기본키 
	private String code; //보종코드
	private String status; //상태코드
	private String creator; //생성자
	private String created; //생성일자
	private String updator; //생성자
	private String updated; //업데이트 일자
	private String path; //경로
	private String contract_date; //계약 일자
	private String product_code; //상품 코드 
	private String registation_num; //접수 번호 
}
