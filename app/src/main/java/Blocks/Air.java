package Blocks;

import Window.Grid;

public class Air extends Particle {

    // does not need to be updated
    @Override
    public int[] update(int[] coords, Grid grid) {
        return coords;
    }

    
}
