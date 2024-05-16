package Entities;

import Blocks.Air;
import Blocks.Particle;
import Grid.Grid;

public class EntityParticle extends Particle{

    int entityID;

    //int particleX;
    //int particleY;

    int velocityX;
    int velocityY;
    float accelerationX = 0;
    float accelerationY = 0;
    
    int maxSpeed = 0;

    // behaviours
    private boolean goDown;
    private boolean goDownLeft;
    private boolean goDownRight;

    public EntityParticle(int id, int maxS) {
        super();
        entityID = id;
        maxSpeed = maxS;
    }

    
    public int[] update(int[] coords, Grid grid, Particle lowerN, int directionX, int directionY) {
        super.update(coords, grid);
        
        previousPosition = coords.clone();
        if (isFreeFalling) {
                grid.setParticle(previousPosition[0], previousPosition[1], new Air());
        }
        previousPosition = coords.clone();

      /*   updateVelocityY(directionY);
        int currentFallingVel = velocityY;

        //System.out.println(coords[0] + " " + coords[1]);
        

        for (int n = 0; n <= velocityY; n++) {

            

            /* // if has air beneath or moved since last frame
            isFreeFalling = lowerN instanceof Air || lowerN.isFreeFalling || !(coords[0] == previousPosition[0] && coords[1] == previousPosition[1]);
            if (isFreeFalling) previousPosition = coords.clone();

            // NOTE: it always swaps with the cell it goes to
            // make smoke disappear (if it is gas it creates air and doesnt make the gas rise)

            if (!isFreeFalling || lowerN instanceof LiquidParticle) { // sabbia quando entra in acqua non ha più gravità e cade lentamente
                velocityX = velocityY;
                resetVelocityY();
            }

            // if block under is not a solid swap with block under
            if (goDown && !(lowerN instanceof SolidParticle)) {
                grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0] + 1, coords[1]));
                grid.setParticle(coords[0] + 1, coords[1], this);
                coords[0]++;
            }

            // updated version for check in other specific classes
            isFreeFalling = lowerN instanceof Air || lowerN.isFreeFalling || !(coords[0] == previousPosition[0] && coords[1] == previousPosition[1]);
            if (isFreeFalling) {
                previousPosition = coords.clone();
                velocityY = currentFallingVel;
            };
 
        } */

        return coords;

    }

    public void setBehaviours(boolean goDown, boolean goDownLeft, boolean goDownRight) {
        this.goDown = goDown;
        this.goDownLeft = goDownLeft;
        this.goDownRight = goDownRight;
    }

    public void setMaxSpeed(int speed) {
        maxSpeed = speed;
    }

    public void setAccelerationX(float acceleration) {
        accelerationX = acceleration;
    }

    public void setAccelerationY(float acceleration) {
        accelerationY = acceleration;
    }

    public void updateVelocityX(int direction) {
        velocityX = direction*Math.min(Math.round(Math.abs(velocityX) + accelerationX), maxSpeed);
    }

    public void updateVelocityY(int direction) {
        velocityY = direction*Math.min(Math.round(Math.abs(velocityY) + accelerationY), maxSpeed);
    }

    public void resetVelocityX() {
        velocityX = 0;
    }

    public void resetVelocityY() {
        velocityY = 0;
    }

    public void setColor(int r, int g, int b) {
        super.setColors(r, g, b);
    }
}
