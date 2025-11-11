package lolaigg;

import java.util.List;
import java.util.ArrayList;

public class MetaDeck {

    private int deckId;
    private String deckName;
    private boolean isHot;
    private String teamCode;
    private String guideLink;
    private int totalCost;
    
    // 기존 DTO 재사용
    private List<UnitInfo> units = new ArrayList<>();
    private List<TraitInfo> traits = new ArrayList<>();
    
    // --- Getters and Setters ---
    
    public int getDeckId() {
        return deckId;
    }
    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }
    public String getDeckName() {
        return deckName;
    }
    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }
    public boolean isHot() {
        return isHot;
    }
    public void setHot(boolean isHot) {
        this.isHot = isHot;
    }
    public String getTeamCode() {
        return teamCode;
    }
    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }
    public String getGuideLink() {
        return guideLink;
    }
    public void setGuideLink(String guideLink) {
        this.guideLink = guideLink;
    }
    public int getTotalCost() {
        return totalCost;
    }
    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }
    public List<UnitInfo> getUnits() {
        return units;
    }
    public void setUnits(List<UnitInfo> units) {
        this.units = units;
    }
    public List<TraitInfo> getTraits() {
        return traits;
    }
    public void setTraits(List<TraitInfo> traits) {
        this.traits = traits;
    }
}