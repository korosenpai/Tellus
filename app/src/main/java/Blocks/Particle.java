package Blocks;

import Window.Grid;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Particle {
    
    private int ID;

    // to be abstracted by the solids
    // they are readonly
    private int colorRed;
    private int colorGreen;
    private int colorBlue;

    public Particle() {
        this.ID = ThreadLocalRandom.current().nextInt(10);
    }
    public Particle(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int id) {
        this.ID = id;
    }

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

    public abstract void update(int j, int i, Grid grid);

}
