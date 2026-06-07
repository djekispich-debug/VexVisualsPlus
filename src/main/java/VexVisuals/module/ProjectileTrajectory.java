package VexVisuals.module;

import VexVisuals.util.ColorManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public final class ProjectileTrajectory extends Module {
    private static boolean active;
    private static final List<ProjectileEntity> trackedProjectiles = new ArrayList<>();

    public ProjectileTrajectory() {
        super(ModuleType.PROJECTILE_TRAJECTORY);
    }

    public static void setActive(boolean value) {
        active = value;
        if (!value) {
            trackedProjectiles.clear();
        }
    }

    public static boolean isActive() {
        return active;
    }

    public static void render(MatrixStack matrices, VertexConsumerProvider.Immediate providers, float partialTicks) {
        if (!active) {
            return;
        }

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.gameRenderer == null) {
            return;
        }

        VertexConsumer lines = providers.getBuffer(RenderLayer.getLines());
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();

        for (ProjectileEntity projectile : trackedProjectiles) {
            if (projectile.isRemoved()) continue;
            drawTrajectory(matrix, lines, projectile, cameraPos);
        }

        providers.drawCurrentLayer();
    }

    private static void drawTrajectory(Matrix4f matrix, VertexConsumer consumer, ProjectileEntity projectile, Vec3d cameraPos) {
        Vec3d pos = projectile.getPos();
        Vec3d velocity = projectile.getVelocity();

        float r = 1.0f;
        float g = 0.5f;
        float b = 0.0f;
        float a = 0.8f;

        int steps = 20;
        Vec3d current = pos;

        for (int i = 0; i < steps; i++) {
            Vec3d next = new Vec3d(
                    current.x + velocity.x * 0.05,
                    current.y + velocity.y * 0.05 - (9.81 * 0.0025),
                    current.z + velocity.z * 0.05
            );

            Vec3d screenCurrent = new Vec3d(
                    current.x - cameraPos.x,
                    current.y - cameraPos.y,
                    current.z - cameraPos.z
            );
            Vec3d screenNext = new Vec3d(
                    next.x - cameraPos.x,
                    next.y - cameraPos.y,
                    next.z - cameraPos.z
            );

            consumer.vertex(matrix, (float) screenCurrent.x, (float) screenCurrent.y, (float) screenCurrent.z)
                    .color(r, g, b, a).normal(0f, 1f, 0f);
            consumer.vertex(matrix, (float) screenNext.x, (float) screenNext.y, (float) screenNext.z)
                    .color(r, g, b, a).normal(0f, 1f, 0f);

            current = next;
            velocity = new Vec3d(velocity.x * 0.99, velocity.y, velocity.z * 0.99);
        }
    }

    public static void trackProjectile(ProjectileEntity projectile) {
        if (active && !trackedProjectiles.contains(projectile)) {
            trackedProjectiles.add(projectile);
        }
    }

    public static void clearTracking() {
        trackedProjectiles.clear();
    }
}
