package VexVisuals.gui;

import VexVisuals.module.Module;
import VexVisuals.module.ModuleRegistry;
import VexVisuals.module.Category;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends Screen {
    private static final int PANEL_WIDTH = 150;
    private static final int PANEL_HEIGHT = 300;
    private static final int PANEL_SPACING = 10;

    private final List<ModulePanel> panels;
    private int scrollX = 0;

    public ClickGuiScreen() {
        super(Text.literal("VexVisuals Click GUI"));
        this.panels = new ArrayList<>();
        initPanels();
    }

    private void initPanels() {
        int x = PANEL_SPACING;
        for (Category category : Category.values()) {
            List<Module> modules = ModuleRegistry.byCategory(category);
            if (!modules.isEmpty()) {
                ModulePanel panel = new ModulePanel(x, 50, PANEL_WIDTH, PANEL_HEIGHT, category.getDisplayName(), modules);
                panels.add(panel);
                x += PANEL_WIDTH + PANEL_SPACING;
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(context, mouseX, mouseY, partialTick);
        
        for (ModulePanel panel : panels) {
            panel.render(context, mouseX, mouseY);
        }

        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("VexVisuals - Click GUI"),
                this.width / 2, 10, 0xFFFFFF);

        super.render(context, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        for (ModulePanel panel : panels) {
            if (panel.mouseScrolled(mouseX, mouseY, scrollY)) {
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (ModulePanel panel : panels) {
            if (panel.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    private static class ModulePanel {
        private final int x, y, width, height;
        private final String title;
        private final List<Module> modules;
        private int scrollOffset = 0;

        ModulePanel(int x, int y, int width, int height, String title, List<Module> modules) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.title = title;
            this.modules = modules;
        }

        void render(DrawContext context, int mouseX, int mouseY) {
            context.fill(x, y, x + width, y + height, 0xFF1a1a1a);
            context.fill(x, y, x + width, y + 20, 0xFF2a2a2a);

            context.drawTextWithShadow(context.getMinecraftClient().textRenderer, title,
                    x + 5, y + 5, 0xFFFFFF);

            int moduleY = y + 25;
            for (Module module : modules) {
                if (moduleY > y + height) break;

                int bgColor = module.isEnabled() ? 0xFF3a8c2f : 0xFF4a4a4a;
                context.fill(x + 2, moduleY, x + width - 2, moduleY + 18, bgColor);
                context.drawTextWithShadow(context.getMinecraftClient().textRenderer, module.getName(),
                        x + 5, moduleY + 5, 0xFFFFFF);

                moduleY += 20;
            }
        }

        boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (mouseX < x || mouseX > x + width || mouseY < y || mouseY > y + height) {
                return false;
            }

            int moduleY = y + 25;
            for (Module module : modules) {
                if (mouseY >= moduleY && mouseY < moduleY + 18) {
                    module.toggle();
                    return true;
                }
                moduleY += 20;
            }
            return false;
        }

        boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
            if (mouseX < x || mouseX > x + width || mouseY < y || mouseY > y + height) {
                return false;
            }
            scrollOffset = (int) Math.max(0, scrollOffset - scrollY * 3);
            return true;
        }
    }
}
