package Blocks.Solids.DynamicSolid;

import Blocks.Particle;
import Blocks.Solids.Solid;
import Window.Grid;



abstract class DynamicParticle extends Solid {
    
    public DynamicParticle() {
        super();
    }

    
    @Override
    public abstract void update(int j, int i, Grid grid);

    
}
