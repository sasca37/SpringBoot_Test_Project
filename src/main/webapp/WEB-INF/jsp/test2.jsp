<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>

<div class="container">
    <br>

    <h2>조회 목록</h2>
    <c:out value="${'Total Search : '} ${boardListCnt}"></c:out>
    <form class="form-inline" action="/MagicScheduler/search" method="post">
    <div class="form-group">
        <label class="mb-2 mr-sm-2">계약 일짜 :</label>
        <input type="date" class="form-control mb-2 mr-sm-2" id="contract_date" name="contract_date">
        <label class="mb-2 mr-sm-2">접수 번호 :</label>
        <input type="text" class="form-control mb-2 mr-sm-2" id="registration_num" placeholder="증권번호, 청약번호" name="registration_num">
        <div class="form-group">
            <label class="mb-2 mr-sm-2">상태 코드 :</label>
            <select class="form-control mb-2 mr-sm-2" id="status" name="status">
                <option>전체</option>
                <option>02</option>
                <option>03</option>
                <option>04</option>
                <option>98</option>
                <option>99</option>
            </select>
        </div>
        <br>
    </div>
    <div class="form-group">
        <label class="mb-2 mr-sm-2">조회 시작 :</label>
        <input type="date" class="form-control mb-2 mr-sm-2" id="created_start" name="created_start">
        <label class="mb-2 mr-sm-2">조회 종료 :</label>
        <input type="date" class="form-control mb-2 mr-sm-2" id="created_end" name="created_end">
        <button type="submit" class="btn btn-primary mb-2">조회</button>
    </div>
    </form>




    <table class="table table-striped">
        <thead>
        <tr>
            <th>계약 일짜</th>
            <th>접수번호</th>
            <th>상태코드</th>
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
            <a class="page-link" href='<c:url value="/MagicScheduler/test2?page=${paging.startPage-1}"/>'>이전</a>
            </li>
        </c:if>
        <c:forEach begin="${paging.startPage}" end="${paging.endPage}" var="num">
            <c:choose>
                <c:when test="${cri.page eq num}">
                    <li class="page-item active">
                        <a class="page-link" href='<c:url value="/MagicScheduler/test2?page=${num}"/>'>${num}</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link" href='<c:url value="/MagicScheduler/test2?page=${num}"/>'>${num}</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:if test="${paging.next && paging.endPage>0}">
            <li class="page-item">
            <a class="page-link" href='<c:url value="/MagicScheduler/test2?page=${paging.endPage+1}"/>'>다음</a>
            </li>
        </c:if>
    </ul>


</div>

<script>
    $(document).ready(function(){
        $("#email2").on("keyup", function() {
            var value = $(this).val().toLowerCase();
            $("#mySearch tr").filter(function() {
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });
    });
</script>
</body>
</html>