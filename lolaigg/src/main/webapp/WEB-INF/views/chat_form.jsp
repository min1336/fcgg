<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>TFT 덱 추천 챗봇</title>
<script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
<style>
    body {
        font-family: -apple-system, BlinkMacSystemFont, "Malgun Gothic", "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
        /* 🌟 [수정] 위젯이므로 body 배경색과 여백 제거 */
        background-color: #ffffff; 
        margin: 0;
        color: #333;
    }

    /* 🌟 [수정] #chat-container가 화면 전체가 아닌 100%를 채우도록 변경 */
    #chat-container {
        width: 100%;
        height: 100vh; /* 🌟 iframe 높이에 꽉 차도록 100vh로 변경 */
        background-color: #ffffff;
        border-radius: 0; /* 🌟 위젯에서는 둥근 모서리 제거 */
        box-shadow: none;   /* 🌟 위젯에서는 그림자 제거 */
        display: flex;
        flex-direction: column;
        overflow: hidden;
        /* 🌟 min-height, max-width 제거 */
    }

    #chat-header {
        background: linear-gradient(135deg, #007bff, #0056b3);
        color: white;
        padding: 18px 20px; /* 🌟 패딩 살짝 줄임 */
        text-align: center;
        font-size: 1.1rem; /* 🌟 폰트 살짝 줄임 */
        font-weight: 700;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        z-index: 10;
        flex-shrink: 0; /* 🌟 헤더 높이 고정 */
    }

    #chat-window {
        flex-grow: 1;
        padding: 20px; /* 🌟 패딩 살짝 줄임 */
        overflow-y: auto;
        display: flex;
        flex-direction: column;
        gap: 15px; /* 🌟 간격 살짝 줄임 */
    }
    
    /* === 스크롤바 디자인 (Webkit 브라우저) === */
    #chat-window::-webkit-scrollbar {
        width: 6px;
    }
    #chat-window::-webkit-scrollbar-track {
        background: transparent;
    }
    #chat-window::-webkit-scrollbar-thumb {
        background-color: #ddd;
        border-radius: 3px;
    }
    #chat-window::-webkit-scrollbar-thumb:hover {
        background-color: #bbb;
    }
    /* === */

    /* === 메시지 등장 애니메이션 === */
    @keyframes fadeIn {
        from {
            opacity: 0;
            transform: translateY(10px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }
    
    .message {
        padding: 14px 20px;
        border-radius: 20px;
        line-height: 1.5;
        max-width: 90%; /* 85% -> 90% */
        animation: fadeIn 0.3s ease-out;
    }

    /* 🌟 .message.bot 내부 폰트 크기 등 미세 조정 (선택 사항) */
    .message.bot {
        font-size: 0.95rem; 
        /* ... 기존 .message.bot 스타일 ... */
        background-color: #f1f3f5;
        color: #212529;
        border-bottom-left-radius: 6px;
        align-self: flex-start;
        border: 1px solid #e9ecef;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);
        word-break: keep-all;
    }
    
    .message.user {
         font-size: 0.95rem;
        /* ... 기존 .message.user 스타일 ... */
        background: linear-gradient(135deg, #007bff, #0056b3);
        color: white;
        border-bottom-right-radius: 6px;
        align-self: flex-end;
        box-shadow: 0 2px 5px rgba(0, 123, 255, 0.2);
    }
    
    /* 3. 챗봇 응답(마크다운 변환) 스타일 (개선) */
    .message.bot h1,
    .message.bot h2,
    .message.bot h3 {
        margin-top: 0.8em;
        margin-bottom: 0.6em;
        font-weight: 600;
        border-bottom: 2px solid #007bff;
        padding-bottom: 5px;
    }
    
    .message.bot ul,
    .message.bot ol {
        padding-left: 22px;
        margin-top: 0.5em;
        margin-bottom: 0.5em;
    }
    
    .message.bot li {
        margin-bottom: 5px;
    }
    
    .message.bot b,
    .message.bot strong {
        color: #0056b3;
        background-color: #e0eefa;
        padding: 2px 6px;
        border-radius: 4px;
        font-weight: 600;
    }
    
    .message.bot code {
        background: #e9ecef;
        padding: 2px 5px;
        border-radius: 4px;
        font-family: "SFMono-Regular", Consolas, "Liberation Mono", Menlo, monospace;
    }
    
    .message.bot pre {
        background: #212529;
        color: #f8f9fa;
        padding: 15px;
        border-radius: 8px;
        overflow-x: auto;
    }
    
    .message.bot pre code {
        background: none;
        padding: 0;
    }
    
    /* === */

    #chat-form {
        display: flex;
        padding: 15px 20px; /* 🌟 패딩 살짝 줄임 */
        border-top: 1px solid #e0e0e0;
        background-color: #ffffff;
        box-shadow: 0 -4px 12px rgba(0, 0, 0, 0.05);
        z-index: 5;
    }

    #message-input {
        flex-grow: 1;
        padding: 12px 18px; /* 🌟 패딩 살짝 줄임 */
        border: 1px solid #ddd;
        border-radius: 20px; /* 🌟 살짝 줄임 */
        font-size: 0.95rem;  /* 🌟 폰트 살짝 줄임 */
        margin-right: 10px;
        outline: none;
        transition: border-color 0.2s, box-shadow 0.2s;
    }
    
    #message-input:focus {
        border-color: #007bff;
        box-shadow: 0 0 0 3px rgba(0,123,255,0.15);
    }

    #send-button {
        flex-shrink: 0;
        width: 44px;  /* 🌟 버튼 크기 살짝 줄임 */
        height: 44px; /* 🌟 버튼 크기 살짝 줄임 */
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 50%;
        font-size: 1.5rem;
        cursor: pointer;
        transition: background-color 0.2s, transform 0.1s;
        
        background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='white' width='24px' height='24px'%3E%3Cpath d='M2.01 21L23 12 2.01 3 2 10l15 2-15 2z'/%3E%3C/svg%3E");
        background-repeat: no-repeat;
        background-position: center;
        background-size: 22px;
        
        padding: 0;
        text-indent: -9999px;
    }
    
    #send-button:hover {
        background-color: #0056b3;
    }
    
    #send-button:active {
        transform: scale(0.95);
    }
    
    /* 🌟🌟 [수정된 로딩 인디케이터 CSS] 🌟🌟 */
    #loading-indicator {
        /* 기본 숨김 */
        display: none; 
        
        /* .message.bot 스타일과 유사하게 적용 */
        padding: 14px 20px;
        border-radius: 20px;
        line-height: 1.5;
        max-width: 90%;
        animation: fadeIn 0.3s ease-out;
        background-color: #f1f3f5;
        border-bottom-left-radius: 6px;
        align-self: flex-start;
        border: 1px solid #e9ecef;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.04);
        
        /* 점(span)들을 정렬하기 위한 Flex 설정 */
        align-items: center;
        min-height: 30px; /* 최소 높이 */
        margin: 0; /* 기존 마진 제거 */
    }
    
    /* "띠로롱" 효과를 위한 @keyframes */
    @keyframes boing {
        0%, 80%, 100% {
            transform: translateY(0);
            opacity: 0.6;
        }
        40% {
            transform: translateY(-8px); /* 점프 높이 살짝 줄임 */
            opacity: 1;
        }
    }

    /* 로딩 ... 점들에 대한 스타일 */
    #loading-indicator span {
        display: inline-block;
        font-size: 2.5rem; /* 점 크기 */
        font-weight: bold;
        color: #b0b0b0; /* 점 색상 */
        line-height: 1; 
        
        animation: boing 1.4s infinite;
        margin: 0 2px; /* 점 사이 간격 */
    }

    #loading-indicator span:nth-child(1) {
        animation-delay: 0s;
    }
    #loading-indicator span:nth-child(2) {
        animation-delay: 0.2s;
    }
    #loading-indicator span:nth-child(3) {
        animation-delay: 0.4s;
    }
