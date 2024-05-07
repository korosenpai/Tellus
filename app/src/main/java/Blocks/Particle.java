package Blocks;

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

    public void update(int i, int j) {
        // i, j -> coords of particle (known by the position in the double for loop)
    }
}
