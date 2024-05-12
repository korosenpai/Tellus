package Blocks.Gases;

import Window.Grid;

public class Smoke extends GasParticle  {

    public Smoke() {
        super();

        int offset = getColorOffset();
        setColors(0 + offset, 8 + offset, 8 + offset);
    }

    @Override
    public int[] update(int[] coords, Grid grid) {
        super.update(coords, grid);
        return coords;
    }

    
}
