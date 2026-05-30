package VexVisuals.modules.impl.render;

import VexVisuals.modules.Module;
import VexVisuals.modules.ModuleRegistry;
import VexVisuals.modules.ModuleManager; // Нужен для получения списка активных модулей
// import VexVisuals.utils.ColorManager; // Если есть свой ColorManager

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class HudArrayList extends Module {

    private Setting chromaSpeedSetting;
    private Setting alphaSetting;
    private Setting colorModeSetting; // Режим цвета (Chroma, Static)
    private Setting staticColorSetting; // Статический цвет, если Chroma не выбран

    public HudArrayList(String name, String description, ModuleRegistry.Category category) {
        super(name, description, category);
    }

    @Override
    public List<Module.Setting> getSettings() {
        List<Module.Setting> settings = new ArrayList<>();
        settings.add(chromaSpeedSetting = new Module.Setting("ChromaSpeed", 0.5, 0.1, 2.0));
        settings.add(alphaSetting = new Module.Setting("Alpha", 255, 0, 255));
        settings.add(colorModeSetting = new Module.Setting("ColorMode", "Chroma", new String[]{"Chroma", "Static"}));
        settings.add(staticColorSetting = new Module.Setting("StaticColor", Color.WHITE.getRGB())); // Белый по умолчанию
        return settings;
    }

    @Override
    public void onHudRender() {
        if (mc.player == null || mc.world == null) {
            return;
        }

        List<Module> enabledModules = ModuleManager.getInstance().getEnabledModules();
        if (enabledModules.isEmpty()) {
            return;
        }

        // Получаем настройки
        int alpha = alphaSetting.asInt();
        String colorMode = colorModeSetting.asMode();
        Color staticColor = new Color(staticColorSetting.asInt());
        float chromaSpeed = (float) chromaSpeedSetting.asDouble();

        // Получаем размер экрана
        int screenWidth = mc.getWindow().getScaledWidth();
        int screenHeight = mc.getWindow().getScaledHeight();

        // Позиция ArrayList (обычно в правом верхнем углу)
        float x = screenWidth - 10; // Отступ справа
        float y = 10;              // Отступ сверху

        // Максимальная ширина текста для определения ширины панели
        int maxWidth = 0;
        for (Module module : enabledModules) {
            int moduleNameWidth = mc.textRenderer.getWidth(module.getName());
            if (moduleNameWidth > maxWidth) {
                maxWidth = moduleNameWidth;
            }
        }

        // Добавляем небольшой отступ по ширине
        float panelWidth = maxWidth + 10;
        float panelHeight = enabledModules.size() * 10.5f; // Примерная высота (10px текст + 0.5px отступ)

        // Рисуем фон ArrayList
        // Для простоты, рисуем темный фон с обводкой
        // fill(context, x - panelWidth, y, x, y + panelHeight, new Color(0, 0, 0, 150).getRGB());
        // drawRectWithOutline(context, x - panelWidth, y, x, y + panelHeight, new Color(0, 0, 0, 150).getRGB(), Color.DARK_GRAY.getRGB(), 1);

        // Рисуем каждый активный модуль
        float currentY = y;
        for (Module module : enabledModules) {
            String moduleName = module.getName();
            int moduleNameWidth = mc.textRenderer.getWidth(moduleName);

            // Определяем цвет для текущего модуля
            int moduleColor = getModuleColor(module, alpha, colorMode, staticColor, chromaSpeed);

            // Рисуем название модуля (выравниваем по правому краю)
            // mc.textRenderer.drawWithShadow(context.getMatrices(), Text.literal(moduleName), x - moduleNameWidth - 5, currentY, moduleColor);
            context.drawTextWithShadow(mc.textRenderer, net.minecraft.text.Text.literal(moduleName), x - moduleNameWidth - 5, currentY, moduleColor);


            currentY += 10.5f; // Переходим к следующей строке
        }
    }

    // Получение цвета для модуля (Chroma или Static)
    private int getModuleColor(Module module, int alpha, String colorMode, Color staticColor, float chromaSpeed) {
        switch (colorMode) {
            case "Chroma":
                // Chroma эффект, цвет зависит от позиции в списке (чтобы модули были разного цвета)
                // Используем хеш названия модуля для более стабильного цвета
                int moduleHash = module.getName().hashCode();
                float hue = (System.currentTimeMillis() / (1000f / chromaSpeed) + (moduleHash % 360)) % 360;
                Color chroma = Color.getHSBColor(hue / 360f, 1f, 1f);
                return new Color(chroma.getRed(), chroma.getGreen(), chroma.getBlue(), alpha).getRGB();
            case "Static":
            default:
                return new Color(staticColor.getRed(), staticColor.getGreen(), staticColor.getBlue(), alpha).getRGB();
        }
    }

    // Вспомогательный метод для отрисовки прямоугольника с обводкой
    private void drawRectWithOutline(DrawContext context, float x, float y, float width, float height, int bgColor, int outlineColor, float outlineWidth) {
        // Рисуем фон
        fill(context, x, y, width, height, bgColor);
        // Рисуем обводку
        fill(context, x, y, width, y + outlineWidth, outlineColor); // Верхняя
        fill(context, x, height - outlineWidth, width, height, outlineColor); // Нижняя
        fill(context, x, y, x + outlineWidth, height, outlineColor); // Левая
        fill(context, width - outlineWidth, y, width, height, outlineColor); // Правая
    }

    // Вспомогательный метод для заливки прямоугольника
    private void fill(DrawContext context, float x, float y, float width, float height, int color) {
        if (color == -1) return;
        context.fill((int) x, (int) y, (int) width, (int) height, color);
    }
}
