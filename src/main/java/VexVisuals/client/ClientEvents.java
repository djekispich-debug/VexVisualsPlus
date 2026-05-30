package VexVisuals.client;

import VexVisuals.gui.ClickGuiScreen;
import VexVisuals.module.Module;
import VexVisuals.module.ModuleRegistry;
import VexVisuals.render.JumpCircle;
import VexVisuals.render.ProjectileTrajectory;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
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
        WorldRenderEvents.LAST.register(context -> {
            PoseStack pose = context.matrixStack();
            MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
            float pt = context.tickCounter().getGameTimeDeltaPartialTick(true);
            ProjectileTrajectory.render(pose, buffers, pt);
            JumpCircle.render(pose, buffers, pt);
            buffers.endBatch();
        });
    }

    private static void onTick(Minecraft mc) {
        if (mc.player == null) {
            return;
        }
        JumpCircle.onTick(mc.player);

        long window = mc.getWindow().getWindow();
        boolean shiftDown = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS;
        if (shiftDown && !shiftWasDown && mc.screen == null) {
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
