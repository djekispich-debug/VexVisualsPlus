package VexVisuals.render;

import VexVisuals.util.ColorManager;
import VexVisuals.util.Easing;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public final class JumpCircle {
    private static boolean active;
    private static boolean wasOnGround = true;
    private static final List<Circle> CIRCLES = new ArrayList<>();

    private JumpCircle() {
    }

    public static void setActive(boolean value) {
        active = value;
        if (!value) {
            CIRCLES.clear();
        }
    }

    public static void onTick(Player player) {
        if (!active || player == null) {
            return;
        }
        boolean onGround = player.onGround();
        if (wasOnGround && !onGround) {
            CIRCLES.add(new Circle(player.position(), System.currentTimeMillis(), 600, 1.2f));
        }
        wasOnGround = onGround;

        long now = System.currentTimeMillis();
        CIRCLES.removeIf(c -> now - c.startMs > c.durationMs);
    }

    public static void render(PoseStack poseStack, MultiBufferSource.BufferSource buffers, float partialTicks) {
        if (!active || CIRCLES.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        long now = System.currentTimeMillis();
        VertexConsumer lines = buffers.getBuffer(RenderType.debugLineStrip(2.0));
        Matrix4f matrix = poseStack.last().pose();

        for (Circle circle : CIRCLES) {
            float life = (now - circle.startMs) / (float) circle.durationMs;
            if (life > 1f) continue;
            float expand = Easing.easeOutCubic(life);
            float alpha = 1f - Easing.smoothStep(life);
            float radius = circle.maxRadius * expand;
            int color = ColorManager.withAlpha(0xFF8B5CF6, alpha * 0.85f);
            drawRing(matrix, lines, circle.origin, radius, color, poseStack, partialTicks);
        }
    }

    private static void drawRing(Matrix4f matrix, VertexConsumer consumer, Vec3 center, float radius, int argb,
                                 PoseStack pose, float partialTicks) {
        float r = ((argb >> 16) & 0xFF) / 255f;
        float g = ((argb >> 8) & 0xFF) / 255f;
        float b = (argb & 0xFF) / 255f;
        float a = ((argb >> 24) & 0xFF) / 255f;
        int segments = 48;
        Vec3 prev = null;
        for (int i = 0; i <= segments; i++) {
            double ang = (Math.PI * 2 * i) / segments;
            Vec3 p = new Vec3(
                    center.x + Math.cos(ang) * radius,
                    center.y + 0.03,
                    center.z + Math.sin(ang) * radius
            );
            if (prev != null) {
                consumer.addVertex(matrix, (float) prev.x, (float) prev.y, (float) prev.z)
                        .setColor(r, g, b, a).setNormal(pose, 0f, 1f, 0f);
                consumer.addVertex(matrix, (float) p.x, (float) p.y, (float) p.z)
                        .setColor(r, g, b, a).setNormal(pose, 0f, 1f, 0f);
            }
            prev = p;
        }
    }

    private record Circle(Vec3 origin, long startMs, int durationMs, float maxRadius) {
    }
}
