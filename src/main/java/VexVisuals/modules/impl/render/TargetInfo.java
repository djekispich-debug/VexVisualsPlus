package VexVisuals.modules.impl.render;

import VexVisuals.modules.Module;
import VexVisuals.modules.ModuleRegistry;
// import VexVisuals.utils.ColorManager; // Если у вас есть свой ColorManager, раскомментируйте и используйте его

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper; // Для работы с цветами

import java.awt.Color; // Используем стандартный Color
import java.util.ArrayList;
import java.util.List;

// Предполагается, что у вас есть класс Setting, как в Module.java
// import VexVisuals.modules.Module.Setting;

public class TargetInfo extends Module {

    // Опции для отображения
    private Setting showHealthSetting;
    private Setting showPingSetting;
    private Setting showArmorSetting;
    private Setting showItemsSetting;
    private Setting chromaSpeedSetting;
    private Setting positionSetting; // Пример настройки положения

    // Координаты для отрисовки HUD
    private float x = 100; // Исходное положение, будет переопределяться
    private float y = 100;

    // Для перемещения HUD
    private boolean isDragging = false;
    private float dragX, dragY;

    public TargetInfo(String name, String description, ModuleRegistry.Category category) {
        super(name, description, category);
    }

    @Override
    public List<Module.Setting> getSettings() {
        List<Module.Setting> settings = new ArrayList<>();
        settings.add(showHealthSetting = new Module.Setting("ShowHealth", true));
        settings.add(showPingSetting = new Module.Setting("ShowPing", true));
        settings.add(showArmorSetting = new Module.Setting("ShowArmor", true));
        settings.add(showItemsSetting = new Module.Setting("ShowItems", true));
        settings.add(chromaSpeedSetting = new Module.Setting("ChromaSpeed", 0.5, 0.1, 2.0)); // Ползунок скорости Chroma
        settings.add(positionSetting = new Module.Setting("Position", "TopLeft", new String[]{"TopLeft", "TopRight", "BottomLeft", "BottomRight", "Center"})); // Режим положения
        return settings;
    }

    @Override
    public void onHudRender() {
        if (mc.crosshairTarget == null || !(mc.crosshairTarget.getEntity() instanceof PlayerEntity) || mc.player == null) {
            return;
        }

        PlayerEntity target = (PlayerEntity) mc.crosshairTarget.getEntity();
        ClientPlayerEntity player = mc.player;

        // Проверка, что игрок не смотрит на себя или невидимую сущность
        if (target == player || target.getDisplayName().getString().equals("Invisible")) {
            return;
        }

        // Определение положения HUD
        updatePosition();

        // Отрисовка панели
        drawTargetInfoPanel(target, player, new DrawContext(mc, new MatrixStack(), 0, 0, 0, 0, 0)); // Используем временный DrawContext
    }

    private void updatePosition() {
        // Получаем настройки положения
        String positionMode = positionSetting.asMode();
        int screenWidth = mc.getWindow().getScaledWidth();
        int screenHeight = mc.getWindow().getScaledHeight();

        int panelWidth = getPanelWidth(target); // Ширина панели зависит от контента
        int panelHeight = getPanelHeight(target); // Высота панели зависит от контента

        switch (positionMode) {
            case "TopLeft":
                x = 5; y = 5;
                break;
            case "TopRight":
                x = screenWidth - panelWidth - 5; y = 5;
                break;
            case "BottomLeft":
                x = 5; y = screenHeight - panelHeight - 5;
                break;
            case "BottomRight":
                x = screenWidth - panelWidth - 5; y = screenHeight - panelHeight - 5;
                break;
            case "Center":
                x = (screenWidth - panelWidth) / 2.0f;
                y = (screenHeight - panelHeight) / 2.0f;
                break;
        }
    }

    private int getPanelWidth(PlayerEntity target) {
        // Примерная ширина, можно рассчитать точнее по содержимому
        int baseWidth = 150;
        if (showItemsSetting.asBoolean()) baseWidth += 40; // Добавляем место для предметов
        return baseWidth;
    }

    private int getPanelHeight(PlayerEntity target) {
        // Примерная высота, можно рассчитать точнее по содержимому
        int height = 60;
        if (showArmorSetting.asBoolean()) height += 20; // Добавляем место для брони
        if (showItemsSetting.asBoolean()) height += 20; // Добавляем место для предметов
        return height;
    }

