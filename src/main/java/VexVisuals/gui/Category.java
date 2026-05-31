package com.vexvisual.gui;

public enum Category {
    COMBAT("Combat"),
    RENDER("Render"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    MISC("Misc"),
    MUSIC("Music"); // Специальная категория для Spotify

    public final String name;

    Category(String name) {
        this.name = name;
    }
}
