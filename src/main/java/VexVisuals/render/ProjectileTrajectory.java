package VexVisuals.render;

import VexVisuals.util.ColorManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class ProjectileTrajectory {
    private static boolean active;
    private static final int MAX_STEPS = 80;
    private static final double GRAVITY = 0.03;
    private static final double DRAG = 0.99;

    private ProjectileTrajectory() {
    }

    public static void setActive(boolean value) {
        active = value;
    }

    public static boolean isActive() {
        return active;
    }

    public static void render(PoseStack poseStack, MultiBufferSource.BufferSource buffers, float partialTicks) {
        if (!active) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Level level = mc.level;
        if (player == null || level == null) {
            return;
        }
        if (!player.getMainHandItem().is(Items.ENDER_PEARL) && !player.getMainHandItem().is(Items.SPLASH_POTION)
                && !player.getMainHandItem().is(Items.LINGERING_POTION)) {
            return;
        }

        Vec3 eye = player.getEyePosition(partialTicks);
        Vec3 look = player.getViewVector(partialTicks);
        double speed = player.getMainHandItem().is(Items.ENDER_PEARL) ? 1.5 : 0.5;
        Vec3 motion = look.scale(speed);
        Vec3 pos = eye;

        VertexConsumer lines = buffers.getBuffer(RenderType.debugLineStrip(2.0));
        Matrix4f matrix = poseStack.last().pose();
        Vec3 prev = pos;
        Vec3 landing = pos;
        boolean blocked = false;

        for (int i = 0; i < MAX_STEPS; i++) {
            motion = motion.add(0, -GRAVITY, 0).scale(DRAG);
            pos = pos.add(motion);

            BlockHitResult hit = level.clip(new ClipContext(
                    prev, pos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    player
            ));
            boolean hitBlock = hit.getType() == HitResult.Type.BLOCK;
            Vec3 drawTo = hitBlock ? hit.getLocation() : pos;
            int color = hitBlock ? 0xFFFF3333 : 0xFF33FF55;
            addLine(matrix, lines, prev, drawTo, color, poseStack);

            if (hitBlock) {
                landing = hit.getLocation();
                blocked = true;
                break;
            }
            prev = pos;
            landing = pos;
        }

        drawMarker(matrix, buffers, landing, blocked ? 0xFFFF3333 : 0xFF33FF55, poseStack);
    }

    private static void addLine(Matrix4f matrix, VertexConsumer consumer, Vec3 a, Vec3 b, int argb, PoseStack pose) {
        float r = ((argb >> 16) & 0xFF) / 255f;
        float g = ((argb >> 8) & 0xFF) / 255f;
        float bl = (argb & 0xFF) / 255f;
        float al = ((argb >> 24) & 0xFF) / 255f;
        consumer.addVertex(matrix, (float) a.x, (float) a.y, (float) a.z).setColor(r, g, bl, al).setNormal(pose, 0f, 1f, 0f);
        consumer.addVertex(matrix, (float) b.x, (float) b.y, (float) b.z).setColor(r, g, bl, al).setNormal(pose, 0f, 1f, 0f);
    }

    private static void drawMarker(Matrix4f matrix, MultiBufferSource.BufferSource buffers, Vec3 at, int argb, PoseStack pose) {
        float r = ((argb >> 16) & 0xFF) / 255f;
        float g = ((argb >> 8) & 0xFF) / 255f;
        float b = (argb & 0xFF) / 255f;
        float a = ((argb >> 24) & 0xFF) / 255f;
        double s = 0.15;
        VertexConsumer box = buffers.getBuffer(RenderType.debugLineStrip(2.5));
        Vec3[] ring = new Vec3[5];
        for (int i = 0; i < 4; i++) {
            double ang = i * Math.PI * 0.5;
            ring[i] = at.add(Math.cos(ang) * s, 0.05, Math.sin(ang) * s);
        }
        ring[4] = ring[0];
        for (int i = 0; i < 4; i++) {
            addLine(matrix, box, ring[i], ring[i + 1], argb, pose);
        }
        addLine(matrix, box, at.add(0, 0, 0), at.add(0, s * 2, 0), ColorManager.withAlpha(argb, 0.9f), pose);
    }
}
