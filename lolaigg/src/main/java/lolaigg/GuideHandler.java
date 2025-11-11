package lolaigg;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GuideHandler implements CommandHandler {

    private Api api;

    public GuideHandler() {
        api = new Api();
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {

        String deckIdParam = request.getParameter("deckId");
        int deckId = 0;

        try {
            deckId = Integer.parseInt(deckIdParam);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "잘못된 덱 ID입니다.");
            return "guide_detail"; // 오류 페이지로
        }

        // --- 1. Data Dragon 이미지 맵 로드 (meta_list와 동일) ---
        Map<String, String> championImageMap = Collections.emptyMap();
        Map<String, String> itemImageMap = Collections.emptyMap();
        Map<String, Integer> championCostMap = Collections.emptyMap();
        Map<String, String> synergyImageMap = Collections.emptyMap();

        try {
            championImageMap = api.getChampionImageMap();
            itemImageMap = api.getItemImageMap();
            championCostMap = api.getChampionCostMap();
            synergyImageMap = api.getSynergyImageMap();
        } catch (IOException e) {
            System.err.println("데이터 드래곤 API 호출 중 오류 발생(GuideHandler): " + e.getMessage());
            request.setAttribute("warningMessage", "챔피언/아이템/시너지 이미지 정보를 불러오는데 실패했습니다.");
        }
        
        // JSP로 전달
        request.setAttribute("championImageMap", championImageMap);
        request.setAttribute("itemImageMap", itemImageMap);
        request.setAttribute("championCostMap", championCostMap);
        request.setAttribute("synergyImageMap", synergyImageMap);

        // --- 2. DB에서 특정 메타 덱 조회 ---
        try {
            MetaDeck deck = api.getMetaDeckById(deckId);
            if (deck != null) {
                request.setAttribute("deck", deck);
            } else {
                request.setAttribute("errorMessage", "덱 정보를 찾을 수 없습니다.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("DB에서 덱 상세 조회 중 오류: " + e.getMessage());
            request.setAttribute("errorMessage", "덱 상세 정보를 불러오는 중 오류가 발생했습니다.");
        }

        return "guide_detail"; // guide_detail.jsp로 포워딩
    }
}