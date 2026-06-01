package VexVisuals.gui;

import VexVisuals.module.Module;
import VexVisuals.module.Setting;
import VexVisuals.module.BooleanSetting;
import VexVisuals.module.NumberSetting;
import VexVisuals.module.ModeSetting;
import VexVisuals.module.ModuleRegistry;
import VexVisuals.module.Category;
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

    private static final int PANEL_MAX_W = 720;
    private static final int PANEL_MAX_H = 420;
    private static final int HEADER_HEIGHT = 36;
    private static final int CATEGORY_BAR_Y_OFFSET = 42;
    private static final int CONTENT_Y_OFFSET = 68;
    private static final int TAB_PADDING = 8;
    private static final int SCROLL_SPEED = 14;
    private static final float ANIMATION_DURATION = 320f;

    private Category selectedCategory = Category.COMBAT;
    private Module selectedModule;
    private Module bindingModule;

    private float openProgress = 0f;
    private long openStartTime;

    private int panelScroll = 0;
    private int settingsScroll = 0;
    private float smoothPanelScroll = 0f;
    private float smoothSettingsScroll = 0f;

    private Button spotifyPrevBtn, spotifyPlayBtn, spotifyNextBtn, themeBtn;

    public ClickGuiScreen() {
        super(Text.literal(CLIENT_TITLE));
    }

    @Override
    protected void init() {
        openStartTime = System.currentTimeMillis();
        openProgress = 0f;
        panelScroll = 0;
        settingsScroll = 0;
        smoothPanelScroll = 0f;
        smoothSettingsScroll = 0f;

        spotifyPrevBtn = new Button(0, 0, 45, 20, "\u23EE");
        spotifyPlayBtn = new Button(0, 0, 75, 20, "\u25B6");
        spotifyNextBtn = new Button(0, 0, 45, 20, "\u23ED");
        themeBtn = new Button(0, 0, 130, 20, "Cycle UI Theme");
    }

    @Override
    public void tick() {
        long elapsed = System.currentTimeMillis() - openStartTime;
        openProgress = Math.min(1f, elapsed / ANIMATION_DURATION);

        smoothPanelScroll += (panelScroll - smoothPanelScroll) * 0.3f;
        smoothSettingsScroll += (settingsScroll - smoothSettingsScroll) * 0.3f;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        context.fill(0, 0, width, height, 0x65000000);

        float progress = Easing.easeOutCubic(openProgress);
        int panelW = Math.min(PANEL_MAX_W, width - 40);
        int panelH = Math.min(PANEL_MAX_H, height - 50);

        int x = (width - panelW) / 2;
        int y = (height - panelH) / 2 + (int) ((1f - progress) * 40);
        int scaleW = (int) (panelW * progress);
        int scaleH = (int) (panelH * progress);
        int sx = x + (panelW - scaleW) / 2;
        int sy = y + (panelH - scaleH) / 2;

        GUITheme theme = ThemeManager.getCurrentTheme();

        context.fill(sx - 2, sy - 2, sx + scaleW + 2, sy + scaleH + 2, 0x40000000);
        RenderUtil.fillRounded(context, sx, sy, scaleW, scaleH, theme.radius, theme.panel.getRGB());

        context.fill(sx, sy, sx + scaleW, sy + 3, theme.primary.getRGB());

        renderHeader(context, sx, sy, scaleW, theme);
        renderCategoryBar(context, sx, sy + CATEGORY_BAR_Y_OFFSET, scaleW, mouseX, mouseY, theme);

        int contentY = sy + CONTENT_Y_OFFSET;
        int contentH = scaleH - 76;
        int colW = scaleW / 3;

        if (selectedCategory == Category.MUSIC) {
            renderSpotifyTab(context, sx + 15, contentY, scaleW - 30, contentH, mouseX, mouseY, theme);
        } else {
            renderModuleList(context, sx + 8, contentY, colW - 12, contentH, mouseX, mouseY, theme);
            renderModuleDetails(context, sx + colW + 8, contentY, scaleW - colW - 16, contentH, mouseX, mouseY, theme);
        }

        if (bindingModule != null) {
            String txt = "Press a key or mouse button for " + bindingModule.getName() + " (ESC to reset)";
            int tw = textRenderer.getWidth(txt);
            int hintX = width / 2 - tw / 2 - 8;
            int hintY = height - 24;
            context.fill(hintX, hintY, hintX + tw + 16, hintY + 12, theme.background.getRGB());
            textRenderer.draw(Text.literal(txt), hintX + 4, hintY + 2, theme.primary.getRGB());
        }

        super.render(context, mouseX, mouseY, partialTicks);
    }

    private void renderHeader(DrawContext context, int x, int y, int w, GUITheme theme) {
        context.fill(x + 6, y + 6, x + w - 6, y + 6 + HEADER_HEIGHT, theme.panel.getRGB());

        String title = CLIENT_TITLE;
        String nickname = client.player != null ? client.player.getName().getString() : "Offline";
        String time = LocalTime.now().format(CLOCK);

        int titleColor = ColorManager.chroma(System.currentTimeMillis(), 0);
        textRenderer.draw(Text.literal(title), x + 14, y + 16, titleColor);

        int nickW = textRenderer.getWidth(nickname);
        textRenderer.draw(Text.literal(nickname), x + (w - nickW) / 2, y + 16, theme.text.getRGB());

        int timeW = textRenderer.getWidth(time);
        textRenderer.draw(Text.literal(time), x + w - timeW - 14, y + 16, theme.primary.getRGB());

        String sub = "v1.21.11  |  Theme: " + theme.name;
        textRenderer.draw(Text.literal(sub), x + 14, y + 26, theme.textSecondary.getRGB());
    }

    private void renderCategoryBar(DrawContext context, int x, int y, int w, int mouseX, int mouseY, GUITheme theme) {
        Category[] cats = Category.values();
        int tabW = (w - TAB_PADDING * 2) / cats.length;
        for (int i = 0; i < cats.length; i++) {
            Category cat = cats[i];
            int tx = x + TAB_PADDING + i * tabW;
            boolean sel = cat == selectedCategory;
            boolean hover = mouseX >= tx && mouseX < tx + tabW - 4 && mouseY >= y && mouseY < y + 20;

            int bgColor;
            if (sel) {
                bgColor = ColorManager.withAlpha(theme.primary.getRGB(), 180);
            } else if (hover) {
                bgColor = ColorManager.withAlpha(theme.border.getRGB(), 80);
            } else {
                bgColor = 0x00000000;
            }

            if (bgColor != 0) {
                RenderUtil.fillRounded(context, tx, y, tabW - 4, 20, 6, bgColor);
            }

            String label = cat.getDisplayName();
            int lw = textRenderer.getWidth(label);
            int textColor = sel ? theme.text.getRGB() : theme.textSecondary.getRGB();
            textRenderer.draw(Text.literal(label), tx + (tabW - 4 - lw) / 2, y + 6, textColor);
        }
    }

    private void renderModuleList(DrawContext context, int x, int y, int w, int h, int mouseX, int mouseY, GUITheme theme) {
        var modules = ModuleRegistry.byCategory(selectedCategory);
        textRenderer.draw(Text.literal("Modules (" + modules.size() + ")"), x, y - 2, theme.textSecondary.getRGB());

        int scroll = Math.round(smoothPanelScroll);
        int baseY = y + 14 - scroll;

        int index = 0;
        for (Module module : modules) {
            int rowY = baseY + index * 18;
            if (rowY + 18 < y || rowY > y + h) {
                index++;
                continue;
            }

            ModuleGui gui = new ModuleGui(module, x, rowY, w, 16);
            gui.setSelected(module == selectedModule);
            gui.render(context, mouseX, mouseY);
            index++;
        }

        int totalHeight = modules.size() * 18;
        int maxScroll = Math.max(0, totalHeight - h);
        if (maxScroll > 0) {
            float barHeight = Math.max(20, (float) h / totalHeight * h);
            float barY = y + (float) scroll / maxScroll * (h - barHeight);
            RenderUtil.fillRounded(context, x + w - 3, (int) barY, 3, (int) barHeight, 2,
                    ColorManager.withAlpha(theme.primary.getRGB(), 120));
        }
    }

    private void renderModuleDetails(DrawContext context, int x, int y, int w, int h, int mouseX, int mouseY, GUITheme theme) {
        if (selectedModule == null) {
            String placeholder = "Select a module to view settings";
            int tw = textRenderer.getWidth(placeholder);
            textRenderer.draw(Text.literal(placeholder), x + (w - tw) / 2, y + h / 3, theme.textSecondary.getRGB());
            return;
        }

        textRenderer.draw(Text.literal(selectedModule.getName()), x, y, theme.primary.getRGB());
        textRenderer.draw(Text.literal(selectedModule.getDescription()), x, y + 12, theme.textSecondary.getRGB());

        int scroll = Math.round(smoothSettingsScroll);
        int sy = y + 36 - scroll;
        int settingsW = w - 10;

        for (Setting<?> setting : selectedModule.getSettings()) {
            if (sy > y + h) break;
            if (sy + 20 >= y + 36) {
                renderSettingItem(context, x, sy, settingsW, setting, mouseX, mouseY, theme);
            }
            sy += (setting instanceof NumberSetting) ? 26 : 18;
        }

        int totalHeight = selectedModule.getSettings().stream()
                .mapToInt(s -> s instanceof NumberSetting ? 26 : 18).sum();
        int maxScroll = Math.max(0, totalHeight - (h - 36));
        if (maxScroll > 0) {
            float barHeight = Math.max(20, (float) (h - 36) / totalHeight * (h - 36));
            float barY = y + 36 + (float) scroll / maxScroll * ((h - 36) - barHeight);
            RenderUtil.fillRounded(context, x + settingsW + 4, (int) barY, 3, (int) barHeight, 2,
                    ColorManager.withAlpha(theme.primary.getRGB(), 120));
        }
    }

    private void renderSettingItem(DrawContext context, int x, int y, int w, Setting<?> setting, int mouseX, int mouseY, GUITheme theme) {
        textRenderer.draw(Text.literal(setting.getName()), x, y + 2, theme.text.getRGB());

        if (setting instanceof BooleanSetting bs) {
            boolean on = bs.get();
            int toggleX = x + w - 22;
            int toggleY = y + 2;
            int toggleW = 18;
            int toggleH = 10;
            RenderUtil.fillRounded(context, toggleX, toggleY, toggleW, toggleH, toggleH / 2,
                    on ? theme.primary.getRGB() : theme.border.getRGB());
            int dotX = on ? toggleX + toggleW - 7 : toggleX + 2;
            RenderUtil.fillRounded(context, dotX, toggleY - 1, 7, 7, 3, 0xFFFFFFFF);
        } else if (setting instanceof ModeSetting ms) {
            String mode = ms.get();
            int modeW = textRenderer.getWidth(mode);
            textRenderer.draw(Text.literal(mode), x + w - modeW - 4, y + 2, theme.primary.getRGB());
        } else if (setting instanceof NumberSetting ns) {
            int barX = x;
            int barY = y + 14;
            int barW = w - 8;
            int barH = 4;
            RenderUtil.fillRounded(context, barX, barY, barW, barH, 2, theme.border.getRGB());
            double pct = (ns.get() - ns.getMin()) / (ns.getMax() - ns.getMin());
            int fillW = (int) (barW * pct);
            RenderUtil.fillRounded(context, barX, barY, fillW, barH, 2, theme.primary.getRGB());

            String val = String.format("%.2f", ns.get());
            int valW = textRenderer.getWidth(val);
            textRenderer.draw(Text.literal(val), x + w - valW - 4, y + 2, theme.textSecondary.getRGB());
        }
    }

    private void renderSpotifyTab(DrawContext context, int x, int y, int w, int h, int mouseX, int mouseY, GUITheme theme) {
        RenderUtil.fillRounded(context, x, y, w, 80, 8, ColorManager.withAlpha(0x000000, 40));
        textRenderer.draw(Text.literal("Spotify Player (Pulse Integration)"), x + 15, y + 15, theme.primary.getRGB());

        SpotifyManager spotify = SpotifyManager.getInstance();
        String track = spotify.getCurrentTrack();
        textRenderer.draw(Text.literal("Track: " + track), x + 15, y + 32, theme.text.getRGB());

        spotifyPrevBtn.x = x + 15; spotifyPrevBtn.y = y + 50;
        spotifyPlayBtn.x = x + 65; spotifyPlayBtn.y = y + 50;
        spotifyNextBtn.x = x + 145; spotifyNextBtn.y = y + 50;
        themeBtn.x = x + w - 145; themeBtn.y = y + 15;

        spotifyPrevBtn.render(context, mouseX, mouseY);
        spotifyPlayBtn.text = spotify.isPlaying() ? "\u23F8" : "\u25B6";
        spotifyPlayBtn.render(context, mouseX, mouseY);
        spotifyNextBtn.render(context, mouseX, mouseY);
        themeBtn.render(context, mouseX, mouseY);
    }

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

        int catBarY = sy + CATEGORY_BAR_Y_OFFSET;
        if (mouseY >= catBarY && mouseY < catBarY + 20) {
            Category[] cats = Category.values();
            int tabW = (panelW - TAB_PADDING * 2) / cats.length;
            int catX = sx + TAB_PADDING;
            for (int i = 0; i < cats.length; i++) {
                if (mouseX >= catX + i * tabW && mouseX < catX + i * tabW + tabW - 4) {
                    selectedCategory = cats[i];
                    selectedModule = null;
                    panelScroll = 0; smoothPanelScroll = 0;
                    settingsScroll = 0; smoothSettingsScroll = 0;
                    return true;
                }
            }
        }

        if (selectedCategory == Category.MUSIC) {
            if (spotifyPrevBtn.mouseClicked(mouseX, mouseY, button)) {
                SpotifyManager.getInstance().previous();
                return true;
            }
            if (spotifyPlayBtn.mouseClicked(mouseX, mouseY, button)) {
                SpotifyManager.getInstance().togglePlay();
                return true;
            }
            if (spotifyNextBtn.mouseClicked(mouseX, mouseY, button)) {
                SpotifyManager.getInstance().next();
                return true;
            }
            if (themeBtn.mouseClicked(mouseX, mouseY, button)) {
                ThemeManager.nextTheme();
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        int contentY = sy + CONTENT_Y_OFFSET;
        int colW = panelW / 3;
        var modules = ModuleRegistry.byCategory(selectedCategory);
        int rowY = contentY + 14 - panelScroll;
        int modListX = sx + 8;
        int modListW = colW - 12;

        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            int ry = rowY + i * 18;
            if (ry + 16 < contentY || ry > contentY + panelH - 76) continue;
            if (mouseX >= modListX && mouseX < modListX + modListW && mouseY >= ry && mouseY < ry + 16) {
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
        }

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
