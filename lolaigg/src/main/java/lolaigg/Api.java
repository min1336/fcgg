package lolaigg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class Api {
		
	//데이터베이스 연결
	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection conn = null;
		String jdbcDriver = "com.mysql.cj.jdbc.Driver";
		String jdbcURL    = "jdbc:mysql://localhost:3306/tftgg";
		String dbUser     = "root";
		String dbPass     = "rootpw";
			
		Class.forName(jdbcDriver);
		conn = DriverManager.getConnection(jdbcURL, dbUser, dbPass);
		return conn;
	}
	
		
	//API 관련 모든 처리 담당
	private static final String API_KEY = "RGAPI-17fdc05c-3ddb-4a1b-859c-c10e9fc62c08";
		
	public List<String> getMatchIds(String puuid) throws IOException {
	    String urlStr = "https://asia.api.riotgames.com/tft/match/v1/matches/by-puuid/"
	           + puuid + "/ids?type=ranked&start=0&count=30&api_key=" + API_KEY;

	    String response = sendGet(urlStr);
	    JSONArray arr = new JSONArray(response);

	    List<String> matchIds = new ArrayList<>();
	    for (int i = 0; i < arr.length(); i++) {
	        matchIds.add(arr.getString(i));
	    }

	    return matchIds;
	}
		
	public JSONObject getMatchDetail(String matchId) throws IOException {
	    String urlStr = "https://asia.api.riotgames.com/tft/match/v1/matches/" + matchId;
	    String response = sendGet(urlStr);
	    return new JSONObject(response);
	}
	
	// 소환사 티어 정보
	public JSONArray getRankDetail(String puuid) throws IOException {
		String urlStr = "https://kr.api.riotgames.com/tft/league/v1/by-puuid/"
		           + puuid + "?api_key=" + API_KEY;
		
		String response = sendGet(urlStr);
	    return new JSONArray(response);
	}
	
	//소환사 정보 조회
	public JSONObject getSummonerByName(String summonerName, String summonerTag) throws IOException {
		String encodedSummonerName;
		try {
	        encodedSummonerName = URLEncoder.encode(summonerName, StandardCharsets.UTF_8.toString());
	    } catch (Exception e) {
	        throw new IOException("URL 인코딩 실패: " + e.getMessage());
	    }
		
	    String urlStr = "https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id/"
	            + encodedSummonerName + "/" + summonerTag + "?api_key=" + API_KEY;

	    String response = sendGet(urlStr);
	    return new JSONObject(response);
	}
	
	public boolean hasRecentMatchData(String puuid) throws ClassNotFoundException, SQLException {
	    String sql = "SELECT COUNT(*) FROM tft_match WHERE puuid = ?";
	    try (Connection conn = getConnection(); PreparedStatement pstat = conn.prepareStatement(sql)) {
	    	
	    pstat.setString(1, puuid);
	    
	    try(ResultSet rs = pstat.executeQuery()) {
		    if (rs.next()) {
		    	return rs.getInt(1) >= 30; // 30경기 이상 있으면 "충분함"
		    }
	    } catch (SQLException e) {
			e.printStackTrace();
	    }
	    return false;
	    }
	}
	
	public Summoner findSummoner(String name, String tag) throws ClassNotFoundException, SQLException {
		// DB SELECT 쿼리
		String sql = "SELECT * FROM summoner WHERE summoner_name = ? AND summoner_tag = ?";
		try (Connection conn = getConnection(); PreparedStatement pstat = conn.prepareStatement(sql)) {
			
			pstat.setString(1, name);
			pstat.setString(2, tag);
			
			try (ResultSet rs = pstat.executeQuery()) {
				if (rs.next()) {
					Summoner summoner = new Summoner();
					summoner.setId(rs.getLong("id"));
					summoner.setSummonerName(rs.getString("summoner_name"));
					summoner.setSummonerTag(rs.getString("summoner_tag"));
					summoner.setPuuid(rs.getString("puuid"));
					summoner.setDataJson(rs.getString("data_json"));
					summoner.setLastUpdated(rs.getTimestamp("last_updated"));
					return summoner;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void insert(String name, String tag, String jsonData) throws ClassNotFoundException, SQLException, IOException {
		// DB INSERT 쿼리
		JSONObject apiSearch = getSummonerByName(name, tag);
		String puuid = apiSearch.getString("puuid");
		
		String sql = "INSERT INTO summoner (summoner_name, summoner_tag, puuid, data_json, last_updated) " +
                "VALUES (?, ?, ?, ?, NOW())";
		
		try (Connection conn = getConnection(); PreparedStatement pstat = conn.prepareStatement(sql)) {
			pstat.setString(1, name);
			pstat.setString(2, tag);
			pstat.setString(3, puuid);
			pstat.setString(4, jsonData);
			
			pstat.executeUpdate(); // INSERT 실행
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update(Long id, String jsonData) throws ClassNotFoundException, SQLException, IOException {
		// DB UPDATE 쿼리
		 String sql = "UPDATE summoner " +
                 "SET data_json = ?, last_updated = NOW() " +
                 "WHERE id = ?";
		 
		 try (Connection conn = getConnection(); PreparedStatement pstat = conn.prepareStatement(sql)) {
			 pstat.setString(1, jsonData);
		     pstat.setLong(2, id);
		     
		     pstat.executeUpdate(); // UPDATE 실행
		 } catch (SQLException e) {
		     e.printStackTrace();
		 }
	}
	
	public void insertMatchInfo(String matchId, String puuid, int placement, int level, int goldLeft, int totalDamage) throws SQLException, ClassNotFoundException {
	    String sql = "INSERT IGNORE INTO tft_match(match_id, puuid, placement, level, gold_left, total_damage) VALUES (?, ?, ?, ?, ?, ?)";
	    try (Connection conn = getConnection(); PreparedStatement pstat = conn.prepareStatement(sql)) {
	        pstat.setString(1, matchId);
	        pstat.setString(2, puuid);
	        pstat.setInt(3, placement);
	        pstat.setInt(4, level);
	        pstat.setInt(5, goldLeft);
	        pstat.setInt(6, totalDamage);
	        
	        pstat.executeUpdate();
	    } catch (SQLException e) {
		     e.printStackTrace();
		 }
	}
	
	public void insertMatchChampion(String matchId, String puuid, String champion, int tier, String item1, String item2, String item3) throws SQLException, ClassNotFoundException {
	    String sql = "INSERT IGNORE INTO match_champion(match_id, puuid, champion, cham_tier, cham_item1, cham_item2, cham_item3) VALUES (?, ?, ?, ?, ?, ? ,?)";
	    try (Connection conn = getConnection(); PreparedStatement pstat = conn.prepareStatement(sql)) {
	        pstat.setString(1, matchId);
	        pstat.setString(2, puuid);
	        pstat.setString(3, champion);
	        pstat.setInt(4, tier);
	        pstat.setString(5, item1);
	        pstat.setString(6, item2);
	        pstat.setString(7, item3);
	        pstat.executeUpdate();
	    } catch (SQLException e) {
		     e.printStackTrace();
		 }
	}
	
	public void insertMatchSynergy(String matchId, String puuid, String synergy) throws SQLException, ClassNotFoundException {
	    String sql = "INSERT IGNORE INTO match_synergy(match_id, puuid, synergy) VALUES (?, ?, ?)";
	    try (Connection conn = getConnection(); PreparedStatement pstat = conn.prepareStatement(sql)) {
	        pstat.setString(1, matchId);
	        pstat.setString(2, puuid);
	        pstat.setString(3, synergy);
	        pstat.executeUpdate();
	    } catch (SQLException e) {
		     e.printStackTrace();
		 }
	}
	
	public String getTopChampion(String puuid) throws SQLException, ClassNotFoundException {
	    String sql = "SELECT champion, COUNT(*) AS cnt FROM match_champion WHERE puuid = ? GROUP BY champion ORDER BY cnt DESC LIMIT 1";
	    try (Connection conn = getConnection(); PreparedStatement pstat = conn.prepareStatement(sql)) {
	        pstat.setString(1, puuid);
	        ResultSet rs = pstat.executeQuery();
	        if (rs.next()) {
	            return rs.getString("champion");
	        }
	    } catch (SQLException e) {
		     e.printStackTrace();
		 }
	    return null;
	}

	public String getTopSynergy(String puuid) throws SQLException, ClassNotFoundException {
	    String sql = "SELECT synergy, COUNT(*) AS cnt FROM match_synergy WHERE puuid = ? GROUP BY synergy ORDER BY cnt DESC LIMIT 1";
	    try (Connection conn = getConnection(); PreparedStatement pstat = conn.prepareStatement(sql)) {
	        pstat.setString(1, puuid);
	        ResultSet rs = pstat.executeQuery();
	        if (rs.next()) {
	            return rs.getString("synergy");
	        }
	    } catch (SQLException e) {
		     e.printStackTrace();
		 }
	    return null;
	}
	
	public void updateSummonerLove(String puuid, String topChampion, String topSynergy) throws SQLException, ClassNotFoundException {
	    String sql = "UPDATE summoner SET lovecham = ?, lovesyn = ? WHERE puuid = ?";
	    try (Connection conn = getConnection(); PreparedStatement pstat = conn.prepareStatement(sql)) {
	        pstat.setString(1, topChampion);
	        pstat.setString(2, topSynergy);
	        pstat.setString(3, puuid);
	        pstat.executeUpdate();
	    } catch (SQLException e) {
		     e.printStackTrace();
		 }
	}
	
	public Summoner selectSummonerLove(String puuid)  throws SQLException, ClassNotFoundException {
		String sql = "SELECT lovecham, lovesyn FROM summoner WHERE puuid = ?";
		try (Connection conn = getConnection(); PreparedStatement pstat = conn.prepareStatement(sql)) {
			
			pstat.setString(1, puuid);
			
			try (ResultSet rs = pstat.executeQuery()) {
				if (rs.next()) {
					Summoner s = new Summoner();
					s.setLovecham(rs.getString("lovecham"));
					s.setLovesyn(rs.getString("lovesyn"));
					return s;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<SummonerMatch> getMatchHistoryFromDB(String puuid) throws SQLException, ClassNotFoundException {
		String sql = "SELECT match_id, placement, level, gold_left, total_damage FROM tft_match WHERE puuid = ? "
	               + "ORDER BY match_id DESC LIMIT 30";
	    List<SummonerMatch> matchHistory = new ArrayList<>();
	    
	    try (Connection conn = getConnection(); 
	         PreparedStatement pstat = conn.prepareStatement(sql)) {
	        
	        pstat.setString(1, puuid);
	        
	        try (ResultSet rs = pstat.executeQuery()) {
	            while (rs.next()) {
	                SummonerMatch sm = new SummonerMatch();
	                String matchId = rs.getString("match_id"); // 매치 ID를 지역 변수로 저장
	                
	                sm.setMatchId(matchId);
	                sm.setPlacement(rs.getInt("placement"));
	                sm.setLevel(rs.getInt("level"));
	                sm.setGoldLeft(rs.getInt("gold_left"));
	                sm.setTotalDamageToPlayers(rs.getInt("total_damage"));
	                
	                sm.setUnits(this.getUnitsForMatch(matchId, puuid)); // setUnits 사용
	                sm.setTraits(this.getTraitsForMatch(matchId, puuid)); // setTraits 사용
	                
	                matchHistory.add(sm);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return matchHistory;
	}
	
	public List<UnitInfo> getUnitsForMatch(String matchId, String puuid) throws SQLException, ClassNotFoundException {
	    String sql = "SELECT champion FROM match_champion WHERE match_id = ? AND puuid = ?";
	    List<UnitInfo> units = new ArrayList<>();
	    
	    try (Connection conn = getConnection(); 
	         PreparedStatement pstat = conn.prepareStatement(sql)) {
	        
	        pstat.setString(1, matchId);
	        pstat.setString(2, puuid);
	        
	        try (ResultSet rs = pstat.executeQuery()) {
	            while (rs.next()) {
	            	UnitInfo unit = new UnitInfo();
	            	
	            	String fullChampId = rs.getString("champion");
	                
	                // 🏆 핵심 수정: DB에서 가져온 ID를 정제합니다. 🏆
	                String cleanedChamp = fullChampId.startsWith("TFT") 
	                                    ? fullChampId.substring(fullChampId.indexOf("_") + 1) 
	                                    : fullChampId;
	                
	            	unit.setChampionId(cleanedChamp);
	            	unit.setChampionName("champion");
	                units.add(unit);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return units;
	}

	/**
	 * 특정 매치의 소환사가 활성화한 시너지 목록을 DB에서 조회합니다.
	 */
	public List<String> getTraitsForMatch(String matchId, String puuid) throws SQLException, ClassNotFoundException {
	    String sql = "SELECT synergy FROM match_synergy WHERE match_id = ? AND puuid = ?";
	    List<String> traits = new ArrayList<>();
	    
	    try (Connection conn = getConnection(); 
	         PreparedStatement pstat = conn.prepareStatement(sql)) {
	        
	        pstat.setString(1, matchId);
	        pstat.setString(2, puuid);
	        
	        try (ResultSet rs = pstat.executeQuery()) {
	            while (rs.next()) {
	                String traitName = rs.getString("synergy");
	                traits.add(traitName);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return traits;
	}
	
	// 이미지 관련 메서드
	
	public Map<String, String> getChampionImageMap() throws IOException {
        Map<String, String> champImageMap = new HashMap<>();
        String version = "15.18.1"; // 예시 버전, 실제로는 최신 TFT 버전을 가져오는 로직 추가 권장
        String champJsonUrl = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/ko_KR/tft-champion.json"; // 한국어 데이터 사용
        try {
            String champJsonStr = sendStaticRequest(champJsonUrl);
            JSONObject champJsonData = new JSONObject(champJsonStr).optJSONObject("data");
            if (champJsonData != null) {
                for (String key : champJsonData.keySet()) {
                    JSONObject champ = champJsonData.getJSONObject(key);
                    String champId = champ.getString("id");
                    // API 응답의 ID (e.g., TFT10_Kaisa)에서 실제 사용 ID (Kaisa) 추출 필요 시
                    String cleanedChampId = champId.startsWith("TFT") ? champId.substring(champId.indexOf("_") + 1) : champId;
                    String imgFile = champ.getJSONObject("image").getString("full");
                    // 이미지 URL 구성
                    champImageMap.put(cleanedChampId, "https://ddragon.leagueoflegends.com/cdn/" + version + "/img/tft-champion/" + imgFile);
                }
            }
        } catch (IOException e) {
             // System.err.println("챔피언 데이터 가져오기 오류(Data Dragon): " + e.getMessage()); // 로그 제거
             throw e;
        } catch (org.json.JSONException e) {
            // System.err.println("챔피언 JSON 파싱 오류(Data Dragon): " + e.getMessage()); // 로그 제거
            // 빈 맵 반환 또는 예외 처리
        }
        return champImageMap;
    }
	
	public Map<String, String> getItemImageMap() throws IOException {
        Map<String, String> itemImageMap = new HashMap<>();
        String version = "15.18.1"; // 예시 버전
        String itemJsonUrl  = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/ko_KR/tft-item.json"; // 한국어 데이터 사용
         try {
            String itemJsonStr = sendStaticRequest(itemJsonUrl);
            JSONObject itemJsonData = new JSONObject(itemJsonStr).optJSONObject("data");
            if (itemJsonData != null) {
                for (String key : itemJsonData.keySet()) { // key가 아이템 ID (e.g., TFT_Item_BFSword)
                    JSONObject item = itemJsonData.getJSONObject(key);
                    String imgFile = item.getJSONObject("image").getString("full");
                    itemImageMap.put(key, "https://ddragon.leagueoflegends.com/cdn/" + version + "/img/tft-item/" + imgFile);
                }
            }
        } catch (IOException e) {
             // System.err.println("아이템 데이터 가져오기 오류(Data Dragon): " + e.getMessage()); // 로그 제거
             throw e;
        } catch (org.json.JSONException e) {
            // System.err.println("아이템 JSON 파싱 오류(Data Dragon): " + e.getMessage()); // 로그 제거
        }
        return itemImageMap;
    }
	
	public Map<String, Integer> getChampionCostMap() throws IOException {
        Map<String, Integer> champCostMap = new HashMap<>();
        String version = "15.18.1"; // 예시 버전
        String champJsonUrl = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/ko_KR/tft-champion.json"; // 한국어 데이터 사용
        try {
            String champJsonStr = sendStaticRequest(champJsonUrl);
            JSONObject champJsonData = new JSONObject(champJsonStr).optJSONObject("data");
            if (champJsonData != null) {
                for (String key : champJsonData.keySet()) {
                    JSONObject champ = champJsonData.getJSONObject(key);
                    String champId = champ.getString("id");
                    String cleanedChampId = champId.startsWith("TFT") ? champId.substring(champId.indexOf("_") + 1) : champId;
                    int cost = champ.getInt("tier"); // 'tier'가 TFT에서는 비용(cost)임
                    champCostMap.put(cleanedChampId, cost);
                }
            }
         } catch (IOException e) {
             // System.err.println("챔피언 비용 데이터 가져오기 오류(Data Dragon): " + e.getMessage()); // 로그 제거
             throw e;
        } catch (org.json.JSONException e) {
            // System.err.println("챔피언 비용 JSON 파싱 오류(Data Dragon): " + e.getMessage()); // 로그 제거
        }
        return champCostMap;
    }
	
	public Map<String, String> getSynergyImageMap() throws IOException {
        Map<String, String> synergyImageMap = new HashMap<>();
        String version = "15.18.1"; // 예시 버전, 실제로는 최신 TFT 버전을 가져오는 로직 추가 권장
        String traitJsonUrl = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/ko_KR/tft-trait.json"; // 한국어 데이터 사용
        try {
            String traitJsonStr = sendStaticRequest(traitJsonUrl);
            JSONObject traitJsonData = new JSONObject(traitJsonStr).optJSONObject("data");
            if (traitJsonData != null) {
                for (String key : traitJsonData.keySet()) { // key가 시너지 ID (e.g., Set10_KDA)
                    JSONObject trait = traitJsonData.getJSONObject(key);
                    // 이미지 정보가 없는 trait 스킵 (예: 세트 로테이션으로 제외된 시너지)
                    if (!trait.has("image")) continue;
                    JSONObject imageObj = trait.getJSONObject("image");
                    if (!imageObj.has("full")) continue;

                    String imgFile = imageObj.getString("full");
                 
                    synergyImageMap.put(key, "https://ddragon.leagueoflegends.com/cdn/" + version + "/img/tft-trait/" + imgFile); // 경로 수정 가능성 높음
                }
            }
        } catch (IOException e) {
             // System.err.println("시너지 데이터 가져오기 오류(Data Dragon): " + e.getMessage()); // 로그 제거
             throw e;
        } catch (org.json.JSONException e) {
            // System.err.println("시너지 JSON 파싱 오류(Data Dragon): " + e.getMessage()); // 로그 제거
            // 빈 맵 반환 또는 예외 처리
        }
        return synergyImageMap;
    }
	
	public Map<String, String> getRankImageMap() throws IOException {
        Map<String, String> rankImageMap = new HashMap<>();
        String version = "15.18.1"; // 예시 버전, 실제로는 최신 TFT 버전을 가져오는 로직 추가 권장
        String regaliaJsonUrl = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/ko_KR/tft-regalia.json"; // 한국어 데이터 사용
        try {
            String regaliaJsonStr = sendStaticRequest(regaliaJsonUrl);
            JSONObject regaliaJsonData = new JSONObject(regaliaJsonStr).optJSONObject("data");
            if (regaliaJsonData != null) {
                for (String key : regaliaJsonData.keySet()) { // key가 티어명 (e.g., IRON, GOLD)
                    JSONObject regalia = regaliaJsonData.getJSONObject(key);
                    // 이미지 정보가 없는 경우 스킵
                    if (!regalia.has("image")) continue;
                    JSONObject imageObj = regalia.getJSONObject("image");
                    if (!imageObj.has("full")) continue;

                    // **** 수정: 사용자가 알려준 파일명 형식("TFT_Regalia_Gold.png")으로 구성 ****
                    // JSON key (GOLD)를 이용하여 파일명 생성
                    String tierNameCapitalized = key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase(); // Gold
                    String imgFile = "TFT_Regalia_" + tierNameCapitalized + ".png"; // TFT_Regalia_Gold.png

                    // 이미지 URL 구성 (사용자가 알려준 경로 사용)
                    rankImageMap.put(key.toLowerCase(), "https://ddragon.leagueoflegends.com/cdn/" + version + "/img/tft-regalia/" + imgFile);
                }
            }
        } catch (IOException e) {
             // System.err.println("랭크(Regalia) 데이터 가져오기 오류(Data Dragon): " + e.getMessage()); // 로그 제거
             throw e;
        } catch (org.json.JSONException e) {
            // System.err.println("랭크(Regalia) JSON 파싱 오류(Data Dragon): " + e.getMessage()); // 로그 제거
            // 빈 맵 반환 또는 예외 처리
        }
        // "UNRANKED"에 대한 기본 이미지 경로 추가 (필요시)
        // 실제 언랭크 이미지 파일명 확인 필요 (예: TFT_Regalia_Unranked.png)
        // rankImageMap.put("unranked", "https://ddragon.leagueoflegends.com/cdn/" + version + "/img/tft-regalia/TFT_Regalia_Unranked.png");
        return rankImageMap;
    }
	
	
	
	
	
		
	//공통 GET 요청
	private String sendGet(String urlStr) throws IOException {
	    URL url = new URL(urlStr);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("User-Agent", "Mozilla/5.0");
	    conn.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
	    conn.setRequestProperty("X-Riot-Token", API_KEY);

	    BufferedReader br = new BufferedReader(
	            new InputStreamReader(conn.getInputStream(), "UTF-8"));
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = br.readLine()) != null) {
	        sb.append(line);
	    }
	    br.close();
	    return sb.toString();
	}
	
	// 이미지 관련 GET 요청
	private String sendStaticRequest(String urlStr) throws IOException {
        HttpURLConnection conn = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        int responseCode = -1;

        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // 일반적인 User-Agent
            conn.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7"); // 한국어 우선 설정

            responseCode = conn.getResponseCode();

            if (responseCode >= 200 && responseCode < 300) { // 성공 응답 코드 확인
                inputStreamReader = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
            } else {
                // 실패 시 에러 스트림 읽기 시도
                // System.err.println("정적 데이터 요청 실패: " + urlStr + ", 응답 코드: " + responseCode); // 로그 제거
                inputStreamReader = conn.getErrorStream() != null ? new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8) : null;
            }

            // inputStreamReader가 null이 아닐 때만 읽기
            if (inputStreamReader != null) {
                br = new BufferedReader(inputStreamReader);
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } else if (!(responseCode >= 200 && responseCode < 300)) {
                // 실패했고 에러 스트림도 없는 경우
                throw new IOException("HTTP GET 요청 실패: " + responseCode + " (응답 본문 없음)");
            }

            // 실패 응답 코드인 경우 예외 발생 (에러 스트림을 읽었더라도)
            if (!(responseCode >= 200 && responseCode < 300)) {
                throw new IOException("HTTP GET 요청 실패: " + responseCode + ", 응답: " + sb.toString());
            }

        } finally {
            // 자원 해제
            if (br != null) try { br.close(); } catch (IOException e) { /* 무시 */ }
            if (inputStreamReader != null) try { inputStreamReader.close(); } catch (IOException e) { /* 무시 */ }
            if (conn != null) conn.disconnect();
        }

        return sb.toString();
    }
	
	// 시즌 당 한번 호출
	
	public void saveAllTFTUnits() throws IOException, SQLException {
	    // 챔피언 이미지 맵을 가져오는 로직을 재사용하여 JSON 데이터를 파싱합니다.
	    String version = "15.18.1"; // 실제로는 최신 버전으로 업데이트 필요
	    String champJsonUrl = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/ko_KR/tft-champion.json";

	    // INSERT SQL: unit_id와 unit_name 컬럼에 데이터를 넣습니다.
	    String insertSql = "INSERT INTO tft_unit (unit_id, unit_name) VALUES (?, ?) ON DUPLICATE KEY UPDATE unit_name = VALUES(unit_name)";
	    
	    // DB 연결은 try-with-resources 구문으로 안정적으로 처리합니다.
	    try (Connection conn = getConnection(); 
	         PreparedStatement pstat = conn.prepareStatement(insertSql)) {
	        
	        // Data Dragon JSON 요청
	        String champJsonStr = sendStaticRequest(champJsonUrl);
	        JSONObject champJsonData = new JSONObject(champJsonStr).optJSONObject("data");
	        
	        if (champJsonData != null) {
	            for (String key : champJsonData.keySet()) {
	                JSONObject champ = champJsonData.getJSONObject(key);
	                
	                String champId = champ.getString("id"); // 예: TFT10_Kaisa
	                String name = champ.getString("name");   // 예: 카이사 (한국어 이름)
	                
	                // 1. 챔피언 ID 정제 (이미지 맵 키와 일치)
	                String cleanedChampId = champId.startsWith("TFT") 
	                                      ? champId.substring(champId.indexOf("_") + 1) 
	                                      : champId; // 예: Kaisa
	                                      
	                // 2. PreparedStatement에 값 설정
	                pstat.setString(1, cleanedChampId); // unit_id 컬럼에 정제된 ID 삽입 (Map의 키)
	                pstat.setString(2, name);           // unit_name 컬럼에 챔피언 이름 삽입
	                
	                pstat.addBatch(); // 배치 처리로 효율성 향상
	            }
	            pstat.executeBatch(); // 일괄 실행
	        }
	        
	    } catch (IOException e) {
	        throw new IOException("챔피언 데이터(Data Dragon) 로딩 중 오류 발생: " + e.getMessage(), e);
	    } catch (org.json.JSONException e) {
	        throw new SQLException("챔피언 JSON 파싱 중 오류 발생", e);
	    } catch (ClassNotFoundException e) {
	        throw new SQLException("데이터베이스 드라이버 로드 실패", e);
	    }
	}
}
