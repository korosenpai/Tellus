package Shaders;

import java.awt.Color;

import org.checkerframework.checker.units.qual.min;


public class Shader {
    private int height;
    private int width;

    private int r,g,b;
    private double y,x;

    public Shader(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public void set(int r, int g, int b, double y, double x) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.y = y;
        this.x = x;
    }

    public Color clear() {
        return new Color(r, g, b);
    }
    public Color white() {
        return new Color(255, 255, 255);
    }

    public Color centerCircle() {
        // // circle in center of screen
        int radius = 50;
        if (Math.sqrt(Math.pow(width / 2 - x, 2) + Math.pow(height / 2 - y, 2)) <= radius) return new Color(r, g, b);
        else return new Color(0, 0, 0);
    }

    public Color cave() {

        // radial circle from center of screen

        // normalize window from 0..width|height to 0..1
        double normalizedX = x / width;
        double normalizedY = y / height;

        //System.out.println(normalizedX + " " + normalizedY);

        double centerX = 0.5;
        double centerY = 0.5;
        double distFromCenter = Math.sqrt(Math.pow(centerX - normalizedX, 2) + Math.pow(centerY - normalizedY, 2)); // 0...0.707 (on the angles)


        //distFromCenter = 0.5 - distFromCenter;

        // NOTE: in normalized space 0..1 use pythagoras theorem
        // on corner to get that number
        double maxValue = 0.707;
        distFromCenter /= maxValue; // 0..1
        //distFromCenter = 1 - distFromCenter;


        // exponential decay function
        // TODO: make it work with center radius
        // double CENTER_RADIUS = 100; // radius of center where brightness is full
        double EXPONENTIAL_FACTOR = 10; // steepness of decay
        double brightnessAdjustment = Math.exp(-EXPONENTIAL_FACTOR * Math.pow(distFromCenter, 2));
        brightnessAdjustment = Math.max(brightnessAdjustment, 0); // never go below 0

        int scaleFactor = (int) (255 * brightnessAdjustment);

        // Create a new color with the adjusted brightness
        return ShaderUtils.adjustBrightness(r, g, b, scaleFactor);

    }

    // NOTE: can graph only functions
    private double zoom = 2; // if function is too small or too big just zoom
    public Color geogebra() {

        // range: -height/2 ... + height/2; same with width
        // y is flipped in normal direction
        double centeredX = x - this.width / 2;
        double centeredY = this.height / 2 - y;

        // axis
        if (centeredX == 0 || centeredY == 0) {
            return new Color(0, 255, 0);
            // return ShaderUtils.mixColors(r, g, b, 0, 255, 0);
        }

        // expression; range: -inf ... inf 
        double f;
        // f = Math.pow(centeredX, 2);
        // f = Math.log(centeredX) * 10; // resolution is too low to render negative part
        // f = Math.sin(centeredX / 10) * 5;
        // f = centeredX;
        // f = Math.sqrt(centeredX);
        f = Math.log(centeredX) * 10 + Math.sin(centeredX / 10) * 5;
        // f = Math.abs(Math.sin(centeredX / 10) * 5);

        double distFromPoint = Math.abs(f - centeredY); // -inf...inf
        if (distFromPoint >= height)
            return new Color(0,0,0);
            // return ShaderUtils.mixColors(r, g, b, 0, 0, 0);
        // range: -height/2 ... height/2

        // the height was too big, with this it enables to see smaller graphs
        // also dont need it for width
        // so we divide it by the zoom
        distFromPoint /= height / zoom; // 0..1

        // to get sharper image
        distFromPoint = ShaderUtils.exponentialDecay(distFromPoint, 300);

        return new Color((int)(distFromPoint * 255), 0, 0); // only graph
        // return ShaderUtils.mixColors(r, g, b, (int)(distFromPoint * 255), 0, 0);

    }

    public class Vector2d {
        public double x;
        public double y;

        public Vector2d(double x, double y) {
            set(x, y);
        }
        public void set(double x, double y) {
            this.x = x;
            this.y = y;
        }

    }
    public Vector2d squareImaginary() {
        return new Vector2d(
            Math.pow(x, 2) - Math.pow(y, 2),
            2 * x * y
        );
    }
    private final int range = 2;
    private final int MAX_ITERATIONS = 100;
    private final int MAND_ZOOM = 1;
    public Color mandelbrotSet() {

        // range -2..2
        // if width > height on the x there will be more space,
        // inverted if height > width
        // (will be our C)
        double normalizedX = x / Math.min(width, height) * range * 2 - range;
        double normalizedY = y / Math.min(width, height) * range * 2 - range;


        double zX = normalizedX;
        double zY = normalizedY;

        double lenX = 0;
        double maxIter = 0; // max iteration reached
        for (maxIter = 0; maxIter < MAX_ITERATIONS; maxIter++) {
            zX = Math.pow(zX, 2) - Math.pow(zY, 2) + normalizedX;
            zY = 2 * zX * zY + normalizedY;

            lenX = Math.sqrt(Math.pow(zX, 2) + Math.pow(zY, 2));

            if (lenX > range)
                break;
        }

        if (maxIter == MAX_ITERATIONS) {
            return new Color(0, 0, 0);
        }
        else {
            double val = maxIter / MAX_ITERATIONS;
            return Color.getHSBColor((float)val, 1f, 1f);
            // return new Color((int)(val * 255), (int)(val * 255), (int)(val * 255)); // grayscale

        }


    }

// public class MandelbrotZoomer {
//     private static final int MAX_ITERATIONS = 1000;
//     private static final double ESCAPE_RADIUS = 2.0;
//     private static final double ESCAPE_RADIUS_SQUARED = ESCAPE_RADIUS * ESCAPE_RADIUS;
// 
//     public Color calculateMandelbrot(double centerX, double centerY, double zoomLevel, double x, double y) {
//         double real = (x / zoomLevel) - centerX;
//         double imaginary = (y / zoomLevel) - centerY;
// 
//         double realSquared = real * real;
//         double imaginarySquared = imaginary * imaginary;
// 
//         int iterations = 0;
//         while (iterations < MAX_ITERATIONS && realSquared + imaginarySquared <= ESCAPE_RADIUS_SQUARED) {
//             real = realSquared - imaginarySquared + real;
//             imaginary = 2 * real * imaginary + imaginary;
//             iterations++;
//         }
// 
//         if (iterations == MAX_ITERATIONS) {
//             return Color.BLACK; // Outside the set
//         } else {
//             float hue = ((float) iterations / MAX_ITERATIONS) % 1.0f;
//             return Color.getHSBColor(hue, 1.0f, 1.0f); // Inside the set
//         }
//     }
// }

}
