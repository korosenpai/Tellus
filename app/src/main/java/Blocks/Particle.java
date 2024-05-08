package Blocks;

import Window.Grid;

import java.util.concurrent.ThreadLocalRandom;

public class Particle {
    
    private int ID;

    public Particle() {
        this.ID = ThreadLocalRandom.current().nextInt(10);
    }
    public Particle(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return this.ID;
    }

    public void update(int j, int i, Grid grid) {
        Particle bottomTile = grid.getLowerNeighbors(j, i)[1];
        System.out.println(bottomTile);
    } 

}
