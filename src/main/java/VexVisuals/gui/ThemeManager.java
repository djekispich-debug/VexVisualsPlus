package VexVisuals.gui;

public final class ThemeManager {
    private static GUITheme currentTheme = GUITheme.DARK;

    private ThemeManager() {
    }

    public static GUITheme getCurrentTheme() {
        return currentTheme;
    }

    public static void setCurrentTheme(GUITheme theme) {
        if (theme != null) {
            currentTheme = theme;
        }
    }

    public static void setThemeByName(String name) {
        switch (name.toLowerCase()) {
            case "purple" -> currentTheme = GUITheme.PURPLE;
            case "red" -> currentTheme = GUITheme.RED;
            case "dark" -> currentTheme = GUITheme.DARK;
            default -> currentTheme = GUITheme.DARK;
        }
    }

    public static String getCurrentThemeName() {
        return currentTheme.name;
    }
}
