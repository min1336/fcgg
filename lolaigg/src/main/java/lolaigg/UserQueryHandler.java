package lolaigg;

import java.io.IOException;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections; // Collections import 추가
import java.util.Comparator; // Comparator import 추가
import java.util.List;
import java.util.Map; // Map import 추가

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException; // JSONException import 추가
import org.json.JSONObject;

public class UserQueryHandler implements CommandHandler {
	private Api api;

	public UserQueryHandler() {
		api = new Api();
	}

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException, ClassNotFoundException {

		String summonerName = request.getParameter("summonerName");
		String summonerTag = request.getParameter("summonerTag");

		// 입력값 유효성 검사 (간단하게)
		if (summonerName == null || summonerName.trim().isEmpty() || summonerTag == null || summonerTag.trim().isEmpty()) {
			request.setAttribute("errorMessage", "소환사 이름과 태그를 모두 입력해주세요.");
			return "user_form";
		}
		summonerName = summonerName.trim();
		summonerTag = summonerTag.trim();


		boolean isRefresh = "true".equals(request.getParameter("refresh"));

		// --- 데이터 드래곤 정보 미리 가져오기 ---
        Map<String, String> championImageMap = Collections.emptyMap(); // 초기화
        Map<String, String> itemImageMap = Collections.emptyMap();
        Map<String, Integer> championCostMap = Collections.emptyMap();
        Map<String, String> synergyImageMap = Collections.emptyMap();
        Map<String, String> rankImageMap = Collections.emptyMap(); // **** 추가: 랭크 이미지 맵 ****

        try {
            championImageMap = api.getChampionImageMap();
            itemImageMap = api.getItemImageMap();
            championCostMap = api.getChampionCostMap();
            synergyImageMap = api.getSynergyImageMap();
            rankImageMap = api.getRankImageMap(); // **** 추가: 랭크 이미지 맵 가져오기 ****
        } catch (IOException e) {
            System.err.println("데이터 드래곤 API 호출 중 오류 발생(Handler): " + e.getMessage()); // 로그 유지 권장
            request.setAttribute("warningMessage", "챔피언/아이템/시너지/랭크 이미지 정보를 불러오는데 실패했습니다."); // 메시지 수정
        }
        // JSP로 전달하기 위해 request에 추가
        request.setAttribute("championImageMap", championImageMap);
        request.setAttribute("itemImageMap", itemImageMap);
        request.setAttribute("championCostMap", championCostMap);
        request.setAttribute("synergyImageMap", synergyImageMap);
        request.setAttribute("rankImageMap", rankImageMap); // **** 추가: 랭크 이미지 맵 전달 ****
        // --- 데이터 드래곤 정보 가져오기 끝 ---

		// DB에 저장된 소환사 정보 찾기
	    Summoner summoner = null;
        try {
            summoner = api.findSummoner(summonerName, summonerTag);
        } catch (SQLException | ClassNotFoundException e) {
             System.err.println("DB에서 소환사 정보 조회 중 오류: " + e.getMessage()); // 로그 유지 권장
             request.setAttribute("errorMessage", "데이터베이스 조회 중 오류가 발생했습니다.");
             return "user_form"; // DB 오류 시 폼으로
        }

        // 캐시 확인: DB에 있고, 새로고침 요청이 아니며, 데이터가 만료되지 않았는지
        boolean useCache = summoner != null && !isRefresh && !isExpired(summoner.getLastUpdated());

	    if (!useCache) {
            System.out.println("캐시 사용 안함 또는 만료됨. Riot API 호출 시작: " + summonerName + "#" + summonerTag); // 로그 유지 권장
	    	// Riot API 호출하여 최신 정보 가져오기
	    	JSONObject apiResult = null;
            try {
                 apiResult = api.getSummonerByName(summonerName, summonerTag);
                 // apiResult가 null이거나 puuid가 없는 경우 처리 (Api.java에서 IOException 발생 가능)
            } catch (IOException e) {
                 System.err.println("Riot API(소환사 정보) 호출 중 오류 발생: " + e.getMessage()); // 로그 유지 권장
                 request.setAttribute("errorMessage", "소환사 정보를 찾을 수 없습니다. (API 오류)");
                 return "user_form"; // 오류 발생 시 폼으로
            }

            // DB 작업 (Insert or Update)
            try {
                if (summoner == null) {
                    System.out.println("DB에 소환사 정보 없음. Insert 시도."); // 로그 유지 권장
                    // apiResult가 null이 아닐 때만 insert 시도
                    if (apiResult != null && apiResult.has("puuid")) {
                         api.insert(summonerName, summonerTag, apiResult.toString());
                    } else {
                         // apiResult가 잘못되었으면 에러 처리
                         throw new IOException("API 결과에서 PUUID를 찾을 수 없어 DB에 삽입할 수 없습니다.");
                    }
                } else {
                    System.out.println("DB에 소환사 정보 있음. Update 시도."); // 로그 유지 권장
                    // apiResult가 null이 아닐 때만 update 시도
                     if (apiResult != null) {
                          api.update(summoner.getId(), apiResult.toString());
                     } else {
                          // apiResult가 잘못되었으면 업데이트 건너뛰거나 에러 처리
                          System.err.println("API 결과가 없어 DB 업데이트를 건너<0xEB><0x9A><0x81>니다."); // 로그 유지 권장
                     }
                }
                // DB 작업 후 최신 정보 다시 조회
                summoner = api.findSummoner(summonerName, summonerTag);
            } catch (SQLException | ClassNotFoundException | IOException | JSONException e ) { // JSONException 추가 (apiResult.has)
                System.err.println("DB Insert/Update 중 오류 발생: " + e.getMessage()); // 로그 유지 권장
                request.setAttribute("errorMessage", "데이터베이스 저장 중 오류가 발생했습니다.");
                return "user_form";
            }

            // DB 작업 후에도 summoner 객체를 못 가져오면 심각한 오류
            if (summoner == null) {
                 System.err.println("DB 작업 후 소환사 정보 로드 실패."); // 로그 유지 권장
                 request.setAttribute("errorMessage", "데이터 처리 중 심각한 오류가 발생했습니다.");
                 return "user_form";
            }
            System.out.println("Riot API 호출 및 DB 업데이트 완료."); // 로그 유지 권장
		} else {
             System.out.println("캐시된 소환사 정보 사용: " + summonerName + "#" + summonerTag); // 로그 유지 권장
        }

        // 이 시점에서 summoner 객체는 유효해야 함
	    request.setAttribute("summoner", summoner);

	    String puuid = summoner.getPuuid();
        if (puuid == null) {
            System.err.println("소환사 PUUID를 가져올 수 없습니다."); // 로그 유지 권장
            request.setAttribute("errorMessage", "소환사 고유 ID(PUUID)를 가져올 수 없습니다.");
            return "user_result"; // 정보는 표시하되 매치 이력은 못가져옴
        }

	    // 매치 상세 정보를 담을 리스트
	    List<SummonerMatch> matchHistory = new ArrayList<>();

	    // 매치 데이터 캐시 확인: DB 데이터가 충분하고(30개 이상), 캐시를 사용하는 경우(useCache)
        boolean useMatchCache = false; // 기본값 false
        try { // DB 접근은 예외 처리 필요
	        useMatchCache = useCache && api.hasRecentMatchData(puuid);
        } catch (SQLException | ClassNotFoundException e) {
             System.err.println("DB에서 hasRecentMatchData 확인 중 오류: " + e.getMessage()); // 로그 유지 권장
             request.setAttribute("warningMessage", (request.getAttribute("warningMessage") != null ? request.getAttribute("warningMessage") + " " : "") + "매치 캐시 확인 중 오류가 발생했습니다.");
             // 캐시 확인 실패 시 API 호출하도록 유도 (useMatchCache = false 유지)
        }

        // [수정] 랭크 게임만 조회하기 위해 DB 캐시 사용 로직 비활성화
        useMatchCache = false; 
        System.out.println("랭크 게임 필터링을 위해 항상 API를 호출합니다. (useMatchCache=false)");


	    if (useMatchCache) {
            // [수정] 이 로직은 이제 실행되지 않지만, 만약을 위해 남겨둡니다.
            System.out.println("캐시된 매치 데이터 사용."); // 로그 유지 권장
			 // 즐겨찾기 정보는 이미 summoner 객체에 로드됨
			 request.setAttribute("topChampion", summoner.getLovecham());
			 request.setAttribute("topSynergy",  summoner.getLovesyn());

             try {
			    matchHistory = api.getMatchHistoryFromDB(puuid); // DB에서 매치 기록 가져오기
                for (SummonerMatch match : matchHistory) {
                    if (match.getTraits() == null) { 
                        match.setTraits(new ArrayList<>());
                    }
                }
             } catch (SQLException | ClassNotFoundException e) {
                 System.err.println("DB에서 매치 히스토리 조회 중 오류 (Cache Path): " + e.getMessage()); // 로그 유지 권장
                 request.setAttribute("warningMessage", (request.getAttribute("warningMessage") != null ? request.getAttribute("warningMessage") + " " : "") + "캐시된 매치 기록 로딩 중 오류.");
             }
		} else {
            System.out.println("매치 히스토리 API 호출 시작: " + puuid); // 로그 유지 권장
            try {
                // 30게임 matchId 가져오기 (Api.java에서 type=ranked로 이미 필터링됨)
                List<String> matchIds = api.getMatchIds(puuid);

                for (String matchId : matchIds) {
                    System.out.println("매치 상세 정보 처리 중: " + matchId); // 로그 유지 권장
                    JSONObject matchDetail = api.getMatchDetail(matchId);

                    // [수정] 11/4 랭크 게임 (Queue 1100) 인지 확인
                    // info 객체 존재 여부 및 queueId 확인
                    JSONObject infoObject = matchDetail.optJSONObject("info");
                    if (infoObject == null || infoObject.optInt("queueId", 0) != 1100) { 
                        // 1100이 TFT 랭크 솔로 큐 ID입니다. (더블업 랭크는 1150)
                        System.err.println("랭크 게임(Queue: 1100)이 아니거나 info 객체 오류. 건너<0xEB><0x9A><0x81>니다: " + matchId + " (Queue: " + (infoObject != null ? infoObject.optInt("queueId", 0) : "N/A") + ")");
                        continue; // 랭크 솔로 큐가 아니면 이 매치를 스킵
                    }
                    
                    // [기존 코드 수정] infoObject가 null이 아님이 보장되었으므로 .has("participants") 체크만 수행
                     if (!infoObject.has("participants")) {
                         System.err.println("매치 상세 정보 API 응답에 'participants' 배열이 없습니다: " + matchId); // 로그 유지 권장
                         continue;
                    }

                    JSONArray participants = infoObject.getJSONArray("participants");

                    SummonerMatch currentMatch = new SummonerMatch();
                    currentMatch.setMatchId(matchId);
                    boolean foundParticipant = false; // 해당 puuid 찾았는지 플래그

                    for (int i = 0; i < participants.length(); i++) {
                        JSONObject p = participants.getJSONObject(i);

                        // puuid가 없는 participant 건너뛰기 (오류 방지)
                        if (!p.has("puuid")) continue;

                        if (p.getString("puuid").equals(puuid)) {
                            foundParticipant = true;
                            // DB에 저장 및 Match 객체에 정보 저장
                            int placement = p.optInt("placement", 0); // 기본값 0
                            int level = p.optInt("level", 0);
                            int totalDamage = p.optInt("total_damage_to_players", 0);
                            int goldLeft = p.optInt("gold_left", 0);

                            // 매치 기본 정보 DB 저장 (트랜잭션 고려 가능)
                            // [수정] 랭크 게임(1100)만 DB에 저장됨
                            api.insertMatchInfo(matchId, puuid, placement, level, goldLeft, totalDamage);

                            currentMatch.setPlacement(placement);
                            currentMatch.setLevel(level);
                            currentMatch.setTotalDamageToPlayers(totalDamage);
                            currentMatch.setGoldLeft(goldLeft);

                            // units 처리 (UnitInfo 객체 사용)
                            JSONArray unitsArray = p.optJSONArray("units");
                            if (unitsArray != null) {
                                for (int j = 0; j < unitsArray.length(); j++) {
                                    JSONObject unit = unitsArray.getJSONObject(j);
                                    String champ = unit.optString("character_id", "");
                                    if (!champ.isEmpty()) {
                                        UnitInfo unitInfo = new UnitInfo(); // UnitInfo 객체 생성
                                        String cleanedChamp = champ.startsWith("TFT") ? champ.substring(champ.indexOf("_") + 1) : champ;
                                        unitInfo.setChampionId(cleanedChamp);

                                        // 성급(tier) 정보 추출
                                        int tier = unit.optInt("tier", 0); // 기본값 0
                                        unitInfo.setTier(tier);

                                        // 아이템 정보 추출
                                        JSONArray itemNames = unit.optJSONArray("itemNames"); // Riot API 최신 버전 기준 필드명
                                        if (itemNames == null) {
                                             // 구 버전 API 필드명 시도 (itemNames 대신 items 사용했을 경우)
                                             itemNames = unit.optJSONArray("items");
                                        }

                                        if (itemNames != null) {
                                            List<String> items = new ArrayList<>();
                                            for (int k = 0; k < itemNames.length(); k++) {
                                                items.add(itemNames.optString(k));
                                            }
                                            unitInfo.setItems(items);
                                        }

                                        currentMatch.getUnits().add(unitInfo); // String 대신 UnitInfo 객체 추가

                                        // 챔피언 정보 DB 저장
                                        api.insertMatchChampion(matchId, puuid, champ); // API 원본 ID 전달
                                    }
                                }
                            } // units 처리 끝

                            // --- 수정된 시너지 처리 ---
                            JSONArray traits = p.optJSONArray("traits");
                             if (traits != null) {
                                for (int j = 0; j < traits.length(); j++) {
                                    JSONObject trait = traits.getJSONObject(j);
                                    String synName = trait.optString("name", "");
                                    // int numUnits = trait.optInt("num_units", 0); // 현재 표시에는 사용 안 함
                                    // --- 수정된 주석 ---
                                    int style = trait.optInt("style", 0); // 0: 비활성, 1: 브론즈, 2: 실버, 3: 고유, 4: 골드, 5: 프리즘
                                    // --- 수정 끝 ---

                                    if (!synName.isEmpty() && style > 0) { // 활성화된(style > 0) 시너지만 처리
                                        TraitInfo traitInfo = new TraitInfo(); // TraitInfo 객체 생성
                                        traitInfo.setName(synName);
                                        traitInfo.setStyle(style); // 등급 정보 저장
                                        currentMatch.getTraits().add(traitInfo); // 객체를 리스트에 추가

                                        // 시너지 정보 DB 저장 (현재 style 정보는 DB에 저장하지 않음)
                                        api.insertMatchSynergy(matchId, puuid, synName);
                                    }
                                }
                            }
                            // --- 시너지 처리 끝 ---

                            // --- *** 시너지 정렬 로직 추가 *** ---
                            if (currentMatch.getTraits() != null && !currentMatch.getTraits().isEmpty()) {
                                Collections.sort(currentMatch.getTraits(), new Comparator<TraitInfo>() {
                                    @Override
                                    public int compare(TraitInfo t1, TraitInfo t2) {
                                        // style 값 매핑 (5 > 3 > 4 > 2 > 1 순서)
                                        int style1Mapped = mapStyleForSort(t1.getStyle());
                                        int style2Mapped = mapStyleForSort(t2.getStyle());
                                        // 내림차순 정렬
                                        return Integer.compare(style2Mapped, style1Mapped);
                                    }

                                    // 정렬 순서를 위한 style 값 매핑 함수
                                    private int mapStyleForSort(int style) {
                                        switch (style) {
                                            case 5: return 5; // Prism
                                            case 3: return 4; // Unique (Gold보다 높게 취급)
                                            case 4: return 3; // Gold
                                            case 2: return 2; // Silver
                                            case 1: return 1; // Bronze
                                            default: return 0; // Others (including inactive)
                                        }
                                    }
                                });
                            }
                            // --- *** 시너지 정렬 로직 끝 *** ---


                            break; // 해당 소환사 찾았으므로 participants 루프 종료
                        }
                    } // participants 루프 끝

                    if(foundParticipant) {
                       matchHistory.add(currentMatch); // 참가자 정보를 찾은 경우에만 추가
                    } else {
                         System.err.println("매치 " + matchId + "에서 puuid " + puuid + "를 찾지 못했습니다."); // 로그 유지 권장
                    }

                } // matchIds 루프 끝

                System.out.println("매치 히스토리 API 호출 및 DB 저장 완료."); // 로그 유지 권장

                // 매치 API 호출 후 즐겨찾기 정보 업데이트
                String topChampion = api.getTopChampion(puuid);
                String topSynergy = api.getTopSynergy(puuid);
                api.updateSummonerLove(puuid, topChampion, topSynergy);
                request.setAttribute("topChampion", topChampion);
                request.setAttribute("topSynergy", topSynergy);

            } catch (IOException | SQLException | ClassNotFoundException | JSONException e) { // JSONException 추가
                System.err.println("매치 히스토리 처리 중 오류 발생: " + e.getMessage()); // 로그 유지 권장
                e.printStackTrace(); // 개발 중 상세 오류 확인용
                request.setAttribute("warningMessage", (request.getAttribute("warningMessage") != null ? request.getAttribute("warningMessage") + " " : "") + "매치 기록을 가져오는 중 오류가 발생했습니다.");
                // [수정] API 오류가 발생해도 DB에서 가져오지 않습니다 (어차피 랭크 필터링이 안됨).
                // 즐겨찾기 정보만이라도 로드
                request.setAttribute("topChampion", summoner.getLovecham());
                request.setAttribute("topSynergy", summoner.getLovesyn());
            }
	    }

	    // 랭크 정보 조회
	    JSONArray rankArray = null;
	    try {
	        rankArray = api.getRankDetail(puuid);
	    } catch (IOException e) { // IOException 처리
	        System.err.println("랭크 API 호출 실패: " + e.getMessage()); // 로그 유지 권장
            request.setAttribute("warningMessage", (request.getAttribute("warningMessage") != null ? request.getAttribute("warningMessage") + " " : "") + "랭크 정보를 가져오지 못했습니다.");
	    } catch (JSONException e) { // JSON 파싱 오류 처리
             System.err.println("랭크 정보 파싱 오류: " + e.getMessage()); // 로그 유지 권장
             request.setAttribute("warningMessage", (request.getAttribute("warningMessage") != null ? request.getAttribute("warningMessage") + " " : "") + "랭크 정보 처리 중 오류가 발생했습니다.");
        }

	    String tier = "UNRANKED";
	    String rank = "";

	    if (rankArray != null) {
	        for (int i = 0; i < rankArray.length(); i++) {
	            try {
	                JSONObject apiRank = rankArray.getJSONObject(i);
	                // TFT 랭크 큐 타입 확인 (예: "RANKED_TFT") - 정확한 값 확인 필요
	                if ("RANKED_TFT".equals(apiRank.optString("queueType"))) {
	                    tier = apiRank.getString("tier"); // 예: "GOLD"
	                    rank = apiRank.getString("rank"); // 예: "I", "II"
	                    break; // TFT 랭크 찾으면 중단
	                }
	            } catch (org.json.JSONException e) {
	                System.err.println("랭크 JSON 객체 파싱 중 오류 발생 (반복문 내): " + e.getMessage()); // 로그 유지 권장
	                // 개별 랭크 정보 파싱 오류는 무시하고 다음 것 처리
	            }
	        }
	    }

	    request.setAttribute("tier", tier);
	    request.setAttribute("rank", rank);

	    // 매치 상세 정보 리스트를 JSP로 전달
	    request.setAttribute("matchHistory", matchHistory);

		return "user_result"; // 최종 결과 페이지로 포워딩
	}

	/**
	 * 마지막 업데이트 시간으로부터 지정된 시간(분)이 지났는지 확인합니다.
	 * @param lastUpdated 마지막 업데이트 Timestamp 객체 (null일 수 있음)
	 * @return 만료되었으면 true, 아니면 false
	 */
	private boolean isExpired(Timestamp lastUpdated) {
        if (lastUpdated == null) {
            return true; // 마지막 업데이트 시간이 없으면 만료된 것으로 간주
        }
        // 캐시 만료 시간 (분 단위) - 필요에 따라 조정
        final long EXPIRATION_MINUTE = 2; // 예: 2분
    	long diffMillis = System.currentTimeMillis() - lastUpdated.getTime();
    	long diffMinutes = diffMillis / (60 * 1000);
    	return diffMinutes > EXPIRATION_MINUTE; // 지정된 시간(분) 초과 시 만료
    }

}