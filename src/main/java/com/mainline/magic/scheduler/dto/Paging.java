package com.mainline.magic.scheduler.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
// 게시판 하단의 페이징
public class Paging {

    private int totalCount; // 게시판 전체 데이터 개수
    private int displayPageNum = 10; // 게시판 화면에서 한번에 보여질 페이지 번호의 개수
    private int startPage; // 시작 번호
    private int endPage;   // 끝 번호
    private boolean prev;  // 이전 버튼 활성화 여부
    private boolean next;  // 다음 버튼 활성화 여부
    private Criteria cri;


    //전체 데이터 개수 확인 메서드
    public void setTotalCounting(int totalCount) {
        this.totalCount = totalCount;

        pagingData(); // 총 데이터 개수 확인 후에 pagingData() 호출
    }
    private void pagingData() {
        // endPage = (현재 페이지 번호 / 화면에 보여질 페이지 번호의 개수) * 화면에 보여질 페이지 번호의 개수
        endPage = (int) (Math.ceil(cri.getPage() / (double) displayPageNum) * displayPageNum);
        // startPage = (끝 페이지 번호 - 화면에 보여질 페이지 번호의 개수) + 1
        startPage = (endPage - displayPageNum) + 1;
        // 마지막 페이지 번호 = 총 게시글 수 / 한 페이지당 보여줄 게시글의개수
        int tempEndPage = (int) (Math.ceil(totalCount / (double) cri.getPerPageNum()));
        if(endPage > tempEndPage) {
            endPage = tempEndPage;
        }

        // 이전 버튼 생성 여부 = 시작 페이지 번호가 1과 같으면 false, 아니면 true
        prev = startPage == 1 ? false : true;
        // 다음 버튼 생성 여부 = 끝 페이지 번호 * 한 페이지당 보여줄 게시글의 개수가 총 게시글의 수보다
        // 크거나 같으면 false, 아니면 true
        next = endPage * cri.getPerPageNum() >= totalCount ? false : true;
    }

}


