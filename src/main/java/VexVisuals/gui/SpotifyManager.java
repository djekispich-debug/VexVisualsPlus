package com.vexvisual.spotify;

import com.vexvisual.gui.DrawContext;
import net.minecraft.text.Text;

public class SpotifyManager {
    private static SpotifyManager instance;
    private boolean connected = false;
    private String currentTrack = "Not playing";
    private boolean isPlaying = false;

    public static SpotifyManager getInstance() {
        if (instance == null) instance = new SpotifyManager();
        return instance;
    }

    public void connect() {
        // TODO: Реализовать через Spotify Web API + HttpClient
        connected = true;
        // Можно использовать библиотеку spotify-web-api-java
    }

    public void togglePlay() {
        if (connected) isPlaying = !isPlaying;
    }

    public void next() { /* API call */ }
    public void previous() { /* API call */ }

    public void render(DrawContext ctx, int x, int y, int mouseX, int mouseY) {
        // Красивый Spotify блок
        ctx.drawCenteredTextWithShadow(mc.textRenderer, "§b♫ " + currentTrack, x + 180, y, 0xFFFFFF);

        // Кнопки Play/Pause, Next, Prev
        // ... (можно сделать отдельными Button)
    }
}
