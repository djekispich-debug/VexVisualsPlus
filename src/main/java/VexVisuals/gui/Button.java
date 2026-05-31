package com.vexvisual.gui;

import com.vexvisual.gui.theme.GUITheme;
import com.vexvisual.gui.theme.ThemeManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class Button {
    public int x, y, width, height;
    public String text;
    public boolean toggled = false;
    private float hoverAnim = 0f;
    private float toggleAnim = 0f;

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

        hoverAnim = hovered ? Math.min(hoverAnim + 0.25f, 1f) : Math.max(hoverAnim - 0.25f, 0f);
        toggleAnim = toggled ? Math.min(toggleAnim + 0.2f, 1f) : Math.max(toggleAnim - 0.2f, 0f);

        int bg = hovered ? brighten(theme.panel, 35) : theme.panel.getRGB();
        int accent = theme.primary.getRGB();

        drawRoundedRect(context, x, y, width, height, theme.radius, bg);

        if (toggled) {
            drawRoundedRectOutline(context, x, y, width, height, theme.radius, accent);
        }

        int textColor = toggled ? accent : theme.text.getRGB();
        context.drawCenteredTextWithShadow(mc.textRenderer, Text.literal(text),
                x + width/2, y + height/2 - 4, textColor);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered((int)mouseX, (int)mouseY) && button == 0) {
            toggled = !toggled;
            return true;
        }
        return false;
    }

    private boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    // Helper methods (drawRoundedRect, brighten и т.д.) — оставь как в прошлом сообщении
}
