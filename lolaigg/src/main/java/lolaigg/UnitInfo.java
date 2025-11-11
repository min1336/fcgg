package lolaigg;

import java.util.ArrayList;
import java.util.List;

public class UnitInfo {
    private String championId;
    private int tier; // 성급 (1, 2, 3)
    private List<String> items = new ArrayList<>(); // 아이템 ID 목록
    
    // ▼▼▼▼▼ 추가된 필드 ▼▼▼▼▼
    private int hexRow; // 배치 행 (1-4)
    private int hexCol; // 배치 열 (1-7)
    // ▲▲▲▲▲ 추가 끝 ▲▲▲▲▲

    // Getters and Setters
    public String getChampionId() {
        return championId;
    }
    public void setChampionId(String championId) {
        this.championId = championId;
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
    
    // ▼▼▼▼▼ 추가된 Getter/Setter ▼▼▼▼▼
    public int getHexRow() {
        return hexRow;
    }
    public void setHexRow(int hexRow) {
        this.hexRow = hexRow;
    }
    public int getHexCol() {
        return hexCol;
    }
    public void setHexCol(int hexCol) {
        this.hexCol = hexCol;
    }
    // ▲▲▲▲▲ 추가 끝 ▲▲▲▲▲
}