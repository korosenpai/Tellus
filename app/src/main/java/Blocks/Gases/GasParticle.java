package Blocks.Gases;

import Blocks.Air;
import Blocks.Particle;
import Window.Grid;

abstract class GasParticle extends Particle {

    public GasParticle() {
        super();

        isRising = true;
    }

    @Override
    public int[] update(int[] coords, Grid grid) {
        hasMoved = true;

        // TODO: add in grid second pass to update from top to bottom

        Particle[] upper = grid.getUpperNeighbors(coords[0], coords[1]);
        if (upper[1] == null) {
            isRising = false;
            return coords;
        }

        isRising = upper[1] instanceof Air || upper[1].isRising
            || !(coords[0] == previousPosition[0] && coords[1] == previousPosition[1]);
        if (isRising) previousPosition = coords.clone();

        // if block upper is not a solid swap with block upper
        if (isRising && upper[1] != null && !(upper[1] instanceof GasParticle)) {
            grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0] - 1, coords[1]));
            grid.setParticle(coords[0] - 1, coords[1], this);
            coords[0]--;
        }

        // go to block to left if is not solid
        else if (isRising && upper[0] != null && !(upper[0] instanceof GasParticle)) {
            grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0] - 1, coords[1] - 1));
            grid.setParticle(coords[0] - 1, coords[1] - 1, this);
            coords[0]--;
            coords[1]--;
        }

        // // go to block to right if is not solid
        // else if (upper[2] != null && !(upper[2] instanceof GasParticle)) {
        //     grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0] - 1, coords[1] + 1));
        //     grid.setParticle(coords[0] - 1, coords[1] + 1, this);
        //     coords[0]--;
        //     coords[1]++;
        // }


        return coords;
    }

}
