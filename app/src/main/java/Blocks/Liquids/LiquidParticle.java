package Blocks.Liquids;

import Blocks.Particle;
import Blocks.Solids.SolidParticle;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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

        spawnRate = 0.5f;
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
        super.update(coords, grid);

        updateVelocity();

        for (int n = 0; n <= velocity; n++) {

            Particle[] under = grid.getLowerNeighbors(coords[0], coords[1]);

            // TODO: avoid moving up more than once a frame
            // TODO: check for vasi comunicanti (fixata dai bias)

            // move down
            // NOTE: remove check for liquidParticle to make if flow to bottom visual effect
            if (under[1] != null && !(under[1] instanceof SolidParticle || under[1] instanceof LiquidParticle) ) {
                grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0] + 1, coords[1]));
                grid.setParticle(coords[0] + 1, coords[1], this);
                coords[0]++;
            }

            // move to bottom left
            else if (under[0] != null && !(under[0] instanceof SolidParticle || under[0] instanceof LiquidParticle)) {
                grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0] + 1, coords[1] - 1));
                grid.setParticle(coords[0] + 1, coords[1] - 1, this);
                coords[0]++;
                coords[1]--;
            }

            // move bottom right
            else if (under[2] != null && !(under[2] instanceof SolidParticle || under[2] instanceof LiquidParticle)) {
                grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0] + 1, coords[1] + 1));
                grid.setParticle(coords[0] + 1, coords[1] + 1, this);
                coords[0]++;
                coords[1]++;
            }

            else {

                // move sideways in random direction

                Particle[] side = grid.getSideNeighbors(coords[0], coords[1]);

                // TODO: maybe move based on ripples from surface
                // TODO: go to left or right randomly, giving higher chance of going where it is empty
                // NOTE: now it goes left or right in a random ass way

                if (side[0] != null && (ThreadLocalRandom.current().nextInt(2) == 0) && !(side[0] instanceof LiquidParticle || side[0] instanceof SolidParticle)) {
                    grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0], coords[1] - 1));
                    grid.setParticle(coords[0], coords[1] - 1, this);
                    coords[1]--;
                }
                else if (side[1] != null && !(side[1] instanceof SolidParticle)) {
                    grid.setParticle(coords[0], coords[1], grid.getAtPosition(coords[0], coords[1] + 1));
                    grid.setParticle(coords[0], coords[1] + 1, this);
                    coords[1]++;
                }
            }
        }


        return coords;

    }

    
}
