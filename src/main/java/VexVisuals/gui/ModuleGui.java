package VexVisuals.gui;

import VexVisuals.module.Module;
import VexVisuals.gui.GUITheme;
import VexVisuals.gui.ThemeManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ModuleGui {
    public Module module;
    public int x, y, width, height;

    private float hoverAnim = 0f;
    private float toggleAnim = 0f;
    private boolean selected = false;

    public ModuleGui(Module module, int x, int y, int width, int height) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setSelected(boolean sel) {
        this.selected = sel;
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        GUITheme theme = ThemeManager.getCurrentTheme();
        boolean hovered = isHovered(mouseX, mouseY);
        boolean enabled = module.isEnabled();

        hoverAnim += (hovered ? 0.25f : -0.25f);
        hoverAnim = Math.max(0, Math.min(1, hoverAnim));
        toggleAnim += (enabled ? 0.25f : -0.25f);
        toggleAnim = Math.max(0, Math.min(1, toggleAnim));

        int bgColor;
        if (selected) {
            bgColor = lerpColor(theme.primary.getRGB(), theme.panel.getRGB(), 0.4f);
        } else if (hovered) {
            bgColor = lerpColor(theme.text.getRGB(), theme.panel.getRGB(), 0.08f);
        } else {
            bgColor = theme.panel.getRGB();
        }
        drawRoundedRect(context, x, y, width, height, 6, bgColor);

        // Индикатор включения
        int dotSize = 6;
        int dotX = x + 5;
        int dotY = y + (height - dotSize) / 2;
        int dotColor = enabled ? theme.primary.getRGB() : theme.border.getRGB();
        drawRoundedRect(context, dotX, dotY, dotSize, dotSize, dotSize / 2, dotColor);

        // Название модуля
        MinecraftClient mc = MinecraftClient.getInstance();
        int textX = x + 16;
        int textY = y + (height - mc.textRenderer.fontHeight) / 2;
        int textColor = enabled ? theme.text.getRGB() : theme.textSecondary.getRGB();
        context.drawTextWithShadow(mc.textRenderer, Text.literal(module.getName()), textX, textY, textColor);

        // Назначенная клавиша
        String bind = getBindDisplay();
        if (!bind.isEmpty()) {
            int bindW = mc.textRenderer.getWidth(bind);
            context.drawTextWithShadow(mc.textRenderer, Text.literal(bind),
                    x + width - bindW - 6, textY, theme.textSecondary.getRGB());
        }
    }

    private boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private String getBindDisplay() {
        int key = module.getKeyBind();
        if (key == GLFW.GLFW_KEY_UNKNOWN) return "";
        String name = GLFW.glfwGetKeyName(key, 0);
        return name != null ? "[" + name + "]" : "";
    }

    private int lerpColor(int c1, int c2, float t) {
        int r = (int)(((c1 >> 16) & 0xFF) * (1 - t) + ((c2 >> 16) & 0xFF) * t);
        int g = (int)(((c1 >> 8) & 0xFF) * (1 - t) + ((c2 >> 8) & 0xFF) * t);
        int b = (int)((c1 & 0xFF) * (1 - t) + (c2 & 0xFF) * t);
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    private void drawRoundedRect(DrawContext context, int x, int y, int w, int h, int radius, int color) {
        // Если RenderUtil.fillRounded доступен, используй его; иначе обычный fill
        try {
            VexVisuals.util.RenderUtil.fillRounded(context, x, y, w, h, radius, color);
        } catch (NoSuchMethodError e) {
            context.fill(x, y, x + w, y + h, color);
        }
    }
}
