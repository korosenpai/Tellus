package Blocks;

import Window.Grid;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Particle {
    
    // to be abstracted by the solids
    // they are readonly
    private int colorRed;
    private int colorGreen;
    private int colorBlue;

    public void setColors(int r, int g, int b) {
        this.colorRed = parseColor(r);
        this.colorGreen = parseColor(g);
        this.colorBlue = parseColor(b);
    }
    public int parseColor(int c) {
        // check if it is in bounds and in case return min or max values
        return Math.max(0, Math.min(c, 255));
    }

    public int getColorRed() {
        return colorRed;
    }

    public int getColorGreen() {
        return colorGreen;
    }

    public int getColorBlue() {
        return colorBlue;
    }

    // give random offset to add more texture to color
    public int getColorOffset() {
         //OFFSET IS += 100 quindi fare -50 da original rgb value
        return new Random().nextInt(100);
    }

    public abstract int[] update(int[] coords, Grid grid);

}
