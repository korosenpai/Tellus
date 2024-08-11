package Shaders;

import Grid.Grid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.math.BigDecimal;
import java.math.RoundingMode;

import Blocks.Particle;

public class ShaderManager {

    private final Grid GRID;
    private final int TILE_DIMENSION;

    // TODO:
    // private final int ROWS;
    // private final int COLS;

    public ShaderManager(Grid grid, int tileDimension) {
        this.GRID = grid;
        this.TILE_DIMENSION = tileDimension;
    }


    // NOTE: same thing as window.drawgrid() to avoid calling millions of functions
    // for every pixel
    public void render(Graphics2D g) {
        for (int j = 0; j < GRID.getViewportRows(); j++){
            for (int i = 0; i < GRID.getViewportColumns(); i++) {
                Particle curr = GRID.getAtPosition(j + GRID.getViewportOffsetY(), i + GRID.getViewportOffsetX());
                // g.setColor(new Color(curr.getColorRed(), curr.getColorGreen(), curr.getColorBlue()));
                g.setColor(shadePixel(
                    curr.getColorRed(),
                    curr.getColorGreen(),
                    curr.getColorBlue(),
                    j,
                    i,
                    GRID.getViewportRows(),
                    GRID.getViewportColumns()
                ));
                g.fillRect(i * TILE_DIMENSION, j * TILE_DIMENSION, TILE_DIMENSION, TILE_DIMENSION);
            }
        }

    }

    // NOTE: for now only include the cave shader
    public Color shadePixel(int r, int g, int b, int y, int x, int height, int width) {
        // // circle in center of screen
        // int radius = 50;
        // if (Math.sqrt(Math.pow(width / 2 - x, 2) + Math.pow(height / 2 - y, 2)) <= radius) return new Color(r, g, b);
        // else return new Color(0, 0, 0);

        // radial circle from center of screen

        // normalize window from 0..width|height to 0..1
        double normalizedX = (double)x / (double)width;
        double normalizedY = (double)y / (double)height;

        //System.out.println(normalizedX + " " + normalizedY);

        double centerX = 0.5;
        double centerY = 0.5;
        double distFromCenter = Math.sqrt(Math.pow(centerX - normalizedX, 2) + Math.pow(centerY - normalizedY, 2)); // 0...0.707 (on the angles)


        //distFromCenter = 0.5 - distFromCenter;

        double maxValue = 0.707;
        distFromCenter /= maxValue; // 0..1
        //distFromCenter = 1 - distFromCenter;


        // TODO: make it work with center radius
        double CENTER_RADIUS = 100; // radius of center where brightness is full
        double EXPONENTIAL_FACTOR = 10; // steepness of decay
        double brightnessAdjustment = Math.exp(-EXPONENTIAL_FACTOR * Math.pow(distFromCenter, 2));
        brightnessAdjustment = Math.max(brightnessAdjustment, 0); // never go below 0

        int scaleFactor = (int) (255 * brightnessAdjustment);

        // Create a new color with the adjusted brightness
        int red = Math.max((r * scaleFactor) >> 8, 0);
        int green = Math.max((g * scaleFactor) >> 8, 0);
        int blue = Math.max((b * scaleFactor) >> 8, 0);

        return new Color(red, green, blue, 255);

    }

    public Color adjustBrightness(int r, int g, int b, float brightnessFactor) {
        if (brightnessFactor < 0 || brightnessFactor > 1)
            throw new RuntimeException("brightness factor must be 0 < x < 1: " + brightnessFactor);

        // Convert the original color to HSB
        float[] hsbValues = new float[3];
        Color.RGBtoHSB(r, g, b, hsbValues);

        // Adjust the brightness
        // The brightness factor should be between 0.0 (black) and 1.0 (full brightness)
        // For example, a factor of 0.5 would halve the brightness
        hsbValues[2] *= brightnessFactor;

        // Convert back to RGB
        return Color.getHSBColor(hsbValues[0], hsbValues[1], hsbValues[2]);
    }

    public Color mixColors(int r1, int g1, int b1, int r2, int g2, int b2) {
        float ratio = 0.5f;
        int r = (int)((r1 + r2) * ratio);
        int g = (int)((g1 + g2) * ratio);
        int b = (int)((b1 + b2) * ratio);
        return new Color(r, g, b, 255);
    }

    public Color mixColors(Color... colors) {
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
}
