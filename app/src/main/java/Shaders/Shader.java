package Shaders;

import java.awt.Color;

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
        return Utilities.adjustBrightness(r, g, b, scaleFactor);

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
        // range: -height/2 ... height/2

        // the height was too big, with this it enables to see smaller graphs
        // also dont need it for width
        // so we divide it by the zoom
        distFromPoint /= height / zoom; // 0..1

        // to get sharper image
        distFromPoint = Utilities.exponentialDecay(distFromPoint, 300);

        return new Color((int)(distFromPoint * 255), 0, 0);



    }

    public Color mandelbrotSet(int r, int g, int b, int y, int x, int height, int width) {
        return new Color(0, 0, 0);
    }
}
