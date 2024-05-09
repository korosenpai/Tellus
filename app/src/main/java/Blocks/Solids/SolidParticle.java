package Blocks.Solids;

import Blocks.Particle;
import Window.Grid;

public abstract class SolidParticle extends Particle {

    public SolidParticle() {
        super();
    }

    @Override
    public abstract void update(int j, int i, Grid grid);
}
