package VexVisuals.util;

public final class ColorManager {
    private ColorManager() {
    }

    public static int rgba(int r, int g, int b, int a) {
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }

    public static int withAlpha(int argb, float alpha) {
        int a = (int) (Math.clamp(alpha, 0f, 1f) * 255);
        return (argb & 0x00FFFFFF) | (a << 24);
    }

    public static int lerpColor(int from, int to, float t) {
        t = Math.clamp(t, 0f, 1f);
        int af = (from >> 24) & 0xFF, rf = (from >> 16) & 0xFF, gf = (from >> 8) & 0xFF, bf = from & 0xFF;
        int at = (to >> 24) & 0xFF, rt = (to >> 16) & 0xFF, gt = (to >> 8) & 0xFF, bt = to & 0xFF;
        return rgba(
                (int) (rf + (rt - rf) * t),
                (int) (gf + (gt - gf) * t),
                (int) (bf + (bt - bf) * t),
                (int) (af + (at - af) * t)
        );
    }

    public static int rainbow(long timeMs, float speed, float saturation, float brightness) {
        float hue = ((timeMs % 10000L) / 10000f) * speed;
        return java.awt.Color.HSBtoRGB(hue, saturation, brightness);
    }

    public static int chroma(long timeMs, int offset) {
        return rainbow(timeMs + offset * 120L, 2.5f, 0.75f, 1f) | 0xFF000000;
    }

    public static int gradient(int c1, int c2, float t) {
        return lerpColor(c1, c2, t);
    }

    public static int hex(String hex) {
        String h = hex.startsWith("#") ? hex.substring(1) : hex;
        if (h.length() == 6) {
            h = "FF" + h;
        }
        return (int) Long.parseLong(h, 16);
    }
}
