package lolaigg;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MetaHandler implements CommandHandler {

    private Api api;

    public MetaHandler() {
        api = new Api();
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {

        // --- 1. Data Dragon 이미지 맵 로드 (user_result.jsp와 재사용하기 위해) ---
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
            System.err.println("데이터 드래곤 API 호출 중 오류 발생(MetaHandler): " + e.getMessage());
            request.setAttribute("warningMessage", "챔피언/아이템/시너지 이미지 정보를 불러오는데 실패했습니다.");
        }
        
        // JSP로 전달
        request.setAttribute("championImageMap", championImageMap);
        request.setAttribute("itemImageMap", itemImageMap);
        request.setAttribute("championCostMap", championCostMap);
        request.setAttribute("synergyImageMap", synergyImageMap);

        // --- 2. DB에서 메타 덱 목록 조회 ---
        try {
            List<MetaDeck> metaDecks = api.getMetaDecks();
            request.setAttribute("metaDecks", metaDecks);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("DB에서 메타 덱 조회 중 오류: " + e.getMessage());
            request.setAttribute("errorMessage", "추천 덱 목록을 불러오는 중 오류가 발생했습니다.");
        }

        return "meta_list"; // meta_list.jsp로 포워딩
    }
}