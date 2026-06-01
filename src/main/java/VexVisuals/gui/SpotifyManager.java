package VexVisuals.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpotifyManager {
    private static SpotifyManager instance;

    private boolean connected = false;
    private boolean isPlaying = false;
    private int currentTrackIndex = 0;
    private long trackStartTime = 0;
    private int trackDurationMs = 180000;

    private final List<Track> playlist = new ArrayList<>();
    private final Random random = new Random();

    public static class Track {
        public final String title;
        public final String artist;
        public final int durationMs;

        public Track(String title, String artist, int durationMs) {
            this.title = title;
            this.artist = artist;
            this.durationMs = durationMs;
        }

        public String getFullName() {
            return artist + " - " + title;
        }
    }

    private SpotifyManager() {
        playlist.add(new Track("Blinding Lights", "The Weeknd", 200000));
        playlist.add(new Track("Shape of You", "Ed Sheeran", 233000));
        playlist.add(new Track("Bohemian Rhapsody", "Queen", 355000));
        playlist.add(new Track("Stairway to Heaven", "Led Zeppelin", 482000));
        playlist.add(new Track("Hotel California", "Eagles", 391000));
        playlist.add(new Track("Imagine", "John Lennon", 186000));
        playlist.add(new Track("Smells Like Teen Spirit", "Nirvana", 301000));
        playlist.add(new Track("Billie Jean", "Michael Jackson", 294000));
    }

    public static SpotifyManager getInstance() {
        if (instance == null) instance = new SpotifyManager();
        return instance;
    }

    public void connect() {
        connected = true;
        if (!playlist.isEmpty()) {
            currentTrackIndex = 0;
            trackStartTime = System.currentTimeMillis();
            trackDurationMs = playlist.get(currentTrackIndex).durationMs;
        }
    }

    public boolean isConnected() { return connected; }

    public String getCurrentTrack() {
        if (!connected || playlist.isEmpty()) return "Not connected";
        return playlist.get(currentTrackIndex).getFullName();
    }

    public String getCurrentArtist() {
        if (!connected || playlist.isEmpty()) return "";
        return playlist.get(currentTrackIndex).artist;
    }

    public boolean isPlaying() { return connected && isPlaying; }

    public void togglePlay() {
        if (!connected) connect();
        isPlaying = !isPlaying;
        if (isPlaying) {
            trackStartTime = System.currentTimeMillis() - getCurrentProgressMs();
        }
    }

    public void next() {
        if (!connected || playlist.isEmpty()) return;
        currentTrackIndex = (currentTrackIndex + 1) % playlist.size();
        trackStartTime = System.currentTimeMillis();
        trackDurationMs = playlist.get(currentTrackIndex).durationMs;
        isPlaying = true;
    }

    public void previous() {
        if (!connected || playlist.isEmpty()) return;
        currentTrackIndex = (currentTrackIndex - 1 + playlist.size()) % playlist.size();
        trackStartTime = System.currentTimeMillis();
        trackDurationMs = playlist.get(currentTrackIndex).durationMs;
        isPlaying = true;
    }

    public int getCurrentProgressMs() {
        if (!isPlaying) return 0;
        long elapsed = System.currentTimeMillis() - trackStartTime;
        if (elapsed >= trackDurationMs) {
            next();
            return 0;
        }
        return (int) elapsed;
    }

    public int getTrackDurationMs() { return trackDurationMs; }

    public String getFormattedProgress() {
        int current = getCurrentProgressMs() / 1000;
        int total = trackDurationMs / 1000;
        return formatTime(current) + " / " + formatTime(total);
    }

    private String formatTime(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%d:%02d", min, sec);
    }

    public void control(String action) {
        switch (action) {
            case "play": togglePlay(); break;
            case "next": next(); break;
            case "prev": previous(); break;
        }
    }

    public void shuffle() {
        if (!playlist.isEmpty()) {
            currentTrackIndex = random.nextInt(playlist.size());
            trackStartTime = System.currentTimeMillis();
            trackDurationMs = playlist.get(currentTrackIndex).durationMs;
            isPlaying = true;
        }
    }
}
