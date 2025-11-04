package lolaigg;

import java.util.ArrayList;
import java.util.List;

public class SummonerMatch {
	private String matchId;
    private int placement;         // 최종 순위
    private int level;             // 최종 레벨
    private int totalDamageToPlayers; // 플레이어에게 입힌 총 피해량
    private int goldLeft;          // 라운드 종료 시 남은 골드
    
    // 기물(유닛) 정보
    private List<UnitInfo> units = new ArrayList<>(); 
	// 시너지(특성) 정보를 담을 리스트 (예: "Bruiser(4)", "A.D.M.I.N.(2)")
    private List<String> traits = new ArrayList<>();
    
    public List<UnitInfo> getUnits() {
		return units;
	}
	public void setUnits(List<UnitInfo> units) {
		this.units = units;
	}
	public List<String> getTraits() {
		return traits;
	}
	public void setTraits(List<String> traits) {
		this.traits = traits;
	}
	public String getMatchId() {
		return matchId;
	}
	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}
	public int getPlacement() {
		return placement;
	}
	public void setPlacement(int placement) {
		this.placement = placement;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getTotalDamageToPlayers() {
		return totalDamageToPlayers;
	}
	public void setTotalDamageToPlayers(int totalDamageToPlayers) {
		this.totalDamageToPlayers = totalDamageToPlayers;
	}
	public int getGoldLeft() {
		return goldLeft;
	}
	public void setGoldLeft(int goldLeft) {
		this.goldLeft = goldLeft;
	}
}
