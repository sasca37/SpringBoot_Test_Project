<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <style >
        @import url(//fonts.googleapis.com/earlyaccess/nanumpenscript.css);
        @import url('https://fonts.googleapis.com/css2?family=Nanum+Gothic+Coding&display=swap');
        @import url(//fonts.googleapis.com/earlyaccess/hanna.css);


        .table {
            font-family: 'Nanum Gothic Coding', monospace;
            font-size: 15px;
        }
        div {
            text-align: center;
            font-family: 'Nanum Gothic Coding', monospace;
           /* font-family: 'Noto Sans KR', sans-serif;*/
            font-size: 15px;
        }
        h2{
            font-family: 'Hanna', fantasy;
            font-size: 40px;
        }
        p {
            font-family: 'Nanum Pen Script', cursive;
            font-size: 25px;
        }
        #form1 {
            display: inline-block;
            text-align: center;
        }
        input[type="date"]::before {content:attr(data-placeholder); width: 70%}
        input[type="date"]:focus::before,
        input[type="date"]:valid::before {display:none}

        #button1, #button2 {
            background-color: white;
            color: black;
            border: 2px solid deepskyblue;
            transition-duration: 0.4s;
            width: 105px;
            font-size : 15px;
            font-family: 'Nanum Pen Script', cursive;
        }

        #button1:hover, #button2:hover {
            background-color: #6495ED;
            color: white;
        }
    </style>
</head>
<body>


<div class="container">
    <br>
    <h2>조회 목록</h2>
    <p><c:out value="${'조회된 데이터 수 : '} ${boardListCnt}${'개, '}${'조회 가능 일짜 (최근 3개월) : '} ${threeMonthAgo}${' ~ '}${threeMonthLater}"></c:out></p>

    <form class="form-inline" action="/MagicScheduler/test4" method="get" id="form1" >
        <div class="form-group">
            <label class="mb-2 mr-sm-2">계약 일짜 :</label>
            <input type="date" class="form-control mb-2 mr-sm-2" style="width: 200px" id="contract_date" name="contract_date" value="${cri.contract_date}">
            <label class="mb-2 mr-sm-2">접수 번호 :</label>
            <input type="text" class="form-control mb-2 mr-sm-2" style="width: 200px" id="registration_num" placeholder="증권번호, 청약번호"
                   name="registration_num" value="${cri.registration_num}">
            <div class="form-group">
                <label class="mb-2 mr-sm-2">상태:</label>
                <select class="form-control mb-2 mr-sm-2" style="width: 200px" id="status" name="status" >
                    <option value=""
                            <c:out value="${cri.status == ''?'selected':''}"/> >전체</option>
                    <option value="03"
                            <c:out value="${cri.status == '03'?'selected':''}"/> >약관 제작 시작</option>
                    <option value="04"
                            <c:out value="${cri.status == '04'?'selected':''}"/> >약관 제작 완료</option>
                    <option value="90"
                            <c:out value="${cri.status == '90'?'selected':''}"/> >약관 제작 실패</option>

                </select>
            </div>
            <br>
        </div>
        <div class="form-group">
            <label class="mb-2 mr-sm-2">조회 시작 :</label>
            <input type="date" data-placeholder="${threeMonthAgo}" required aria-required="true" class="form-control mb-2 mr-sm-2" id="created_start" style="width: 200px" value="${cri.created_start}" >
            <label class="mb-2 mr-sm-2">조회 종료 :</label>
            <input type="date" data-placeholder="${threeMonthLater}" required aria-required="true" class="form-control mb-2 mr-sm-2" id="created_end"  style="width: 200px" name="created_end" value="${cri.created_end}">
            <button type="button" id="button1" href="javascript:void(0)" onclick="func('1')"  class="btn btn-primary mb-2 mr-2 ml-3">  조회  </button>
            <button type="button" id="button2" href="javascript:void(0)" onclick="func('home')"  class="btn btn-primary mb-2 ml-3">처음으로</button>
        </div>
    </form>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>계약 일짜</th>
            <th>접수번호</th>
            <th>상태</th>
            <th>생성 일짜</th>
        </tr>
        </thead>

        <tbody id="mySearch">
        <c:forEach items="${list}" var="k">
            <tr>
                <th>${k.contractDate}</th>
                <th>${k.registrationNum}</th>
                <th>${k.status}</th>
                <th>${k.created}</th>
            </tr>
        </c:forEach>
        </tbody>

    </table>

    <ul class="pagination h-100 justify-content-center align-items-center">
        <c:if test="${paging.prev}">
            <li class="page-item">
                <a class="page-link" href="javascript:void(0)" onclick="func(${paging.startPage-1})">이전</a>
            </li>
        </c:if>
        <c:forEach begin="${paging.startPage}" end="${paging.endPage}" var="num">
            <c:choose>
                <c:when test="${cri.page eq num}">
                    <li class="page-item active">
                        <a class="page-link" href="javascript:void(0)" onclick="func(${num})">${num}</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link" href="javascript:void(0)" onclick="func(${num})">${num}</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:if test="${paging.next && paging.endPage>0}">
            <li class="page-item">
                <a class="page-link" href="javascript:void(0)" onclick="func(${paging.endPage+1})">다음</a>
            </li>
        </c:if>
    </ul>


