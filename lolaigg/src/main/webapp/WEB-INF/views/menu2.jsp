<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TFTGG - 메인 메뉴</title>
</head>
<body>
<table style="padding: 5px; margin: 10px; width: 300px; align-content: center;">
		<tr>
				<th colspan="3" align="center">메뉴 선택</th>
		</tr>
		<tr height="10px"></tr>
		<tr>
				<td><button onclick="location.href='user.do'">소환사 정보 조회</button></td>
				<td><button onclick="location.href='meta.do'">추천 메타</button></td>
				<td><button onclick="location.href='api.do'">api 테스트</button></td>
		</tr>
</table>
</body>
</html>