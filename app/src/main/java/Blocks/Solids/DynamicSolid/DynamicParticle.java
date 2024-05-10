package Blocks.Solids.DynamicSolid;

import Blocks.Air;
import Blocks.Particle;
import Blocks.Solids.SolidParticle;
import Blocks.Solids.DynamicSolid.Gravel;

import Window.Grid;



abstract class DynamicParticle extends SolidParticle {

    private int maxSpeed = 0; // how many cells to move ain one frame
    private float acceleration = 0; // 32bits, will never need more
    private float velocity = 0;

    // behaviours
    private boolean goDown;
    private boolean goDownLeft;
    private boolean goDownRight;

    
    public DynamicParticle() {
        super();
    }

    public void setBehaviours(boolean goDown, boolean goDownLeft, boolean goDownRight) {
        this.goDown = goDown;
        this.goDownLeft = goDownLeft;
        this.goDownRight = goDownRight;
    }

    public void setMaxSpeed(int max) {
        maxSpeed = max;
    }

    public void setAcceleration(float accel) {
        acceleration = accel;
    }

    public void resetVelocity() {
        velocity = 0;
    }

    public float getVelocity() {
        return velocity;
    }

    public void updateVelocity() {
        velocity = Math.min(velocity + acceleration, maxSpeed);
    }

    // NOTE: coords -> j, i passed by reference
    @Override
    public int[] update(int[] coords, Grid grid) {
        updateVelocity();

        for (int n = 0; n <= velocity; n++) {

            Particle[] under = grid.getLowerNeighbors(coords[0], coords[1]);
            if (under[1] == null) return coords; // cannot move or you finish out of bounds

            // NOTE: it always swaps with the cell it goes to
            // make smoke disappear (if it is gas it creates air and doesnt make the gas rise)

            if (!(under[1] instanceof Air)) { // sabbia quando entra in acqua non ha più gravità e cade lentamente
                resetVelocity(); 
                if (under[1] instanceof Gravel) return coords;
            }
            

            // if block under is not a solid swap with block under
            if (goDown && !(under[1] instanceof SolidParticle)) {
                grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0] + 1, coords[1]));
                grid.setParticle(coords[0] + 1, coords[1], this);
                coords[0]++;
            }

            // Gravel doesnt fall left/right only down
            // go to block to left if is not solid
            else if (goDownLeft && !(this instanceof Gravel) && under[0] != null && !(under[0] instanceof SolidParticle)) {
                grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0] + 1, coords[1] - 1));
                grid.setParticle(coords[0] + 1, coords[1] - 1, this);
                coords[0]++;
                coords[1]--;
            }

            // go to block to right if is not solid
            else if (goDownRight && !(this instanceof Gravel) && under[2] != null && !(under[2] instanceof SolidParticle)) {
                grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0] + 1, coords[1] + 1));
                grid.setParticle(coords[0] + 1, coords[1] + 1, this);
                coords[0]++;
                coords[1]++;
            }

        }

        
        Particle under = grid.getLowerNeighbors(coords[0], coords[1])[1];
        
        if (under instanceof Air) return coords;

        if (this instanceof Gravel) {
            

            Particle[] side = grid.getSideNeighbors(coords[0], coords[1]);


            if (side[0] != null && side[0] instanceof Air) {
                grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0], coords[1]-1));
                grid.setParticle(coords[0], coords[1] - 1 , this);
                return coords;
            }

            else if (side[1] != null && side[1] instanceof Air) {
                grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0], coords[1] + 1));
                grid.setParticle(coords[0], coords[1] + 1 , this);
                return coords;
            }
        }



        return coords;


    }

    
}