</style>
</head>
<body>

    <div id="chat-container">
        <div id="chat-header">TFT 덱 추천 챗봇</div>
        
        <div id="chat-window">
            <div class="message bot">
                안녕하세요! TFT 덱 추천 봇입니다. 초반 기물이나 '빠른 9렙 운영'처럼 원하는 스타일을 말씀해주세요!
            </div>
            
            <!-- 🌟 [수정] 로딩 인디케이터를 chat-window 안으로 이동 -->
            <div id="loading-indicator">
	            <span>.</span>
	            <span>.</span>
	            <span>.</span>
	        </div>
        </div>

        <form id="chat-form">
		    <input type="text" id="message-input" placeholder="메시지를 입력하세요..." autocomplete="off">
		    <button type="submit" id="send-button"></button>
		</form>
    </div>

    <script>
        const chatWindow = document.getElementById('chat-window');
        const chatForm = document.getElementById('chat-form');
        const messageInput = document.getElementById('message-input');
        const loadingIndicator = document.getElementById('loading-indicator');
        
        chatForm.addEventListener('submit', async function(e) {
            e.preventDefault(); 
            
            const userMessage = messageInput.value.trim();
            if (userMessage === '') return;
            
            addMessageToWindow(userMessage, 'user');
            messageInput.value = ''; 
            
            // 🌟 [수정된 JS 로직] 🌟
            // 1. 로딩 인디케이터를 채팅창의 맨 마지막으로 이동시킴
            chatWindow.appendChild(loadingIndicator);
            // 2. 로딩 인디케이터 표시 (flex로 해야 점들이 정렬됨)
            loadingIndicator.style.display = 'flex'; 
            // 3. 스크롤 맨 아래로
            chatWindow.scrollTop = chatWindow.scrollHeight; 
            
            try {
                const response = await fetch('chat.do', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ message: userMessage })
                });
                
                console.log('Response status:', response.status); // 추가
                console.log('Response ok:', response.ok); // 추가
                
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                
                const data = await response.json();
                console.log('Response data:', data); // 추가
                
                const botHtmlReply = marked.parse(data.reply); 
                
                loadingIndicator.style.display = 'none';
                addMessageToWindow(botHtmlReply, 'bot');
                
            } catch (error) {
                console.error('Detailed error:', error); // 추가
                console.error('Error stack:', error.stack); // 추가
                
                const errorHtml = marked.parse(`**오류가 발생했습니다:**\n\`\`\`\n${error.message}\n\`\`\``);
                
                loadingIndicator.style.display = 'none';
                addMessageToWindow(errorHtml, 'bot');
            } finally {
                // 🌟 finally에서는 스크롤만 처리
                chatWindow.scrollTop = chatWindow.scrollHeight;
            }
        });

        function addMessageToWindow(message, sender) {
            const messageDiv = document.createElement('div');
            messageDiv.classList.add('message', sender);
            messageDiv.innerHTML = message; 
            
            chatWindow.appendChild(messageDiv);
            chatWindow.scrollTop = chatWindow.scrollHeight;
        }
    </script>

</body>
</html>