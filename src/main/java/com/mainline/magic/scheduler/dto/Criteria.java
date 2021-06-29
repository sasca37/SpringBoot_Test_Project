
package com.mainline.magic.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@ToString
@AllArgsConstructor
// 특정 페이지 조회를 위한 클래스
public class Criteria {

    private int page; // 현재 페이지 번호
    private int perPageNum; // 페이지당 보여줄 게시글의 개수
    private String contract_date;
    private String registration_num;
    private String status;
    private String created_start;
    private String created_end;


    public Criteria() {
        // 기본 생성자 : 최초 게시판에 진입시 필요한 기본값
        this.page = 1;
        this.perPageNum = 10;
        this.status=null;
    }
    public int getPageStart() {
        // 특정 페이지의 범위를 정하는 구간, 현재 페이지의 게시글 시작 번호
        // 0 ~ 10 , 10 ~ 20 이런식으로
        return (this.page -1) * perPageNum;
    }

    public String getStatusy(){
        return status;
    }
}


