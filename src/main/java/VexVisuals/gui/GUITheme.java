package com.VexVisuals.gui;

import java.awt.Color;

public class GUITheme {
    public final String name;
    public final Color background;
    public final Color panel;
    public final Color primary;
    public final Color secondary;
    public final Color text;
    public final Color textSecondary;
    public final Color border;
    public final int radius;

    public GUITheme(String name, Color bg, Color panel, Color primary, Color secondary,
                    Color text, Color textSec, Color border, int radius) {
        this.name = name;
        this.background = bg;
        this.panel = panel;
        this.primary = primary;
        this.secondary = secondary;
        this.text = text;
        this.textSecondary = textSec;
        this.border = border;
        this.radius = radius;
    }

    // Популярные темы
    public static final GUITheme DARK = new GUITheme(
            "Dark", 
            new Color(18, 18, 22), 
            new Color(28, 28, 35), 
            new Color(0, 122, 255), 
            new Color(138, 43, 226),
            new Color(240, 240, 245),
            new Color(170, 170, 180),
            new Color(45, 45, 55),
            10
    );

    public static final GUITheme PURPLE = new GUITheme(
            "Purple", 
            new Color(20, 15, 35), 
            new Color(35, 25, 60), 
            new Color(180, 80, 255), 
            new Color(255, 60, 180),
            Color.WHITE,
            new Color(200, 180, 255),
            new Color(80, 50, 120),
            12
    );

    public static final GUITheme RED = new GUITheme(
            "Red PVP", 
            new Color(22, 16, 16), 
            new Color(40, 25, 25), 
            new Color(255, 60, 60), 
            new Color(255, 140, 0),
            Color.WHITE,
            new Color(220, 180, 180),
            new Color(70, 30, 30),
            8
    );
}
