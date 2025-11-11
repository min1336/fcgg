<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
    body {
        font-family: 'Arial', sans-serif;
        margin: 0;
        padding: 0;
        background: linear-gradient(135deg, #8EC5FC, #E0C3FC);
        min-height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .card {
        background: #fff;
        border-radius: 16px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.2);
        padding: 30px;
        max-width: 500px;
        width: 90%;
        text-align: center;
        word-wrap: break-word;
    	word-break: break-all;
        animation: fadeIn 0.6s ease-in-out;
    }

    .card h2 {
        margin-bottom: 10px;
        color: #333;
    }

    .card p {
        font-size: 16px;
        margin: 6px 0;
        color: #555;
    }

    .highlight {
        font-weight: bold;
        color: #007bff;
    }

    .btn {
        display: inline-block;
        margin-top: 20px;
        padding: 10px 20px;
        border-radius: 8px;
        background: #007bff;
        color: white;
        text-decoration: none;
        transition: 0.3s;
    }

    .btn:hover {
        background: #0056b3;
    }

    @keyframes fadeIn {
        from { opacity: 0; transform: translateY(20px); }
        to { opacity: 1; transform: translateY(0); }
    }
</style>
</head>
<body>
	<div class="card">
	    <h2>소환사 정보</h2>
	        <p><span class="highlight">Game ID:</span> ${gameId}</p>
	        <p><span class="highlight">Map ID:</span> ${mapId}</p>
	        <p><span class="highlight">Game Mode:</span> ${gameMode}</p>
	    <a href="user_form.jsp" class="btn">다른 소환사 검색</a>
	</div>
</body>
</html>