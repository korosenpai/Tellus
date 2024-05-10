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
    
    public DynamicParticle() {
        super();
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

    @Override
    public void update(int j, int i, Grid grid) {
        updateVelocity();

        for (int n = 0; n <= velocity; n++) {

            Particle[] under = grid.getLowerNeighbors(j, i);
            if (under[1] == null) return; // cannot move or you finish out of bounds

            // NOTE: it always swaps with the cell it goes to
            // make smoke disappear (if it is gas it creates air and doesnt make the gas rise)

            if (!(under[1] instanceof Air)) { // sabbia quando entra in acqua non ha più gravità e cade lentamente
                resetVelocity(); 
                if (under[1] instanceof Gravel) return;
            }
            

            // if block under is not a solid swap with block under
            if (!(under[1] instanceof SolidParticle)) {
                grid.setParticle(j, i, grid.getAtPosition(j + 1, i));
                grid.setParticle(j + 1, i, this);
                j = j + 1;    
            }

            // Gravel doesnt fall left/right only down
            // go to block to left if is not solid
            else if (!(this instanceof Gravel) && under[0] != null && !(under[0] instanceof SolidParticle)) {
                grid.setParticle(j, i, grid.getAtPosition(j + 1, i - 1));
                grid.setParticle(j + 1, i - 1, this);
                j = j + 1;
                i = i - 1;
            }

            // go to block to right if is not solid
            else if (!(this instanceof Gravel) && under[2] != null && !(under[2] instanceof SolidParticle)) {
                grid.setParticle(j, i, grid.getAtPosition(j + 1, i + 1));
                grid.setParticle(j + 1, i + 1, this);
                j = j + 1;
                i = i + 1;
            }

        }


        Particle under = grid.getLowerNeighbors(j, i)[1];
        
        if (under instanceof Air) return;

        //doesnt freefall diagonally
        if (this instanceof Gravel) {
            

            Particle[] side = grid.getSideNeighbors(j, i);


            if (side[0] != null && side[0] instanceof Air) {
                grid.setParticle(j, i, grid.getAtPosition(j, i-1));
                grid.setParticle(j, i - 1 , this);
                return;
            }

            else if (side[1] != null && side[1] instanceof Air) {
                grid.setParticle(j, i, grid.getAtPosition(j, i+1));
                grid.setParticle(j, i + 1 , this);
                return;
            }
        }


    }

    
}