    private void drawTargetInfoPanel(PlayerEntity target, ClientPlayerEntity player, DrawContext context) {
        int panelWidth = getPanelWidth(target);
        int panelHeight = getPanelHeight(target);

        // Рисуем фон панели (с Chroma эффектом)
        Color chromaColor = getChromaColor((float) chromaSpeedSetting.asDouble());
        Color backgroundColor = new Color(30, 30, 30, 200); // Темный фон с прозрачностью

        // Рисуем фон с Chroma цветом
        //fill(context, x, y, x + panelWidth, y + panelHeight, chromaColor.getRGB()); // Полная заливка Chroma
        //fill(context, x + 2, y + 2, x + panelWidth - 2, y + panelHeight - 2, backgroundColor.getRGB()); // Внутренняя заливка
        drawRectWithOutline(context, x, y, x + panelWidth, y + panelHeight, backgroundColor.getRGB(), chromaColor.getRGB(), 1); // Фон с Chroma обводкой

        // Начинаем отрисовку текста и других элементов
        float currentY = y + 5; // Отступ сверху

        // 1. Имя игрока
        context.drawTextWithShadow(mc.textRenderer, Text.literal(target.getDisplayName().getString()), x + 5, currentY, Color.WHITE.getRGB());
        currentY += 10; // Отступ для следующей строки

        // 2. Здоровье
        if (showHealthSetting.asBoolean()) {
            float health = target.getHealth();
            float maxHealth = target.getMaxHealth();
            String healthStr = String.format("%.1f / %.1f", health, maxHealth);
            int healthColor = getHealthColor(health, maxHealth);
            context.drawTextWithShadow(mc.textRenderer, Text.literal("Health: " + healthStr), x + 5, currentY, healthColor);
            currentY += 10;
        }

        // 3. Пинг
        if (showPingSetting.asBoolean()) {
            int ping = getPlayerPing(target);
            int pingColor = getPingColor(ping);
            context.drawTextWithShadow(mc.textRenderer, Text.literal("Ping: " + ping + "ms"), x + 5, currentY, pingColor);
            currentY += 10;
        }

        // 4. Броня
        if (showArmorSetting.asBoolean()) {
            int armorValue = target.getArmor();
            String armorStr = armorValue + " HP";
            int armorColor = getArmorColor(armorValue); // Можно сделать настраиваемый цвет по значению
            context.drawTextWithShadow(mc.textRenderer, Text.literal("Armor: " + armorStr), x + 5, currentY, armorColor);
            currentY += 10;
        }

        // 5. Основные предметы в инвентаре (например, руки и основной слот)
        if (showItemsSetting.asBoolean()) {
            float currentX = x + 5;
            float itemY = currentY; // Координата Y для предметов

            // Основной слот (main hand)
            ItemStack mainHandItem = target.getMainHandStack();
            if (!mainHandItem.isEmpty()) {
                drawItemWithTooltip(context, mainHandItem, currentX, itemY, "Main Hand");
                currentX += 20; // Отступ для следующего предмета
            }

            // Второй слот (off hand)
            ItemStack offHandItem = target.getOffHandStack();
            if (!offHandItem.isEmpty()) {
                drawItemWithTooltip(context, offHandItem, currentX, itemY, "Off Hand");
                currentX += 20;
            }

            // Можно добавить отрисовку других слотов, если нужно
            // Например, основной слот в инвентаре
            // ItemStack mainSlotItem = target.getInventory().getStack(target.getInventory().selectedSlot);
            // if (!mainSlotItem.isEmpty()) {
            //     drawItemWithTooltip(context, mainSlotItem, currentX, itemY, "Selected Slot");
            //     currentX += 20;
            // }
        }
    }

    // Вспомогательный метод для отрисовки предмета и его подсказки
    private void drawItemWithTooltip(DrawContext context, ItemStack stack, float x, float y, String tooltip) {
        if (stack.isEmpty()) return;

        // Рисуем иконку предмета
        // MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(stack, (int) x, (int) y); // Старый метод
        // Используем DrawContext для рендеринга
        context.drawItemWithoutEnchantmentGlint(stack, (int) x, (int) y);
        context.drawItemInOverlay(mc.textRenderer, stack, (int) x, (int) y, tooltip); // Отображает счетчик и глиттер

        // Если нужно кастомное отображение подсказки
        // mc.textRenderer.drawWithShadow(context.getMatrices(), Text.literal(tooltip), x, y - 10, Color.WHITE.getRGB());
    }


    // Получение пинга игрока
    private int getPlayerPing(PlayerEntity target) {
        if (mc.player == null) return 0;
        PlayerListEntry playerEntry = mc.getNetworkHandler().getPlayerListEntry(target.getUuid());
        return (playerEntry == null) ? 0 : playerEntry.getLatency();
    }

    // Получение цвета для здоровья
    private int getHealthColor(float health, float maxHealth) {
        float healthPercentage = health / maxHealth;
        if (healthPercentage <= 0.25) return Color.RED.getRGB();
        if (healthPercentage <= 0.5) return Color.ORANGE.getRGB();
        if (healthPercentage <= 0.75) return Color.YELLOW.getRGB();
        return Color.GREEN.getRGB();
    }

    // Получение цвета для пинга
    private int getPingColor(int ping) {
        if (ping >= 150) return Color.RED.getRGB();
        if (ping >= 100) return Color.ORANGE.getRGB();
        if (ping >= 50) return Color.YELLOW.getRGB();
        return Color.GREEN.getRGB();
    }

    // Получение цвета для брони
    private int getArmorColor(int armorValue) {
        if (armorValue <= 5) return Color.RED.getRGB();
        if (armorValue <= 10) return Color.ORANGE.getRGB();
        if (armorValue <= 15) return Color.YELLOW.getRGB();
        return Color.GREEN.getRGB();
    }

    // Базовая реализация Chroma цвета, если ColorManager не используется
    private Color getChromaColor(float speed) {
        float hue = (System.currentTimeMillis() / (1000f / speed)) % 360;
        return Color.getHSBColor(hue / 360f, 1f, 1f);
    }

    // Вспомогательный метод для отрисовки прямоугольника с обводкой
    private void drawRectWithOutline(DrawContext context, float x, float y, float width, float height, int bgColor, int outlineColor, float outlineWidth) {
        // Рисуем фон
        fill(context, x, y, width, height, bgColor);
        // Рисуем обводку
        // Верхняя линия
        fill(context, x, y, width, y + outlineWidth, outlineColor);
        // Нижняя линия
        fill(context, x, height - outlineWidth, width, height, outlineColor);
        // Левая линия
        fill(context, x, y, x + outlineWidth, height, outlineColor);
        // Правая линия
        fill(context, width - outlineWidth, y, width, height, outlineColor);
    }

    // Вспомогательный метод для заливки прямоугольника (аналог fillRect в AWT)
    private void fill(DrawContext context, float x, float y, float width, float height, int color) {
        if (color == -1) return; // Не отрисовывать, если цвет -1
        context.fill((int) x, (int) y, (int) width, (int) height, color);
    }
}
