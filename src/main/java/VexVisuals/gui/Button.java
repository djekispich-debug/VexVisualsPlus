package com.yourmod.gui;

import com.yourmod.gui.theme.GUITheme;
import com.yourmod.gui.theme.ThemeManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.Color;

public class Button {
    public int x, y, width, height;
    public String text;
    public boolean toggled = false;
    private float hoverAnim = 0f;

    public Button(int x, int y, int width, int height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        GUITheme theme = ThemeManager.getCurrentTheme();
        boolean hovered = isHovered(mouseX, mouseY);

        hoverAnim = hovered ? Math.min(hoverAnim + 0.2f, 1f) : Math.max(hoverAnim - 0.2f, 0f);

        int bgColor = hovered ? brighten(theme.panel, 30) : theme.panel.getRGB();
        int accent = theme.primary.getRGB();

        // Закруглённый прямоугольник
        drawRoundedRect(context, x, y, width, height, theme.radius, bgColor);

        // Активная обводка
        if (toggled) {
            drawRoundedRect(context, x, y, width, height, theme.radius, accent);
        }

        // Текст
        int textColor = toggled ? accent : theme.text.getRGB();
        context.drawCenteredTextWithShadow(mc.textRenderer, Text.literal(text),
                x + width/2, y + height/2 - 4, textColor);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        if (isHovered(mouseX, mouseY) && button == 0) {
            toggled = !toggled;
            return true;
        }
        return false;
    }

    private boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    // Вспомогательные методы
    private void drawRoundedRect(DrawContext ctx, int x, int y, int w, int h, int radius, int color) {
        // Используй встроенный метод или RenderSystem
        ctx.fill(x + radius, y, x + w - radius, y + h, color);
        ctx.fill(x, y + radius, x + w, y + h - radius, color);
        // Здесь можно добавить круги по углам через drawCircle, но для простоты оставим так
    }

    private int brighten(Color color, int amount) {
        return new Color(
                Math.min(255, color.getRed() + amount),
                Math.min(255, color.getGreen() + amount),
                Math.min(255, color.getBlue() + amount)
        ).getRGB();
    }
}
