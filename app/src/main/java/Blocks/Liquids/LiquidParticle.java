package Blocks.Liquids;

import Blocks.Particle;
import Blocks.Solids.SolidParticle;
import Blocks.Air;
import Window.Grid;



public abstract class LiquidParticle extends Particle {

    private int maxSpeed = 0; // how many cells to move ain one frame
    private float acceleration = 0; // 32bits, will never need more
    private float velocity = 0;
    private boolean isOnGround = false;
    private int density = 0;
    
    public LiquidParticle() {
        super();
    }

    public void setMaxSpeed(int max) {
        maxSpeed = max;
    }

    public void setAcceleration(float accel) {
        acceleration = accel;
    }

    public void setDensity(int dens) {
        density = dens;
    }

    public int getDensity() {
        return density;
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

    @Override
    public int[] update(int[] coords, Grid grid) {
        updateVelocity();

        for (int n = 0; n <= velocity; n++) {

            Particle[] under = grid.getLowerNeighbors(coords[0], coords[1]);
            if (under[1] == null) return coords; // cannot move or you finish out of bounds

            // NOTE: it always swaps with the cell it goes to
            // make smoke disappear (if it is gas it creates air and doesnt make the gas rise)

            if (under[1] instanceof SolidParticle || under[1] instanceof LiquidParticle) {
                resetVelocity();
            }

            // if block under is not a solid swap with block under
            if (!(under[1] instanceof SolidParticle || under[1] instanceof LiquidParticle)) {
                grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0] + 1, coords[1]));
                grid.setParticle(coords[0] + 1, coords[1], this);
                coords[0]++;
            
            }

            // go to block to left if is not solid
            else if (under[0] != null && !(under[0] instanceof SolidParticle || under[0] instanceof LiquidParticle)) {
                grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0] + 1, coords[1] - 1));
                grid.setParticle(coords[0] + 1, coords[1] - 1, this);
                coords[0]++;
                coords[1]--;
            }

            // go to block to right if is not solid
            else if (under[2] != null && !(under[2] instanceof SolidParticle || under[2] instanceof LiquidParticle)) {
                grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0] + 1, coords[1] + 1));
                grid.setParticle(coords[0] + 1, coords[1] + 1, this);
                coords[0]++;
                coords[1]++;
                
            }
        }


        //doesnt freefall diagonally
        Particle under = grid.getLowerNeighbors(coords[0], coords[1])[1];
        if (under instanceof Air) return coords;

        Particle[] side = grid.getSideNeighbors(coords[0], coords[1]);

        

        if (side[0] != null && side[0] instanceof Air) {
            grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0], coords[1] - 1));
            grid.setParticle(coords[0], coords[1] - 1 , this);
            return coords;
        }

        else if (side[1] != null && side[1] instanceof Air) {
            grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0], coords[1] + 1));
            grid.setParticle(coords[0], coords[1] + 1 , this);
            return coords;
        }
        
        return coords;



    }

    
}
