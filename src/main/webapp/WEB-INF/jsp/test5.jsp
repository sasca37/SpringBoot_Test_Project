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

    <form class="form-inline" action="/MagicScheduler/test3" method="get">
        <div class="form-group">
            <label class="mb-2 mr-sm-2">계약 일짜 :</label>
            <input type="date" class="form-control mb-2 mr-sm-2" id="contract_date" name="contract_date" value="${cri.contract_date}">
            <label class="mb-2 mr-sm-2">접수 번호 :</label>
            <input type="text" class="form-control mb-2 mr-sm-2" id="registration_num" placeholder="증권번호, 청약번호"
                   name="registration_num" value="${cri.registration_num}">
            <div class="form-group">
                <label class="mb-2 mr-sm-2">상태 코드 :</label>
                <select class="form-control mb-2 mr-sm-2" id="status" name="status" >
                    <option value=""
                            <c:out value="${cri.status == ''?'selected':''}"/> >전체</option>
                    <option value="02"
                            <c:out value="${cri.status == '02'?'selected':''}"/> >02</option>
                    <option value="03"
                            <c:out value="${cri.status == '03'?'selected':''}"/> >03</option>
                    <option value="04"
                            <c:out value="${cri.status == '04'?'selected':''}"/> >04</option>
                    <option value="98"
                            <c:out value="${cri.status == '98'?'selected':''}"/> >98</option>
                    <option value="99"
                            <c:out value="${cri.status == '99'?'selected':''}"/> >99</option>
                </select>
            </div>
            <br>
        </div>
        <div class="form-group">
            <label class="mb-2 mr-sm-2">조회 시작 :</label>
            <input type="date" class="form-control mb-2 mr-sm-2" id="created_start" name="created_start" value="${cri.created_start}">
            <label class="mb-2 mr-sm-2">조회 종료 :</label>
            <input type="date" class="form-control mb-2 mr-sm-2" id="created_end" name="created_end" value="${cri.created_end}">
            <button type="button" onclick="func()" class="btn btn-primary mb-2">조회</button>
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
                <a class="page-link" href='
                            <c:choose>
                                <c:when test="${cri.status eq null}">
                                    <c:url value="/MagicScheduler/test3"/>
                                </c:when>
                                <c:otherwise>
                                     <c:url var="listUrl" value="/MagicScheduler/test3">
                                        <c:param name="status" value="${cri.status}"/>
                                     </c:url>
                                </c:otherwise>
                            </c:choose>
                        '>이전</a>
            </li>
        </c:if>
        <c:forEach begin="${paging.startPage}" end="${paging.endPage}" var="num">
            <c:choose>
                <c:when test="${cri.page eq num}">
                    <li class="page-item active">
                        <a class="page-link" href='
                                <%--<c:url value="/MagicScheduler/test3?page=${num}"/>--%>
                               <c:choose>
                                    <c:when test="${cri.status eq null}">
                                         <c:url value="/MagicScheduler/test3">
                                            <c:param name="page" value="${num}"/>
                                         </c:url>
                                    </c:when>
                                    <c:otherwise>
                                         <c:url value="/MagicScheduler/test3">
                                            <c:param name="page" value="${num}"/>
                                            <c:param name="status" value="${cri.status}"/>
                                         </c:url>
                                    </c:otherwise>
                                </c:choose>
                            '>${num}</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link" href='
                                <%--<c:url value="/MagicScheduler/test3?page=${num}"/>--%>
                                <c:choose>
                                    <c:when test="${cri.status eq null}">
                                         <c:url value="/MagicScheduler/test3">
                                            <c:param name="page" value="${num}"/>
                                         </c:url>
                                    </c:when>
                                    <c:otherwise>
                                         <c:url value="/MagicScheduler/test3">
                                            <c:param name="page" value="${num}"/>
                                            <c:param name="status" value="${cri.status}"/>
                                         </c:url>
                                    </c:otherwise>
                                </c:choose>
                                '>${num}</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:if test="${paging.next && paging.endPage>0}">
            <li class="page-item">
                <a class="page-link" href='
                       <%-- <c:url value="/MagicScheduler/test3?page=${paging.endPage+1}"/>--%>
                       <c:choose>
                            <c:when test="${cri.status eq null}">
                                 <c:url value="/MagicScheduler/test3">
                                    <c:param name="page" value="${paging.endPage+1}"/>
                                 </c:url>
                            </c:when>
                            <c:otherwise>
                                 <c:url value="/MagicScheduler/test3">
                                    <c:param name="page" value="${paging.endPage+1}"/>
                                    <c:param name="status" value="${cri.status}"/>
                                 </c:url>
                            </c:otherwise>
                        </c:choose>
                        '>다음</a>
            </li>
        </c:if>
    </ul>


</div>
<script type="text/javascript">
    function func() {
        var contract_date = document.getElementById("contract_date").value;
        var status = document.getElementById("status").value;
        var registration_num = document.getElementById("registration_num").value;
        var created_start = document.getElementById("created_start").value;
        var created_end = document.getElementById("created_end").value;

        /* alert("이름을 기입해 주십시오.");*/
        const key = ['contract_date', 'registration_num', 'status', 'created_start', 'created_end'];
        const value = [contract_date, registration_num, status, created_start, created_end];
        const arr =[];
        /* const newArr = arr.filter((element,i) => element !== null);
       newArr.forEach((e,i) => {
            document.write(i+" : " + e +'<br>')
            console.log(i+" : " + e +'<br>')
        })*/

        console.log(value.filter(n => n))
        for(var i =0; i<value.length; i++){
            if(value[i] != null && value[i] != '') {
                /* document.write("i: "+value[i]);*/
                arr.push(i);
            }
        }
        if(arr.length ==0 ){
            alert("최소 한개 이상 검색 값을 넣어주세요.");
        }
        else if(arr.length == 1){
            location.href="test3?"+key[arr[0]]+"="+value[arr[0]];
        }
        else if (arr.length == 2) {
            location.href="test3?"+key[arr[0]]+"="+value[arr[0]]+"&"+key[arr[1]]+"="+value[arr[1]];
        }
        else if (arr.length == 3) {
            location.href="test3?"+key[arr[0]]+"="+value[arr[0]]+"&"+key[arr[1]]+"="+value[arr[1]]+"&"+key[arr[2]]+"="+value[arr[2]];
        }
        else if (arr.length == 4) {
            location.href="test3?"+key[arr[0]]+"="+value[arr[0]]+"&"+key[arr[1]]+"="+value[arr[1]]+"&"+key[arr[2]]+"="+value[arr[2]]+key[arr[3]]+"="+value[arr[3]];
        }
        else {
            location.href= "test3?contract_date="+contract_date+"&registration_num="+registration_num+"&status="+status+
                "&created_start="+created_start+"&created_end="+created_end;
        }
        /*document.write("ais: "+arr);*/
        /* location.href= "test3?contract_date="+contract_date+"&registration_num="+registration_num+"&status="+status+
             "&created_start="+created_start+"&created_end="+created_end;*/
        /* location.href="test3?"+key[0]+"="+value[0]+"&"+key[1]+"="+value[1];*/
    }
</script>
<%--<script>

    $(document).ready(function(){
        $("#email2").on("keyup", function() {
            var value = $(this).val().toLowerCase();
            $("#mySearch tr").filter(function() {
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });
    });
</script>--%>
</body>
</html>