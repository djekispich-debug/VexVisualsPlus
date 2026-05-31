package VexVisuals.util;

import net.minecraft.client.gui.DrawContext;

public final class RenderUtil {
    private RenderUtil() {
    }

    public static void fillRounded(DrawContext context, int x, int y, int w, int h, int radius, int color) {
        context.fill(x + radius, y, x + w - radius, y + h, color);
        context.fill(x, y + radius, x + w, y + h - radius, color);
        context.fill(x, y, x + radius, y + radius, color);
        context.fill(x + w - radius, y, x + w, y + radius, color);
        context.fill(x, y + h - radius, x + radius, y + h, color);
        context.fill(x + w - radius, y + h - radius, x + w, y + h, color);
    }

    public static void horizontalGradient(DrawContext context, int x, int y, int w, int h, int left, int right) {
        context.fillGradient(x, y, x + w, y + h, left, right);
    }
}
