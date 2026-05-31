package VexVisuals.module;

import VexVisuals.util.ColorManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public final class ProjectileTrajectory extends Module {
    private static boolean active;
    private static final int MAX_STEPS = 80;
    private static final double GRAVITY = 0.03;
    private static final double DRAG = 0.99;

    public ProjectileTrajectory() {
        super(ModuleType.PEARL_COOLDOWN);
    }

    public static void setActive(boolean value) {
        active = value;
    }

    public static boolean isActive() {
        return active;
    }

    public static void render(MatrixStack matrices, VertexConsumerProvider.Immediate providers, float partialTicks) {
        if (!active) {
            return;
        }
        MinecraftClient mc = MinecraftClient.getInstance();
        PlayerEntity player = mc.player;
        World level = mc.world;
        if (player == null || level == null || mc.gameRenderer == null) {
            return;
        }
        if (!player.getMainHandStack().isOf(Items.ENDER_PEARL) && !player.getMainHandStack().isOf(Items.SPLASH_POTION)
                && !player.getMainHandStack().isOf(Items.LINGERING_POTION)) {
            return;
        }

        // Отримуємо позицію камери для переходу у відносні координати світу (World-to-Camera)
        Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();

        Vec3d eye = player.getEyePos();
        Vec3d look = player.getRotationVec(partialTicks);
        double speed = player.getMainHandStack().isOf(Items.ENDER_PEARL) ? 1.5 : 0.5;
        Vec3d motion = look.multiply(speed);
        Vec3d pos = eye;

        // Використовуємо RenderLayer.getLines() для Yarn-маппінгів
        VertexConsumer lines = providers.getBuffer(RenderLayer.getLines());
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        Vec3d prev = pos;
        Vec3d landing = pos;
        boolean blocked = false;

        for (int i = 0; i < MAX_STEPS; i++) {
            motion = motion.add(0, -GRAVITY, 0).multiply(DRAG);
            pos = pos.add(motion);

            // Трасування променів у Yarn через RaycastContext
            BlockHitResult hit = level.raycast(new RaycastContext(
                    prev, pos,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    player
            ));
            boolean hitBlock = hit.getType() == HitResult.Type.BLOCK;
            Vec3d drawTo = hitBlock ? hit.getPos() : pos;
            int color = hitBlock ? 0xFFFF3333 : 0xFF33FF55;
            
            // Малюємо лінію з урахуванням зсуву відносно камери
            addLine(matrix, lines, prev.subtract(cameraPos), drawTo.subtract(cameraPos), color);

            if (hitBlock) {
                landing = hit.getPos();
                blocked = true;
                break;
            }
            prev = pos;
            landing = pos;
        }

        drawMarker(matrix, providers, landing.subtract(cameraPos), blocked ? 0xFFFF3333 : 0xFF33FF55);
    }

    private static void addLine(Matrix4f matrix, VertexConsumer consumer, Vec3d a, Vec3d b, int argb) {
        float r = ((argb >> 16) & 0xFF) / 255f;
        float g = ((argb >> 8) & 0xFF) / 255f;
        float bl = (argb & 0xFF) / 255f;
        float al = ((argb >> 24) & 0xFF) / 255f;
        consumer.vertex(matrix, (float) a.x, (float) a.y, (float) a.z).color(r, g, bl, al).normal(0f, 1f, 0f);
        consumer.vertex(matrix, (float) b.x, (float) b.y, (float) b.z).color(r, g, bl, al).normal(0f, 1f, 0f);
    }

    private static void drawMarker(Matrix4f matrix, VertexConsumerProvider.Immediate providers, Vec3d at, int argb) {
        double s = 0.15;
        VertexConsumer box = providers.getBuffer(RenderLayer.getLines());
        Vec3d[] ring = new Vec3d[5];
        for (int i = 0; i < 4; i++) {
            double ang = i * Math.PI * 0.5;
            ring[i] = at.add(Math.cos(ang) * s, 0.05, Math.sin(ang) * s);
        }
        ring[4] = ring[0];
        for (int i = 0; i < 4; i++) {
            addLine(matrix, box, ring[i], ring[i + 1], argb);
        }
        addLine(matrix, box, at.add(0, 0, 0), at.add(0, s * 2, 0), ColorManager.withAlpha(argb, 0.9f));
    }
}
