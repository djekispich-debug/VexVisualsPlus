package com.VexVisuals.gui;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {
    private static final List<GUITheme> themes = new ArrayList<>();
    private static int currentIndex = 0;

    static {
        themes.add(GUITheme.DARK);
        themes.add(GUITheme.PURPLE);
        themes.add(GUITheme.RED);
        // Добавляй новые темы здесь
    }

    // ---------- Управление темами ----------

    public static GUITheme getCurrentTheme() {
        return themes.get(currentIndex);
    }

    /** Возвращает текущую тему как объект (для старого кода, если нужно) */
    public static GUITheme getCurrent() {
        return getCurrentTheme();
    }

    public static void nextTheme() {
        currentIndex = (currentIndex + 1) % themes.size();
    }

    /** То же, что nextTheme(), для обратной совместимости */
    public static void cycleTheme() {
        nextTheme();
    }

    public static List<GUITheme> getAllThemes() {
        return themes;
    }

    // ---------- Быстрый доступ к цветам через статику ----------

    /** Фон всего экрана (за панелью) */
    public static int background() {
        return getCurrentTheme().background.getRGB();
    }

    /** Основной цвет панели */
    public static int panel() {
        return getCurrentTheme().panel.getRGB();
    }

    /** Акцентный / основной цвет (primary) */
    public static int accent() {
        return getCurrentTheme().primary.getRGB();
    }

    /** Вторичный акцентный цвет (secondary) */
    public static int secondary() {
        return getCurrentTheme().secondary.getRGB();
    }

    /** Основной цвет текста */
    public static int text() {
        return getCurrentTheme().text.getRGB();
    }

    /** Приглушённый текст (textSecondary) */
    public static int textMuted() {
        return getCurrentTheme().textSecondary.getRGB();
    }

    /** Цвет рамок / бордеров */
    public static int border() {
        return getCurrentTheme().border.getRGB();
    }

    /** Цвета для градиентного заголовка */
    public static int headerGradientTop() {
        // Осветлённый panel
        return brighter(getCurrentTheme().panel.getRGB(), 0.2f);
    }

    public static int headerGradientBottom() {
        return getCurrentTheme().panel.getRGB();
    }

    // ---------- Вспомогательные методы ----------

    private static int brighter(int color, float factor) {
        int r = Math.min(255, (int) ((color >> 16 & 0xFF) * (1 + factor)));
        int g = Math.min(255, (int) ((color >> 8 & 0xFF) * (1 + factor)));
        int b = Math.min(255, (int) ((color & 0xFF) * (1 + factor)));
        return (color & 0xFF000000) | (r << 16) | (g << 8) | b;
    }
}
