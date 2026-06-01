package VexVisuals.module;

public enum Category {
    HUD("HUD / Интерфейс"),
    COMBAT_VISUALS("Combat Visuals"),
    INDICATORS("Indicators & Graphs"),
    COSMETICS("Cosmetics"),
    WORLD_STYLE("World Style"),
    SCREEN_CAMERA_CHAT("Screen, Camera & Chat");
    MUSIC("Music");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
