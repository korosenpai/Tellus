package Blocks.Solids.StaticParticle;

import Blocks.Particle;
import Blocks.Solids.Solid;
import Window.Grid;



abstract class StaticParticle extends Solid {

    private int maxSpeed = 0; // how many cells to move ain one frame
    private float acceleration = 0; // 32bits, will never need more
    private float velocity = 0;
    
    public StaticParticle() {
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
    public void update(int j, int i, Grid grid){}


    

    
}
