package com.mainline.magic.scheduler.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchCriteria extends Criteria {

    private String contract_date;
    private String registration_num;
    private String status;
    private String created_start;
    private String created_end;


}
