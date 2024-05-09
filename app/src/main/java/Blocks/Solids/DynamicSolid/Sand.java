package Blocks.Solids.DynamicSolid;

import java.util.Random;

import Window.Grid;

public class Sand extends DynamicParticle {
    
    public Sand() {
        setID(255);

        // TODO: make them shift a bit to create texture
        // original sand = 182, 155, 99
        int offset = new Random().nextInt(190);
        setColors(82 + offset, 55 + offset, offset);
    }

    @Override
    public void update(int j, int i, Grid grid) {
    }

    
}
