<%-- 파일 경로: lolaigg/src/main/webapp/WEB-INF/views/user_result.jsp --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TFTGG - ${summoner.summonerName}</title> <%-- summoner 객체가 null일 수 있으므로 주의 --%>
<%-- 외부 CSS 파일 연결 --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/user_result_style.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/chat_widget.css">
</head>

<body>

    <div class="card profile-card">
        <%-- 오류 메시지 표시 --%>
        <c:if test="${not empty errorMessage}">
            <p class="message error-message">${errorMessage}</p>
            <a href="user.do" class="btn">검색으로 돌아가기</a>
        </c:if>
        <%-- 경고 메시지 표시 --%>
         <c:if test="${not empty warningMessage}">
             <p class="message warning-message">${warningMessage}</p>
         </c:if>


        <c:if test="${empty errorMessage and not empty summoner}">
            <div class="profile-info">
                <%-- 프로필 이미지 (나중에 추가 가능) --%>
                <%-- <img src="소환사_아이콘_URL" alt="프로필 아이콘"> --%>
                <div class="profile-text">
                 <h2>
                     ${summoner.summonerName}<span style="color:#888;">#${summoner.summonerTag}</span>

                     <%-- 티어 이미지 추가 --%>
                     <c:set var="tierLower" value="${fn:toLowerCase(tier)}" />
                     <c:if test="${tier ne 'UNRANKED' and not empty rankImageMap[tierLower]}">
                         <img src="${rankImageMap[tierLower]}" alt="${tier}" class="rank-icon" style="vertical-align: middle; margin-left: 10px;"> <%-- 스타일 추가 --%>
                     </c:if>
                 </h2>
                 <p>
                     <%-- 티어 텍스트 표시 --%>
                     <span style="color:#aaa;">티어:</span> <span class="highlight">${tier} ${rank}</span>
                 </p>
                 <p>
                     <span style="color:#aaa;">마지막 갱신:</span> <span class="highlight">${summoner.lastUpdated}</span>
                 </p>
             </div>


             <div style="margin-top: 20px; display: flex; flex-wrap: wrap; justify-content: center; gap: 10px;">
                <form action="userQuery.do" method="get" style="display:inline-block; margin: 0;">
                    <input type="hidden" name="summonerName" value="${summoner.summonerName}">
                    <input type="hidden" name="summonerTag" value="${summoner.summonerTag}">
                    <input type="hidden" name="refresh" value="true">
                    <button type="submit" class="btn btn-refresh">전적 갱신</button>
                 </form>
                 <%-- 라이브 게임 조회 버튼 --%>
                 <form action="userLiveQuery.do" method="get" style="display:inline-block; margin: 0;">
                    <input type="hidden" name="summonerName" value="${summoner.summonerName}">
                    <input type="hidden" name="summonerTag" value="${summoner.summonerTag}">
                    <button type="submit" class="btn">라이브 게임 조회</button>
                </form>

             <a href="user.do" class="btn">다른 소환사 검색</a>
            </div>
        </c:if>
        <c:if test="${empty errorMessage and empty summoner}">
            <p>소환사 정보를 불러올 수 없습니다. 다시 검색해 주세요.</p>
            <a href="user.do" class="btn">검색으로 돌아가기</a>
        </c:if>
    </div>

    <%-- 주요 통계 --%>
    <c:if test="${empty errorMessage and not empty summoner}">
        <div class="card">
            <h2>주요 통계 (최근 ${fn:length(matchHistory)}게임)</h2>
            <div class="stats-container">
                <c:choose>
                    <c:when test="${not empty topChampion}">
                        <div class="stat-item">
                            <p>가장 많이 사용한 챔피언</p>
                            <c:set var="topChampId" value="${topChampion}" />
                            <c:if test="${not empty championImageMap[topChampId]}">
                                 <img src="${championImageMap[topChampId]}" alt="${topChampId}"
                                      class="stat-champ-img unit-cost-${championCostMap[topChampId]}">
                             </c:if>
                            <h3 class="highlight">${topChampion}</h3>
                        </div>
                    </c:when>
                    <c:otherwise>
                          <div class="stat-item"><p>챔피언 데이터 없음</p></div>
                    </c:otherwise>
                </c:choose>
                <c:choose>
                    <c:when test="${not empty topSynergy}">
                             <div class="stat-item">
                            <p>가장 많이 사용한 시너지</p>
                            <%-- 시너지 아이콘 (선택 사항) --%>
                             <c:if test="${not empty synergyImageMap[topSynergy]}">
                                <img src="${synergyImageMap[topSynergy]}" alt="${topSynergy}" class="stat-champ-img">
                            </c:if>
                         <h3 class="highlight">${topSynergy}</h3>
                        </div>
                    </c:when>
                     <c:otherwise>
                        <div class="stat-item"><p>시너지 데이터 없음</p></div>
                    </c:otherwise>
                 </c:choose>
             </div>
        </div>
    </c:if>

    <%-- 매치 기록 --%>
    <c:if test="${empty errorMessage and not empty summoner}">
        <div class="card match-list-card">
             <h2>최근 매치 기록</h2>

            <c:choose>
                <c:when test="${not empty matchHistory}">
                     <c:forEach var="match" items="${matchHistory}">
                        <div class="match-card">
                           <div class="match-header">
                                <%-- 순위 뱃지 --%>
                                <span class="placement-badge
                                     <c:choose>
                                        <c:when test="${match.placement == 1}">placement-1</c:when>
                                        <c:when test="${match.placement <= 4}">placement-2-4</c:when>
                                        <c:otherwise>placement-5-8</c:otherwise>
                                    </c:choose>
                                 ">
                                    #${match.placement}
                                </span>
                                <%-- 오른쪽 상세 정보 --%>
                                <div class="match-details-right">
                                    <div><span class="detail-label">레벨:</span> ${match.level}</div>
                                     <div><span class="detail-label">남은 골드:</span> ${match.goldLeft}</div>
                                </div>
                            </div>

                             <div class="match-body">
                                <%-- 시너지 정보 --%>
                                <div>
                                  <span class="detail-label">시너지:</span>
                                     <div class="tag-container">
                                        <%-- --- 수정된 시너지 반복문 (새로운 등급 매핑 적용) --- --%>
                                        <c:forEach var="traitInfo" items="${match.traits}">
                                            <c:set var="synergyName" value="${traitInfo.name}" />
                                            <c:set var="synergyStyle" value="${traitInfo.style}" />
                                            <c:set var="synergyImageUrl" value="${synergyImageMap[synergyName]}" />

                                            <%-- 새로운 등급(style) 값에 따라 적용할 CSS 클래스 결정 --%>
                                            <c:choose>
                                                <c:when test="${synergyStyle == 1}"> <%-- 브론즈 --%>
                                                    <c:set var="styleClass" value="trait-style-1" />
                                                </c:when>
                                                <c:when test="${synergyStyle == 2}"> <%-- 실버 --%>
                                                    <c:set var="styleClass" value="trait-style-2" />
                                                </c:when>
                                                <c:when test="${synergyStyle == 3}"> <%-- 고유 (기존 unique 스타일 사용) --%>
                                                    <c:set var="styleClass" value="trait-unique" />
                                                    <%-- 고유 등급도 이름별로 스타일 다르게 하려면 아래 주석 해제 및 수정 --%>
                                                    <%-- <c:if test="${synergyName eq 'TFT15_DragonFist'}"><c:set var="styleClass" value="trait-unique trait-unique-dragonfist" /></c:if> --%>
                                                </c:when>
                                                <c:when test="${synergyStyle == 4}"> <%-- 골드 (기존 style-3 색상 사용) --%>
                                                    <c:set var="styleClass" value="trait-style-3" />
                                                </c:when>
                                                <c:when test="${synergyStyle == 5}"> <%-- 프리즘 (기존 style-4 색상 사용) --%>
                                                    <c:set var="styleClass" value="trait-style-4" />
                                                </c:when>
                                                <c:otherwise> <%-- 그 외 (비활성 등) 기본 스타일 --%>
                                                    <c:set var="styleClass" value="" /> <%-- 또는 기본 클래스 지정 --%>
                                                </c:otherwise>
                                            </c:choose>

                                            <%-- 결정된 스타일 클래스 적용 --%>
                                            <span class="tag trait-tag ${styleClass}">
                                                 <c:if test="${not empty synergyImageUrl}">
                                                     <img src="${synergyImageUrl}" alt="${synergyName}">
                                                 </c:if>
                                                 <span class="trait-name">${synergyName}</span> <%-- 이름은 여전히 포함 (alt 속성 대체용) --%>
                                            </span>
                                        </c:forEach>
                                        <%-- --- 시너지 반복문 끝 --- --%>
                                     </div>
                                 </div>

                                <%-- 최종 기물 정보 --%>
                                <div>
                                     <span class="detail-label">최종 기물:</span>
                                         <div class="tag-container unit-container">
                                        <c:forEach var="unitInfo" items="${match.units}">
                                             <c:set var="unitId" value="${unitInfo.championId}" />
                                            <c:set var="unitTier" value="${unitInfo.tier}" />
                                             <c:set var="unitItems" value="${unitInfo.items}" />
                                            <c:set var="unitCost" value="${championCostMap[unitId]}" />

                                             <div class="unit-display unit-cost-${empty unitCost ? 1 : unitCost}">
                                                <%-- 성급 (Stars) --%>
                                                 <c:if test="${unitTier > 0}">
                                                    <span class="stars stars-${unitTier}">
                                                          <c:forEach begin="1" end="${unitTier}">★</c:forEach>
                                                    </span>
                                                 </c:if>
                                                 <%-- 챔피언 아이콘 --%>
                                                  <div class="champ-icon-container">
                                                    <c:if test="${not empty championImageMap[unitId]}">
                                                     <img src="${championImageMap[unitId]}" alt="${unitId}" class="champ-icon">
                                                    </c:if>
                                                    <c:if test="${empty championImageMap[unitId]}">
                                                         <span class="unit-name-fallback">${unitId}</span>
                                                    </c:if>
                                                  </div>
                                                <%-- 아이템 아이콘 --%>
                                                 <c:if test="${not empty unitItems}">
                                                   <div class="item-icons">
                                                         <c:forEach var="itemId" items="${unitItems}">
                                                            <c:if test="${not empty itemImageMap[itemId]}">
                                                                 <img src="${itemImageMap[itemId]}" alt="${itemId}" class="item-icon">
                                                             </c:if>
                                                        </c:forEach>
                                                     </div>
                                                </c:if>
                                             </div> <%-- // unit-display --%>
                                        </c:forEach>
                                    </div> <%-- // unit-container --%>
                                 </div>

                             </div> <%-- // match-body --%>
                        </div> <%-- // match-card --%>
                     </c:forEach>
                 </c:when>
                 <c:otherwise>
                    <p class="highlight" style="text-align: center;">아직 매치 기록이 없습니다. 전적 갱신을 해주세요.</p>
                </c:otherwise>
            </c:choose>
        </div> <%-- // match-list-card --%>
    </c:if> <%-- // empty errorMessage and not empty summoner --%>
	<div class="chat-widget-container" id="chatWidgetContainer">
        <iframe src="chat.do"></iframe>
    </div>

    <button class="chat-bubble-button" id="chatBubbleButton">
        <svg xmlns="http://www.w3.org/2000/svg" height="28px" viewBox="0 0 24 24" width="28px" fill="#FFFFFF">
            <path d="M0 0h24v24H0V0z" fill="none"/>
            <path d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm0 14H6l-2 2V4h16v12zM7 9h10v2H7zm0 3h7v2H7z"/>
        </svg>
    </button>

	<script>
            
            // 1. 챗봇 상태를 확인하고 UI에 적용하는 (재사용될) 함수
            function checkChatWidgetState() {
                const chatWidgetContainer = document.getElementById('chatWidgetContainer');
                const storageKey = 'tftggChatWidgetState';
                
                if (!chatWidgetContainer) {
                    console.error("checkChatWidgetState: 챗봇 컨테이너를 찾을 수 없습니다.");
                    return;
                }

                try {
                    const currentState = localStorage.getItem(storageKey);
                    if (currentState === 'open') {
                        chatWidgetContainer.classList.add('active');
                    } else {
                        // 'closed'이거나 null일 때
                        chatWidgetContainer.classList.remove('active');
                    }
                } catch (e) {
                    console.error("localStorage 접근 중 오류 발생:", e);
                }
            }

            // 2. [최초 로드] DOMContentLoaded - 클릭 리스너 등록 및 최초 상태 확인
            document.addEventListener('DOMContentLoaded', function() {
                const chatBubbleButton = document.getElementById('chatBubbleButton');
                const chatWidgetContainer = document.getElementById('chatWidgetContainer');
                const storageKey = 'tftggChatWidgetState';

                if (chatBubbleButton && chatWidgetContainer) {
                    
                    // 2-1. 클릭 이벤트 리스너 등록 (한 번만 하면 됨)
                    chatBubbleButton.addEventListener('click', function() {
                        chatWidgetContainer.classList.toggle('active');
                        try {
                            if (chatWidgetContainer.classList.contains('active')) {
                                localStorage.setItem(storageKey, 'open');
                            } else {
                                localStorage.setItem(storageKey, 'closed');
                            }
                        } catch (e) {
                             console.error("localStorage 저장 중 오류 발생:", e);
                        }
                    });

                    // 2-2. 최초 페이지 로드 시 상태 확인
                    checkChatWidgetState();
                    
                } else {
                    console.error("DOMContentLoaded: 챗봇 위젯 요소를 찾을 수 없습니다.");
                }
            });

            // 3. [뒤로가기 / 복원] 'pageshow' - bfcache 복원 시 상태 다시 확인
            // 이 리스너는 DOMContentLoaded와 *별개로* 전역에 등록합니다.
            window.addEventListener('pageshow', function(event) {
                // event.persisted가 true이면 bfcache (뒤로가기 캐시)에서 로드된 것
                if (event.persisted) {
                    // DOMContentLoaded가 실행되지 않았으므로,
                    // 여기서 localStorage 상태를 다시 확인하여 UI에 적용합니다.
                    console.log("페이지가 bfcache에서 복원되었습니다. 챗봇 상태를 다시 확인합니다.");
                    checkChatWidgetState();
                }
            });
            
        </script>
</body>
</html>