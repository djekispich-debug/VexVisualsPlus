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

        // Временно отключаем весь текст, чтобы избежать краша
        // renderHeader(...);
        // renderCategoryBar(...);
        // renderModuleList(...);
        // renderModuleDetails(...);

        if (bindingModule != null) {
            String hint = "Бинд: " + bindingModule.getName() + " — клавиша / колёсико / ESC = сброс";
            int tw = this.textRenderer.getWidth(hint);
            context.fill(this.width / 2 - tw / 2 - 8, this.height - 36, this.width / 2 + tw / 2 + 8, this.height - 18, ThemeManager.background());
            // context.drawText(this.textRenderer, Text.literal(hint), this.width / 2 - tw / 2, this.height - 32, ThemeManager.accent(), true);
        }
        super.render(context, mouseX, mouseY, partialTick);
    }

    private void renderBlurredBackground(DrawContext context) {
        context.fill(0, 0, this.width, this.height, ThemeManager.background());
    }

    // Обработка кликов, скролла, клавиш – всё работает
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (bindingModule != null) {
            bindingModule.setKeyBind(GLFW.GLFW_MOUSE_BUTTON_1 + button);
            bindingModule = null;
            return true;
        }
        if (handleCategoryClick((int) mouseX, (int) mouseY, button)) return true;
        if (handleModuleListClick((int) mouseX, (int) mouseY, button)) return true;
        if (handleSettingsClick((int) mouseX, (int) mouseY, button)) return true;
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
                if (setting instanceof BooleanSetting bs) bs.set(!bs.get());
                else if (setting instanceof ModeSetting ms) ms.cycle();
                return true;
            }
            y += h;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (mouseX < width / 2) panelScroll = Math.max(0, panelScroll - (int) scrollY * 12);
        else settingsScroll = Math.max(0, settingsScroll - (int) scrollY * 12);
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (bindingModule != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) bindingModule.setKeyBind(GLFW.GLFW_KEY_UNKNOWN);
            else bindingModule.setKeyBind(keyCode);
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

    private int settingHeight(Setting<?> setting) {
        return switch (setting.getType()) {
            case NUMBER -> 28;
            case COLOR -> 22;
            default -> 18;
        };
    }
}
