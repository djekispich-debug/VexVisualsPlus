package VexVisuals.gui;

import VexVisuals.module.*;
import VexVisuals.util.ColorManager;
import VexVisuals.util.Easing;
import VexVisuals.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClickGuiScreen extends Screen {
    public static final String CLIENT_TITLE = "VexVisualsPlus";
    private static final DateTimeFormatter CLOCK = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final Minecraft mc = Minecraft.getInstance();
    private Category selectedCategory = Category.HUD;
    private Module selectedModule;
    private Module bindingModule;
    private float openProgress;
    private long openStartMs;
    private int panelScroll;
    private int settingsScroll;

    public ClickGuiScreen() {
        super(Component.literal(CLIENT_TITLE));
    }

    @Override
    protected void init() {
        openStartMs = System.currentTimeMillis();
        openProgress = 0f;
    }

    @Override
    public void tick() {
        long elapsed = System.currentTimeMillis() - openStartMs;
        openProgress = Math.min(1f, elapsed / 320f);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBlurredBackground(g);
        float anim = Easing.easeOutBack(openProgress);
        int panelW = Math.min(720, width - 40);
        int panelH = Math.min(420, height - 50);
        int baseX = (width - panelW) / 2;
        int baseY = (height - panelH) / 2;
        int slideY = (int) ((1f - Easing.easeOutCubic(openProgress)) * 48);
        int x = baseX;
        int y = baseY + slideY;
        float scale = Easing.lerp(0.88f, 1f, anim);
        int scaledW = (int) (panelW * scale);
        int scaledH = (int) (panelH * scale);
        int sx = x + (panelW - scaledW) / 2;
        int sy = y + (panelH - scaledH) / 2;

        RenderUtil.fillRounded(g, sx, sy, scaledW, scaledH, 10, ThemeManager.panel());
        g.fill(sx, sy, sx + scaledW, sy + 2, ThemeManager.accent());

        renderHeader(g, sx, sy, scaledW);
        renderCategoryBar(g, sx, sy + 42, scaledW, mouseX, mouseY);
        int contentY = sy + 68;
        int contentH = scaledH - 76;
        int colW = scaledW / 3;
        renderModuleList(g, sx + 8, contentY, colW - 12, contentH, mouseX, mouseY);
        renderModuleDetails(g, sx + colW + 8, contentY, colW * 2 - 16, contentH, mouseX, mouseY);

        if (bindingModule != null) {
            String hint = "Бинд: " + bindingModule.getName() + " — клавиша / колёсико / ESC = сброс";
            int tw = mc.font.width(hint);
            g.fill(width / 2 - tw / 2 - 8, height - 36, width / 2 + tw / 2 + 8, height - 18, ThemeManager.background());
            g.drawString(mc.font, hint, width / 2 - tw / 2, height - 32, ThemeManager.accent());
        }
        super.render(g, mouseX, mouseY, partialTick);
    }

    private void renderBlurredBackground(GuiGraphics g) {
        g.fill(0, 0, width, height, ThemeManager.background());
    }

    /** Шапка: название клиента, никнейм, реальное время */
    private void renderHeader(GuiGraphics g, int x, int y, int w) {
        int barH = 36;
        RenderUtil.horizontalGradient(g, x + 6, y + 6, w - 12, barH, ThemeManager.headerGradientTop(), ThemeManager.headerGradientBottom());

        String title = CLIENT_TITLE;
        String nickname = mc.player != null ? mc.player.getName().getString() : "Оффлайн";
        String time = LocalTime.now().format(CLOCK);

        int titleColor = ColorManager.chroma(System.currentTimeMillis(), 0);
        g.drawString(mc.font, title, x + 14, y + 16, titleColor);
        int nickW = mc.font.width(nickname);
        g.drawString(mc.font, nickname, x + (w - nickW) / 2, y + 16, ThemeManager.text());
        int timeW = mc.font.width(time);
        g.drawString(mc.font, time, x + w - timeW - 14, y + 16, ThemeManager.accent());

        String sub = "v" + (mc.getVersionType() != null ? "1.21.11" : "1.21.11") + "  |  Тема: " + ThemeManager.getCurrent().name();
        g.drawString(mc.font, sub, x + 14, y + 26, ThemeManager.textMuted());
    }

    private void renderCategoryBar(GuiGraphics g, int x, int y, int w, int mouseX, int mouseY) {
        Category[] cats = Category.values();
        int tabW = (w - 16) / cats.length;
        for (int i = 0; i < cats.length; i++) {
            Category cat = cats[i];
            int tx = x + 8 + i * tabW;
            boolean sel = cat == selectedCategory;
            boolean hover = mouseX >= tx && mouseX < tx + tabW - 4 && mouseY >= y && mouseY < y + 20;
            int bg = sel ? ThemeManager.accent() : (hover ? ThemeManager.border() : 0x00000000);
            if (bg != 0) {
                g.fill(tx, y, tx + tabW - 4, y + 20, ColorManager.withAlpha(bg, sel ? 0.35f : 0.2f));
            }
            String label = cat.getDisplayName();
            int lw = mc.font.width(label);
            g.drawString(mc.font, label, tx + (tabW - 4 - lw) / 2, y + 6, sel ? ThemeManager.text() : ThemeManager.textMuted());
        }
    }

    private void renderModuleList(GuiGraphics g, int x, int y, int w, int h, int mouseX, int mouseY) {
        List<Module> modules = ModuleRegistry.byCategory(selectedCategory);
        g.drawString(mc.font, "Модули (" + modules.size() + ")", x, y, ThemeManager.textMuted());
        int rowY = y + 14 - panelScroll;
        for (Module module : modules) {
            if (rowY > y + h) break;
            if (rowY + 18 >= y) {
                boolean hover = mouseX >= x && mouseX < x + w && mouseY >= rowY && mouseY < rowY + 16;
                boolean sel = module == selectedModule;
                if (hover || sel) {
                    g.fill(x, rowY, x + w, rowY + 16, ColorManager.withAlpha(ThemeManager.accent(), sel ? 0.35f : 0.15f));
                }
                int nameColor = module.isEnabled() ? ThemeManager.accent() : ThemeManager.text();
                g.drawString(mc.font, module.getName(), x + 4, rowY + 4, nameColor);
                String bind = keyName(module.getKeyBind());
                int bw = mc.font.width(bind);
                g.drawString(mc.font, bind, x + w - bw - 4, rowY + 4, ThemeManager.textMuted());
            }
            rowY += 18;
        }
    }

    private void renderModuleDetails(GuiGraphics g, int x, int y, int w, int h, int mouseX, int mouseY) {
        if (selectedModule == null) {
            g.drawString(mc.font, "Выберите модуль слева", x, y + 20, ThemeManager.textMuted());
            return;
        }
        g.drawString(mc.font, selectedModule.getName(), x, y, ThemeManager.accent());
        g.drawString(mc.font, selectedModule.getDescription(), x, y + 12, ThemeManager.textMuted());
        String status = selectedModule.isEnabled() ? "Вкл" : "Выкл";
        g.drawString(mc.font, "Статус: " + status + "  |  Бинд: " + keyName(selectedModule.getKeyBind()), x, y + 24, ThemeManager.text());

        int sy = y + 40 - settingsScroll;
        for (Setting<?> setting : selectedModule.getSettings()) {
            if (sy > y + h) break;
            renderSetting(g, x, sy, w, setting, mouseX, mouseY);
            sy += settingHeight(setting);
        }
    }

    private int settingHeight(Setting<?> setting) {
        return switch (setting.getType()) {
            case NUMBER -> 28;
            case COLOR -> 22;
            default -> 18;
        };
    }

    private void renderSetting(GuiGraphics g, int x, int sy, int w, Setting<?> setting, int mouseX, int mouseY) {
        g.drawString(mc.font, setting.getName(), x, sy + 2, ThemeManager.text());
        switch (setting.getType()) {
            case BOOLEAN -> {
                BooleanSetting bs = (BooleanSetting) setting;
                String v = bs.get() ? "✓" : "✗";
                g.drawString(mc.font, v, x + w - 12, sy + 2, bs.get() ? ThemeManager.accent() : ThemeManager.textMuted());
            }
            case NUMBER -> {
                NumberSetting ns = (NumberSetting) setting;
                int barX = x;
                int barY = sy + 14;
                int barW = w - 8;
                g.fill(barX, barY, barX + barW, barY + 4, ThemeManager.border());
                double t = (ns.get() - ns.getMin()) / (ns.getMax() - ns.getMin());
                int fill = (int) (barW * t);
                g.fill(barX, barY, barX + fill, barY + 4, ThemeManager.accent());
                String val = String.format("%.2f", ns.get());
                g.drawString(mc.font, val, x + w - mc.font.width(val), sy + 2, ThemeManager.textMuted());
            }
            case MODE -> {
                ModeSetting ms = (ModeSetting) setting;
                g.drawString(mc.font, ms.get(), x + w - mc.font.width(ms.get()) - 4, sy + 2, ThemeManager.accent());
            }
            case COLOR -> {
                ColorSetting cs = (ColorSetting) setting;
                g.fill(x + w - 24, sy, x + w - 4, sy + 14, cs.get() | 0xFF000000);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (bindingModule != null) {
            bindingModule.setKeyBind(GLFW.GLFW_MOUSE_BUTTON_1 + button);
            bindingModule = null;
            return true;
        }
        if (handleCategoryClick((int) mouseX, (int) mouseY, button)) {
            return true;
        }
        if (handleModuleListClick((int) mouseX, (int) mouseY, button)) {
            return true;
        }
        if (handleSettingsClick((int) mouseX, (int) mouseY, button)) {
            return true;
        }
        if (button == 0 && mouseX > width - 120 && mouseY < 42) {
            ThemeManager.cycleTheme();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean handleCategoryClick(int mouseX, int mouseY, int button) {
        if (button != 0) return false;
        int panelW = Math.min(720, width - 40);
        int x = (width - panelW) / 2;
        int y = (height - Math.min(420, height - 50)) / 2 + 68;
        Category[] cats = Category.values();
        int tabW = (panelW - 16) / cats.length;
        int barY = y - 26;
        for (int i = 0; i < cats.length; i++) {
            int tx = x + 8 + i * tabW;
            if (mouseX >= tx && mouseX < tx + tabW - 4 && mouseY >= barY && mouseY < barY + 20) {
                selectedCategory = cats[i];
                selectedModule = null;
                panelScroll = 0;
                return true;
            }
        }
        return false;
    }

    private boolean handleModuleListClick(int mouseX, int mouseY, int button) {
        int panelW = Math.min(720, width - 40);
        int panelH = Math.min(420, height - 50);
        int x = (width - panelW) / 2 + 8;
        int y = (height - panelH) / 2 + 68 + 14 - panelScroll;
        int colW = panelW / 3 - 12;
        List<Module> modules = ModuleRegistry.byCategory(selectedCategory);
        for (Module module : modules) {
            if (mouseX >= x && mouseX < x + colW && mouseY >= y && mouseY < y + 16) {
                if (button == 0) {
                    selectedModule = module;
                    settingsScroll = 0;
                } else if (button == 1) {
                    module.toggle();
                } else if (button == 2) {
                    bindingModule = module;
                }
                return true;
            }
            y += 18;
        }
        return false;
    }

    private boolean handleSettingsClick(int mouseX, int mouseY, int button) {
        if (selectedModule == null || button != 0) return false;
        int panelW = Math.min(720, width - 40);
        int panelH = Math.min(420, height - 50);
        int x = (width - panelW) / 2 + panelW / 3 + 8;
        int y = (height - panelH) / 2 + 68 + 40 - settingsScroll;
        int w = panelW * 2 / 3 - 16;
        for (Setting<?> setting : selectedModule.getSettings()) {
            int h = settingHeight(setting);
            if (mouseY >= y && mouseY < y + h) {
                if (setting instanceof BooleanSetting bs) {
                    bs.set(!bs.get());
                } else if (setting instanceof ModeSetting ms) {
                    ms.cycle();
                }
                return true;
            }
            y += h;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (mouseX < width / 2) {
            panelScroll = Math.max(0, panelScroll - (int) scrollY * 12);
        } else {
            settingsScroll = Math.max(0, settingsScroll - (int) scrollY * 12);
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (bindingModule != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                bindingModule.setKeyBind(GLFW.GLFW_KEY_UNKNOWN);
                bindingModule = null;
                return true;
            }
            bindingModule.setKeyBind(keyCode);
            bindingModule = null;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return bindingModule == null;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static String keyName(int key) {
        if (key == GLFW.GLFW_KEY_UNKNOWN) return "NONE";
        if (key >= GLFW.GLFW_MOUSE_BUTTON_1 && key <= GLFW.GLFW_MOUSE_BUTTON_8) {
            return "M" + (key - GLFW.GLFW_MOUSE_BUTTON_1 + 1);
        }
        String name = GLFW.glfwGetKeyName(key, 0);
        return name != null ? name.toUpperCase() : "K" + key;
    }
}
