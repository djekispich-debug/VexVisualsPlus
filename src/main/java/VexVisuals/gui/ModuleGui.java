package com.yourmod.gui;

import com.yourmod.modules.Module;
import net.minecraft.client.gui.DrawContext;

public class ModuleGui {
    public Module module;
    public Button button;

    public ModuleGui(Module module, int x, int y) {
        this.module = module;
        this.button = new Button(x, y, 180, 28, module.getName());
        this.button.toggled = module.isEnabled();
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        button.render(context, mouseX, mouseY);
    }

    public void updateToggle() {
        if (button.toggled != module.isEnabled()) {
            module.toggle();
        }
    }
}
