package lolaigg;

public class Summoner {
	private Long id;
	private String summonerName;
    private String summonerTag;
    private String puuid;
    private String dataJson;     // Riot API에서 받은 전체 JSON
    private String lovecham;
    private String lovesyn;
    
    public String getLovecham() {
		return lovecham;
	}
	public void setLovecham(String lovecham) {
		this.lovecham = lovecham;
	}
	public String getLovesyn() {
		return lovesyn;
	}
	public void setLovesyn(String lovesyn) {
		this.lovesyn = lovesyn;
	}
	private java.sql.Timestamp lastUpdated;
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSummonerName() {
		return summonerName;
	}
	public void setSummonerName(String summonerName) {
		this.summonerName = summonerName;
	}
	public String getSummonerTag() {
		return summonerTag;
	}
	public void setSummonerTag(String summonerTag) {
		this.summonerTag = summonerTag;
	}
	public String getPuuid() {
		return puuid;
	}
	public void setPuuid(String puuid) {
		this.puuid = puuid;
	}
	public String getDataJson() {
		return dataJson;
	}
	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}
	public java.sql.Timestamp getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(java.sql.Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
