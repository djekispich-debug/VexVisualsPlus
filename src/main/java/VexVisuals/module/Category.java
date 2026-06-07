package VexVisuals.module;

public enum Category {
    HUD("HUD", 0xFF6C5CE7),
    COMBAT_VISUALS("Combat Visuals", 0xFFE74C3C),
    INDICATORS("Indicators", 0xFF3498DB),
    COSMETICS("Cosmetics", 0xFFF39C12),
    WORLD_STYLE("World Style", 0xFF2ECC71),
    SCREEN_CAMERA_CHAT("Screen & Camera", 0xFF9B59B6);

    private final String displayName;
    private final int color;

    Category(String displayName, int color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getColor() {
        return color;
    }
}
