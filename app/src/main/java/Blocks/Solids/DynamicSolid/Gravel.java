package Blocks.Solids.DynamicSolid;

import java.util.Random;

import Blocks.Air;
import Blocks.Particle;
import Window.Grid;

public class Gravel extends DynamicParticle {
    
    public Gravel() {
        super();

        // make them shift a bit to create texture
        // original sand = 182, 155, 99
        int offset = getColorOffset();
        setColors(33 + offset, 34 + offset, 28 + offset);

        setMaxSpeed(5);
        setAcceleration(0.6f);

        setBehaviours(true, false, false);
    }

    @Override
    public int[] update(int[] coords, Grid grid) {
        // run generic dynamic solid update
        super.update(coords, grid);

        // TODO: make them work  here and not in dynamic solid
        // additional gravel updates

        //Particle under = grid.getLowerNeighbors(coords[0], coords[1])[1];
        //if (under instanceof Air || under instanceof Gravel) return coords;

        //doesnt freefall diagonally
        // if (this instanceof Gravel) {
        //     

        //     Particle[] side = grid.getSideNeighbors(coords[0], coords[1]);


        //     if (side[0] != null && side[0] instanceof Air) {
        //         grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0], coords[1]-1));
        //         grid.setParticle(coords[0], coords[1] - 1 , this);
        //         return coords;
        //     }

        //     else if (side[1] != null && side[1] instanceof Air) {
        //         grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0], coords[1] + 1));
        //         grid.setParticle(coords[0], coords[1] + 1 , this);
        //         return coords;
        //     }
        // }

        return coords;
    }
}
