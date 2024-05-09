package Blocks.Solids.DynamicSolid;

import java.util.Arrays;
import java.util.Random;

import Blocks.Air;
import Blocks.Particle;
import Window.Grid;

public class Sand extends DynamicParticle {
    
    public Sand() {
        super();

        setID(255);

        // make them shift a bit to create texture
        // original sand = 182, 155, 99
        int offset = new Random().nextInt(140); // change luminosity
        setColors(132 + offset, 105 + offset, 50 + offset);
    }

    @Override
    public void update(int j, int i, Grid grid) {
        Particle[] under = grid.getLowerNeighbors(j, i);
        if (under[1] == null) return; // cannot move or you finish out of bounds

        if (under[1].getType() != 1) {
            grid.setParticle(j + 1, i, this);
            grid.setParticle(j, i, new Air());
        }


    }

    
}
