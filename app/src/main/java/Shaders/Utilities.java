package Shaders;

import java.awt.Color;

public class Utilities {

    public static Color adjustBrightness(int r, int g, int b, int brightnessFactor) {
        // this uses float berightness factor 0 <= x <= 1
        // if (brightnessFactor < 0 || brightnessFactor > 1)
        //     throw new RuntimeException("brightness factor must be 0 < x < 1: " + brightnessFactor);

        // // Convert the original color to HSB
        // float[] hsbValues = new float[3];
        // Color.RGBtoHSB(r, g, b, hsbValues);

        // // Adjust the brightness
        // // The brightness factor should be between 0.0 (black) and 1.0 (full brightness)
        // // For example, a factor of 0.5 would halve the brightness
        // hsbValues[2] *= brightnessFactor;

        // // Convert back to RGB
        // return Color.getHSBColor(hsbValues[0], hsbValues[1], hsbValues[2]);
        if (brightnessFactor > 255) throw new RuntimeException("brightness factor is > 255: " + brightnessFactor);
        return new Color(
            Math.max((r * brightnessFactor) >> 8, 0),
            Math.max((g * brightnessFactor) >> 8, 0),
            Math.max((b * brightnessFactor) >> 8, 0),
            255
        );
    }

    public static Color mixColors(int r1, int g1, int b1, int r2, int g2, int b2) {
        float ratio = 0.5f;
        int r = (int)((r1 + r2) * ratio);
        int g = (int)((g1 + g2) * ratio);
        int b = (int)((b1 + b2) * ratio);
        return new Color(r, g, b, 255);
    }

    public static Color mixColors(Color... colors) {
        if (colors == null || colors.length <= 0) {
            throw new RuntimeException("no arguments provided");
        }

        float ratio = 1f / ((float) colors.length);
        int r = 0, g = 0, b = 0, a = 0;
        for (Color color : colors) {
            r += color.getRed() * ratio;
            g += color.getGreen() * ratio;
            b += color.getBlue() * ratio;
            a += color.getAlpha() * ratio;
        }
        return new Color(r, g, b, a);
    }

    public static double exponentialDecay(double value, double exponentialFactor) {
        double res = Math.exp(-exponentialFactor * Math.pow(value, 2));
        return Math.max(res, 0); // never go below 0
    }
}
