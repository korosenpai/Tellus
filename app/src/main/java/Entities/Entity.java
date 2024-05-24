package Entities;

import javax.swing.JPanel;

import Blocks.Particle;
import Blocks.Solids.SolidParticle;
import Grid.Grid;

import java.awt.Color;
import java.util.ArrayList;

import Blocks.Air;

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

    int initialJumpVel;
    int currentJumpVel = 0; // how long jump button is pressed determines how high it goes
    boolean isOnGround = false;

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
            //newP.setBehaviours(true, false, false);
            newP.setColor(111, 0, 161);
            this.particleList.add(newP);
        }
        //System.out.println(particleList);
    }

    public ArrayList<EntityParticle> getParticleList() {
        return particleList;
    }

    public void setDimension(int x, int y) {
        entityDimensionX = x;
        entityDimensionY = y;
    }

    /* public void update(Grid grid, int directionX, int directionY) {

        // temporary istances
        Particle lowerN = new Particle() {};
        Particle upperN = new Particle() {};
        Particle leftN = new Particle() {};
        Particle rightN = new Particle() {};

        // flags
        boolean shouldFreeFall = true;
        boolean canGoUp = true;
        boolean  canGoLeft = true;
        boolean canGoRight = true;

        int[] coords;
        ArrayList <int[]> totalCoords = new ArrayList<>(); // list of coordinates
        int currentVelX = Math.abs(velocityX);
        int vy = 0, vx = 0;


        // loop that boundes the movement of the entity on the y axis
        for (; vy < Math.abs(velocityY); vy++) {
            if (currentJumpVel == 0) {

                // loop that boundes the horizontal movement of the Entity
                for (; vx < currentVelX; vx++) {
                    //System.out.println(vx);
        
                    // checks if the entity is going left
                    if (directionX < 0) {
        
                        for (int i = 0; i < particleList.size(); i++){
                            // local coordinates
                            coords = fromPosToCoords(i);
                        
                            if (i <= particleList.size() - getDimensionX()){
                                if (i % getDimensionX() == 0){
                                    // left side logic
                                    leftN = grid.getSideNeighbors(coords[0], coords[1]-vx)[0];
                                    canGoLeft = (canGoLeft && !(leftN == null) && !(leftN instanceof  SolidParticle));
                                }
                            }
                            if (i == particleList.size() - getDimensionX()){
                                // left-down diagonal logic
                            }
                            if (i >= particleList.size() - getDimensionX()){
                                // lower side logic
                                lowerN = grid.getSingleLowerNeighbor(coords[0], coords[1], vy);
                                shouldFreeFall = (shouldFreeFall && !(lowerN == null) && !(lowerN instanceof SolidParticle && lowerN.isFreeFalling == false));
                            }
                        }
                        if (!shouldFreeFall && !canGoLeft) break;
        
                        // TODO: SONO ARRIVATO QUI NON TOCCATE NULLA


                    // checks if the entity is going right
                    } else if (directionX > 0) {
        
                        // iterates the right column of the entity to check right nighbors
                        for(int i = entityDimensionX-1; i < particleList.size(); i += entityDimensionX){
                            //System.out.println(i);
        
                            coords = fromPosToCoords(i);
                            rightN = grid.getSideNeighbors(coords[0], coords[1]+vx)[1];
                            canGoRight = (canGoRight && !(rightN == null) && !(rightN instanceof  SolidParticle));
                            
                        }
                        if (!canGoRight) break;
                    }
                }
                
            } else {

                // loop that iterates the first row of particles of the entity to check the upper neighbors
                for(int i = 0; i < getDimensionX(); i++) {

                    coords = fromPosToCoords(i);
                    upperN = grid.getSingleUpperNeighbor(coords[0], coords[1], vy); 
                
                    canGoUp = (canGoUp && upperN != null && !(upperN instanceof SolidParticle)); 
                }
                // no jump conditions anymore
                if (!canGoUp) break;
            }
        }


        if (isOnGround){
            if (directionY < 0) {
                setJump();
            }
        } else {
            if (directionY < 0){
                if (currentJumpVel > 0) currentJumpVel--;
                else {
                directionY = 1;
                }
            }
        }

        // loops the whole entity to update every particle
        for (int j = 0; j < particleList.size(); j++) {
            coords = fromPosToCoords(j);
            EntityParticle tempParticle = particleList.get(j);

            //boolean isInJumpState = false; 
            if (shouldFreeFall){
                tempParticle.isFreeFalling = true;
            } else {
                tempParticle.isFreeFalling = false;
                resetVelocityY();
            }

            totalCoords.add(tempParticle.update(coords, grid, lowerN, vx*directionX, vy*directionY));
        }
        
        //local update section
        updateVelocityX(directionX);
        //System.out.println("Y: " + directionY);
        updateVelocityY(directionY); // gravity
        
        if (directionY >= 0 || !canGoUp){
            resetJump();
        }
        //System.out.println("velocity : " + vy + " in dirY: " + directionY);

        setMoveX(totalCoords.get(0)[1]);
        setMoveY(totalCoords.get(0)[0]);

        //System.out.println("X: " + totalCoords.get(0)[1] + " Y: " + totalCoords.get(0)[0]);
        // System.out.println("Grid rows: " + grid.getRows());
        //set particles in the grid section
        for (int i = 0; i < totalCoords.size(); i++) {
            //System.out.println("X: " + totalCoords.get(i)[1] + " Y: " + totalCoords.get(i)[0]);
            
             grid.setParticle(totalCoords.get(i)[0],totalCoords.get(i)[1], particleList.get(i));
        
        }
       
        isOnGround = !shouldFreeFall;

        return;
    } */


    public void update(Grid grid, int directionX, int directionY) {

        // temporary istances
        Particle lowerN = new Particle() {};
        Particle upperN = new Particle() {};
        Particle leftN = new Particle() {};
        Particle rightN = new Particle() {};

        // flags
        boolean shouldFreeFall = true;
        boolean canGoUp = true;
        boolean  canGoLeft = true;
        boolean canGoRight = true;

        int[] coords;
        ArrayList <int[]> totalCoords = new ArrayList<>(); // list of coordinates
        int currentVelX = Math.abs(velocityX);
        int vy = 0, vx = 0;


        // loop that boundes the movement of the entity on the y axis
        for (; vy < Math.abs(velocityY); vy++) {

            if (currentJumpVel == 0) {
                // loop that iterates the lowest row of particles of the entity to check the lower neighbors
                for(int i = (getDimensionY()-1)*getDimensionX(); i < particleList.size(); i++) {

                    coords = fromPosToCoords(i);
                    lowerN = grid.getSingleLowerNeighbor(coords[0], coords[1], vy);
                    // System.out.println("Prima: "+shouldFreeFall);
                    shouldFreeFall = (shouldFreeFall && !(lowerN == null) && !(lowerN instanceof SolidParticle && lowerN.isFreeFalling == false));
                    // System.out.println(lowerN);
                    // System.out.println("Dopo: "+shouldFreeFall);

                }
                if (!shouldFreeFall) break;
            } else {

                // loop that iterates the first row of particles of the entity to check the upper neighbors
                for(int i = 0; i < getDimensionX(); i++) {

                    coords = fromPosToCoords(i);
                    upperN = grid.getSingleUpperNeighbor(coords[0], coords[1], vy); 
                
                    canGoUp = (canGoUp && upperN != null && !(upperN instanceof SolidParticle)); 
                }
                // no jump conditions anymore
                if (!canGoUp) break;
            }
        }
        

        // loop that boundes the horizontal movement of the Entity
        for (; vx < currentVelX; vx++) {
            //System.out.println(vx);

            // checks if the entity is going left
            if (directionX < 0) {

                // iterates the left column of the entity to check left nighbors
                for(int i = 0; i < particleList.size()-entityDimensionX+1; i += entityDimensionX){
                    //System.out.println(i);

                    coords = fromPosToCoords(i);
                    leftN = grid.getSideNeighbors(coords[0], coords[1]-vx)[0];
                    canGoLeft = (canGoLeft && !(leftN == null) && !(leftN instanceof  SolidParticle));
                    
                }
                if (!canGoLeft) break;

            // checks if the entity is going right
            } else if (directionX > 0) {

                // iterates the right column of the entity to check right nighbors
                for(int i = entityDimensionX-1; i < particleList.size(); i += entityDimensionX){
                    //System.out.println(i);

                    coords = fromPosToCoords(i);
                    rightN = grid.getSideNeighbors(coords[0], coords[1]+vx)[1];
                    canGoRight = (canGoRight && !(rightN == null) && !(rightN instanceof  SolidParticle));
                    
                }
                if (!canGoRight) break;
            }
        }

        if (isOnGround){
            if (directionY < 0) {
                setJump();
            }
        } else {
            if (directionY < 0){
                if (currentJumpVel > 0) currentJumpVel--;
                else {
                directionY = 1;
                }
            }
        }

        // loops the whole entity to update every particle
        for (int j = 0; j < particleList.size(); j++) {
            coords = fromPosToCoords(j);
            EntityParticle tempParticle = particleList.get(j);

            //boolean isInJumpState = false; 
            if (shouldFreeFall){
                tempParticle.isFreeFalling = true;
            } else {
                tempParticle.isFreeFalling = false;
                resetVelocityY();
            }

            totalCoords.add(tempParticle.update(coords, grid, lowerN, vx*directionX, vy*directionY));
        }
        
        //local update section
        updateVelocityX(directionX);
        //System.out.println("niggeers " + directionY*currentJumpHeight);
        //System.out.println("Y: " + directionY);
        updateVelocityY(directionY); // gravity
        
        if (directionY >= 0 || !canGoUp){
            resetJump();
        }
        //System.out.println("velocity : " + vy + " in dirY: " + directionY);

        setMoveX(totalCoords.get(0)[1]);
        setMoveY(totalCoords.get(0)[0]);

        //System.out.println("X: " + totalCoords.get(0)[1] + " Y: " + totalCoords.get(0)[0]);
        // System.out.println("Grid rows: " + grid.getRows());
        //set particles in the grid section
        for (int i = 0; i < totalCoords.size(); i++) {
            //System.out.println("X: " + totalCoords.get(i)[1] + " Y: " + totalCoords.get(i)[0]);
            
             grid.setParticle(totalCoords.get(i)[0],totalCoords.get(i)[1], particleList.get(i));
        
        }
       
        isOnGround = !shouldFreeFall;

        return;
    }

    
    public int[] fromPosToCoords(int index){
        //int x = moveX/tileDimension + (index % entityDimensionX);
        //int y = moveY/tileDimension + (index % entityDimensionX);
        int x = 0, y = 0;
        index++;

        // loops the list to get the coorinate of a certain particle
        for (int i = 0; i < entityDimensionY; i++) {
            for (int j = 0; j < entityDimensionX; j++){
                index--;
                if (index == 0){
                    x = moveX + j;
                    y = moveY + i;
                }
            }
        }

        int[] coords = {y, x};
        return coords;
    }
        
    public void updateVelocityX(int direction) {
        velocityX = direction*Math.min(Math.round(Math.abs(velocityX) + accelerationX), maxSpeed);
    }

    public void updateVelocityY(int direction) {
        //velocityY = direction*Math.min(Math.round(Math.abs(velocityY) + accelerationY), maxSpeed);
        if (direction >= 0){
            velocityY = Math.min(Math.round(Math.abs(velocityY) + accelerationY), maxSpeed); 
        } else {
            velocityY = -currentJumpVel; //+ (int) accelerationY;
        }
        //System.out.println("before: " + velocityY);       
        //System.out.println("velY: " + velocityY);
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

    public void setInitialJump(int jump) {
        initialJumpVel = jump;
    }

    public void setJump (){
        currentJumpVel = initialJumpVel;
    }

    public void resetJump (){
        currentJumpVel = 0;
    }

}


