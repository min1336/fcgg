package lolaigg;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserLiveQueryHandler implements CommandHandler {
	private Api api;
	
	public UserLiveQueryHandler() {
		api = new Api();
	}

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException, ClassNotFoundException {
		
		String summonerName = request.getParameter("summonerName");
		String summonerTag = request.getParameter("summonerTag");
		
		JSONObject apiResult = api.getSummonerByName(summonerName, summonerTag);
		
		String puuid = apiResult.getString("puuid");
		JSONObject liveData = api.getSummonerLive(puuid);
		
		JSONArray participants = liveData.getJSONArray("participants");

		// puuid만 추출
		List<String> puuidList = new ArrayList<>();
		for (int i = 0; i < participants.length(); i++) {
		    JSONObject player = participants.getJSONObject(i);
		    String playerpuuid = player.getString("puuid");
		    puuidList.add(playerpuuid);
		}

		// request에 넘기기 (뷰에서 출력 가능)
		request.setAttribute("puuidList", puuidList);
	    
		return "user_liveResult";
	}
}

