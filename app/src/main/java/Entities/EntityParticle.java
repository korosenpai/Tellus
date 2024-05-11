package Entities;

import Blocks.Particle;
import Window.Grid;

public class EntityParticle extends Particle{

    int entityID;

    int particleX;
    int particleY;

    public EntityParticle(int id) {
        super();
        entityID = id;
    }

    public int getX() {
        return particleX;
    }

    public int getY() {
        return particleY;
    }

    @Override
    public int[] update(int[] coords, Grid grid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    
}
