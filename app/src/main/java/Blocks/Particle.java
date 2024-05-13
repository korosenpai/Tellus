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

    public float spawnRate = 1f;

    public int[] previousPosition = new int[] {-1, -1}; // used to calculate isFreeFalling by seeing if moved last frame
    public boolean isFreeFalling;

    // isFreeFalling for gases, if they can still go up
    public boolean isRising;


    public int scanDirection = 1; // NOTE: 1 bottom to top, 2 top to bottom

    // set top true when moved, set to false after rendering by the Window, to avoid calling on it update() more than once a frame
    public boolean hasMoved;
    // TODO: check when setting has moved if needing to change even particle that was moved

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

    public int[] update(int[] coords, Grid grid) {
        hasMoved = true;
        return coords;
    }

}
