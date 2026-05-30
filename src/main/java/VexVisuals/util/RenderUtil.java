package VexVisuals.util;

import net.minecraft.client.gui.GuiGraphics;

public final class RenderUtil {
    private RenderUtil() {
    }

    public static void fillRounded(GuiGraphics g, int x, int y, int w, int h, int radius, int color) {
        g.fill(x + radius, y, x + w - radius, y + h, color);
        g.fill(x, y + radius, x + w, y + h - radius, color);
        g.fill(x, y, x + radius, y + radius, color);
        g.fill(x + w - radius, y, x + w, y + radius, color);
        g.fill(x, y + h - radius, x + radius, y + h, color);
        g.fill(x + w - radius, y + h - radius, x + w, y + h, color);
    }

    public static void horizontalGradient(GuiGraphics g, int x, int y, int w, int h, int left, int right) {
        g.fillGradient(x, y, x + w, y + h, left, right);
    }
}
