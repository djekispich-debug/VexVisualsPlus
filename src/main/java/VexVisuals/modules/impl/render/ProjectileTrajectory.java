package VexVisuals.modules.impl.render;

import VexVisuals.modules.Module;
import VexVisuals.modules.ModuleRegistry;
// import VexVisuals.utils.ColorManager; // Если есть свой ColorManager

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ProjectileItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.ProjectileUtil; // Для предсказания траектории

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ProjectileTrajectory extends Module {

    private Setting colorSetting;
    private Setting chromaSpeedSetting;
    private Setting alphaSetting;
    private Setting maxLengthSetting; // Максимальная длина траектории
    private Setting segmentLengthSetting; // Длина одного отрезка траектории

    public ProjectileTrajectory(String name, String description, ModuleRegistry.Category category) {
        super(name, description, category);
    }

    @Override
    public List<Module.Setting> getSettings() {
        List<Module.Setting> settings = new ArrayList<>();
        settings.add(colorSetting = new Module.Setting("Color", Color.YELLOW.getRGB()));
        settings.add(chromaSpeedSetting = new Module.Setting("ChromaSpeed", 0.5, 0.1, 2.0));
        settings.add(alphaSetting = new Module.Setting("Alpha", 255, 0, 255));
        settings.add(maxLengthSetting = new Module.Setting("MaxLength", 50, 1, 200)); // Максимальное количество отрезков
        settings.add(segmentLengthSetting = new Module.Setting("SegmentLength", 0.1, 0.01, 1.0)); // Длина сегмента
        return settings;
    }

    @Override
    public void onWorldRender(float partialTicks) {
        if (!this.isEnabled() || mc.player == null || mc.world == null) {
            return;
        }

        ItemStack stack = mc.player.getMainHandStack(); // Проверяем основной рукой
        if (stack.isEmpty() || !(stack.getItem() instanceof ProjectileItem)) {
            // Если в основной руке не метательный предмет, пробуем левую руку
            stack = mc.player.getOffHandStack();
            if (stack.isEmpty() || !(stack.getItem() instanceof ProjectileItem)) {
                return; // Нет метательных предметов
            }
        }

        // Предсказываем траекторию
        List<Vec3d> trajectoryPoints = predictTrajectory(mc.player, stack, partialTicks);

        if (trajectoryPoints == null || trajectoryPoints.isEmpty()) {
            return;
        }

        // Получаем настройки
        int alpha = alphaSetting.asInt();
        Color baseColor = new Color(colorSetting.asInt());
        float chromaSpeed = (float) chromaSpeedSetting.asDouble();

        // Отрисовка траектории
        renderTrajectory(trajectoryPoints, baseColor, alpha, chromaSpeed, partialTicks);
    }

    // Метод для предсказания траектории
    private List<Vec3d> predictTrajectory(PlayerEntity player, ItemStack stack, float partialTicks) {
        List<Vec3d> points = new ArrayList<>();
        World world = player.getWorld();

        double velocityX = 0;
        double velocityY = 0;
        double velocityZ = 0;

        // Получаем начальную скорость из предмета (упрощенно)
        // В реальном коде нужно учесть разные типы снарядов и их свойства (сила броска, гравитация)
        // Пример для стандартных снарядов (стрелы, яйца, снежки)
        if (stack.isOf(Items.ARROW) || stack.isOf(Items.TIPPED_ARROW)) {
            // Для стрел используется свойство `arrow_velocity`
            // velocityY = -ProjectileUtil.getArrowVelocity(stack); // Неправильно, это свойство предмета, а не предмета в руке
            // Нужно получить силу броска, которая зависит от времени задержки кнопки (charge)
            // Пока что используем примерную скорость
            double charge = 1.0; // Полная зарядка
            double arrowVelocity = (double) stack.getEnchantments().stream()
                    .filter(e -> e.getKey().getValue() == Items.ARROW.getEnchantments().get(0).getValue()) // Примерно, для получения силы
                    .findFirst().map(e -> (double)e.getLevel()).orElse(1.0); // Если есть Power, то больше

            // Ориентировочная скорость, реальная зависит от charge
            float pitch = player.getPitch();
            float yaw = player.getYaw();

            double horizontalSpeed = Math.cos(Math.toRadians(yaw));
            double verticalSpeed = -Math.sin(Math.toRadians(pitch));
            double motionX = horizontalSpeed * Math.cos(Math.toRadians(pitch)); // Неправильно, pitch и yaw перепутаны
            double motionY = verticalSpeed;
            double motionZ = horizontalSpeed * Math.sin(Math.toRadians(pitch));

            // Примерная коррекция скорости, основанная на игре
            // Необходимо получить реальную скорость броска
            // Это сложная часть, зависит от того, как игра рассчитывает скорость.
            // Для простоты, примем стандартное значение, которое можно настроить.

            // Примерное получение скорости, нужно будет точно настроить
            double baseArrowVelocity = 1.5; // Базовая скорость стрелы
            double currentCharge = 1.0; // Предполагаем полную зарядку

            double speed = baseArrowVelocity * currentCharge; // Скорость броска

            // Направление броска
            float lookPitch = player.getPitch();
            float lookYaw = player.getYaw();

            double dirX = -Math.sin(Math.toRadians(lookYaw)) * Math.cos(Math.toRadians(lookPitch));
            double dirY = -Math.sin(Math.toRadians(lookPitch));
            double dirZ = Math.cos(Math.toRadians(lookYaw)) * Math.cos(Math.toRadians(lookPitch));

            velocityX = dirX * speed;
            velocityY = dirY * speed;
            velocityZ = dirZ * speed;

        } else if (stack.getItem() instanceof ProjectileItem) {
            // Для других ProjectileItem (яйца, снежки)
            // Получаем силу броска (charge)
            // В игре это зависит от времени удержания кнопки.
            // Для предсказания, можно принять максимальную силу.

            double charge = 1.0; // Максимальная зарядка
            // Скорость броска зависит от типа предмета и силы
            // Примерная скорость для яиц/снежков
            double baseItemVelocity = 1.5; // Нужно уточнить для каждого типа

            float pitch = player.getPitch();
            float yaw = player.getYaw();

            double dirX = -Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
            double dirY = -Math.sin(Math.toRadians(pitch));
            double dirZ = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));

            double speed = baseItemVelocity * charge;

            velocityX = dirX * speed;
            velocityY = dirY * speed;
            velocityZ = dirZ * speed;
        } else {
            return null; // Неподдерживаемый предмет
        }

        // Начальная позиция
        double x = player.getX();
        double y = player.getY() + player.getEyeHeight(player.getPose()); // Позиция глаз игрока
        double z = player.getZ();

        double gravity = world.getGravity(); // Гравитация
        double drag = 0.99; // Сопротивление воздуха (упрощенно)

        int maxSegments = maxLengthSetting.asInt();
        double segmentLength = segmentLengthSetting.asDouble();

        for (int i = 0; i < maxSegments; i++) {
            // Добавляем текущую точку
            points.add(new Vec3d(x, y, z));

            // Применяем гравитацию к скорости по Y
            velocityY -= gravity;

            // Применяем сопротивление воздуха
            velocityX *= drag;
            velocityY *= drag;
            velocityZ *= drag;

            // Перемещаем точку на основе скорости
            x += velocityX * segmentLength;
            y += velocityY * segmentLength;
            z += velocityZ * segmentLength;

            // Проверяем столкновение с блоками или землей
            Box projectileBox = new Box(x, y, z, x + 1, y + 1, z + 1); // Упрощенная коробка для проверки
            if (world.getBlockState(BlockPos.ofFloored(x, y, z)).isFullOpaque()) { // Проверка на столкновение с непрозрачным блоком
                break; // Столкнулись с блоком
            }
            if (y < world.getBottomY()) { // Проверка на падение ниже мира
                break;
            }

            // Если точка вышла за пределы мира (очень далеко)
            if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
                break;
            }
        }

        return points;
    }


    // Функция отрисовки траектории
    private void renderTrajectory(List<Vec3d> points, Color baseColor, int alpha, float chromaSpeed, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        // Используем VertexFormats.POSITION_COLOR для отрисовки линий с цветом
        bufferBuilder.begin(VertexFormats.LINES, RenderLayer.getTranslucent()); // Прозрачный слой

        int pointIndex = 0;
        for (Vec3d point : points) {
            int currentColor = baseColor.getRGB();
            // Применяем Chroma эффект, цвет меняется в зависимости от положения в траектории
            Color chroma = getChromaColor(baseColor, alpha, chromaSpeed, pointIndex, points.size(), partialTicks);
            currentColor = chroma.getRGB();

            bufferBuilder.vertex(new MatrixStack().peek().getPositionMatrix(), (float) point.x, (float) point.y, (float) point.z)
                    .color(currentColor)
                    .next();
            pointIndex++;
        }

        tessellator.draw();
    }

    // Базовая реализация Chroma цвета
    private Color getChromaColor(Color baseColor, int alpha, float speed, int pointIndex, int totalPoints, float partialTicks) {
        float hue = (System.currentTimeMillis() / (1000f / speed) + (pointIndex * 360f / totalPoints)) % 360;
        Color chroma = Color.getHSBColor(hue / 360f, 1f, 1f);
        return new Color(chroma.getRed(), chroma.getGreen(), chroma.getBlue(), alpha);
    }
}
