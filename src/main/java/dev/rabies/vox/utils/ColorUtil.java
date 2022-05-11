package dev.rabies.vox.utils;

import java.awt.*;

public class ColorUtil {

    public static Color getRainbowColor(double offset, double p) {
        double rainbow = Math.ceil((System.currentTimeMillis() + (offset * 2) * 2) / 5);
        rainbow %= p;
        return Color.getHSBColor((float) (rainbow / p), 0.78f, 0.88f);
    }

    public static Color getSuperCoolColor(double offset, double p) {
        float[] fractions = {0.0F, 0.3F, 0.7F, 1.0F};
        Color[] colors = {
                new Color(55, 33, 255),
                new Color(255, 70, 90),
                new Color(50, 150, 255),
                new Color(55, 33, 255)
        };
        double progress = Math.ceil((System.currentTimeMillis() + (offset * 2) * 2) / 5);
        progress %= p;
        return DrawUtils.blendColors(fractions, colors, (float) (progress / p)).brighter();
    }
}
