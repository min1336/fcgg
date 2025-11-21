package lolaigg;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap; // Map 사용을 위해 추가
import java.util.List;
import java.util.Map; // Map 사용을 위해 추가
import java.util.stream.Collectors; // 11월 11일 김민혁 : 데이터베이스와 챗봇 연결
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
// Servlet 관련 import는 Api 클래스 자체에서는 불필요하므로 제거해도 됩니다.
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

public class Api {

	//데이터베이스 관련
	private Connection conn = null;
	// Statement, ResultSet, PreparedStatement는 메소드 지역 변수로 사용하는 것이 좋습니다.
	// private Statement  stat = null;
	// private ResultSet  rs   = null;
	// private PreparedStatement pstat = null;

	//데이터베이스 연결
	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection conn = null;
		String jdbcDriver = "com.mysql.cj.jdbc.Driver";
		// DB URL, 사용자명, 비밀번호는 설정 파일 등으로 분리하는 것이 좋습니다.
		String jdbcURL    = "jdbc:mysql://localhost:3306/tftgg?characterEncoding=UTF-8&serverTimezone=UTC"; // 인코딩 및 타임존 추가 권장
		String dbUser     = "root";
		String dbPass     = "rootpw";

		Class.forName(jdbcDriver);
		conn = DriverManager.getConnection(jdbcURL, dbUser, dbPass);
		return conn;
	}


	//API 관련 모든 처리 담당
	// API 키는 외부에 노출되지 않도록 설정 파일이나 환경 변수로 관리하는 것이 좋습니다.
	private static final String API_KEY = "RGAPI-e380153f-9b82-4a48-88a8-87d2ea9cb4f4"; // 실제 키로 교체 필요

	/**
	 * PUUID로 최근 랭크 게임 매치 ID 목록(최대 30개)을 가져옵니다.
	 * @param puuid 소환사의 PUUID
	 * @return 매치 ID 목록
	 * @throws IOException API 호출 실패 시
	 */
	public List<String> getMatchIds(String puuid) throws IOException {
	    String urlStr = "https://asia.api.riotgames.com/tft/match/v1/matches/by-puuid/"
	           + puuid + "/ids?type=ranked&start=0&count=30&api_key=" + API_KEY;

	    String response = sendGet(urlStr);
	    JSONArray arr = new JSONArray(response); // JSON 파싱 오류 가능성 있음

	    List<String> matchIds = new ArrayList<>();
	    for (int i = 0; i < arr.length(); i++) {
	        matchIds.add(arr.getString(i));
	    }

	    return matchIds;
	}

	/**
	 * 매치 ID로 해당 게임의 상세 정보를 가져옵니다.
	 * @param matchId 매치 ID
	 * @return 매치 상세 정보 JSON 객체
	 * @throws IOException API 호출 실패 시
	 */
	public JSONObject getMatchDetail(String matchId) throws IOException {
	    String urlStr = "https://asia.api.riotgames.com/tft/match/v1/matches/" + matchId + "?api_key=" + API_KEY; // API 키 추가
	    String response = sendGet(urlStr);
	    return new JSONObject(response); // JSON 파싱 오류 가능성 있음
	}

	/**
	 * PUUID로 소환사의 TFT 랭크 티어 정보를 가져옵니다.
	 * @param puuid 소환사의 PUUID
	 * @return 랭크 정보 JSON 배열
	 * @throws IOException API 호출 실패 시
	 */
	public JSONArray getRankDetail(String puuid) throws IOException {
		String urlStr = "https://kr.api.riotgames.com/tft/league/v1/by-puuid/"
		           + puuid + "?api_key=" + API_KEY;

		String response = sendGet(urlStr);
	    // 빈 배열 또는 오류 응답일 수 있음
	    try {
	        return new JSONArray(response);
	    } catch (org.json.JSONException e) {
	        // System.err.println("랭크 정보 파싱 오류: " + response + ", 오류: " + e.getMessage()); // 로그 제거
	        return new JSONArray(); // 빈 배열 반환 또는 예외 처리
	    }
	}

	/**
	 * 소환사 이름과 태그로 계정 정보(puuid 등)를 조회합니다.
	 * @param summonerName 소환사 이름 (게임 닉네임)
	 * @param summonerTag 소환사 태그 (# 제외)
	 * @return 계정 정보 JSON 객체
	 * @throws IOException API 호출 실패 또는 URL 인코딩 실패 시
	 */
	public JSONObject getSummonerByName(String summonerName, String summonerTag) throws IOException {
		String encodedSummonerName;
		try {
			// 공백 등 특수문자 인코딩
	        encodedSummonerName = URLEncoder.encode(summonerName, StandardCharsets.UTF_8.toString());
	    } catch (Exception e) {
	        // 일반적으로 발생하지 않지만, 발생 시 IOException으로 래핑
	        throw new IOException("URL 인코딩 실패: " + e.getMessage());
	    }

	    String urlStr = "https://asia.api.riotgames.com/riot/account/v1/accounts/by-riot-id/"
	            + encodedSummonerName + "/" + summonerTag + "?api_key=" + API_KEY;

	    String response = sendGet(urlStr);
        try {
            return new JSONObject(response); // JSON 파싱 오류 가능성 있음
        } catch (org.json.JSONException e) {
            // System.err.println("소환사 이름 검색 결과 파싱 오류: " + response + ", 오류: " + e.getMessage()); // 로그 제거
            // 404 등의 오류일 수 있으므로 빈 객체나 null 반환 또는 예외 처리
            // return null;
            throw new IOException("소환사 정보를 찾을 수 없거나 API 오류입니다.");
        }
	}

	/**
	 * PUUID로 현재 진행 중인 게임 정보(관전 정보)를 가져옵니다.
	 * 게임 중이 아닐 경우 404 오류가 발생할 수 있습니다.
	 * @param puuid 소환사의 PUUID
	 * @return 라이브 게임 정보 JSON 객체 (게임 중이 아니면 오류 또는 빈 객체)
	 * @throws IOException API 호출 실패 시
	 */
	public JSONObject getSummonerLive(String puuid) throws IOException {
		String urlStr = "https://kr.api.riotgames.com/lol/spectator/tft/v5/active-games/by-puuid/" // TFT 경로 확인 필요 (정확한 경로로 수정됨)
									+ puuid + "?api_key=" + API_KEY;

		String response = sendGet(urlStr); // sendGet 내부에서 404 등 처리 필요

        try {
            // 게임 중이 아닐 때 Riot API는 보통 404를 반환하며, sendGet에서 처리될 수 있음
            // sendGet에서 IOException을 던지지 않았다면 유효한 JSON으로 가정
            return new JSONObject(response);
        } catch (org.json.JSONException e) {
             // sendGet에서 404 등을 처리하지 못했거나, 예상 외의 응답일 경우
            // System.err.println("라이브 게임 정보 파싱 오류: " + response + ", 오류: " + e.getMessage()); // 로그 제거
             if (response.contains("Data not found")) { // 간단한 404 메시지 확인
                 // 게임 중이 아님을 나타내는 빈 객체 또는 특정 상태 반환
                 return new JSONObject("{\"status\": \"NOT_IN_GAME\"}");
             }
            // 그 외 JSON 오류는 그대로 던지거나 로깅
            throw new IOException("라이브 게임 정보 처리 중 오류 발생", e);
        }
	}

	/**
	 * DB에 해당 PUUID의 최근 매치 데이터가 충분히(30개 이상) 있는지 확인합니다.
	 * @param puuid 소환사의 PUUID
	 * @return 30개 이상 데이터가 있으면 true, 아니면 false
	 * @throws ClassNotFoundException DB 드라이버 로딩 실패 시
	 * @throws SQLException DB 쿼리 실행 실패 시
	 */
	public boolean hasRecentMatchData(String puuid) throws ClassNotFoundException, SQLException {
	    String sql = "SELECT COUNT(*) FROM tft_match WHERE puuid = ?";
	    // try-with-resources 구문으로 Connection, PreparedStatement 자동 해제
	    try (Connection conn = getConnection();
	         PreparedStatement pstat = conn.prepareStatement(sql)) {

	        pstat.setString(1, puuid);

	        try (ResultSet rs = pstat.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1) >= 30; // 30경기 이상 있으면 true
	            }
	        }
	    } catch (SQLException e) {
	        // System.err.println("hasRecentMatchData DB 오류: " + e.getMessage()); // 로그 제거
	        throw e; // 예외를 다시 던져서 호출 측에서 알 수 있도록 함
	    }
	    return false; // 쿼리 실패 또는 결과 없을 시 false
	}


	/**
	 * DB에서 소환사 이름과 태그로 소환사 정보를 조회합니다.
	 * @param name 소환사 이름
	 * @param tag 소환사 태그
	 * @return Summoner 객체 (없으면 null)
	 * @throws ClassNotFoundException DB 드라이버 로딩 실패 시
	 * @throws SQLException DB 쿼리 실행 실패 시
	 */
	public Summoner findSummoner(String name, String tag) throws ClassNotFoundException, SQLException {
		String sql = "SELECT * FROM summoner WHERE summoner_name = ? AND summoner_tag = ?";
		try (Connection conn = getConnection();
		     PreparedStatement pstat = conn.prepareStatement(sql)) {

			pstat.setString(1, name);
			pstat.setString(2, tag);

			try (ResultSet rs = pstat.executeQuery()) {
				if (rs.next()) {
					Summoner summoner = new Summoner();
					summoner.setId(rs.getLong("id"));
					summoner.setSummonerName(rs.getString("summoner_name"));
					summoner.setSummonerTag(rs.getString("summoner_tag"));
					// data_json 컬럼에서 puuid 추출 (API 호출 줄이기 위함)
					String jsonData = rs.getString("data_json");
                    summoner.setDataJson(jsonData); // JSON 원본 저장
                    if (jsonData != null && !jsonData.isEmpty()) {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonData);
                            summoner.setPuuid(jsonObject.optString("puuid", null)); // puuid가 없을 경우 null
                        } catch (org.json.JSONException e) {
                            // System.err.println("DB의 JSON 파싱 오류 (findSummoner): " + e.getMessage()); // 로그 제거
                            summoner.setPuuid(null); // 파싱 실패 시 null 설정
                        }
                    } else {
                         summoner.setPuuid(null);
                    }
					summoner.setLastUpdated(rs.getTimestamp("last_updated"));
					summoner.setLovecham(rs.getString("lovecham")); // 즐겨찾기 정보 로드
					summoner.setLovesyn(rs.getString("lovesyn"));
					return summoner;
				}
			}
		} catch (SQLException e) {
			// System.err.println("findSummoner DB 오류: " + e.getMessage()); // 로그 제거
			throw e;
		}
		return null; // 조회 결과 없을 시 null 반환
	}

	/**
	 * 새로운 소환사 정보를 DB에 삽입합니다.
	 * @param name 소환사 이름
	 * @param tag 소환사 태그
	 * @param jsonData Riot API에서 받은 계정 정보 JSON 문자열
	 * @throws ClassNotFoundException DB 드라이버 로딩 실패 시
	 * @throws SQLException DB 쿼리 실행 실패 시
	 * @throws IOException API 호출 실패 시 (내부 getSummonerByName 호출)
	 */
	public void insert(String name, String tag, String jsonData) throws ClassNotFoundException, SQLException, IOException {
		JSONObject apiSearch = new JSONObject(jsonData); // 파라미터로 받은 jsonData 사용
		String puuid = apiSearch.getString("puuid"); // jsonData에서 puuid 추출

		String sql = "INSERT INTO summoner (summoner_name, summoner_tag, puuid, data_json, last_updated) " +
                "VALUES (?, ?, ?, ?, NOW())";

		try (Connection conn = getConnection();
		     PreparedStatement pstat = conn.prepareStatement(sql)) {
			pstat.setString(1, name);
			pstat.setString(2, tag);
			pstat.setString(3, puuid);
			pstat.setString(4, jsonData); // API 응답 원본 저장

			pstat.executeUpdate();
		} catch (SQLException e) {
			// System.err.println("insert DB 오류: " + e.getMessage()); // 로그 제거
			throw e;
		}
	}

	/**
	 * 기존 소환사 정보(JSON 데이터, 마지막 업데이트 시간)를 DB에서 업데이트합니다.
	 * @param id 업데이트할 소환사의 DB id
	 * @param jsonData Riot API에서 새로 받은 계정 정보 JSON 문자열
	 * @throws ClassNotFoundException DB 드라이버 로딩 실패 시
	 * @throws SQLException DB 쿼리 실행 실패 시
	 */
	public void update(Long id, String jsonData) throws ClassNotFoundException, SQLException {
		 String sql = "UPDATE summoner " +
                 "SET data_json = ?, last_updated = NOW() " + // puuid는 변경되지 않는 값이므로 업데이트 제외 가능
                 "WHERE id = ?";

		 try (Connection conn = getConnection();
		      PreparedStatement pstat = conn.prepareStatement(sql)) {
			 pstat.setString(1, jsonData);
		     pstat.setLong(2, id);

		     pstat.executeUpdate();
		 } catch (SQLException e) {
		     // System.err.println("update DB 오류: " + e.getMessage()); // 로그 제거
		     throw e;
		 }
	}

	/**
	 * 매치 기본 정보를 DB(tft_match)에 삽입합니다. (중복 시 무시 - IGNORE)
	 * @param matchId 매치 ID
	 * @param puuid 소환사 PUUID
	 * @param placement 최종 순위
	 * @param level 최종 레벨
	 * @param goldLeft 남은 골드
	 * @param totalDamage 플레이어에게 입힌 총 피해량
	 * @throws SQLException DB 쿼리 실행 실패 시
	 * @throws ClassNotFoundException DB 드라이버 로딩 실패 시
	 */
	public void insertMatchInfo(String matchId, String puuid, int placement, int level, int goldLeft, int totalDamage) throws SQLException, ClassNotFoundException {
	    String sql = "INSERT IGNORE INTO tft_match(match_id, puuid, placement, `level`, gold_left, total_damage) VALUES (?, ?, ?, ?, ?, ?)"; // level에 백틱 추가 (예약어일 수 있음)
	    try (Connection conn = getConnection();
	         PreparedStatement pstat = conn.prepareStatement(sql)) {
	        pstat.setString(1, matchId);
	        pstat.setString(2, puuid);
	        pstat.setInt(3, placement);
	        pstat.setInt(4, level);
	        pstat.setInt(5, goldLeft);
	        pstat.setInt(6, totalDamage);

	        pstat.executeUpdate();
	    } catch (SQLException e) {
	        // System.err.println("insertMatchInfo DB 오류: " + e.getMessage()); // 로그 제거
	        throw e;
		 }
	}

	/**
	 * 해당 매치에서 사용한 챔피언 정보를 DB(match_champion)에 삽입합니다. (중복 시 무시 - IGNORE)
	 * @param matchId 매치 ID
	 * @param puuid 소환사 PUUID
	 * @param champion 챔피언 ID (API 원본 ID)
	 * @throws SQLException DB 쿼리 실행 실패 시
	 * @throws ClassNotFoundException DB 드라이버 로딩 실패 시
	 */
	public void insertMatchChampion(String matchId, String puuid, String champion) throws SQLException, ClassNotFoundException {
	    // DB에는 접두사가 제거된 ID를 저장할 경우
        String cleanedChampionId = champion.startsWith("TFT") ? champion.substring(champion.indexOf("_") + 1) : champion;

	    String sql = "INSERT IGNORE INTO match_champion(match_id, puuid, champion) VALUES (?, ?, ?)";
	    try (Connection conn = getConnection();
	         PreparedStatement pstat = conn.prepareStatement(sql)) {
	        pstat.setString(1, matchId);
	        pstat.setString(2, puuid);
	        pstat.setString(3, cleanedChampionId); // 정리된 ID 저장
	        pstat.executeUpdate();
	    } catch (SQLException e) {
	        // System.err.println("insertMatchChampion DB 오류: " + e.getMessage()); // 로그 제거
	        throw e;
		 }
	}

	/**
	 * 해당 매치에서 활성화된 시너지 정보를 DB(match_synergy)에 삽입합니다. (중복 시 무시 - IGNORE)
	 * @param matchId 매치 ID
	 * @param puuid 소환사 PUUID
	 * @param synergy 시너지 ID (API 원본 ID)
	 * @throws SQLException DB 쿼리 실행 실패 시
	 * @throws ClassNotFoundException DB 드라이버 로딩 실패 시
	 */
	public void insertMatchSynergy(String matchId, String puuid, String synergy) throws SQLException, ClassNotFoundException {
	    String sql = "INSERT IGNORE INTO match_synergy(match_id, puuid, synergy) VALUES (?, ?, ?)";
	    try (Connection conn = getConnection();
	         PreparedStatement pstat = conn.prepareStatement(sql)) {
	        pstat.setString(1, matchId);
	        pstat.setString(2, puuid);
	        pstat.setString(3, synergy);
	        pstat.executeUpdate();
	    } catch (SQLException e) {
	        // System.err.println("insertMatchSynergy DB 오류: " + e.getMessage()); // 로그 제거
	        throw e;
		 }
	}

	/**
	 * 특정 소환사가 가장 많이 사용한 챔피언(최근 30게임 기준)을 DB에서 조회합니다.
	 * @param puuid 소환사 PUUID
	 * @return 가장 많이 사용한 챔피언 ID (없으면 null)
	 * @throws SQLException DB 쿼리 실행 실패 시
	 * @throws ClassNotFoundException DB 드라이버 로딩 실패 시
	 */
	public String getTopChampion(String puuid) throws SQLException, ClassNotFoundException {
	    // 최근 30개 매치 ID를 먼저 가져옵니다.
	    String subSql = "SELECT match_id FROM tft_match WHERE puuid = ? ORDER BY match_id DESC LIMIT 30";
	    List<String> recentMatchIds = new ArrayList<>();
	    try (Connection conn = getConnection();
	         PreparedStatement subPstat = conn.prepareStatement(subSql)) {
	        subPstat.setString(1, puuid);
	        try (ResultSet subRs = subPstat.executeQuery()) {
	            while (subRs.next()) {
	                recentMatchIds.add(subRs.getString("match_id"));
	            }
	        }
	    } catch (SQLException e) {
	        // System.err.println("getTopChampion (match_id 조회) DB 오류: " + e.getMessage()); // 로그 제거
	        throw e;
	    }

	    if (recentMatchIds.isEmpty()) {
	        return null; // 조회할 매치가 없음
	    }

	    // IN 절을 만들기 위한 플레이스홀더 생성 (?,?,?)
	    StringBuilder placeholders = new StringBuilder();
	    for (int i = 0; i < recentMatchIds.size(); i++) {
	        placeholders.append("?");
	        if (i < recentMatchIds.size() - 1) {
	            placeholders.append(",");
	        }
	    }

	    // 최근 30개 매치 내에서 가장 많이 사용된 챔피언 집계
	    String sql = "SELECT champion, COUNT(*) AS cnt " +
	                 "FROM match_champion " +
	                 "WHERE puuid = ? AND match_id IN (" + placeholders.toString() + ") " +
	                 "GROUP BY champion ORDER BY cnt DESC LIMIT 1";

	    try (Connection conn = getConnection();
	         PreparedStatement pstat = conn.prepareStatement(sql)) {
	        pstat.setString(1, puuid);
	        // IN 절에 매치 ID 바인딩
	        for (int i = 0; i < recentMatchIds.size(); i++) {
	            pstat.setString(i + 2, recentMatchIds.get(i));
	        }

	        try (ResultSet rs = pstat.executeQuery()) {
	            if (rs.next()) {
	                return rs.getString("champion");
	            }
	        }
	    } catch (SQLException e) {
	        // System.err.println("getTopChampion (챔피언 집계) DB 오류: " + e.getMessage()); // 로그 제거
	        throw e;
	    }
	    return null; // 결과 없을 시 null
	}


	/**
	 * 특정 소환사가 가장 많이 사용한 시너지(최근 30게임 기준)를 DB에서 조회합니다.
	 * @param puuid 소환사 PUUID
	 * @return 가장 많이 사용한 시너지 ID (없으면 null)
	 * @throws SQLException DB 쿼리 실행 실패 시
	 * @throws ClassNotFoundException DB 드라이버 로딩 실패 시
	 */
	public String getTopSynergy(String puuid) throws SQLException, ClassNotFoundException {
        // 최근 30개 매치 ID 조회 (getTopChampion과 동일 로직)
        String subSql = "SELECT match_id FROM tft_match WHERE puuid = ? ORDER BY match_id DESC LIMIT 30";
        List<String> recentMatchIds = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement subPstat = conn.prepareStatement(subSql)) {
            subPstat.setString(1, puuid);
            try (ResultSet subRs = subPstat.executeQuery()) {
                while (subRs.next()) {
                    recentMatchIds.add(subRs.getString("match_id"));
                }
            }
        } catch (SQLException e) {
	        // System.err.println("getTopSynergy (match_id 조회) DB 오류: " + e.getMessage()); // 로그 제거
	        throw e;
	    }

	    if (recentMatchIds.isEmpty()) {
	        return null;
	    }

	    StringBuilder placeholders = new StringBuilder();
	    for (int i = 0; i < recentMatchIds.size(); i++) {
	        placeholders.append("?");
	        if (i < recentMatchIds.size() - 1) {
	            placeholders.append(",");
	        }
	    }

        // 최근 30개 매치 내에서 가장 많이 사용된 시너지 집계
	    String sql = "SELECT synergy, COUNT(*) AS cnt " +
                     "FROM match_synergy " +
                     "WHERE puuid = ? AND match_id IN (" + placeholders.toString() + ") " +
                     "GROUP BY synergy ORDER BY cnt DESC LIMIT 1";
	    try (Connection conn = getConnection();
	         PreparedStatement pstat = conn.prepareStatement(sql)) {
	        pstat.setString(1, puuid);
             for (int i = 0; i < recentMatchIds.size(); i++) {
	            pstat.setString(i + 2, recentMatchIds.get(i));
	        }

	        try(ResultSet rs = pstat.executeQuery()){
		        if (rs.next()) {
		            return rs.getString("synergy");
		        }
	        }
	    } catch (SQLException e) {
	        // System.err.println("getTopSynergy (시너지 집계) DB 오류: " + e.getMessage()); // 로그 제거
	        throw e;
		 }
	    return null;
	}

	/**
	 * 소환사의 즐겨찾기 챔피언/시너지 정보를 DB에 업데이트합니다.
	 * @param puuid 소환사 PUUID
	 * @param topChampion 가장 많이 사용한 챔피언 ID
	 * @param topSynergy 가장 많이 사용한 시너지 ID
	 * @throws SQLException DB 쿼리 실행 실패 시
	 * @throws ClassNotFoundException DB 드라이버 로딩 실패 시
	 */
	public void updateSummonerLove(String puuid, String topChampion, String topSynergy) throws SQLException, ClassNotFoundException {
	    String sql = "UPDATE summoner SET lovecham = ?, lovesyn = ? WHERE puuid = ?";
	    try (Connection conn = getConnection();
	         PreparedStatement pstat = conn.prepareStatement(sql)) {
	        // null 값 처리를 위해 setString 대신 setNull 사용 고려 가능
	        pstat.setString(1, topChampion);
	        pstat.setString(2, topSynergy);
	        pstat.setString(3, puuid);
	        pstat.executeUpdate();
	    } catch (SQLException e) {
	        // System.err.println("updateSummonerLove DB 오류: " + e.getMessage()); // 로그 제거
	        throw e;
		 }
	}

	/**
	 * DB에서 특정 소환사의 즐겨찾기 챔피언/시너지 정보만 조회합니다.
	 * @param puuid 소환사 PUUID
	 * @return 즐겨찾기 정보가 담긴 Summoner 객체 (없으면 null)
	 * @throws SQLException DB 쿼리 실행 실패 시
	 * @throws ClassNotFoundException DB 드라이버 로딩 실패 시
	 */
	public Summoner selectSummonerLove(String puuid)  throws SQLException, ClassNotFoundException {
		String sql = "SELECT lovecham, lovesyn FROM summoner WHERE puuid = ?";
		try (Connection conn = getConnection();
		     PreparedStatement pstat = conn.prepareStatement(sql)) {

			pstat.setString(1, puuid);

			try (ResultSet rs = pstat.executeQuery()) {
				if (rs.next()) {
					Summoner s = new Summoner(); // puuid 등 다른 정보는 필요 없으므로 설정 안 함
					s.setLovecham(rs.getString("lovecham"));
					s.setLovesyn(rs.getString("lovesyn"));
					return s;
				}
			}
		} catch (SQLException e) {
			// System.err.println("selectSummonerLove DB 오류: " + e.getMessage()); // 로그 제거
			throw e;
		}
		return null;
	}

	/**
	 * DB에서 특정 소환사의 최근 매치 기록(최대 30개) 상세 정보를 조회하여 List<SummonerMatch> 형태로 반환합니다.
	 * 각 매치별 유닛과 시너지 정보도 함께 조회합니다.
	 * @param puuid 소환사 PUUID
	 * @return 매치 기록 리스트
	 * @throws SQLException DB 쿼리 실행 실패 시
	 * @throws ClassNotFoundException DB 드라이버 로딩 실패 시
	 */
	public List<SummonerMatch> getMatchHistoryFromDB(String puuid) throws SQLException, ClassNotFoundException {
	    // match_id 기준 내림차순 정렬 추가하여 최신 게임부터 가져오도록 함
	    String sql = "SELECT match_id, placement, `level`, gold_left, total_damage FROM tft_match WHERE puuid = ? ORDER BY match_id DESC LIMIT 30";
	    List<SummonerMatch> matchHistory = new ArrayList<>();

	    try (Connection conn = getConnection();
	         PreparedStatement pstat = conn.prepareStatement(sql)) {

	        pstat.setString(1, puuid);

	        try (ResultSet rs = pstat.executeQuery()) {
	            while (rs.next()) {
	                SummonerMatch sm = new SummonerMatch();
	                String matchId = rs.getString("match_id");

	                sm.setMatchId(matchId);
	                sm.setPlacement(rs.getInt("placement"));
	                sm.setLevel(rs.getInt("level"));
	                sm.setGoldLeft(rs.getInt("gold_left"));
	                sm.setTotalDamageToPlayers(rs.getInt("total_damage"));

	                sm.setUnits(new ArrayList<>());
	                sm.setTraits(new ArrayList<>());

	                matchHistory.add(sm);
	            }
	        }
	    } catch (SQLException e) {
	        // System.err.println("getMatchHistoryFromDB DB 오류: " + e.getMessage()); // 로그 제거
	        throw e;
	    }
	    return matchHistory;
	}


	/**
	 * 특정 매치, 특정 소환사의 사용 유닛 목록을 DB에서 조회합니다.
	 * @param matchId 매치 ID
	 * @param puuid 소환사 PUUID
	 * @return 챔피언 ID 목록
	 * @throws SQLException DB 쿼리 실행 실패 시
	 * @throws ClassNotFoundException DB 드라이버 로딩 실패 시
	 */
	public List<String> getUnitsForMatch(String matchId, String puuid) throws SQLException, ClassNotFoundException {
	    String sql = "SELECT champion FROM match_champion WHERE match_id = ? AND puuid = ?";
	    List<String> units = new ArrayList<>();

	    try (Connection conn = getConnection();
	         PreparedStatement pstat = conn.prepareStatement(sql)) {

	        pstat.setString(1, matchId);
	        pstat.setString(2, puuid);

	        try (ResultSet rs = pstat.executeQuery()) {
	            while (rs.next()) {
	                units.add(rs.getString("champion")); // DB에 저장된 (정리된) ID 사용
	            }
	        }
	    } catch (SQLException e) {
	        // System.err.println("getUnitsForMatch DB 오류: " + e.getMessage()); // 로그 제거
	        throw e;
	    }
	    return units;
	}

	/**
	 * 특정 매치, 특정 소환사의 활성화 시너지 목록을 DB에서 조회합니다.
	 * @param matchId 매치 ID
	 * @param puuid 소환사 PUUID
	 * @return 시너지 ID 목록 (예: "Set10_KDA")
	 * @throws SQLException DB 쿼리 실행 실패 시
	 * @throws ClassNotFoundException DB 드라이버 로딩 실패 시
	 */
	public List<String> getTraitsForMatch(String matchId, String puuid) throws SQLException, ClassNotFoundException {
	    // 시너지 레벨/활성 유닛 수를 함께 표시하려면 JOIN 또는 별도 쿼리 필요
	    // 여기서는 시너지 이름만 가져옵니다.
	    String sql = "SELECT synergy FROM match_synergy WHERE match_id = ? AND puuid = ?";
	    List<String> traits = new ArrayList<>();

	    try (Connection conn = getConnection();
	         PreparedStatement pstat = conn.prepareStatement(sql)) {

	        pstat.setString(1, matchId);
	        pstat.setString(2, puuid);

	        try (ResultSet rs = pstat.executeQuery()) {
	            while (rs.next()) {
	                String traitName = rs.getString("synergy");
	                // 필요하다면 여기서 traitName 가공 (예: "Set10_" 제거)
	                traits.add(traitName);
	            }
	        }
	    } catch (SQLException e) {
	        // System.err.println("getTraitsForMatch DB 오류: " + e.getMessage()); // 로그 제거
	        throw e;
	    }
	    return traits;
	}


    // --- 데이터 드래곤 정보 가져오기 메소드 ---

    /**
     * Riot의 데이터 드래곤 API에서 최신 버전의 챔피언 이미지 URL 맵을 가져옵니다.
     * @return Map<챔피언ID, 이미지URL>
     * @throws IOException API 호출 실패 시
     */
     public Map<String, String> getChampionImageMap() throws IOException {
        Map<String, String> champImageMap = new HashMap<>();
        String version = "15.18.1"; // 예시 버전, 실제로는 최신 TFT 버전을 가져오는 로직 추가 권장
        String champJsonUrl = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/ko_KR/tft-champion.json"; // 한국어 데이터 사용
        try {
            String champJsonStr = sendStaticRequest(champJsonUrl);
            JSONObject champJsonData = new JSONObject(champJsonStr).optJSONObject("data");
            if (champJsonData != null) {
                for (String key : champJsonData.keySet()) {
                	
                	// [추가] 11/4 튜토리얼 건너뛰기
                	if (key.contains("TFTSetTutorial")) {
                        continue;
                    }
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


    /**
     * Riot의 데이터 드래곤 API에서 최신 버전의 아이템 이미지 URL 맵을 가져옵니다.
     * @return Map<아이템ID, 이미지URL>
     * @throws IOException API 호출 실패 시
     */
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

     /**
     * Riot의 데이터 드래곤 API에서 최신 버전의 챔피언 비용(티어) 맵을 가져옵니다.
     * @return Map<챔피언ID, 비용(int)>
     * @throws IOException API 호출 실패 시
     */
    public Map<String, Integer> getChampionCostMap() throws IOException {
        Map<String, Integer> champCostMap = new HashMap<>();
        String version = "15.18.1"; // 예시 버전
        String champJsonUrl = "https://ddragon.leagueoflegends.com/cdn/" + version + "/data/ko_KR/tft-champion.json"; // 한국어 데이터 사용
        try {
            String champJsonStr = sendStaticRequest(champJsonUrl);
            JSONObject champJsonData = new JSONObject(champJsonStr).optJSONObject("data");
            if (champJsonData != null) {
                for (String key : champJsonData.keySet()) {

                	// [추가] 11/4 튜토리얼 건너뛰기
                	if (key.contains("TFTSetTutorial")) {
                        continue;
                    }
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

    /**
     * Riot의 데이터 드래곤 API에서 최신 버전의 시너지 이미지 URL 맵을 가져옵니다.
     * @return Map<시너지ID, 이미지URL>
     * @throws IOException API 호출 실패 시
     */
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
                    // 이미지 URL 구성 (Data Dragon TFT 이미지는 trait 폴더가 아니라 tft-trait sprite 안에 있음, 개별 이미지는 다른 경로일 수 있으나 일단 sprite 기준으로 경로 설정)
                    // 정확한 개별 trait 이미지 경로는 확인 필요 - 여기서는 임시로 champion 이미지 경로 구조를 따름
                     // 주의: Data Dragon의 실제 trait 이미지는 스프라이트 시트에 있거나 다른 경로일 수 있습니다.
                     // 아래 URL은 일반적인 패턴이며, 실제 작동하지 않을 수 있습니다. 정확한 URL 구조 확인이 필요합니다.
                    synergyImageMap.put(key, "https://ddragon.leagueoflegends.com/cdn/" + version + "/img/tft-trait/" + imgFile); // 경로 수정 가능성 높음
                     // 예시: 스프라이트 시트 URL을 저장하거나, Riot CDN의 다른 경로를 사용할 수 있습니다.
                     // 임시로 champion 이미지와 유사한 경로 사용
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

    /**
     * **** 수정됨 ****
     * Riot의 데이터 드래곤 API에서 랭크(Regalia) 이미지 URL 맵을 가져옵니다.
     * @return Map<티어명(소문자), 이미지URL>
     * @throws IOException API 호출 실패 시
     */
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


    /**
     * 데이터 드래곤과 같이 API 키가 필요 없는 정적 데이터 URL에 GET 요청을 보냅니다.
     * @param urlStr 요청할 URL 문자열
     * @return 응답 본문 문자열
     * @throws IOException 요청 실패 또는 비정상 응답 코드 시
     */
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


	/**
	 * Riot API 엔드포인트에 API 키를 포함하여 GET 요청을 보냅니다.
	 * @param urlStr 요청할 URL 문자열 (API 키 제외)
	 * @return 응답 본문 문자열
	 * @throws IOException 요청 실패 또는 비정상 응답 코드 시
	 */
	private String sendGet(String urlStr) throws IOException {
	    HttpURLConnection conn = null;
	    InputStreamReader inputStreamReader = null;
	    BufferedReader br = null;
	    StringBuilder sb = new StringBuilder();
	    int responseCode = -1;
	    boolean isError = false;

	    try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
            conn.setRequestProperty("X-Riot-Token", API_KEY); // Riot API 키 헤더

            responseCode = conn.getResponseCode(); // 응답 코드 먼저 확인
            isError = !(responseCode >= 200 && responseCode < 300);

            if (!isError) { // 성공 시
                inputStreamReader = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
            } else { // 실패 시
                // 에러 스트림이 null일 수 있으므로 주의
                inputStreamReader = (conn.getErrorStream() != null) ?
                                    new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8) :
                                    null; // 에러 스트림 없으면 null
                // System.err.println("Riot API 요청 실패: " + urlStr + ", 응답 코드: " + responseCode); // 로그 제거
            }

            // inputStreamReader가 null이 아닐 때만 읽기 시도
            if (inputStreamReader != null) {
                 br = new BufferedReader(inputStreamReader);
                 String line;
                 while ((line = br.readLine()) != null) {
                     sb.append(line);
                 }
            } else if (isError) {
                 // 에러인데 에러 스트림도 없는 경우 (네트워크 문제 등)
                 sb.append("{\"error\":\"HTTP ").append(responseCode).append(", 응답 본문 없음\"}"); // 간단한 에러 메시지 생성
            }

            // 실패 응답 코드인 경우, 로그 출력 및 예외 발생 또는 특정 값 반환
             if (isError) {
                 // System.err.println("Riot API 응답 본문(오류): " + sb.toString()); // 로그 제거
                 // 404 Not Found 이면서 라이브 게임 조회인 경우, 특정 상태 반환 가능
                 if (responseCode == 404 && urlStr.contains("/active-games/")) {
                     // System.out.println("라이브 게임 정보 없음 (404)"); // 로그 제거
                     // return "{\"status\": \"NOT_IN_GAME\"}"; // 핸들러에서 처리하도록 원본 또는 빈 JSON 반환
                 }
                 // 다른 오류들은 IOException으로 던져서 핸들러가 처리하도록 함
                 throw new IOException("Riot API 요청 실패: " + responseCode + ", 응답: " + sb.toString());
            }
        } finally {
             // 자원 해제
            if (br != null) try { br.close(); } catch (IOException e) { /* 무시 */ }
            if (inputStreamReader != null) try { inputStreamReader.close(); } catch (IOException e) { /* 무시 */ }
            if (conn != null) conn.disconnect();
        }

	    return sb.toString();
	}
	
	// Api.java 클래스의 맨 끝 (sendGet 메소드 닫는 괄호 뒤)에 아래 메소드들을 추가합니다.

    /**
     * DB에 저장된 모든 추천 메타 덱 목록을 가져옵니다.
     * @return List<MetaDeck> 덱 정보 리스트
     * @throws SQLException DB 오류
     * @throws ClassNotFoundException 드라이버 오류
     */
    public List<MetaDeck> getMetaDecks() throws SQLException, ClassNotFoundException {
        List<MetaDeck> metaDecks = new ArrayList<>();
        String deckSql = "SELECT * FROM meta_decks ORDER BY is_hot DESC, deck_name ASC";

        // 1. 모든 덱의 기본 정보 조회
        try (Connection conn = getConnection();
             PreparedStatement pstat = conn.prepareStatement(deckSql);
             ResultSet rs = pstat.executeQuery()) {

            while (rs.next()) {
                MetaDeck deck = new MetaDeck();
                deck.setDeckId(rs.getInt("deck_id"));
                deck.setDeckName(rs.getString("deck_name"));
                deck.setHot(rs.getBoolean("is_hot"));
                deck.setTeamCode(rs.getString("team_code"));
                deck.setGuideLink(rs.getString("guide_link"));
                deck.setTotalCost(rs.getInt("total_cost"));

                // 2. 각 덱에 속한 유닛 정보 조회
                deck.setUnits(getUnitsForMetaDeck(deck.getDeckId()));
                
                // 3. 각 덱에 속한 시너지 정보 조회
                deck.setTraits(getTraitsForMetaDeck(deck.getDeckId()));

                metaDecks.add(deck);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("getMetaDecks DB 오류: " + e.getMessage());
            throw e;
        }
        return metaDecks;
    }

    /**
     * 특정 메타 덱 ID에 해당하는 유닛 목록을 조회합니다. (getMetaDecks 헬퍼)
     * @param deckId 덱 ID
     * @return List<UnitInfo> 유닛 정보 리스트
     * @throws SQLException DB 오류
     * @throws ClassNotFoundException 드라이버 오류
     */
    private List<UnitInfo> getUnitsForMetaDeck(int deckId) throws SQLException, ClassNotFoundException {
        List<UnitInfo> units = new ArrayList<>();
        // ▼▼▼▼▼ 수정된 SQL (컬럼 이름 확인) ▼▼▼▼▼
        String unitSql = "SELECT * FROM meta_deck_units WHERE deck_id = ?";
        // ▲▲▲▲▲ 수정 끝 ▲▲▲▲▲
        
        try (Connection conn = getConnection();
             PreparedStatement pstat = conn.prepareStatement(unitSql)) {
            
            pstat.setInt(1, deckId);
            try (ResultSet rs = pstat.executeQuery()) {
                while (rs.next()) {
                    UnitInfo unit = new UnitInfo();
                    unit.setChampionId(rs.getString("champion_id"));
                    
                    String itemsStr = rs.getString("items");
                    if (itemsStr != null && !itemsStr.isEmpty()) {
                        // 1. 쉼표로 분리
                        String[] itemArray = itemsStr.split(",");
                        // 2. 공백 제거를 위해 새로운 리스트 생성
                        List<String> itemList = new ArrayList<>();
                        for (String item : itemArray) {
                            itemList.add(item.trim()); // 3. trim()으로 공백 제거 후 추가
                        }
                        unit.setItems(itemList); // 4. 공백이 제거된 리스트를 설정
                    } else {
                        unit.setItems(Collections.emptyList());
                    }
                    
                    // ▼▼▼▼▼ 추가된 로직 ▼▼▼▼▼
                    // rs.getInt가 null을 0으로 반환하므로 DB에 0 또는 NULL로 저장되어 있으면 됨
                    unit.setHexRow(rs.getInt("hex_row"));
                    unit.setHexCol(rs.getInt("hex_col"));
                    // ▲▲▲▲▲ 추가 끝 ▲▲▲▲▲
                    
                    // Meta 덱은 성급(tier) 정보가 없으므로 0 (기본값)으로 둡니다.
                    units.add(unit);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("getUnitsForMetaDeck DB 오류: " + e.getMessage());
            throw e;
        }
        return units;
    }

    
    
    /**
     * 특정 메TA 덱 ID에 해당하는 시너지 목록을 조회합니다. (getMetaDecks 헬퍼)
     * @param deckId 덱 ID
     * @return List<TraitInfo> 시너지 정보 리스트
     * @throws SQLException DB 오류
     * @throws ClassNotFoundException 드라이버 오류
     */
    private List<TraitInfo> getTraitsForMetaDeck(int deckId) throws SQLException, ClassNotFoundException {
        List<TraitInfo> traits = new ArrayList<>();
        String traitSql = "SELECT * FROM meta_deck_synergies WHERE deck_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstat = conn.prepareStatement(traitSql)) {
            
            pstat.setInt(1, deckId);
            try (ResultSet rs = pstat.executeQuery()) {
                while (rs.next()) {
                    TraitInfo trait = new TraitInfo();
                    trait.setName(rs.getString("synergy_id"));
                    trait.setStyle(rs.getInt("style"));
                    traits.add(trait);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("getTraitsForMetaDeck DB 오류: " + e.getMessage());
            throw e;
        }
        
        // 시너지 정렬 (UserQueryHandler에서 사용한 로직과 동일하게)
        if (!traits.isEmpty()) {
            Collections.sort(traits, new Comparator<TraitInfo>() {
                @Override
                public int compare(TraitInfo t1, TraitInfo t2) {
                    int style1Mapped = mapStyleForSort(t1.getStyle());
                    int style2Mapped = mapStyleForSort(t2.getStyle());
                    return Integer.compare(style2Mapped, style1Mapped);
                }
                private int mapStyleForSort(int style) {
                    switch (style) {
                        case 5: return 5; // Prism
                        case 3: return 4; // Unique
                        case 4: return 3; // Gold
                        case 2: return 2; // Silver
                        case 1: return 1; // Bronze
                        default: return 0;
                    }
                }
            });
        }
        return traits;
    }
    
    // ▼▼▼▼▼ 새로 추가된 메서드 ▼▼▼▼▼
    /**
     * DB에서 특정 ID의 메타 덱 정보를 1개 가져옵니다.
     * @param deckId 조회할 덱의 ID
     * @return MetaDeck 객체 (유닛, 특성 포함), 없으면 null
     * @throws SQLException DB 오류
     * @throws ClassNotFoundException 드라이버 오류
     */
    public MetaDeck getMetaDeckById(int deckId) throws SQLException, ClassNotFoundException {
        MetaDeck deck = null;
        String deckSql = "SELECT * FROM meta_decks WHERE deck_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstat = conn.prepareStatement(deckSql)) {
            
            pstat.setInt(1, deckId);
            
            try (ResultSet rs = pstat.executeQuery()) {
                if (rs.next()) {
                    deck = new MetaDeck();
                    deck.setDeckId(rs.getInt("deck_id"));
                    deck.setDeckName(rs.getString("deck_name"));
                    deck.setHot(rs.getBoolean("is_hot"));
                    deck.setTeamCode(rs.getString("team_code"));
                    deck.setGuideLink(rs.getString("guide_link"));
                    deck.setTotalCost(rs.getInt("total_cost"));

                    // 2. 덱에 속한 유닛 정보 조회 (hex_row, hex_col 포함)
                    deck.setUnits(getUnitsForMetaDeck(deck.getDeckId()));
                    
                    // 3. 덱에 속한 시너지 정보 조회
                    deck.setTraits(getTraitsForMetaDeck(deck.getDeckId()));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("getMetaDeckById DB 오류: " + e.getMessage());
            throw e;
        }
        return deck; // 덱을 찾았으면 deck 객체, 못 찾았으면 null 반환
    }
    // ▲▲▲▲▲ 추가 끝 ▲▲▲▲▲
	public String getChallengerDeckSummary() throws SQLException, ClassNotFoundException {
	    
	    StringBuilder summary = new StringBuilder();
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    
	    int latestFetchId = -1;

	    try {
	        conn = getConnection(); // [cite: Api.java]

	        // 1. meta_fetch_log에서 가장 최신 fetch_id 가져오기
	        String fetchIdSql = "SELECT MAX(fetch_id) AS latest_id FROM meta_fetch_log"; // [cite: tftgg_meta_fetch_log.sql]
	        pstmt = conn.prepareStatement(fetchIdSql);
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            latestFetchId = rs.getInt("latest_id");
	        }
	        
	        if (latestFetchId == -1) {
	            return "## 챌린저 메타 데이터\n\n아직 분석된 메타 데이터가 없습니다.";
	        }
	        
	        summary.append("## 챌린저 메타 분석 (DB 기반, ID: ").append(latestFetchId).append(")\n\n");

	        // 2. 1티어 시너지 (Top 5) 가져오기 (tft_synergies와 JOIN)
	        String synergySql = "SELECT " +
	                            "    COALESCE(t.korean_name, s.synergy_id) AS display_name, " + // [cite: tftgg_tft_synergies.sql]
	                            "    s.pick_rate " + // [cite: tftgg_challenger_meta_synergies.sql]
	                            "FROM " +
	                            "    challenger_meta_synergies s " + // [cite: tftgg_challenger_meta_synergies.sql]
	                            "LEFT JOIN " +
	                            "    tft_synergies t ON s.synergy_id = t.synergy_id " + // [cite: tftgg_tft_synergies.sql]
	                            "WHERE " +
	                            "    s.fetch_id = ? " +
	                            "ORDER BY " +
	                            "    s.meta_rank ASC LIMIT 5";
	        
	        pstmt.close();
	        pstmt = conn.prepareStatement(synergySql);
	        pstmt.setInt(1, latestFetchId);
	        rs.close();
	        rs = pstmt.executeQuery();
	        
	        summary.append("### 1티어 시너지 (Top 5)\n");
	        while (rs.next()) {
	            summary.append(String.format("* **%s** (픽률: %.2f%%)\n", 
	                             rs.getString("display_name"), rs.getDouble("pick_rate"))); // 🌟 korean_name 사용
	        }
	        summary.append("\n");

	        // 3. 1티어 챔피언 (Top 10) 및 BIS 아이템 가져오기 (tft_items와 JOIN)
	        String champItemSql = 
	            "SELECT " +
	            "    c.champion_name, " + // [cite: tftgg_challenger_meta_champions.sql]
	            "    c.pick_rate AS champ_pick_rate, " + // [cite: tftgg_challenger_meta_champions.sql]
	            "    COALESCE(it.korean_name, i.item_id) AS item_display_name, " + // [cite: tftgg_tft_items.sql]
	            "    i.pick_rate_on_champ " + // [cite: tftgg_challenger_meta_item_distribution.sql]
	            "FROM " +
	            "    challenger_meta_champions c " + // [cite: tftgg_challenger_meta_champions.sql]
	            "LEFT JOIN " +
	            "    challenger_meta_item_distribution i ON c.fetch_id = i.fetch_id AND c.champion_name = i.champion_name " + // [cite: tftgg_challenger_meta_item_distribution.sql]
	            "LEFT JOIN " +
	            "    tft_items it ON i.item_id = it.item_id " + // 🌟 [수정] tft_items 테이블 JOIN [cite: tftgg_tft_items.sql]
	            "WHERE " +
	            "    c.fetch_id = ? AND c.meta_rank <= 10 AND (i.rank_on_champ <= 3 OR i.rank_on_champ IS NULL) " +
	            "ORDER BY " +
	            "    c.meta_rank ASC, i.rank_on_champ ASC";
	            
	        pstmt.close();
	        pstmt = conn.prepareStatement(champItemSql);
	        pstmt.setInt(1, latestFetchId);
	        rs.close();
	        rs = pstmt.executeQuery();

	        summary.append("### 1티어 챔피언 (Top 10) 및 핵심 아이템 (BIS)\n");
	        
	        Map<String, List<String>> champItemsMap = new HashMap<>();
	        Map<String, Double> champPickRateMap = new HashMap<>();
	        List<String> orderedChamps = new ArrayList<>(); 

	        while (rs.next()) {
	            String champName = rs.getString("champion_name");
	            String itemName = rs.getString("item_display_name"); // 🌟 item_id 대신 item_display_name 사용
	            
	            if (!champItemsMap.containsKey(champName)) {
	                champItemsMap.put(champName, new ArrayList<>());
	                champPickRateMap.put(champName, rs.getDouble("champ_pick_rate"));
	                orderedChamps.add(champName);
	            }
	            
	            if (itemName != null) {
	                // 🌟 [수정] .replace() 제거
	                champItemsMap.get(champName).add(String.format("%s (%.2f%%)", 
	                    itemName, rs.getDouble("pick_rate_on_champ")));
	            }
	        }
	        
	        for (String champName : orderedChamps) {
	            summary.append(String.format("* **%s** (픽률: %.2f%%)\n", champName, champPickRateMap.get(champName)));
	            List<String> items = champItemsMap.get(champName);
	            if (items.isEmpty()) {
	                summary.append("    * (핵심 아이템 데이터 없음)\n");
	            } else {
	                summary.append("    * BIS: ").append(items.stream().collect(Collectors.joining(", "))).append("\n");
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e; 
	    } finally {
	        if (rs != null) rs.close();
	        if (pstmt != null) pstmt.close();
	        if (conn != null) conn.close();
	    }
	    
	    return summary.toString();
	}
}