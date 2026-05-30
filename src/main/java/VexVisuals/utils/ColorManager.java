package VexVisuals.utils;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import java.awt.Color;

public class ColorManager {

    // Пример статического цвета
    public static Color staticColor = Color.WHITE;

    // Метод для получения цвета градиента между двумя цветами
    public static Color getGradientColor(Color startColor, Color endColor, float progress) {
        int r = (int) (startColor.getRed() + (endColor.getRed() - startColor.getRed()) * progress);
        int g = (int) (startColor.getGreen() + (endColor.get
