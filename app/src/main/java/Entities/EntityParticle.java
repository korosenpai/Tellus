package Entities;

import Blocks.Air;
import Blocks.Particle;
import Grid.Grid;

public class EntityParticle extends Particle{

    int entityID;
    
    int maxSpeed = 0;

    public EntityParticle(int id, int maxS) {
        super();
        entityID = id;
        maxSpeed = maxS;
    }

    
    public int[] update(int[] coords, Grid grid, Particle lowerN, int velocityX, int velocityY) {
        super.update(coords, grid);
        
        previousPosition = coords.clone();
        coords[0] = coords[0] + velocityY;
        /* if (isFreeFalling && velocityY > 0) {
            coords[0] = coords[0] + velocityY;
            
        } else if (!isFreeFalling && velocityY < 0){
            
        } */

        if (previousPosition != currentPosition){
            grid.setParticle(previousPosition[0], previousPosition[1], new Air());
        }   
        coords[1] = coords[1] + velocityX;
        //System.out.println(velocityX);
        currentPosition = coords.clone();

        return coords;

    }

    /* public void setBehaviours(boolean goDown, boolean goDownLeft, boolean goDownRight) {
        this.goDown = goDown;
        this.goDownLeft = goDownLeft;
        this.goDownRight = goDownRight;
    } */

    public int getEntityID() {
        return entityID;
    }

    public void setMaxSpeed(int speed) {
        maxSpeed = speed;
    }

    public void setColor(int r, int g, int b) {
        super.setColors(r, g, b);
    }
}
