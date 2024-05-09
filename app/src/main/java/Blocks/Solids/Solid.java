package Blocks.Solids;

import Blocks.Particle;
import Window.Grid;

public abstract class Solid extends Particle {

    public Solid() {
        super();
        
        setType(1);
    }

    @Override
    public abstract void update(int j, int i, Grid grid);
}
