package Blocks.Solids.DynamicSolid;

import java.util.Random;

import Blocks.Particle;
import Blocks.Solids.SolidParticle;
import Window.Grid;


public class Snow extends DynamicParticle {
    public boolean hasMovedLastFrame = true; //way to make it go slower than 1 fps

    public Snow() {
        super();
        this.hasMovedLastFrame = hasMovedLastFrame;

        // make them shift a bit to create texture
        int offset = getColorOffset(); // change luminosity
        setColors(155 + offset, 155 + offset, 155 + offset);

        
        setMaxSpeed(1);
        setAcceleration(0.1f);

    }

    @Override
    public void update(int j, int i, Grid grid) {
        
        //snow moves once every 2 frame
        if (this.hasMovedLastFrame == true){
            this.hasMovedLastFrame = false;

            return;
        } 
         
        // we dont update the velocity so the fall speed stays the same
        //updateVelocity()
        
        for (int n = 0; n <= super.getVelocity(); n++) {

            Particle[] under = grid.getLowerNeighbors(j, i);
            if (under[1] == null) return; // cannot move or you finish out of bounds
            
            this.hasMovedLastFrame = true;
            // NOTE: it always swaps with the cell it goes to
            // make smoke disappear (if it is gas it creates air and doesnt make the gas rise)

            if (under[1] instanceof SolidParticle) resetVelocity();

            // if block under is not a solid swap with block under
            if (!(under[1] instanceof SolidParticle)) {
                grid.setParticle(j, i, grid.getAtPosition(j + 1, i));
                grid.setParticle(j + 1, i, this);
                j = j + 1;
            }
            // go to block to left if is not solid
            else if (under[0] != null && !(under[0] instanceof SolidParticle)) {
                grid.setParticle(j, i, grid.getAtPosition(j + 1, i - 1));
                grid.setParticle(j + 1, i - 1, this);
                j = j + 1;
                i = i - 1;
            }

            // go to block to right if is not solid
            else if (under[2] != null && !(under[2] instanceof SolidParticle)) {
                grid.setParticle(j, i, grid.getAtPosition(j + 1, i + 1));
                grid.setParticle(j + 1, i + 1, this);
                j = j + 1;
                i = i + 1;
            }

        }

    }
}