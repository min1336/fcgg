<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>최근 전적</h1>

<table border="1" width="100%" style="text-align: center;">
    <tr>
        <th>매치 ID</th>
        <th>매치 상세</th>
    </tr>
    <c:forEach var="match" items="${matches}">
        <tr>
            <td>${match.matchId}</td>
            <td>${match.matchDetail}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>