package Entities;

import javax.swing.JPanel;

import Blocks.Air;
import Blocks.Particle;
import Blocks.Liquids.LiquidParticle;
import Blocks.Solids.SolidParticle;
import Window.Grid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Entity extends JPanel{
    
    final int tileDimension;

    int moveX = 0;
    int moveY = 0;
    
    int maxSpeed = 0;
    int velocityX = 0;
    int velocityY = 0;
    float accelerationX = 0;
    float accelerationY = 0;
    int entityDimensionX;
    int entityDimensionY;

    //boolean isFreeFalling;

    int entityID;
    ArrayList<EntityParticle> particleList;

    //JPanel sprite = new JPanel();
    Color color;

    public Entity(int entityID, int dimX, int dimY, int tileDim) {
        this.tileDimension = tileDim;
        entityDimensionX = dimX;
        entityDimensionY = dimY;

        particleList = new ArrayList<EntityParticle>();

        this.entityID = entityID;
        for (int i = 0; i < (entityDimensionX*entityDimensionY); i++){
            EntityParticle newP = new EntityParticle(entityID, maxSpeed);
            newP.setAccelerationX(1);
            newP.setAccelerationY(1.3f);
            newP.setBehaviours(true, false, false);
            newP.setColor(111, 0, 161);
            this.particleList.add(newP);
        }
        //System.out.println(particleList);
    }

    public ArrayList getParticleList() {
        return particleList;
    }

    //TODO: hitbox and physics implementation
    public void setDimension(int x, int y) {
        entityDimensionX = x;
        entityDimensionY = y;
    }

    public void update(Grid grid, int directionX, int directionY) {

        Particle lowerN = new Particle() {};

        for(int i = (getDimensionY()-1)*getDimensionX(); i < particleList.size(); i++){
            
            int[] coords = fromPosToCoords(i);
            
            lowerN = grid.getSingleLowerNeighbor(coords[1], coords[0]);
            //System.out.println(lowerN);
            //System.out.println(lowerN.isFreeFalling);
            
            if ((lowerN == null) || (lowerN instanceof SolidParticle && lowerN.isFreeFalling == false)) {
                
                for (int j = 0; j < particleList.size(); j++){
                    particleList.get(j).isFreeFalling = false;
                    particleList.get(j).resetVelocityY();
                    particleList.get(j).update(coords, grid, lowerN, directionX, directionY);
                }

                return;
            }
        }

        //int[] coords = {moveX, moveY};
        for (int i = 0; i < particleList.size(); i++) {
            int[] coords = fromPosToCoords(i);
            particleList.get(i).isFreeFalling = true;
            //System.out.println(particleList.get(i).isFreeFalling + " Porcod");
            particleList.get(i).update(coords, grid, lowerN, directionX, directionY);
        }
        return;
    }

    public int[] fromPosToCoords(int index){
        //int x = moveX/tileDimension + (index % entityDimensionX);
        //int y = moveY/tileDimension + (index % entityDimensionX);
        int x = 0, y = 0;
        index++;

        for (int i = 0; i < entityDimensionY; i++) {
            for (int j = 0; j < entityDimensionX; j++){
                index--;
                if (index == 0){
                    x = moveX/tileDimension + j;
                    y = moveY/tileDimension + i;
                }
            }
        }

        int[] coords = {y, x};
        return coords;
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

    public Color getColor() {
        return color;
    }

    public void setColor(int r, int g, int b) {
        color = new Color(r, g, b);
    }

}


