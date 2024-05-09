package Blocks.Solids.DynamicSolid;

import Blocks.Particle;
import Window.Grid;



abstract class DynamicParticle extends Particle {
    
    DynamicParticle() {
        super();
    }

    
    @Override
    public abstract void update(int j, int i, Grid grid);

    
}
