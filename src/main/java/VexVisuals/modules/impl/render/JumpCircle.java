package VexVisuals.modules.impl.render;

import VexVisuals.modules.Module;
import VexVisuals.modules.ModuleRegistry;
// import VexVisuals.utils.ColorManager; // Если есть свой ColorManager

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Box;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class JumpCircle extends Module {

    private Setting radiusSetting;
    private Setting colorSetting;
    private Setting chromaSpeedSetting;
    private Setting alphaSetting;
    private Setting onlyWhenJumpingSetting;

    public JumpCircle(String name, String description, ModuleRegistry.Category category) {
        super(name, description, category);
    }

    @Override
    public List<Module.Setting> getSettings() {
        List<Module.Setting> settings = new ArrayList<>();
        settings.add(radiusSetting = new Module.Setting("Radius", 1.0, 0.1, 5.0)); // Радиус в блоках
        settings.add(colorSetting = new Module.Setting("Color", Color.CYAN.getRGB())); // Базовый цвет
        settings.add(chromaSpeedSetting = new Module.Setting("ChromaSpeed", 0.5, 0.1, 2.0)); // Скорость Chroma
        settings.add(alphaSetting = new Module.Setting("Alpha", 100, 0, 255)); // Прозрачность
        settings.add(onlyWhenJumpingSetting = new Module.Setting("OnlyWhenJumping", true)); // Показывать только при прыжке
        return settings;
    }

    @Override
    public void onWorldRender(float partialTicks) {
        if (!this.isEnabled() || mc.player == null || mc.world == null) {
            return;
        }

        PlayerEntity player = mc.player;

        // Проверяем, нужно ли отображать круг
        boolean shouldRender = true;
        if (onlyWhenJumpingSetting.asBoolean()) {
            // Проверяем, находится ли игрок в состоянии прыжка (исходящий прыжок, не падение)
            shouldRender = player.isJumping() && !player.isFallFlying();
        }

        if (!shouldRender) {
            return;
        }

        // Получаем настройки
        float radius = (float) radiusSetting.asDouble();
        int alpha = alphaSetting.asInt();
        Color baseColor = new Color(colorSetting.asInt());
        boolean useChroma = alpha > 0; // Если есть прозрачность, то используем Chroma
        float chromaSpeed = (float) chromaSpeedSetting.asDouble();

        // Получаем текущую позицию игрока с учетом частичного тика
        double playerX = player.prevX + (player.getX() - player.prevX) * partialTicks;
        double playerY = player.prevY + (player.getY() - player.prevY) * partialTicks;
        double playerZ = player.prevZ + (player.getZ() - player.prevZ) * partialTicks;

        // Отрисовываем круг под игроком
        renderCircle(playerX, playerY, playerZ, radius, baseColor, alpha, chromaSpeed, useChroma, partialTicks);
    }

    // Функция отрисовки круга
    private void renderCircle(double x, double y, double z, float radius, Color baseColor, int alpha, float chromaSpeed, boolean useChroma, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        // Применяем текущую матрицу отрисовки
        MatrixStack matrices = new MatrixStack();
        matrices.push(); // Сохраняем состояние матрицы

        // Применяем трансформации для камеры
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F)); // Поворачиваем плоскость круга
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(player.prevYaw + (player.getYaw() - player.prevYaw) * partialTicks)); // Поворот по Y соответствует направлению взгляда игрока

        // Включаем режим отрисовки для непрозрачных объектов (или прозрачных, в зависимости от эффекта)
        RenderLayer renderLayer = RenderLayer.getLightning(); // Или другой подходящий слой для отрисовки поверх мира
        bufferBuilder.begin(VertexFormats.LINES, renderLayer); // Для отрисовки линий (если нужно)

        // Для залитого круга используем VertexFormats.POSITION_COLOR
        bufferBuilder.begin(VertexFormats.POSITION_COLOR, RenderLayer.getTranslucent()); // Используем прозрачный слой

        int numPoints = 360; // Количество точек для отрисовки круга (чем больше, тем плавнее)

        for (int i = 0; i < numPoints; i++) {
            float angleRad = (float) Math.toRadians(i);
            float cos = MathHelper.cos(angleRad);
            float sin = MathHelper.sin(angleRad);

            double circleX = cos * radius;
            double circleZ = sin * radius;

            // Получаем цвет для текущей точки (с Chroma эффектом)
            int currentColor = baseColor.getRGB();
            if (useChroma) {
                currentColor = getChromaColor(baseColor, alpha, chromaSpeed, i, numPoints, partialTicks).getRGB();
            }

            // Добавляем вершину
            bufferBuilder.vertex(matrices.peek().getPositionMatrix(), (float) (x + circleX), (float) (y), (float) (z + circleZ))
                    .color(currentColor)
                    .next();
        }

        tessellator.draw(); // Отрисовываем собранные вершины
        matrices.pop(); // Восстанавливаем состояние матрицы
    }

    // Базовая реализация Chroma цвета
    private Color getChromaColor(Color baseColor, int alpha, float speed, int pointIndex, int totalPoints, float partialTicks) {
        float hue = (System.currentTimeMillis() / (1000f / speed) + (pointIndex * 360f / totalPoints)) % 360;
        Color chroma = Color.getHSBColor(hue / 360f, 1f, 1f);
        return new Color(chroma.getRed(), chroma.getGreen(), chroma.getBlue(), alpha);
    }
}
