<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TFTGG - ${summoner.summonerName}</title>
<style>
    /* 폰트 및 기본 설정 */
    body {
        font-family: 'Malgun Gothic', 'Apple SD Gothic Neo', sans-serif; /* 한국어 환경 고려 */
        margin: 0;
        padding: 0;
        background: #111; /* 기본 블랙 배경 */
        color: #ddd; /* 기본 텍스트 색상 약간 밝게 */
        min-height: 100vh;
        display: flex;
        flex-direction: column;
        align-items: center;
        padding-top: 40px;
        padding-bottom: 40px;
        box-sizing: border-box;
    }

    /* 네온 그린 색상 변수 */
    :root {
        --neon-green: #00ff41;
        --dark-green: #00a02a;
        --highlight-bg: rgba(0, 255, 65, 0.1);
        --card-border: rgba(0, 255, 65, 0.3);
        /* 코스트별 색상 */
        --cost1: #808080; /* Grey */
        --cost2: #1abc9c; /* Green */
        --cost3: #3498db; /* Blue */
        --cost4: #9b59b6; /* Purple */
        --cost5: #f1c40f; /* Gold */
        --error-red: #ff4d4d;
        --warning-yellow: #f1c40f;
    }

    /* 카드 기본 스타일 */
    .card {
        background: #1e1e1e;
        border-radius: 8px;
        border: 1px solid var(--card-border);
        box-shadow: 0 0 10px rgba(0, 255, 65, 0.3); /* 네온 효과 */
        padding: 30px;
        max-width: 800px;
        width: 90%; /* 화면 너비에 따라 조절 */
        margin-bottom: 25px;
        text-align: center;
        animation: fadeIn 0.6s ease-in-out;
        box-sizing: border-box;
    }

    /* 섹션 제목 */
    h2 {
        color: var(--neon-green);
        text-transform: uppercase;
        border-bottom: 2px solid var(--dark-green);
        padding-bottom: 5px;
        margin-top: 0;
        margin-bottom: 20px;
        font-size: 1.4em; /* 제목 크기 조정 */
    }

    /* 하이라이트 텍스트 */
    .highlight {
        color: var(--neon-green);
        font-weight: bold;
    }

    /* 버튼 스타일 */
    .btn {
        display: inline-block;
        padding: 10px 18px; /* 패딩 조정 */
        border-radius: 4px;
        background: var(--dark-green);
        color: #000;
        font-weight: bold;
        text-decoration: none;
        border: none;
        cursor: pointer;
        transition: background 0.3s, box-shadow 0.3s;
        font-size: 0.95em; /* 버튼 폰트 크기 조정 */
        margin: 5px; /* 버튼 간 간격 */
    }

    .btn:hover {
        background: var(--neon-green);
        box-shadow: 0 0 8px var(--neon-green);
    }

    .btn-refresh {
        /* margin-right: 15px; */ /* 개별 마진 대신 gap 사용 */
    }

    /* 프로필 섹션 */
    .profile-info {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-bottom: 20px; /* 버튼과의 간격 */
    }

    .profile-text {
        margin-top: 15px;
        display: flex;
        flex-direction: column;
        align-items: center;
    }
     .profile-text h2 { /* 소환사 이름 스타일 */
        margin-bottom: 10px;
        font-size: 1.8em;
     }

    .profile-text p {
        margin: 8px 0; /* 문단 간격 조정 */
        font-size: 1.1em;
        color: #ccc; /* 약간 밝은 텍스트 */
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .rank-icon {
        width: 40px;
        height: 40px;
        vertical-align: middle;
    }


    /* 메시지 스타일 (오류, 경고) */
    .message {
        padding: 10px 15px;
        border-radius: 4px;
        margin-bottom: 15px;
        font-weight: bold;
    }
    .error-message {
        color: #fff;
        background-color: rgba(255, 77, 77, 0.7);
        border: 1px solid var(--error-red);
    }
    .warning-message {
         color: #000;
         background-color: rgba(241, 196, 15, 0.8);
         border: 1px solid var(--warning-yellow);
    }


    /* 통계 섹션 */
    .stats-container {
        display: flex;
        justify-content: space-around;
        gap: 20px;
        flex-wrap: wrap;
    }

    .stat-item {
        background: #2a2a2a;
        padding: 20px;
        border-radius: 4px;
        width: calc(50% - 20px);
        min-width: 200px;
        text-align: center;
        border-left: 4px solid var(--neon-green);
        box-sizing: border-box;
    }

    .stat-item p {
        margin: 0 0 8px 0;
        font-size: 0.9em;
        color: #aaa;
    }
     .stat-item h3 {
        margin: 5px 0 0 0;
        font-size: 1.3em;
        word-break: keep-all;
     }
     .stat-item img.stat-champ-img {
        width: 48px;
        height: 48px;
        border-radius: 4px;
        margin-bottom: 8px;
        border: 2px solid var(--dark-green);
        box-shadow: 0 0 5px rgba(0,0,0,0.5);
    }


    /* 매치 히스토리 섹션 */
    .match-list-card h2 {
        margin-bottom: 15px;
    }

    .match-card {
        background: #282828;
        padding: 15px 20px;
        text-align: left;
        margin-bottom: 15px;
        border: 1px solid rgba(0, 255, 65, 0.15);
        transition: transform 0.2s, box-shadow 0.2s;
        border-radius: 6px;
    }

    .match-card:hover {
        transform: translateY(-3px);
        box-shadow: 0 4px 18px rgba(0, 255, 65, 0.4);
    }

    .match-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px dashed #444;
        padding-bottom: 12px;
        margin-bottom: 12px;
    }

    .placement-badge {
        font-size: 1.6em;
        padding: 6px 12px;
        border-radius: 4px;
        color: #000;
        background-color: #f0f0f0;
        font-weight: bold;
        min-width: 40px;
        text-align: center;
    }

    .placement-1 { background-color: #FFD700; color: #000;
        box-shadow: 0 0 8px #FFD700;}
    .placement-2-4 { background-color: var(--neon-green); color: #000;
    }
    .placement-5-8 { background-color: #6c757d; color: #fff;
    }

    .match-details-right {
         display: flex;
         gap: 15px;
         font-size: 0.9em;
        color: #bbb;
    }

    .match-body {
        display: flex;
        flex-direction: column;
        gap: 12px;
    }


    .detail-label {
        color: #888;
        margin-right: 8px;
        font-weight: normal;
        display: block;
        margin-bottom: 6px;
    }

    .tag-container {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
    }

    .tag {
        padding: 3px 7px;
        border-radius: 3px;
        font-size: 0.8em;
        display: inline-flex;
        align-items: center;
        gap: 4px;
        line-height: 1.4;
        border: 1px solid;
    }

     .trait-tag {
        background: #333;
        color: #bbb;
        border-color: #555;
        position: relative;
        padding-left: 25px;
        min-height: 20px;
        display: inline-flex;
        align-items: center;
    }

     .trait-tag img {
         width: 20px;
         height: 20px;
         position: absolute;
         left: 2px;
         top: 50%;
         transform: translateY(-50%);
         border-radius: 2px;
    }

      .trait-tag .trait-name {
          margin-left: 2px;
      }

     .trait-style-1 { border-left: 3px solid #cd7f32; /* Bronze */
        padding-left: 25px;}
     .trait-style-2 { border-left: 3px solid #c0c0c0; /* Silver */
        padding-left: 25px;}
     .trait-style-3 { border-left: 3px solid #ffd700; /* Gold */
        padding-left: 25px;}
     .trait-style-4 { border-left: 3px solid #afeeee; /* Prismatic (Turquoise) */
        padding-left: 25px;}


    /* --- 아래 내용을 추가 --- */

    /* 유닛 컨테이너 (챔피언 목록) */
    .unit-container {
        gap: 10px 5px; /* 세로 간격, 가로 간격 */
    }

    /* 개별 유닛 표시 컨테이너 */
    .unit-display {
        display: inline-block; /* 가로 배치 */
        position: relative; /* 성급 위치 기준 */
        text-align: center;
        width: 48px; /* 챔피언 아이콘 크기에 맞춤 */
        vertical-align: top; /* 정렬 기준 */
        margin-bottom: 25px; /* 아이템 공간 확보 */
        border: 2px solid transparent; /* 비용별 테두리 공간 확보 */
        border-radius: 4px; /* 테두리 둥글게 */
        padding-bottom: 2px; /* 아이템과의 간격 */
        background: rgba(0,0,0,0.2); /* 약간의 배경 */
    }

    /* 성급(별) 스타일 */
    .stars {
        position: absolute;
        top: -14px; /* 아이콘 위로 배치 */
        left: 50%;
        transform: translateX(-50%);
        font-size: 0.8em;
        color: #ffd700; /* 금색 별 */
        text-shadow: 0 0 3px black; /* 가독성 위한 그림자 */
        white-space: nowrap; /* 별이 줄바꿈되지 않도록 */
        z-index: 2; /* 아이콘보다 위에 표시 */
    }
    /* 성급별 색상 (선택 사항) */
    .stars-1 { color: #cd7f32; } /* 동색 */
    .stars-2 { color: #c0c0c0; } /* 은색 */
    .stars-3 { color: #ffd700; } /* 금색 */


    /* 챔피언 아이콘 컨테이너 */
    .champ-icon-container {
        width: 48px;
        height: 48px;
        position: relative; /* 필요 시 내부 요소 배치 기준 */
        overflow: hidden; /* 테두리 밖 이미지 숨김 */
        border-radius: 2px; /* 아이콘 모서리 살짝 둥글게 */
    }

    .champ-icon {
        display: block; /* 이미지 하단 여백 제거 */
        width: 100%;
        height: 100%;
        border: none;
    }

    /* 챔피언 이미지 없을 때 텍스트 스타일 */
    .unit-name-fallback {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 100%;
        height: 100%;
        background: #333;
        font-size: 0.7em;
        color: #888;
        text-align: center;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        padding: 2px;
        box-sizing: border-box;
    }


    /* 아이템 아이콘 컨테이너 */
    .item-icons {
        position: absolute; /* 챔피언 아래에 고정 */
        bottom: -22px; /* 챔피언 아이콘 아래 배치 (아이템 크기 + 간격 고려) */
        left: 50%;
        transform: translateX(-50%);
        display: flex;
        justify-content: center;
        gap: 1px; /* 아이템 사이 간격 */
        width: 100%; /* 부모 너비에 맞춤 */
    }

    /* 아이템 아이콘 스타일 */
    .item-icon {
        width: 16px;
        height: 16px;
        border-radius: 2px;
        border: 1px solid #555; /* 아이템 테두리 */
        box-shadow: 0 0 2px rgba(0,0,0,0.5);
    }

    /* 비용별 테두리 색상 (기존 .unit-cost-X 와 병합 또는 별도 유지) */
    /* .unit-display 요소에 적용 */
    .unit-cost-1 { border-color: var(--cost1) !important; } /* important 추가 */
    .unit-cost-2 { border-color: var(--cost2) !important; }
    .unit-cost-3 { border-color: var(--cost3) !important; }
    .unit-cost-4 { border-color: var(--cost4) !important; }
    .unit-cost-5 { border-color: var(--cost5) !important; }

    /* 기존 .unit-tag 관련 CSS 제거 또는 수정 */
    .unit-tag { display: none; } /* 예시: 기존 태그 숨기기 */


    /* --- CSS 추가 끝 --- */


    /* 애니메이션 */
    @keyframes fadeIn {
        from { opacity: 0;
            transform: translateY(10px); }
        to { opacity: 1; transform: translateY(0);
        }
    }

    /* 모바일 반응형 (예시) */
    @media (max-width: 600px) {
        .card {
            width: 95%;
            padding: 20px;
        }
        .stats-container {
            flex-direction: column;
            align-items: center;
        }
        .stat-item {
            width: 90%;
            min-width: unset;
        }
        .match-header {
            flex-direction: column;
            align-items: flex-start;
            gap: 8px;
        }
        .match-details-right {
            width: 100%;
            justify-content: space-between;
        }
        .btn {
             width: calc(50% - 10px);
             box-sizing: border-box;
             margin: 5px;
        }
        .profile-text h2 {
            font-size: 1.5em;
        }
        .rank-icon {
            width: 32px;
            height: 32px;
        }
        /* 추가: 모바일에서 유닛 아이템 간격 등 조정 */
        .unit-display { width: 40px; margin-bottom: 20px;}
        .champ-icon-container { width: 40px; height: 40px; }
        .item-icons { bottom: -18px; }
        .item-icon { width: 13px; height: 13px;}
        .stars { top: -12px; font-size: 0.7em; }
    }

</style>
</head>

<body>

    <div class="card profile-card">
        <c:if test="${not empty summoner}">
            <div class="profile-info">
                <%-- <img src="..." alt="프로필 이미지" style="width: 80px; height: 80px; border-radius: 50%; border: 3px solid var(--neon-green);"> --%>
                
                <div class="profile-text">
                    <h2>${summoner.summonerName}#${summoner.summonerTag}</h2>
                    <p>
                        Tier: <span class="highlight">${tier}, ${rank}</span>
                    </p>
                    <p>
                        마지막 갱신: <span class="highlight">${summoner.lastUpdated}</span>
                    </p>
                </div>
            </div>
            
            <div style="margin-top: 20px;">
                <form action="userQuery.do" method="get" style="display:inline-block;">
                    <input type="hidden" name="summonerName" value="${summoner.summonerName}">
                    <input type="hidden" name="summonerTag" value="${summoner.summonerTag}">
                    <input type="hidden" name="refresh" value="true"> 
                    <button type="submit" class="btn btn-refresh">전적 갱신</button>
                </form>
                
                <a href="user.do" class="btn">다른 소환사 검색</a>
            </div>
        </c:if>
        <c:if test="${empty summoner}">
            <p>소환사 정보를 불러올 수 없습니다. 다시 검색해 주세요.</p>
            <a href="user.do" class="btn">검색으로 돌아가기</a>
        </c:if>
    </div>

    <c:if test="${not empty summoner && (not empty topChampion or not empty topSynergy)}">
        <div class="card">
            <h2>주요 통계</h2>
            <div class="stats-container">
                <c:choose>
                    <c:when test="${not empty topChampion}">

                         <div class="stat-item">
                            <p>가장 많이 사용한 챔피언</p>
                            <c:set var="topChampId" value="${topChampion}" />

                                <c:if test="${not empty championImageMap[topChampId]}">
                                 <img src="${championImageMap[topChampId]}" alt="${topChampId}"
                                      class="stat-champ-img unit-cost-${championCostMap[topChampId]}"> <%-- 비용별 테두리 --%>

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
                             <%-- 시너지 아이콘 (추후 추가 가능)
                             --%>
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

    <c:if test="${not empty matchHistory}">
        <div class="card match-list-card">
            <h2>최근 매치 기록 (${fn:length(matchHistory)} 게임)</h2>
            
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
                                    <%-- <div><span class="detail-label">입힌 피해량:</span> ${match.totalDamageToPlayers}</div> --%>

                                </div>
                            </div>

                            <div class="match-body">

                                <%-- 시너지 정보 --%>
                                <div>
                                    <span class="detail-label">시너지:</span>

                                    <div class="tag-container">
                                        <c:forEach var="trait" items="${match.traits}">

                                            <c:set var="synergyImageUrl" value="${synergyImageMap[trait]}" />
                                            <span class="tag trait-tag">

                                                <c:if test="${not empty synergyImageUrl}">
                                                    <img src="${synergyImageUrl}" alt="${trait}">

                                                </c:if>
                                                <span class="trait-name">${trait}</span>

                                             </span>
                                        </c:forEach>
                                    </div>

                                </div>

                                <%-- 최종 기물 정보 (수정됨) --%>
                                <div>

                                    <span class="detail-label">최종 기물:</span>
                                    <div class="tag-container unit-container"> <%-- unit-container 클래스 추가 --%>
                                        <c:forEach var="unitInfo" items="${match.units}">
                                            <c:set var="unitId" value="${unitInfo.championId}" />
                                            <c:set var="unitTier" value="${unitInfo.tier}" />
                                            <c:set var="unitItems" value="${unitInfo.items}" />
                                            <c:set var="unitCost" value="${championCostMap[unitId]}" /> <%-- 비용 가져오기 --%>

                                            <div class="unit-display unit-cost-${empty unitCost ? 1 : unitCost}"> <%-- 비용별 클래스 및 기본값 --%>
                                                <%-- 성급 (Stars) --%>
                                                <c:if test="${unitTier > 0}">
                                                    <span class="stars stars-${unitTier}">
                                                        <%-- 별 문자 사용 또는 CSS로 이미지 처리 --%>
                                                        <c:forEach begin="1" end="${unitTier}">⭐</c:forEach>
                                                    </span>
                                                </c:if>

                                                <%-- 챔피언 아이콘 --%>
                                                <div class="champ-icon-container">
                                                    <c:if test="${not empty championImageMap[unitId]}">
                                                        <img src="${championImageMap[unitId]}" alt="${unitId}" class="champ-icon">
                                                    </c:if>
                                                    <c:if test="${empty championImageMap[unitId]}">
                                                        <span class="unit-name-fallback">${unitId}</span> <%-- 이미지 없을 때 텍스트 --%>
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
                    <p class="highlight" style="text-align: center;">아직 매치 기록이 없습니다.
                        전적 갱신을 해주세요.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>
    <c:if test="${empty matchHistory && not empty summoner}">
        <div class="card">
             <p class="highlight">아직 매치 기록이 없습니다. 전적 갱신을 해주세요.</p>
        </div>
    </c:if>

</body>
</html>