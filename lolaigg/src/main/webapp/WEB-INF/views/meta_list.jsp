<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TFTGG - 추천 메타</title>
<%-- user_result.jsp와 동일한 CSS 파일을 사용합니다 --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/user_result_style.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/meta_list_style.css">
</head>
<body>

    <%-- 페이지 제목 카드 --%>
    <div class="card profile-card">
        <h2>추천 메타 덱</h2>
        <p class="highlight">현재 시즌의 강력한 덱들을 확인하세요.</p>
        
        <%-- 오류 또는 경고 메시지 표시 --%>
        <c:if test="${not empty errorMessage}">
            <p class="message error-message">${errorMessage}</p>
        </c:if>
        <c:if test="${not empty warningMessage}">
            <p class="message warning-message">${warningMessage}</p>
        </c:if>
    </div>

    <%-- ▼▼▼▼▼ 챔피언 필터 섹션 (수정됨) ▼▼▼▼▼ --%>
    <div class="card champion-filter-card">
        <h2>챔피언 필터</h2>
        <%-- "모두 보기" 버튼을 포함한 filter-actions div 삭제 --%>
        
        <%-- 챔피언 아이콘이 코스트별로 추가될 컨테이너 --%>
        <div id="champion-filter-bar">
            <%-- 이 영역은 JavaScript에 의해 동적으로 채워집니다. --%>
        </div>
    </div>
    <%-- ▲▲▲▲▲ 챔피언 필터 섹션 추가 끝 ▲▲▲▲▲ --%>

    <%-- 메타 덱 목록 --%>
    <c:if test="${empty errorMessage}">
        <%-- user_result.jsp의 match-list-card 스타일 재사용 --%>
        <div class="card match-list-card">
 
            <c:choose>
                <c:when test="${not empty metaDecks}">
                      <%-- DB에서 가져온 덱 목록을 반복 --%>
                     <c:forEach var="deck" items="${metaDecks}">
                
                         <%-- === 새로운 덱 행 구조 === --%>
                         <div class="meta-deck-row">
                            
                            <%-- 1. 덱 정보 (이름, 시너지) --%>
                             <div class="deck-info-section">
                                <%-- 덱 이름 및 HOT 뱃지 --%>
                                <div class="deck-name-container">
                                     <c:if test="${deck.hot}">
                                        <span class="deck-hot-badge">HOT</span> 
                                    </c:if>
                                     <span class="deck-name">${deck.deckName}</span> 
                                </div>
                                <%-- 시너지 아이콘 --%>
                                <div class="deck-traits-container"> 
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
                             </div>

 
                            <%-- 2. 기물 정보 (user_result.jsp의 구조 재사용) --%>
                            <div class="deck-units-section">
                                 <div class="tag-container unit-container"> 
                                    <c:forEach var="unitInfo" items="${deck.units}">
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


      
  
                             <%-- 3. 비용 및 링크 --%>
                             <div class="deck-actions-section"> 
                               <span class="deck-total-cost">${deck.totalCost}</span> 
 
                               <button class="btn copy-button" data-code="${deck.teamCode}">복사</button> 
    
                                <%-- ▼▼▼▼▼ 수정된 부분 ▼▼▼▼▼ --%>
                                 <%-- 기존 guideLink 대신 guide.do로 연결합니다. --%>
                                <a href="guide.do?deckId=${deck.deckId}" class="btn">배치/공략</a> 
                                <%-- ▲▲▲▲▲ 수정 끝 ▲▲▲▲▲ --%>
                            
                          </div>

                        </div> <%-- === 덱 행 구조 끝 === --%>
  
                    </c:forEach>
                 </c:when>
                 <c:otherwise>
                     <p class="highlight" style="text-align: center;">등록된 추천 덱이 없습니다.</p>
                </c:otherwise>
             </c:choose>
        </div>
    </c:if>

    <%-- 클립보드 복사 스크립트 (기존과 동일) --%>
     <script>
        document.querySelectorAll('.copy-button').forEach(button => {
            button.addEventListener('click', () => {
                 const codeToCopy = button.getAttribute('data-code');
                if (!codeToCopy) {
                    alert('복사할 팀 코드가 없습니다.');
                    return;
                }
                 navigator.clipboard.writeText(codeToCopy).then(() => {
                    alert('팀 코드가 클립보드에 복사되었습니다!');
                 }).catch(err => {
                    console.error('클립보드 복사 실패:', err);
                     alert('복사에 실패했습니다.');
                });
             });
        });
    </script>
    
    <%-- ▼▼▼▼▼ 챔피언 필터링 스크립트 (수정됨) ▼▼▼▼▼ --%>
    <script>
        // --- 1. JSTL 데이터를 JavaScript 객체로 변환 ---
        const championImageMap = {
            <c:forEach var="entry" items="${championImageMap}">
                "${entry.key}": "${entry.value}",
            </c:forEach>
        };
        const championCostMap = {
            <c:forEach var="entry" items="${championCostMap}">
                "${entry.key}": ${entry.value}, 
            </c:forEach>
        };

        // --- 2. 챔피언 필터 바(Bar) 동적 생성 ---
        document.addEventListener('DOMContentLoaded', () => {
            const filterBar = document.getElementById('champion-filter-bar');
            if (!filterBar || !championCostMap || !championImageMap) return;

            // 챔피언을 코스트별로 그룹화
            const championsByCost = { 1: [], 2: [], 3: [], 4: [], 5: [] };

            Object.keys(championCostMap)
                .sort() 
                .forEach(champId => {
                    const cost = championCostMap[champId];
                    if (championsByCost[cost]) {
                         championsByCost[cost].push(champId);
                    }
                });

            // 필터 바 HTML 생성
            for (let cost = 1; cost <= 5; cost++) {
                 if (championsByCost[cost].length > 0) {
                    const costRow = document.createElement('div');
                    costRow.className = 'filter-cost-row';
                    
                    const costLabel = document.createElement('div');
                    costLabel.className = 'filter-cost-label unit-cost-' + cost; 
                    costLabel.textContent = 'Cost ' + cost;
                    costRow.appendChild(costLabel);

                    const iconsContainer = document.createElement('div');
                    iconsContainer.className = 'filter-icons-container';
                    
                    // 챔피언 아이콘 생성
                    championsByCost[cost].forEach(champId => {
                        const imgUrl = championImageMap[champId];
                        if (imgUrl) {
                      
                            // ▼▼▼ [수정] 래퍼 DIV 생성 ▼▼▼
                            const container = document.createElement('div');
                            container.className = 'filter-champ-icon-container unit-cost-' + cost;
                            container.dataset.championId = champId; 
                            
                            const img = document.createElement('img');
                            img.src = imgUrl;
                            img.alt = champId;
                            img.title = champId; 
                            img.className = 'filter-champ-icon'; // 이미지는 이 클래스만 가짐
                            
                            container.appendChild(img); // 이미지를 래퍼에 추가
                            // ▲▲▲ [수정] 래퍼 DIV 끝 ▲▲▲
                      
                             // ▼▼▼ 3-1. (수정된) 필터 아이콘 클릭 이벤트 리스너 (container에 추가) ▼▼▼
                            container.addEventListener('click', (event) => {
                                 const clickedContainer = event.currentTarget;
                                const champId = clickedContainer.dataset.championId;

                                // 이미 선택된 아이콘인지 확인
                                 if (clickedContainer.classList.contains('selected')) {
                                    // 이미 선택됨 -> 초기화 (모두 보기)
                                     showAllDecks();
                                 } else {
                                    // 새로 선택됨 -> 필터링
                                    filterDecksByChampion(champId);
                                 }
                            });
                            // ▲▲▲ 수정 끝 ▲▲▲
                            
                            iconsContainer.appendChild(container); // 래퍼(div)를 아이콘 컨테이너에 추가
                         }
                    });
                    costRow.appendChild(iconsContainer);
                    filterBar.appendChild(costRow);
                }
            }

            // (삭제) "모두 보기" 버튼 이벤트 리스너 삭제
        });

        // --- 3. 필터링 함수 (기존과 동일) ---
        function filterDecksByChampion(selectedChampId) {
            
            // 1. 덱 목록 필터링
            const allDecks = document.querySelectorAll('.meta-deck-row');
            allDecks.forEach(deck => {
                const unitsInDeck = deck.querySelectorAll('.champ-icon');
                const found = Array.from(unitsInDeck).some(unitImg => unitImg.alt === selectedChampId);
                deck.style.display = found ? 'flex' : 'none';
            });

            // ▼▼▼ 2. (수정됨) 챔피언 필터 바 아이콘 효과 (컨테이너 기준) ▼▼▼
            const allFilterIcons = document.querySelectorAll('.filter-champ-icon-container');
            allFilterIcons.forEach(icon => {
                if (icon.dataset.championId === selectedChampId) {
                    icon.classList.add('selected');
                    icon.classList.remove('dimmed');
                } else {
                    icon.classList.remove('selected');
                    icon.classList.add('dimmed');
                }
            });
            // ▲▲▲ 수정 끝 ▲▲▲
        }

        // --- 4. 모두 보기 (필터 초기화) 함수 (기존과 동일) ---
        function showAllDecks() {
            
            // 1. 모든 덱 표시
            const allDecks = document.querySelectorAll('.meta-deck-row');
            allDecks.forEach(deck => {
                deck.style.display = 'flex';
            });

            // ▼▼▼ 2. (수정됨) 챔피언 필터 바 아이콘 효과 초기화 (컨테이너 기준) ▼▼▼
            const allFilterIcons = document.querySelectorAll('.filter-champ-icon-container');
            allFilterIcons.forEach(icon => {
                icon.classList.remove('selected');
                icon.classList.remove('dimmed');
            });
            // ▲▲▲ 수정 끝 ▲▲▲
        }
    </script>
    <%-- ▲▲▲▲▲ 스크립트 추가 끝 ▲▲▲▲▲ --%>

</body>
</html>