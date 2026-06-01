package com.yourmod.gui;

import com.yourmod.modules.Module;
import com.yourmod.gui.theme.GUITheme;
import com.yourmod.gui.theme.ThemeManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.Color;

public class ModuleGui {
    public Module module;
    public int x, y, width, height;

    // Анимации для ховера и переключения
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

    /** Вызывается из ClickGuiScreen, чтобы подсветить выбранный модуль */
    public void setSelected(boolean sel) {
        this.selected = sel;
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        GUITheme theme = ThemeManager.getCurrentTheme();
        boolean hovered = isHovered(mouseX, mouseY);
        boolean enabled = module.isEnabled();

        // Плавные анимации
        hoverAnim += (hovered ? 0.25f : -0.25f);
        hoverAnim = Math.max(0, Math.min(1, hoverAnim));
        toggleAnim += (enabled ? 0.25f : -0.25f);
        toggleAnim = Math.max(0, Math.min(1, toggleAnim));

        // Фон строки (закруглённый)
        int bgColor;
        if (selected) {
            bgColor = lerpColor(theme.primary, theme.panel, 0.4f).getRGB();
        } else if (hovered) {
            bgColor = lerpColor(theme.text, theme.panel, 0.08f).getRGB();
        } else {
            bgColor = theme.panel.getRGB();
        }
        drawRoundedRect(context, x, y, width, height, 6, bgColor);

        // Кружок-индикатор включения (слева)
        int dotSize = 6;
        int dotX = x + 5;
        int dotY = y + (height - dotSize) / 2;
        int dotColor = enabled ? theme.primary.getRGB() : theme.border.getRGB();
        drawRoundedRect(context, dotX, dotY, dotSize, dotSize, dotSize / 2, dotColor);

        // Название модуля
        int textX = x + 16;
        int textY = y + (height - MinecraftClient.getInstance().textRenderer.fontHeight) / 2;
        int textColor = enabled ? theme.text.getRGB() : theme.textSecondary.getRGB();
        context.drawTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                Text.literal(module.getName()),
                textX, textY,
                textColor
        );

        // Назначенная клавиша (справа)
        String bindStr = getBindDisplay();
        if (!bindStr.isEmpty()) {
            int bindW = MinecraftClient.getInstance().textRenderer.getWidth(bindStr);
            int bindX = x + width - bindW - 6;
            context.drawTextWithShadow(
                    MinecraftClient.getInstance().textRenderer,
                    Text.literal(bindStr),
                    bindX, textY,
                    theme.textSecondary.getRGB()
            );
        }
    }

    private boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    /** Показывает клавишу в виде [R] или пустую строку, если не назначена */
    private String getBindDisplay() {
        int key = module.getKeyBind();
        if (key == GLFW.GLFW_KEY_UNKNOWN) return "";
        String name = module.getKeyBindName(); // предполагается, что есть такой метод в Module
        return "[" + (name != null ? name : GLFW.glfwGetKeyName(key, 0)) + "]";
    }

    /** Смешивание двух цветов */
    private Color lerpColor(Color a, Color b, float t) {
        int red = (int)(a.getRed() * (1 - t) + b.getRed() * t);
        int green = (int)(a.getGreen() * (1 - t) + b.getGreen() * t);
        int blue = (int)(a.getBlue() * (1 - t) + b.getBlue() * t);
        return new Color(
                Math.min(255, Math.max(0, red)),
                Math.min(255, Math.max(0, green)),
                Math.min(255, Math.max(0, blue))
        );
    }

    /** Заглушка закруглённого прямоугольника – замените на RenderUtil.fillRounded, если он есть */
    private void drawRoundedRect(DrawContext context, int x, int y, int w, int h, int radius, int color) {
        // Если в проекте есть VexVisuals.util.RenderUtil, используйте:
        // VexVisuals.util.RenderUtil.fillRounded(context, x, y, w, h, radius, color);
        // Иначе временно рисуем обычный прямоугольник:
        context.fill(x, y, x + w, y + h, color);
    }
}
