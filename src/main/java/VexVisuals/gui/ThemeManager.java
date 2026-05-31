package com.yourmod.gui.theme;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {
    private static final List<GUITheme> themes = new ArrayList<>();
    private static int currentIndex = 0;

    static {
        themes.add(GUITheme.DARK);
        themes.add(GUITheme.PURPLE);
        themes.add(GUITheme.RED);
        // Добавляй свои темы сюда
    }

    public static GUITheme getCurrentTheme() {
        return themes.get(currentIndex);
    }

    public static void nextTheme() {
        currentIndex = (currentIndex + 1) % themes.size();
    }

    public static List<GUITheme> getAllThemes() {
        return themes;
    }
}
