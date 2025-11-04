package lolaigg;

import java.io.IOException;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException; // JSONException import 추가

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
		
		boolean isRefresh = "true".equals(request.getParameter("refresh"));
		
		Map<String, String> championImageMap = Collections.emptyMap(); // 초기화
        Map<String, String> itemImageMap = Collections.emptyMap();
        Map<String, Integer> championCostMap = Collections.emptyMap();
        Map<String, String> synergyImageMap = Collections.emptyMap();
        Map<String, String> rankImageMap = Collections.emptyMap();
        
        try {
            championImageMap = api.getChampionImageMap();
            itemImageMap = api.getItemImageMap();
            championCostMap = api.getChampionCostMap();
            synergyImageMap = api.getSynergyImageMap();
            rankImageMap = api.getRankImageMap(); // **** 추가: 랭크 이미지 맵 가져오기 ****
        } catch (IOException e) {
            // System.err.println("데이터 드래곤 API 호출 중 오류 발생(Handler): " + e.getMessage()); // 로그 제거
            // 치명적이지 않다면 로깅만 하고 진행
            request.setAttribute("warningMessage", "챔피언/아이템/시너지/랭크 이미지 정보를 불러오는데 실패했습니다."); // 메시지 수정
        }
        // JSP로 전달하기 위해 request에 추가
        request.setAttribute("championImageMap", championImageMap);
        request.setAttribute("itemImageMap", itemImageMap);
        request.setAttribute("championCostMap", championCostMap);
        request.setAttribute("synergyImageMap", synergyImageMap);
        request.setAttribute("rankImageMap", rankImageMap);
		
		// DB에 저장된 소환사 정보 찾기
	    Summoner summoner = api.findSummoner(summonerName, summonerTag);
	    
	    if (summoner != null && !isRefresh && !isExpired(summoner.getLastUpdated())) {
			
			  // 캐시된 데이터 사용 
	    	request.setAttribute("summoner", summoner);
			  
			  String puuid = summoner.getPuuid();
			  
			  //lovecham, lovesyn 가져오기 
			  Summoner s = api.selectSummonerLove(puuid);
			  request.setAttribute("topChampion", s.getLovecham());
			  request.setAttribute("topSynergy", s.getLovesyn());
			  
			// JSP로 전달하기 위해 request에 추가
		        request.setAttribute("championImageMap", championImageMap);
		        request.setAttribute("itemImageMap", itemImageMap);
		        request.setAttribute("championCostMap", championCostMap);
		        request.setAttribute("synergyImageMap", synergyImageMap);
		        request.setAttribute("rankImageMap", rankImageMap);
			 
			  // DB에서 매치 상세 정보를 읽어와 리스트 채우기 
			  List<SummonerMatch> matchHistory =
			  api.getMatchHistoryFromDB(puuid); request.setAttribute("matchHistory", matchHistory);
			  
			 return "user_result";
			 
	    } else {
	    	// 이때 새롭게 API 호출
	    	JSONObject apiResult = api.getSummonerByName(summonerName, summonerTag);
	    	
	    	if (summoner == null) {
	    		// DB에 없으면 새로 insert
	    		api.insert(summonerName, summonerTag, apiResult.toString());
	    		summoner = api.findSummoner(summonerName, summonerTag);
	    	} else {
	    		// DB에 있으면 update
	    		api.update(summoner.getId(), apiResult.toString());
	    		summoner = api.findSummoner(summonerName, summonerTag);
	    	}
			  }
	    request.setAttribute("summoner", summoner);
	    
	    // 최근 30게임 조회 후 DB에 챔피언/시너지 저장
	    String puuid = summoner.getPuuid();  // Summoner 객체에서 puuid 가져오기
	    
	    // 매치 상세 정보를 담을 리스트
	    List<SummonerMatch> matchHistory = new ArrayList<>();
	    
	    // 이미 해당 puuid의 매치 데이터가 존재하면 API 호출 생략
	    if (api.hasRecentMatchData(puuid) && !isRefresh) {
	    	Summoner s = api.selectSummonerLove(puuid);
	    	
			 request.setAttribute("topChampion", s.getLovecham()); 
			 request.setAttribute("topSynergy",  s.getLovesyn());
			 
			 matchHistory = api.getMatchHistoryFromDB(puuid);
	    } else {
	    	// 30게임 matchId 가져오기
		    List<String> matchIds = api.getMatchIds(puuid);  // List<String>로 변환된 상태여야 함

		    for (String matchId : matchIds) {
		        JSONObject matchDetail = api.getMatchDetail(matchId);
		        JSONArray participants = matchDetail.getJSONObject("info").getJSONArray("participants");
		        
		        SummonerMatch currentMatch = new SummonerMatch(); // 👈 새로운 매치 객체 생성
	            currentMatch.setMatchId(matchId);

		        for (int i = 0; i < participants.length(); i++) {
		            JSONObject p = participants.getJSONObject(i);

		            if (!p.getString("puuid").equals(puuid)) continue;

		            //  DB에 저장 및 Match 객체에 정보 저장
		            int placement = p.getInt("placement");
		            int level = p.getInt("level"); 
		            int totalDamage = p.getInt("total_damage_to_players"); 
		            int goldLeft = p.getInt("gold_left");
		            api.insertMatchInfo(matchId, puuid, placement, level, goldLeft, totalDamage);
		            
		            currentMatch.setPlacement(placement);
	                currentMatch.setLevel(p.getInt("level"));
	                currentMatch.setTotalDamageToPlayers(p.getInt("total_damage_to_players"));
	                currentMatch.setGoldLeft(p.getInt("gold_left"));

		            // units → match_champion insert 및 Match 객체에 기물 정보 저장
		            JSONArray unitsArray = p.getJSONArray("units");
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

                                List<String> items = new ArrayList<>();
                                if (itemNames != null) {
                                    for (int k = 0; k < itemNames.length(); k++) {
                                        // itemNames 배열에는 아이템 ID (예: "TFT_Item_BFSword")가 문자열로 들어있음
                                        items.add(itemNames.optString(k));
                                    }
                                    unitInfo.setItems(items);
                                }
                                
                                String item1 = items.size() > 0 ? items.get(0) : "";
                                String item2 = items.size() > 1 ? items.get(1) : "";
                                String item3 = items.size() > 2 ? items.get(2) : "";

                                currentMatch.getUnits().add(unitInfo); // String 대신 UnitInfo 객체 추가

                                api.insertMatchChampion(matchId, puuid, champ, tier, item1, item2, item3); // API 원본 ID 전달
                            }
                        }
                    } // units 처리 끝

		            //  traits → match_synergy insert 및 Match 객체에 시너지 정보 저장
		            JSONArray traits = p.getJSONArray("traits");
	                for (int j = 0; j < traits.length(); j++) {
	                    JSONObject trait = traits.getJSONObject(j);
	                    String synName = trait.getString("name");
	                    int numUnits = trait.getInt("num_units"); // 시너지 활성화된 유닛 수
	                    
	                    // 시너지 이름과 활성화 레벨 정보를 함께 저장 (예: "Bruiser(4)")
	                    currentMatch.getTraits().add(synName + "(" + numUnits + ")"); 
	                    
	                    api.insertMatchSynergy(matchId, puuid, synName); // DB 저장
		            }
	                matchHistory.add(currentMatch);
		        }
		    }
	    }


	    // TOP 챔피언/시너지 집계 → Summoner 테이블 업데이트
	    String topChampion = api.getTopChampion(puuid);
	    String topSynergy = api.getTopSynergy(puuid);
	    api.updateSummonerLove(puuid, topChampion, topSynergy);

	    // JSP에서 바로 보여주기 위해 request에 attribute 저장
	    request.setAttribute("topChampion", topChampion);
	    request.setAttribute("topSynergy", topSynergy);
	    
	    JSONArray rankArray = null;
	    try {
	        rankArray = api.getRankDetail(puuid);
	    } catch (Exception e) {
	        System.err.println("랭크 API 호출 실패: " + e.getMessage());
	    }
	    
	    String tier = "UNRANKED"; 
	    String rank = "";
	    
	    if (rankArray != null && rankArray.length() > 0) {
	        for (int i = 0; i < rankArray.length(); i++) {
	            try {
	                JSONObject apiRank = rankArray.getJSONObject(i);
	                
	                if ("RANKED_TFT".equals(apiRank.getString("queueType"))) {
	                    
	                    // 4. 해당 JSONObject에서 tier와 rank를 추출
	                    tier = apiRank.getString("tier");
	                    rank = apiRank.getString("rank");
	                    
	                    // 원하는 정보를 찾았으므로 루프 종료
	                    break; 
	                }
	            } catch (org.json.JSONException e) {
	                System.err.println("JSON 객체 파싱 중 오류 발생: " + e.getMessage());
	                continue; 
	            }
	        }
	    }
	    
	    request.setAttribute("tier", tier);
	    request.setAttribute("rank", rank);
	    
	    // 매치 상세 정보 리스트를 JSP로 전달
	    request.setAttribute("matchHistory", matchHistory);
	    
		return "user_result";
	}
	
	private boolean isExpired(Timestamp lastUpdated) {
    	long diff = System.currentTimeMillis() - lastUpdated.getTime();
    	return diff > 2 * 60 * 1000; // 2분 초과 시 만료
    }

}

