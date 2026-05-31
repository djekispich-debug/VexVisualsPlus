package VexVisuals.module;

import VexVisuals.util.ColorManager;
import VexVisuals.util.Easing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public final class JumpCircle extends Module {
    private static boolean active;
    private static boolean wasOnGround = true;
    private static final List<Circle> CIRCLES = new ArrayList<>();

    public JumpCircle() {
        super(ModuleType.JUMP_CIRCLE);
    }

    public static void setActive(boolean value) {
        active = value;
        if (!value) {
            CIRCLES.clear();
        }
    }

    public static void onTick(PlayerEntity player) {
        if (!active || player == null) {
            return;
        }
        boolean onGround = player.isOnGround();
        if (wasOnGround && !onGround) {
            CIRCLES.add(new Circle(player.getPos(), System.currentTimeMillis(), 600, 1.2f));
        }
        wasOnGround = onGround;

        long now = System.currentTimeMillis();
        CIRCLES.removeIf(c -> now - c.startMs > c.durationMs);
    }

    public static void render(MatrixStack matrices, VertexConsumerProvider.Immediate providers, float partialTicks) {
        if (!active || CIRCLES.isEmpty()) {
            return;
        }
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.gameRenderer == null) {
            return;
        }
        long now = System.currentTimeMillis();
        
        // В Yarn для кастомных линий используется RenderLayer.getLines()
        VertexConsumer lines = providers.getBuffer(RenderLayer.getLines());
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        // Получаем позицию камеры для корректного сдвига координат в мире
        Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();

        for (Circle circle : CIRCLES) {
            float life = (now - circle.startMs) / (float) circle.durationMs;
            if (life > 1f) continue;
            float expand = Easing.easeOutCubic(life);
            float alpha = 1f - Easing.smoothStep(life);
            float radius = circle.maxRadius * expand;
            int color = ColorManager.withAlpha(0xFF8B5CF6, alpha * 0.85f);
            drawRing(matrix, lines, circle.origin, radius, color, cameraPos);
        }
    }

    private static void drawRing(Matrix4f matrix, VertexConsumer consumer, Vec3d center, float radius, int argb, Vec3d cameraPos) {
        float r = ((argb >> 16) & 0xFF) / 255f;
        float g = ((argb >> 8) & 0xFF) / 255f;
        float b = (argb & 0xFF) / 255f;
        float a = ((argb >> 24) & 0xFF) / 255f;
        int segments = 48;
        Vec3d prev = null;
        
        for (int i = 0; i <= segments; i++) {
            double ang = (Math.PI * 2 * i) / segments;
            // Рассчитываем позицию относительно камеры игрока (World-to-Camera Space)
            Vec3d p = new Vec3d(
                    center.x + Math.cos(ang) * radius - cameraPos.x,
                    center.y + 0.03 - cameraPos.y,
                    center.z + Math.sin(ang) * radius - cameraPos.z
                );
            if (prev != null) {
                consumer.vertex(matrix, (float) prev.x, (float) prev.y, (float) prev.z)
                        .color(r, g, b, a).normal(0f, 1f, 0f);
                consumer.vertex(matrix, (float) p.x, (float) p.y, (float) p.z)
                        .color(r, g, b, a).normal(0f, 1f, 0f);
            }
            prev = p;
        }
    }

    private record Circle(Vec3d origin, long startMs, int durationMs, float maxRadius) {
    }
}
