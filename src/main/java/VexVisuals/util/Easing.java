package VexVisuals.util;

public final class Easing {
    private Easing() {
    }

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * Math.clamp(t, 0f, 1f);
    }

    public static float easeOutCubic(float t) {
        t = Math.clamp(t, 0f, 1f);
        float inv = 1f - t;
        return 1f - inv * inv * inv;
    }

    public static float easeOutBack(float t) {
        t = Math.clamp(t, 0f, 1f);
        float c1 = 1.70158f;
        float c3 = c1 + 1f;
        return 1f + c3 * (float) Math.pow(t - 1f, 3) + c1 * (float) Math.pow(t - 1f, 2);
    }

    public static float smoothStep(float t) {
        t = Math.clamp(t, 0f, 1f);
        return t * t * (3f - 2f * t);
    }
}
