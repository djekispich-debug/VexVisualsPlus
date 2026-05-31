package VexVisuals.client;

import VexVisuals.gui.ClickGuiScreen;
import VexVisuals.module.Module;
import VexVisuals.module.ModuleRegistry;
import VexVisuals.module.JumpCircle;
import VexVisuals.module.ProjectileTrajectory;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
// import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;  // ТИМЧАСОВО ВИМКНЕНО
import net.minecraft.client.MinecraftClient;
// import net.minecraft.client.render.VertexConsumerProvider;  // не потрібно без WorldRenderEvents
// import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public final class ClientEvents {
    private static boolean shiftWasDown;
    private static final Map<Module, Boolean> BIND_HELD = new HashMap<>();

    private ClientEvents() {
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientEvents::onTick);
        
        // ТИМЧАСОВО ВИМКНЕНО — WorldRenderEvents недоступний у цій версії Fabric API
        // TODO: знайти альтернативний спосіб рендерингу ProjectileTrajectory та JumpCircle
        /*
        WorldRenderEvents.LAST.register(context -> {
            // ... код ...
        });
        */
    }

    private static void onTick(MinecraftClient mc) {
        if (mc.player == null) {
            return;
        }
        
        // Викликаємо оновлений тік стрибкових кіл у папці module
        JumpCircle.onTick(mc.player);

        long window = mc.getWindow().getHandle();
        boolean shiftDown = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
        if (shiftDown && !shiftWasDown && mc.currentScreen == null) {
            mc.setScreen(new ClickGuiScreen());
        }
        shiftWasDown = shiftDown;

        for (Module module : ModuleRegistry.all()) {
            int bind = module.getKeyBind();
            if (bind == GLFW.GLFW_KEY_UNKNOWN) {
                continue;
            }
            boolean down = isBindDown(window, bind);
            boolean was = BIND_HELD.getOrDefault(module, false);
            if (down && !was) {
                module.toggle();
            }
            BIND_HELD.put(module, down);
        }
    }

    private static boolean isBindDown(long window, int bind) {
        if (bind >= GLFW.GLFW_MOUSE_BUTTON_1 && bind <= GLFW.GLFW_MOUSE_BUTTON_8) {
            return GLFW.glfwGetMouseButton(window, bind) == GLFW.GLFW_PRESS;
        }
        return GLFW.glfwGetKey(window, bind) == GLFW.GLFW_PRESS;
    }
}
