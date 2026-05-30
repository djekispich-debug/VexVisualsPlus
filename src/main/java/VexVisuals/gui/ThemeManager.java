package VexVisuals.gui;

public final class ThemeManager {
    private static GUITheme current = GUITheme.DARK;

    private ThemeManager() {
    }

    public static GUITheme getCurrent() {
        return current;
    }

    public static void setTheme(GUITheme theme) {
        current = theme;
    }

    public static void cycleTheme() {
        GUITheme[] values = GUITheme.values();
        current = values[(current.ordinal() + 1) % values.length];
    }

    public static int background() {
        return switch (current) {
            case LIGHT -> 0xE8F0F8FF;
            case CYBERPUNK -> 0xD0101828;
            case SAKURA -> 0xD0281A24;
            default -> 0xD0121218;
        };
    }

    public static int panel() {
        return switch (current) {
            case LIGHT -> 0xF5FFFFFF;
            case CYBERPUNK -> 0xF0182030;
            case SAKURA -> 0xF032242C;
            default -> 0xF01A1A22;
        };
    }

    public static int text() {
        return switch (current) {
            case LIGHT -> 0xFF1A1A24;
            case CYBERPUNK -> 0xFFE0FFFF;
            case SAKURA -> 0xFFFFF0F5;
            default -> 0xFFF2F2F7;
        };
    }

    public static int textMuted() {
        return switch (current) {
            case LIGHT -> 0xFF6B7280;
            case CYBERPUNK -> 0xFF7DD3FC;
            case SAKURA -> 0xFFF9A8D4;
            default -> 0xFF9CA3AF;
        };
    }

    public static int accent() {
        return switch (current) {
            case LIGHT -> 0xFF2563EB;
            case CYBERPUNK -> 0xFF00F5FF;
            case SAKURA -> 0xFFFF6B9D;
            default -> 0xFF8B5CF6;
        };
    }

    public static int border() {
        return withAlpha(accent(), 0x80);
    }

    public static int headerGradientTop() {
        return withAlpha(accent(), 0x55);
    }

    public static int headerGradientBottom() {
        return withAlpha(background(), 0xFF);
    }

    private static int withAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }
}
