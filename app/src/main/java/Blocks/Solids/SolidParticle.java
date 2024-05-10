package Blocks.Solids;

import Blocks.Particle;
import Window.Grid;

public abstract class SolidParticle extends Particle {

    public SolidParticle() {
        super();
    }

    @Override
    public abstract int[] update(int[] coords, Grid grid);
}