</div>
<script type="text/javascript">

    function func(page) {
        /* var page/!*= '<c:out value="${cri.page}"/>'*!/;*/
        var threeMonthAgo = '<c:out value="${threeMonthAgo}"/>';
        var threeMonthLater = '<c:out value="${threeMonthLater}"/>';
        console.log("page"+page);
        var contract_date = document.getElementById("contract_date").value;
        var status = document.getElementById("status").value;
        var registration_num = document.getElementById("registration_num").value;
        var created_start = document.getElementById("created_start").value;
        var created_end = document.getElementById("created_end").value;

        const key = ['contract_date', 'registration_num', 'status', 'created_start', 'created_end'];
        const value = [contract_date, registration_num, status, created_start, created_end];
        const arr =[];

        for(var i =0; i<value.length; i++){
            if(value[i] != null && value[i] != '') {
                arr.push(i);
            }
        }
        console.log("threeMonthAgo : " + threeMonthAgo);

        if (page=='home') {
            location.href="test4?created_start="+threeMonthAgo+"&created_end="+threeMonthLater;
        }
        else if(arr.length ==0 && page == '1' ){
            location.href="test4?page="+page+"&created_start="+threeMonthAgo+"&created_end="+threeMonthLater;
             /*alert("최소 한개 이상 검색 값을 넣어주세요.");*/
        }
        else if (arr.length ==0) {
            location.href="test4?page="+page+"&created_start="+threeMonthAgo+"&created_end="+threeMonthLater;
        }
        else if(arr.length == 1){
            location.href="test4?page="+page+"&"+key[arr[0]]+"="+value[arr[0]];
        }
        else if (arr.length == 2) {
            location.href="test4?page="+page+"&"+key[arr[0]]+"="+value[arr[0]]+"&"+key[arr[1]]+"="+value[arr[1]];
        }
        else if (arr.length == 3) {
            location.href="test4?page="+page+"&"+key[arr[0]]+"="+value[arr[0]]+"&"+key[arr[1]]+"="+value[arr[1]]+"&"+key[arr[2]]+"="+value[arr[2]];
        }
        else if (arr.length == 4) {
            location.href="test4?page="+page+"&"+key[arr[0]]+"="+value[arr[0]]+"&"+key[arr[1]]+"="+value[arr[1]]+"&"+key[arr[2]]+"="+value[arr[2]]+"&"+key[arr[3]]+"="+value[arr[3]];
        }
        else {
            location.href= "test4?page="+page+"&"+"contract_date="+contract_date+"&registration_num="+registration_num+"&status="+status+
                "&created_start="+created_start+"&created_end="+created_end;
        }

    }


</script>

</body>
</html>