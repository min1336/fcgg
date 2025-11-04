package lolaigg;

import java.util.ArrayList;
import java.util.List;

public class UnitInfo {
    private String championId;
    private String championName;
	private int tier; // 성급 (1, 2, 3)
    private List<String> items = new ArrayList<>(); // 아이템 ID 목록

    // Getters and Setters
    public String getChampionId() {
        return championId;
    }
    public void setChampionId(String championId) {
        this.championId = championId;
    }
    public String getChampionName() {
		return championName;
	}
	public void setChampionName(String championName) {
		this.championName = championName;
	}
    public int getTier() {
        return tier;
    }
    public void setTier(int tier) {
        this.tier = tier;
    }
    public List<String> getItems() {
        return items;
    }
    public void setItems(List<String> items) {
        this.items = items;
    }
}