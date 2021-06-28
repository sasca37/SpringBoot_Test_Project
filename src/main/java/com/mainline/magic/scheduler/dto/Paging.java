package com.mainline.magic.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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
  /*      cri.setContract_date(cri.getContract_date());
        cri.setRegistration_num(cri.getRegistration_num());
        cri.setStatus(cri.getStatus());
        cri.setCreated_start(cri.getCreated_start());
        cri.setCreated_end(cri.getCreated_end());*/
    }
    public String makeSearch(int page){

        UriComponents uriComponents =
                UriComponentsBuilder.newInstance()
                        .queryParam("page", page)
                        .queryParam("perPageNum", cri.getPerPageNum())
                        .queryParam("status", ((SearchCriteria)cri).getStatus())
                        /*.queryParam("registration_num", ((SearchCriteria)cri).getRegistration_num())*/
                        .build();
        return uriComponents.toUriString();
    }

   /* public int getDisplayPageNum() {
        return displayPageNum;
    }

    public void setDisplayPageNum(int displayPageNum) {
        this.displayPageNum = displayPageNum;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public boolean isPrev() {
        return prev;
    }

    public void setPrev(boolean prev) {
        this.prev = prev;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public Criteria getCri() {
        return cri;
    }

    public void setCri(Criteria cri) {
        this.cri = cri;
    }


    @Override
    public String toString() {
        return "PageMaker [totalCount=" + totalCount + ", startPage=" + startPage + ", endPage=" + endPage + ", prev="
                + prev + ", next=" + next + ", displayPageNum=" + displayPageNum + ", cri=" + cri + "]";
    }*/

}


