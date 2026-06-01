package VexVisuals.gui;

import VexVisuals.module.Module;
import VexVisuals.module.Setting;
import VexVisuals.module.setting.BooleanSetting;
import VexVisuals.module.setting.NumberSetting;
import VexVisuals.module.setting.ModeSetting;
import VexVisuals.module.ModuleRegistry;
import VexVisuals.util.ColorManager;
import VexVisuals.util.Easing;
import VexVisuals.util.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClickGuiScreen extends Screen {
    public static final String CLIENT_TITLE = "VexVisuals+";
    private static final DateTimeFormatter CLOCK = DateTimeFormatter.ofPattern("HH:mm:ss");

    // Конфигурация панели
    private static final int PANEL_MAX_W = 720;
    private static final int PANEL_MAX_H = 420;
    private static final int HEADER_HEIGHT = 36;
    private static final int CATEGORY_BAR_Y_OFFSET = 42;
    private static final int CONTENT_Y_OFFSET = 68;
    private static final int TAB_PADDING = 8;
    private static final int SCROLL_SPEED = 14;
    private static final float ANIMATION_DURATION = 320f; // мс

    private Category selectedCategory = Category.values()[0];
    private Module selectedModule;
    private Module bindingModule;

    // Анимация открытия
    private float openProgress = 0f;
    private long openStartTime;

    // Скроллы
    private int panelScroll = 0;
    private int settingsScroll = 0;
    private float smoothPanelScroll = 0f;
    private float smoothSettingsScroll = 0f;

    public ClickGuiScreen() {
        super(Text.literal(CLIENT_TITLE));
    }

    @Override
    protected void init() {
        openStartTime = System.currentTimeMillis();
        openProgress = 0f;
        // Сброс скроллов при каждом открытии
        panelScroll = 0;
        settingsScroll = 0;
        smoothPanelScroll = 0f;
        smoothSettingsScroll = 0f;
    }

    @Override
    public void tick() {
        long elapsed = System.currentTimeMillis() - openStartTime;
        openProgress = Math.min(1f, elapsed / ANIMATION_DURATION);

        // Плавное приближение скролла
        smoothPanelScroll += (panelScroll - smoothPanelScroll) * 0.3f;
        smoothSettingsScroll += (settingsScroll - smoothSettingsScroll) * 0.3f;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        // Затемнение фона
        context.fill(0, 0, width, height, 0x65000000);

        float progress = Easing.easeOutCubic(openProgress);
        int panelW = Math.min(PANEL_MAX_W, width - 40);
        int panelH = Math.min(PANEL_MAX_H, height - 50);

        // Центрирование и анимация появления
        int x = (width - panelW) / 2;
        int y = (height - panelH) / 2 + (int) ((1f - progress) * 40);
        int scaleW = (int) (panelW * progress);
        int scaleH = (int) (panelH * progress);
        int sx = x + (panelW - scaleW) / 2;
        int sy = y + (panelH - scaleH) / 2;

        // Тень под панелью
        RenderUtil.drawShadow(context, sx, sy, scaleW, scaleH, 15, 0x40000000, 0x00000000);

        // Основной фон панели с закруглением
        RenderUtil.fillRounded(context, sx, sy, scaleW, scaleH, 12, ThemeManager.panel());

        // Акцентная полоса сверху с градиентом
        RenderUtil.horizontalGradient(context, sx, sy, sx + scaleW, sy + 3,
                ThemeManager.accent(), ColorManager.brighter(ThemeManager.accent(), 0.3f));

        // Заголовок
        renderHeader(context, sx, sy, scaleW);
        // Панель вкладок категорий
        renderCategoryBar(context, sx, sy + CATEGORY_BAR_Y_OFFSET, scaleW, mouseX, mouseY);

        int contentY = sy + CONTENT_Y_OFFSET;
        int contentH = scaleH - 76;
        int colW = scaleW / 3;

        // Основной контент (список модулей + детали или Spotify)
        if (selectedCategory == Category.SPOTIFY) {
            renderSpotifyTab(context, sx + 15, contentY, scaleW - 30, contentH, mouseX, mouseY);
        } else {
            renderModuleList(context, sx + 8, contentY, colW - 12, contentH, mouseX, mouseY);
            renderModuleDetails(context, sx + colW + 8, contentY, scaleW - colW - 16, contentH, mouseX, mouseY);
        }

        // Подсказка о биндинге
        if (bindingModule != null) {
            String txt = "Press a key or mouse button for " + bindingModule.getName() + " (ESC to reset)";
            int tw = textRenderer.getWidth(txt);
            int hintX = width / 2 - tw / 2 - 8;
            int hintY = height - 24;
            context.fill(hintX, hintY, hintX + tw + 16, hintY + 12, ThemeManager.background());
            context.drawTextWithShadow(textRenderer, txt, width / 2 - tw / 2, hintY + 2, ThemeManager.accent());
        }

        super.render(context, mouseX, mouseY, partialTicks);
    }

    private void renderHeader(DrawContext context, int x, int y, int w) {
        int barH = HEADER_HEIGHT;
        RenderUtil.horizontalGradient(context, x + 6, y + 6, w - 12, barH,
                ThemeManager.headerGradientTop(), ThemeManager.headerGradientBottom());

        String title = CLIENT_TITLE;
        String nickname = client.player != null ? client.player.getName().getString() : "Offline";
        String time = LocalTime.now().format(CLOCK);

        // Хромированный цвет названия
        int titleColor = ColorManager.chroma(System.currentTimeMillis(), 0);
        context.drawTextWithShadow(textRenderer, title, x + 14, y + 16, titleColor);

        // Никнейм по центру
        int nickW = textRenderer.getWidth(nickname);
        context.drawTextWithShadow(textRenderer, nickname, x + (w - nickW) / 2, y + 16, ThemeManager.text());

        // Часы справа
        int timeW = textRenderer.getWidth(time);
        context.drawTextWithShadow(textRenderer, time, x + w - timeW - 14, y + 16, ThemeManager.accent());

        // Строка версии и темы
        String sub = "v1.21.11  |  Theme: " + ThemeManager.getCurrent().name();
        context.drawTextWithShadow(textRenderer, sub, x + 14, y + 26, ThemeManager.textMuted());
    }

    private void renderCategoryBar(DrawContext context, int x, int y, int w, int mouseX, int mouseY) {
        Category[] cats = Category.values();
        int tabW = (w - TAB_PADDING * 2) / cats.length;
        for (int i = 0; i < cats.length; i++) {
            Category cat = cats[i];
            int tx = x + TAB_PADDING + i * tabW;
            boolean sel = cat == selectedCategory;
            boolean hover = mouseX >= tx && mouseX < tx + tabW - 4 && mouseY >= y && mouseY < y + 20;

            int bgColor;
            if (sel) {
                bgColor = ColorManager.withAlpha(ThemeManager.accent(), 180);
            } else if (hover) {
                bgColor = ColorManager.withAlpha(ThemeManager.border(), 80);
            } else {
                bgColor = 0x00000000;
            }

            if (bgColor != 0) {
                RenderUtil.fillRounded(context, tx, y, tabW - 4, 20, 6, bgColor);
            }

            String label = cat.getDisplayName();
            int lw = textRenderer.getWidth(label);
            int textColor = sel ? ThemeManager.text() : ThemeManager.textMuted();
            context.drawTextWithShadow(textRenderer, label, tx + (tabW - 4 - lw) / 2, y + 6, textColor);
        }
    }

    private void renderModuleList(DrawContext context, int x, int y, int w, int h, int mouseX, int mouseY) {
        var modules = ModuleRegistry.byCategory(selectedCategory);
        context.drawTextWithShadow(textRenderer, "Modules (" + modules.size() + ")", x, y - 2, ThemeManager.textMuted());

        int scroll = Math.round(smoothPanelScroll);
        int rowY = y + 14 - scroll;

        // Обрезаем область списка
        RenderUtil.enableScissor(x, y, w, h);
        for (Module module : modules) {
            if (rowY > y + h) break;
            if (rowY + 18 >= y) {
                boolean hover = mouseX >= x && mouseX < x + w && mouseY >= rowY && mouseY < rowY + 16;
                boolean selected = module == selectedModule;
                ModuleGui.renderRow(context, module, x, rowY, w, hover, selected);
            }
            rowY += 18;
        }
        RenderUtil.disableScissor();

        // Полоса прокрутки
        int totalHeight = modules.size() * 18;
        int maxScroll = Math.max(0, totalHeight - h);
        if (maxScroll > 0) {
            float barHeight = Math.max(20, (float) h / totalHeight * h);
            float barY = y + (float) scroll / maxScroll * (h - barHeight);
            RenderUtil.fillRounded(context, x + w - 3, (int) barY, 3, (int) barHeight, 2,
                    ColorManager.withAlpha(ThemeManager.accent(), 120));
        }
    }

    private void renderModuleDetails(DrawContext context, int x, int y, int w, int h, int mouseX, int mouseY) {
        if (selectedModule == null) {
            String placeholder = "Select a module to view settings";
            int tw = textRenderer.getWidth(placeholder);
            context.drawTextWithShadow(textRenderer, placeholder, x + (w - tw) / 2, y + h / 3, ThemeManager.textMuted());
            return;
        }

        context.drawTextWithShadow(textRenderer, selectedModule.getName(), x, y, ThemeManager.accent());
        context.drawTextWithShadow(textRenderer, selectedModule.getDescription(), x, y + 12, ThemeManager.textMuted());

        int scroll = Math.round(smoothSettingsScroll);
        int sy = y + 36 - scroll;
        int settingsW = w - 10;

        RenderUtil.enableScissor(x, y + 36, w, h - 36);
        for (Setting<?> setting : selectedModule.getSettings()) {
            if (sy > y + h) break;
            if (sy + 20 >= y + 36) {
                renderSettingItem(context, x, sy, settingsW, setting, mouseX, mouseY);
            }
            sy += (setting instanceof NumberSetting) ? 26 : 18;
        }
        RenderUtil.disableScissor();

        // Полоса прокрутки настроек
        int totalHeight = selectedModule.getSettings().stream()
                .mapToInt(s -> s instanceof NumberSetting ? 26 : 18).sum();
        int maxScroll = Math.max(0, totalHeight - (h - 36));
        if (maxScroll > 0) {
            float barHeight = Math.max(20, (float) (h - 36) / totalHeight * (h - 36));
            float barY = y + 36 + (float) scroll / maxScroll * ((h - 36) - barHeight);
            RenderUtil.fillRounded(context, x + settingsW + 4, (int) barY, 3, (int) barHeight, 2,
                    ColorManager.withAlpha(ThemeManager.accent(), 120));
        }
    }

    private void renderSettingItem(DrawContext context, int x, int y, int w, Setting<?> setting, int mouseX, int mouseY) {
        context.drawTextWithShadow(textRenderer, setting.getName(), x, y + 2, ThemeManager.text());

        if (setting instanceof BooleanSetting bs) {
            // Переключатель-ползунок
            boolean on = bs.get();
            int toggleX = x + w - 22;
            int toggleY = y + 2;
            int toggleW = 18;
            int toggleH = 10;
            RenderUtil.fillRounded(context, toggleX, toggleY, toggleW, toggleH, toggleH / 2,
                    on ? ThemeManager.accent() : ThemeManager.border());
            int dotX = on ? toggleX + toggleW - 7 : toggleX + 2;
            RenderUtil.fillRounded(context, dotX, toggleY - 1, 7, 7, 3.5f, 0xFFFFFFFF);
        } else if (setting instanceof ModeSetting ms) {
            String mode = ms.get();
            int modeW = textRenderer.getWidth(mode);
            context.drawTextWithShadow(textRenderer, mode, x + w - modeW - 4, y + 2, ThemeManager.accent());
        } else if (setting instanceof NumberSetting ns) {
            // Слайдер
            int barX = x;
            int barY = y + 14;
            int barW = w - 8;
            int barH = 4;

            RenderUtil.fillRounded(context, barX, barY, barW, barH, 2, ThemeManager.border());
            double pct = (ns.get() - ns.getMin()) / (ns.getMax() - ns.getMin());
            int fillW = (int) (barW * pct);
            RenderUtil.fillRounded(context, barX, barY, fillW, barH, 2, ThemeManager.accent());

            String val = String.format("%.2f", ns.get());
            int valW = textRenderer.getWidth(val);
            context.drawTextWithShadow(textRenderer, val, x + w - valW - 4, y + 2, ThemeManager.textMuted());
        }
    }

    private void renderSpotifyTab(DrawContext context, int x, int y, int w, int h, int mouseX, int mouseY) {
        RenderUtil.fillRounded(context, x, y, w, 80, 8, ColorManager.withAlpha(0x000000, 40));
        context.drawTextWithShadow(textRenderer, "Spotify Player (Pulse Integration)", x + 15, y + 15, ThemeManager.accent());
        String track = SpotifyManager.getCurrentTrack();
        if (track.isEmpty()) track = "No track playing";
        context.drawTextWithShadow(textRenderer, "Track: " + track, x + 15, y + 32, ThemeManager.text());

        int btnY = y + 50;
        boolean hoverPrev = mouseX >= x + 15 && mouseX < x + 60 && mouseY >= btnY && mouseY < btnY + 20;
        boolean hoverPlay = mouseX >= x + 65 && mouseX < x + 140 && mouseY >= btnY && mouseY < btnY + 20;
        boolean hoverNext = mouseX >= x + 145 && mouseX < x + 190 && mouseY >= btnY && mouseY < btnY + 20;

        Button.draw(context, x + 15, btnY, 45, 20, "\u23EE", hoverPrev);
        Button.draw(context, x + 65, btnY, 75, 20, SpotifyManager.isPlaying() ? "\u23F8" : "\u25B6", hoverPlay);
        Button.draw(context, x + 145, btnY, 45, 20, "\u23ED", hoverNext);

        int themeBtnX = x + w - 145;
        int themeBtnY = y + 15;
        boolean themeHover = mouseX >= themeBtnX && mouseX < themeBtnX + 130 && mouseY >= themeBtnY && mouseY < themeBtnY + 35;
        Button.draw(context, themeBtnX, themeBtnY, 130, 20, "Cycle UI Theme", themeHover);
    }

    // ---------- Обработка ввода ----------

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (bindingModule != null) {
            bindingModule.setKeyBind(GLFW.GLFW_MOUSE_BUTTON_1 + button);
            bindingModule = null;
            return true;
        }

        int panelW = Math.min(PANEL_MAX_W, width - 40);
        int panelH = Math.min(PANEL_MAX_H, height - 50);
        int sx = (width - panelW) / 2;
        int sy = (height - panelH) / 2;

        // Категории
        int catBarY = sy + CATEGORY_BAR_Y_OFFSET;
        if (mouseY >= catBarY && mouseY < catBarY + 20) {
            Category[] cats = Category.values();
            int tabW = (panelW - TAB_PADDING * 2) / cats.length;
            int catX = sx + TAB_PADDING;
            for (int i = 0; i < cats.length; i++) {
                if (mouseX >= catX + i * tabW && mouseX < catX + i * tabW + tabW - 4) {
                    selectedCategory = cats[i];
                    selectedModule = null;
                    panelScroll = 0;
                    smoothPanelScroll = 0;
                    settingsScroll = 0;
                    smoothSettingsScroll = 0;
                    return true;
                }
            }
        }

        // Spotify
        if (selectedCategory == Category.SPOTIFY) {
            int spotX = sx + 15;
            int spotY = sy + CONTENT_Y_OFFSET;
            int btnY = spotY + 50;
            if (mouseY >= btnY && mouseY < btnY + 20) {
                if (mouseX >= spotX + 15 && mouseX < spotX + 60) { SpotifyManager.control("prev"); return true; }
                if (mouseX >= spotX + 65 && mouseX < spotX + 140) { SpotifyManager.control("play"); return true; }
                if (mouseX >= spotX + 145 && mouseX < spotX + 190) { SpotifyManager.control("next"); return true; }
            }
            int themeBtnX = spotX + panelW - 175;
            if (mouseX >= themeBtnX && mouseX < themeBtnX + 130 && mouseY >= spotY + 15 && mouseY < spotY + 35) {
                ThemeManager.cycleTheme();
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        // Список модулей
        int contentY = sy + CONTENT_Y_OFFSET;
        int colW = panelW / 3;
        int modListX = sx + 8;
        int modListW = colW - 12;
        var modules = ModuleRegistry.byCategory(selectedCategory);
        int rowY = contentY + 14 - panelScroll;
        for (Module module : modules) {
            if (rowY + 16 < contentY || rowY > contentY + panelH - 76) continue;
            if (mouseX >= modListX && mouseX < modListX + modListW && mouseY >= rowY && mouseY < rowY + 16) {
                if (button == 0) {
                    module.toggle();
                } else if (button == 1) {
                    selectedModule = module;
                    settingsScroll = 0;
                    smoothSettingsScroll = 0;
                } else if (button == 2) {
                    bindingModule = module;
                }
                return true;
            }
            rowY += 18;
        }

        // Настройки
        if (selectedModule != null) {
            int settingsX = sx + colW + 8;
            int settingsY = contentY + 36 - settingsScroll;
            int settingsW = panelW - colW - 16;

            for (Setting<?> setting : selectedModule.getSettings()) {
                int sH = (setting instanceof NumberSetting) ? 26 : 18;
                if (mouseY >= settingsY && mouseY < settingsY + sH && mouseX >= settingsX && mouseX < settingsX + settingsW) {
                    if (setting instanceof BooleanSetting bs) {
                        bs.set(!bs.get());
                        return true;
                    } else if (setting instanceof ModeSetting ms) {
                        ms.cycle();
                        return true;
                    } else if (setting instanceof NumberSetting ns) {
                        int barX = settingsX;
                        int barY = settingsY + 14;
                        int barW = settingsW - 8;
                        if (mouseY >= barY - 4 && mouseY <= barY + 8) {
                            double pct = Math.max(0, Math.min(1, (mouseX - barX) / (double) barW));
                            ns.set(ns.getMin() + pct * (ns.getMax() - ns.getMin()));
                            return true;
                        }
                    }
                }
                settingsY += sH;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int panelW = Math.min(PANEL_MAX_W, width - 40);
        int sx = (width - panelW) / 2;
        int colW = panelW / 3;
        int midX = sx + colW;

        int amount = (int) (verticalAmount * SCROLL_SPEED);

        if (mouseX < midX) {
            var modules = ModuleRegistry.byCategory(selectedCategory);
            int totalH = modules.size() * 18;
            int visibleH = Math.min(PANEL_MAX_H, height - 50) - 76;
            int maxScroll = Math.max(0, totalH - visibleH);
            panelScroll = Math.max(0, Math.min(panelScroll - amount, maxScroll));
        } else if (selectedModule != null) {
            int totalH = selectedModule.getSettings().stream()
                    .mapToInt(s -> s instanceof NumberSetting ? 26 : 18).sum();
            int visibleH = Math.min(PANEL_MAX_H, height - 50) - 76 - 36;
            int maxScroll = Math.max(0, totalH - visibleH);
            settingsScroll = Math.max(0, Math.min(settingsScroll - amount, maxScroll));
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (bindingModule != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                bindingModule.setKeyBind(GLFW.GLFW_KEY_UNKNOWN);
            } else {
                bindingModule.setKeyBind(keyCode);
            }
            bindingModule = null;
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT || keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close();
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
