package Entities;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics2D;

public class Entity extends JPanel{
    int maxSpeed = 0;
    int velocityX = 0;
    int velocityY = 0;
    float accelerationX = 0;
    float accelerationY = 0;

    JPanel sprite = new JPanel();
    Color color;

    //TODO: hitbox and physics implementation

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


