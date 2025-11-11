package lolaigg;

public class TraitInfo {
    private String name; // 시너지 이름 (예: "Set10_KDA")
    private int style;   // 시너지 등급 (0=비활성, 1=브론즈, 2=실버, 3=고유, 4=골드, 5=프리즘)

    // Getter 및 Setter 메소드
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getStyle() {
        return style;
    }
    public void setStyle(int style) {
        this.style = style;
    }
}