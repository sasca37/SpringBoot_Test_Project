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
    <p>테스트3입니다.</p>
    <form class="form-inline" action="/action_page.php">
        <label class="mb-2 mr-sm-2">계약 일짜:</label>
        <input type="date" class="form-control mb-2 mr-sm-2" id="contract_date" name="contract_date">
        <label for="pwd2" class="mb-2 mr-sm-2">접수 번호:</label>
        <input type="text" class="form-control mb-2 mr-sm-2" id="pwd2" placeholder="증권번호, 청약번호" name="pswd">
        <div class="form-group">
            <label for="sel1" class="mb-2 mr-sm-2">상태 코드</label>
            <select class="form-control mb-2 mr-sm-2" id="sel1">
                <option>02</option>
                <option>03</option>
                <option>04</option>
                <option>전체</option>
            </select>
        </div>
        <br>
    </form>
    <form class="form-inline" action="/action_page.php">
        <label class="mb-2 mr-sm-2">조회 시작:</label>
        <input type="date" class="form-control mb-2 mr-sm-2" id="created_start" name="created_start">
        <label class="mb-2 mr-sm-2">조회 종료:</label>
        <input type="date" class="form-control mb-2 mr-sm-2" id="created_end" name="created_end">
        <button type="submit" class="btn btn-primary mb-2">조회</button>
    </form>




    <table class="table table-striped">
        <thead>
        <tr>
            <th>계약 일짜</th>
            <th>접수번호</th>
            <th>상태코드</th>
            <th>조회 시작 날짜</th>
            <th>조회 종료 날짜</th>
        </tr>
        </thead>
        <tbody id="mySearch">
        <tr>
            <td>John</td>
            <td>Doe</td>
            <td>john@example.com</td>
            <td>john@example.com</td>
            <td>john@example.com</td>
        </tr>
        <tr>
            <td>Mary</td>
            <td>Moe</td>
            <td>mary@example.com</td>
        </tr>
        <tr>
            <td>July</td>
            <td>Dooley</td>
            <td>july@example.com</td>
        </tr>
        </tbody>
    </table>
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