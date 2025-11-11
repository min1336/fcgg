package lolaigg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatHandler implements CommandHandler {

    // API 키 (테스트를 위해 하드코딩)
    // 경고: 이 키를 GitHub 등 공개된 장소에 절대 커밋하면 안 됩니다!
    private static final String API_KEY = "AIzaSyCA9s4S_OFBhfGWYZ30se_jFvkM17QDBjk";

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        
        String method = request.getMethod();

        if (method.equalsIgnoreCase("GET")) {
            return "chat_form"; // -> /WEB-INF/views/chat_form.jsp
            
        } else if (method.equalsIgnoreCase("POST")) {
            request.setCharacterEncoding("UTF-8");
            
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            JSONObject requestJson = new JSONObject(sb.toString());
            String userMessage = requestJson.getString("message");

            String aiResponse;
            
            try {
                // 1. Api 객체 생성
                Api api = new Api();
                
                // 2. DB에서 '정답지' 데이터 가져오기 (Api.java에 추가한 메서드)
                String challengerDeckData = api.getChallengerDeckSummary(); 
                
                // 🌟 [핵심 수정] 🌟
                // AI에게 상황별로 다르게 행동하도록 지시하는 프롬프트
                String prompt = "당신은 '전략적 팀 전투(TFT)' 덱 추천 전문가입니다.\n\n" +
                                "아래는 참고용 최신 챌린저 랭커 메타 데이터('정답지')입니다.\n" +
                                "--- (참고용 메타 데이터 시작) ---\n" +
                                challengerDeckData + "\n" +
                                "--- (참고용 메타 데이터 끝) ---\n\n" +
                                "이제 사용자의 질문에 답해주세요. 지침은 다음과 같습니다:\n" +
                                "1. **[일반 추천 요청]**: 사용자가 '덱 추천해줘', '뭐가 좋아?', '1티어 덱 알려줘' 등 **일반적인 덱 추천**을 원한다면, 위 '참고용 메타 데이터'를 **핵심 근거**로 삼아 1티어 덱을 추천해주세요.\n" +
                                "2. **[특정 덱 요청]**: 사용자가 '애니 덱', '펑크 리롤 덱', '수정 갬빗'처럼 **특정 기물이나 시너지를 명시**했다면, 메타 데이터에 없더라도 **사용자가 요청한 덱을 구성하는 방법**을 최우선으로 알려주세요. (이때 메타 데이터는 아이템 조합 시 참고만 하세요.)\n" +
                                "3. **[인사 또는 불명확한 요청]**: 사용자의 메시지가 '안녕', 'ㅎㅇ', 'ㄴ', 'ㅇㅇ', '네' 등 **단순한 인사, 대답이거나 무슨 말인지 모르겠다면**, 덱을 추천하지 말고, \"안녕하세요! 어떤 덱을 추천해 드릴까요?\" 또는 \"어떤 점이 궁금하신가요?\"처럼 대화를 이어가는 **인사말**을 하세요.\n\n" + 
                                "사용자 질문: " + userMessage;
                
                // 4. 강화된 프롬프트로 AI 호출
                aiResponse = callGeminiAPI(prompt);
                
            } catch (Exception e) {
                e.printStackTrace();
                // [수정] DB 조회 오류도 포함할 수 있도록 예외 메시지 수정
                aiResponse = "죄송합니다. AI 응답 서버 또는 DB 조회 중 오류가 발생했습니다: " + e.getMessage();
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            JSONObject responseJson = new JSONObject();
            responseJson.put("reply", aiResponse);

            response.getWriter().write(responseJson.toString());

            return null; // AJAX 핸들러
        }
        
        return null;
    }

    private String callGeminiAPI(String prompt) throws IOException {
        
        String urlStr = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-09-2025:generateContent?key=" + API_KEY;
        String responseString = "";

        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject part = new JSONObject();
            part.put("text", prompt);
            JSONArray partsArray = new JSONArray();
            partsArray.put(part);
            JSONObject content = new JSONObject();
            content.put("parts", partsArray);
            JSONArray contentsArray = new JSONArray();
            contentsArray.put(content);
            JSONObject requestBody = new JSONObject();
            requestBody.put("contents", contentsArray);
            
            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8")) {
                writer.write(requestBody.toString());
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
                    StringBuilder responseSb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        responseSb.append(line);
                    }
                    responseString = responseSb.toString();
                }
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"))) {
                    StringBuilder errorSb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorSb.append(line);
                    }
                    System.err.println("Gemini API Error: " + errorSb.toString());
                    return "Gemini API 호출 실패. 응답 코드: " + responseCode + ", 내용: " + errorSb.toString();
                } catch (Exception e) {
                     return "Gemini API 호출 실패. 응답 코드: " + responseCode;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "API 호출 중 예외 발생: " + e.getMessage();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            JSONObject jsonResponse = new JSONObject(responseString);
            if (!jsonResponse.has("candidates")) {
                 System.err.println("API 응답에 'candidates'가 없음: " + responseString);
                 return "API 응답 형식이 잘못되었습니다. (No 'candidates')";
            }
            String aiText = jsonResponse.getJSONArray("candidates")
                                        .getJSONObject(0)
                                        .getJSONObject("content")
                                        .getJSONArray("parts")
                                        .getJSONObject(0)
                                        .getString("text");
            
            return aiText; 
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("API 응답 파싱 실패: " + responseString);
            return "API 응답 파싱 실패.";
        }
    }
}