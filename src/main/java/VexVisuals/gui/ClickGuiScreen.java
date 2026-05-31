package VexVisuals.gui;

import VexVisuals.module.Category;
import VexVisuals.module.Setting;
import VexVisuals.module.BooleanSetting;
import VexVisuals.module.NumberSetting;
import VexVisuals.module.ModeSetting;
import VexVisuals.module.ColorSetting;
import VexVisuals.module.ModuleRegistry;
import VexVisuals.util.ColorManager;
import VexVisuals.util.Easing;
import VexVisuals.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClickGuiScreen extends Screen {
    public static final String CLIENT_TITLE = "VexVisualsPlus";
    private static final DateTimeFormatter CLOCK = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private Category selectedCategory = Category.HUD;
    private VexVisuals.module.Module selectedModule;
    private VexVisuals.module.Module bindingModule;
    private float openProgress;
    private long openStartMs;
    private int panelScroll;
    private int settingsScroll;

    public ClickGuiScreen() {
        super(Text.literal(CLIENT_TITLE));
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
    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {
        renderBlurredBackground(context);
        float anim = Easing.easeOutBack(openProgress);
        int panelW = Math.min(720, this.width - 40);
        int panelH = Math.min(420, this.height - 50);
        int baseX = (this.width - panelW) / 2;
        int baseY = (this.height - panelH) / 2;
        int slideY = (int) ((1f - Easing.easeOutCubic(openProgress)) * 48);
        int x = baseX;
        int y = baseY + slideY;
        float scale = Easing.lerp(0.88f, 1f, anim);
        int scaledW = (int) (panelW * scale);
        int scaledH = (int) (panelH * scale);
        int sx = x + (panelW - scaledW) / 2;
        int sy = y + (panelH - scaledH) / 2;

        RenderUtil.fillRounded(context, sx, sy, scaledW, scaledH, 10, ThemeManager.panel());
        context.fill(sx, sy, sx + scaledW, sy + 2, ThemeManager.accent());

        renderHeader(context, sx, sy, scaledW);
        renderCategoryBar(context, sx, sy + 42, scaledW, mouseX, mouseY);
        int contentY = sy + 68;
        int contentH = scaledH - 76;
        int colW = scaledW / 3;
        renderModuleList(context, sx + 8, contentY, colW - 12, contentH, mouseX, mouseY);
        renderModuleDetails(context, sx + colW + 8, contentY, colW * 2 - 16, contentH, mouseX, mouseY);

        if (bindingModule != null) {
            String hint = "Бинд: " + bindingModule.getName() + " — клавиша / колёсико / ESC = сброс";
            int tw = this.textRenderer.getWidth(hint);
            context.fill(this.width / 2 - tw / 2 - 8, this.height - 36, this.width / 2 + tw / 2 + 8, this.height - 18, ThemeManager.background());
            context.drawText(this.textRenderer, Text.literal(hint), this.width / 2 - tw / 2, this.height - 32, ThemeManager.accent(), true);
        }
        super.render(context, mouseX, mouseY, partialTick);
    }

    private void renderBlurredBackground(DrawContext context) {
        context.fill(0, 0, this.width, this.height, ThemeManager.background());
    }

    private void renderHeader(DrawContext context, int x, int y, int w) {
        int barH = 36;
        RenderUtil.horizontalGradient(context, x + 6, y + 6, w - 12, barH, ThemeManager.headerGradientTop(), ThemeManager.headerGradientBottom());

        String title = CLIENT_TITLE;
        String nickname = mc.player != null ? mc.player.getName().getString() : "Оффлайн";
        String time = LocalTime.now().format(CLOCK);

        int titleColor = ColorManager.chroma(System.currentTimeMillis(), 0);
        context.drawText(this.textRenderer, Text.literal(title), x + 14, y + 16, titleColor, true);
        int nickW = this.textRenderer.getWidth(nickname);
        context.drawText(this.textRenderer, Text.literal(nickname), x + (w - nickW) / 2, y + 16, ThemeManager.text(), true);
        int timeW = this.textRenderer.getWidth(time);
        context.drawText(this.textRenderer, Text.literal(time), x + w - timeW - 14, y + 16, ThemeManager.accent(), true);

        String sub = "v1.21.11  |  Тема: " + ThemeManager.getCurrent().name();
        context.drawText(this.textRenderer, Text.literal(sub), x + 14, y + 26, ThemeManager.textMuted(), true);
    }

    private void renderCategoryBar(DrawContext context, int x, int y, int w, int mouseX, int mouseY) {
        Category[] cats = Category.values();
        int tabW = (w - 16) / cats.length;
        for (int i = 0; i < cats.length; i++) {
            Category cat = cats[i];
            int tx = x + 8 + i * tabW;
            boolean sel = cat == selectedCategory;
            boolean hover = mouseX >= tx && mouseX < tx + tabW - 4 && mouseY >= y && mouseY < y + 20;
            int bg = sel ? ThemeManager.accent() : (hover ? ThemeManager.border() : 0x00000000);
            if (bg != 0) {
                context.fill(tx, y, tx + tabW - 4, y + 20, ColorManager.withAlpha(bg, sel ? 0.35f : 0.2f));
            }
            String label = cat.getDisplayName();
            int lw = this.textRenderer.getWidth(label);
            context.drawText(this.textRenderer, Text.literal(label), tx + (tabW - 4 - lw) / 2, y + 6, sel ? ThemeManager.text() : ThemeManager.textMuted(), true);
        }
    }

    private void renderModuleList(DrawContext context, int x, int y, int w, int h, int mouseX, int mouseY) {
        List<VexVisuals.module.Module> modules = ModuleRegistry.byCategory(selectedCategory);
        context.drawText(this.textRenderer, Text.literal("Модули (" + modules.size() + ")"), x, y, ThemeManager.textMuted(), true);
        int rowY = y + 14 - panelScroll;
        for (VexVisuals.module.Module module : modules) {
            if (rowY > y + h) break;
            if (rowY + 18 >= y) {
                boolean hover = mouseX >= x && mouseX < x + w && mouseY >= rowY && mouseY < rowY + 16;
                boolean sel = module == selectedModule;
                if (hover || sel) {
                    context.fill(x, rowY, x + w, rowY + 16, ColorManager.withAlpha(ThemeManager.accent(), sel ? 0.35f : 0.15f));
                }
                int nameColor = module.isEnabled() ? ThemeManager.accent() : ThemeManager.text();
                context.drawText(this.textRenderer, Text.literal(module.getName()), x + 4, rowY + 4, nameColor, true);
                String bind = keyName(module.getKeyBind());
                int bw = this.textRenderer.getWidth(bind);
                context.drawText(this.textRenderer, Text.literal(bind), x + w - bw - 4, rowY + 4, ThemeManager.textMuted(), true);
            }
            rowY += 18;
        }
    }

    private void renderModuleDetails(DrawContext context, int x, int y, int w, int h, int mouseX, int mouseY) {
        if (selectedModule == null) {
            context.drawText(this.textRenderer, Text.literal("Выберите модуль слева"), x, y + 20, ThemeManager.textMuted(), true);
            return;
        }
        context.drawText(this.textRenderer, Text.literal(selectedModule.getName()), x, y, ThemeManager.accent(), true);
        context.drawText(this.textRenderer, Text.literal(selectedModule.getDescription()), x, y + 12, ThemeManager.textMuted(), true);
        String status = selectedModule.isEnabled() ? "Вкл" : "Выкл";
        context.drawText(this.textRenderer, Text.literal("Статус: " + status + "  |  Бинд: " + keyName(selectedModule.getKeyBind())), x, y + 24, ThemeManager.text(), true);

        int sy = y + 40 - settingsScroll;
        for (Setting<?> setting : selectedModule.getSettings()) {
            if (sy > y + h) break;
            renderSetting(context, x, sy, w, setting, mouseX, mouseY);
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

    private void renderSetting(DrawContext context, int x, int sy, int w, Setting<?> setting, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, Text.literal(setting.getName()), x, sy + 2, ThemeManager.text(), true);
        switch (setting.getType()) {
            case BOOLEAN -> {
                BooleanSetting bs = (BooleanSetting) setting;
                String v = bs.get() ? "✓" : "✗";
                context.drawText(this.textRenderer, Text.literal(v), x + w - 12, sy + 2, bs.get() ? ThemeManager.accent() : ThemeManager.textMuted(), true);
            }
            case NUMBER -> {
                NumberSetting ns = (NumberSetting) setting;
                int barX = x;
                int barY = sy + 14;
                int barW = w - 8;
                context.fill(barX, barY, barX + barW, barY + 4, ThemeManager.border());
                double t = (ns.get() - ns.getMin()) / (ns.getMax() - ns.getMin());
                int fill = (int) (barW * t);
                context.fill(barX, barY, barX + fill, barY + 4, ThemeManager.accent());
                String val = String.format("%.2f", ns.get());
                context.drawText(this.textRenderer, Text.literal(val), x + w - this.textRenderer.getWidth(val), sy + 2, ThemeManager.textMuted(), true);
            }
            case MODE -> {
                ModeSetting ms = (ModeSetting) setting;
                context.drawText(this.textRenderer, Text.literal(ms.get()), x + w - this.textRenderer.getWidth(ms.get()) - 4, sy + 2, ThemeManager.accent(), true);
            }
            case COLOR -> {
                ColorSetting cs = (ColorSetting) setting;
                context.fill(x + w - 24, sy, x + w - 4, sy + 14, cs.get() | 0xFF000000);
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
        List<VexVisuals.module.Module> modules = ModuleRegistry.byCategory(selectedCategory);
        for (VexVisuals.module.Module module : modules) {
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
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
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
