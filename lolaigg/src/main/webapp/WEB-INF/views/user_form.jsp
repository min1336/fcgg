<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>소환사 검색</title>
<style>
    /* 페이지 전체 배경 */
    body {
        margin: 0;
        padding: 0;
        font-family: 'Arial', sans-serif;
        background-color: #1a1d1a; /* 검정색 배경 */
        color: #878787; /* 기본 텍스트 흰색 */
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
    }

    /* 폼 컨테이너 */
    .search-container {
        background-color: #111; /* 조금 더 밝은 검정 */
        padding: 40px 50px;
        border-radius: 15px;
        box-shadow: 0 0 20px rgba(0, 255, 0, 0.5); /* 초록색 그림자 */
        text-align: center;
    }

    .search-container h1 {
        color: #00ff00; /* 초록색 타이틀 */
        margin-bottom: 30px;
        font-size: 2em;
        text-shadow: 0 0 10px #00ff00;
    }

    /* 입력창 스타일 */
    .search-container input[type="text"] {
        width: 250px;
        padding: 10px 15px;
        margin: 10px 0;
        border: 2px solid #00ff00;
        border-radius: 8px;
        background-color: #000;
        color: #00ff00;
        font-size: 1em;
        outline: none;
        transition: 0.3s;
    }

    .search-container input[type="text"]:focus {
        border-color: #00ff88;
        box-shadow: 0 0 10px #00ff88;
    }

    /* 버튼 스타일 */
    .search-container button {
        width: 280px;
        padding: 12px;
        margin-top: 20px;
        border: none;
        border-radius: 8px;
        background-color: #00ff00;
        color: #000;
        font-size: 1.1em;
        font-weight: bold;
        cursor: pointer;
        transition: 0.3s;
    }

    .search-container button:hover {
        background-color: #00cc00;
        box-shadow: 0 0 15px #00ff00;
    }

</style>
</head>
<body>
    <div class="search-container">
        <h1>소환사 검색</h1>
        <form action="userQuery.do" method="get">
            <input type="text" name="summonerName" placeholder="닉네임 입력" required><br>
            <input type="text" name="summonerTag" placeholder="태그 입력 (# 제외)" required><br>
            <button type="submit">검색</button>
        </form>
    </div>
</body>
</html>
