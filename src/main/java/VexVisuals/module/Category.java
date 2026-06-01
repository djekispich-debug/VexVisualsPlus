package VexVisuals.module;

public enum Category {
    // Основные категории для ClickGui
    COMBAT("Combat"),
    RENDER("Render"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    MISC("Misc"),
    MUSIC("Music"),

    // Дополнительные категории для ModuleType
    HUD("HUD"),
    COMBAT_VISUALS("Combat Visuals"),
    INDICATORS("Indicators"),
    COSMETICS("Cosmetics"),
    WORLD_STYLE("World Style");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return name;
    }

    public String getName() {
        return name;
    }
}
