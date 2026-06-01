package VexVisuals.module;

public enum Category {
    COMBAT("Combat"),
    RENDER("Render"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    MISC("Misc"),
    MUSIC("Music");

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
