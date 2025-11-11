<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TFTGG - <c:out value="${deck.deckName}" /> 공략</title>
<%-- ▼▼▼▼▼ 수정된 CSS 링크 ▼▼▼▼▼ --%>
<%-- user_result_style.css (공통 스타일: 카드, 유닛, 특성 등) --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/user_result_style.css">
<%-- guide_detail_style.css (이 페이지 전용 스타일: 배치판, 설명) --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/guide_detail_style.css">
<%-- ▲▲▲▲▲ 수정 끝 ▲▲▲▲▲ --%>
</head>
<body>

    <%-- 덱 제목 카드 --%>
    <div class="card profile-card" style="max-width: 1100px;">
        <c:if test="${not empty deck}">
            <h2><c:out value="${deck.deckName}" /></h2>
            <%-- 덱 이름 밑에 시너지 아이콘 표시 --%>
            <div class="deck-traits-container" style="justify-content: center; margin-top: 15px;">
                 <c:forEach var="traitInfo" items="${deck.traits}">
                    <c:set var="synergyName" value="${traitInfo.name}" />
                    <c:set var="synergyStyle" value="${traitInfo.style}" />
                    <c:set var="synergyImageUrl" value="${synergyImageMap[synergyName]}" />
                    <c:choose>
                        <c:when test="${synergyStyle == 1}"><c:set var="styleClass" value="trait-style-1" /></c:when>
                        <c:when test="${synergyStyle == 2}"><c:set var="styleClass" value="trait-style-2" /></c:when>
                        <c:when test="${synergyStyle == 3}"><c:set var="styleClass" value="trait-unique" /></c:when>
                        <c:when test="${synergyStyle == 4}"><c:set var="styleClass" value="trait-style-3" /></c:when>
                        <c:when test="${synergyStyle == 5}"><c:set var="styleClass" value="trait-style-4" /></c:when>
                        <c:otherwise><c:set var="styleClass" value="" /></c:otherwise>
                     </c:choose>
                     <span class="tag trait-tag ${styleClass}">
                        <c:if test="${not empty synergyImageUrl}">
                            <img src="${synergyImageUrl}" alt="${synergyName}">
                        </c:if>
                     </span>
                </c:forEach>
             </div>
        </c:if>

        <%-- 오류 또는 경고 메시지 표시 --%>
        <c:if test="${not empty errorMessage}">
            <p class="message error-message">${errorMessage}</p>
        </c:if>
        <c:if test="${not empty warningMessage}">
            <p class="message warning-message">${warningMessage}</p>
        </c:if>
        
        <div style="margin-top: 20px;">
             <a href="meta.do" class="btn">목록으로 돌아가기</a>
        </div>
    </div>

    <%-- 덱 상세 정보 (배치, 유닛, 설명) --%>
    <c:if test="${empty errorMessage and not empty deck}">
        <div class="card match-list-card" style="max-width: 1100px;">
            
            <%-- ▼▼▼▼▼ 동적 덱 배치 섹션 (수정 없음) ▼▼▼▼▼ --%>
            <div class="guide-layout-container">
                <h2>추천 배치</h2>
                
                <div class="guide-layout-board">
                    
                    <%-- 1. 빈 6각형 그리드 (배경) --%>
                    <div class="hex-grid-layer">
                        <c:forEach var="row" begin="1" end="4">
                            <div class="hex-row row-${row}">
                                <c:forEach var="col" begin="1" end="7">
                                    <div class="hex-cell">
                                        <div class="hex-cell-inner"></div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:forEach>
                    </div>

                    <%-- 2. 챔피언 배치 (아이콘) --%>
                    <div class="unit-placement-layer">
                        <c:forEach var="unit" items="${deck.units}">
                            <%-- DB에 위치 정보(row, col)가 있는 유닛만 표시 --%>
                            <c:if test="${unit.hexRow > 0 and unit.hexCol > 0}">
                                <c:set var="unitId" value="${unit.championId}" />
                                <c:set var="unitTier" value="${unit.tier}" />
                                <c:set var="unitItems" value="${unit.items}" />
                                <c:set var="unitCost" value="${championCostMap[unitId]}" />

                                <div class="placed-unit unit-display unit-cost-${empty unitCost ? 1 : unitCost} <c:if test='${unit.hexRow % 2 == 0}'>even-row</c:if>" 
                                     style="--row: ${unit.hexRow}; --col: ${unit.hexCol};">
                                    
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
                                </div> <%-- // placed-unit --%>
                            </c:if>
                        </c:forEach>
                    </div> <%-- // unit-placement-layer --%>
                    
                </div> <%-- // guide-layout-board --%>
            </div>
            <%-- ▲▲▲▲▲ 동적 배치 섹션 끝 ▲▲▲▲▲ --%>
            
            <%-- 3. 부연 설명 --%>
            <div class="guide-section-container guide-explanation">
                <h2>공략 설명</h2>
                <p>
                    (여기에 나중에 부연 설명을 추가할 예정입니다.)
                </p>
                <p>
                    이 덱은 초반 빌드업이 중요하며, 7레벨 리롤을 통해 핵심 3코스트 기물을 찾거나<br>
                    8레벨에 도달하여 4코스트 2성을 완성하는 것을 목표로 합니다.
                </p>
                </div>
            
        </div>
    </c:if> 

</body>
</html>