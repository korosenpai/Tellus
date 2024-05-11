package Entities;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Entity extends JPanel{
    
    int moveX = 0;
    int moveY = 0;
    
    int maxSpeed = 0;
    int velocityX = 0;
    int velocityY = 0;
    float accelerationX = 0;
    float accelerationY = 0;
    int entityDimensionX;
    int entityDimensionY;

    int entityID;
    ArrayList<EntityParticle> particleList = new ArrayList<>();

    //JPanel sprite = new JPanel();
    Color color;

    public Entity(int entityID) {

        this.entityID = entityID;
        for (int i = 0; i < (entityDimensionX*entityDimensionY); i++){
            particleList.add(new EntityParticle(entityID));
        }
    }

    public ArrayList getParticleList() {
        return particleList;
    }

    //TODO: hitbox and physics implementation
    public void setDimension(int x, int y) {
        entityDimensionX = x;
        entityDimensionY = y;
    }

    public int getMoveX() {
        return moveX;
    }

    public int getMoveY() {
        return moveY;
    }

    public void setMoveX(int x) {
        moveX = x;
    }

    public void setMoveY(int y) {
        moveY = y;
    }

    public int getDimensionX() {
        return entityDimensionX;
    }

    public int getDimensionY() {
        return entityDimensionY;
    }

    public void resetVelocityX() {
        velocityX = 0;
    }

    public void resetVelocityY() {
        velocityY = 0;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public void updateVelocityX(int direction) {
        velocityX = direction*Math.min(Math.round(Math.abs(velocityX) + accelerationX), maxSpeed);
    }

    public void updateVelocityY(int direction) {
        velocityY = direction*Math.min(Math.round(Math.abs(velocityY) + accelerationY), maxSpeed);
    }

    public float getAccelerationX() {
        return accelerationX;
    }

    public float getAccelerationY() {
        return accelerationY;
    }

    public void setAccelerationX(float acceleration) {
        accelerationX = acceleration;
    }

    public void setAccelerationY(float acceleration) {
        accelerationY = acceleration;
    }

    public void setMaxSpeed(int speed) {
        maxSpeed = speed;
    }

    public void setColor(int r, int g, int b) {
        color = new Color(r, g, b);
    }

    public Color getColor() {
        return color;
    }
}


