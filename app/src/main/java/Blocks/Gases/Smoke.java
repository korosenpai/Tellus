package Blocks.Gases;

import Window.Grid;

public class Smoke extends GasParticle  {

    public Smoke() {
        super();

        int offset = getColorOffset();
        setColors(30 + offset, 38 + offset, 38 + offset);
    }

    @Override
    public int[] update(int[] coords, Grid grid) {
        super.update(coords, grid);
        return coords;
    }

    
}
