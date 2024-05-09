package Blocks;

import Window.Grid;

// NOTE: use if need to render a particle with a specific color
public class TestParticle extends Particle {

    public TestParticle(int r, int g, int b) {
        this.setColors(r, g, b);
    }

    @Override
    public void update(int j, int i, Grid grid) {
        
    }
}
