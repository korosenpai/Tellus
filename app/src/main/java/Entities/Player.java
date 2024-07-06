package Entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Blocks.Particle;
import Blocks.Solids.SolidParticle;
import Grid.Grid;

public class Player extends Entity{

    final int tileDimension;
    final int screenCenterHeight;
    final int screenCenterWidth;
    
    boolean hasLanded = true;

    public Player(int tileDimension, int screenCenterHeight, int screenCenterWidth, int entityID) {
        
        super(entityID, 3, 5, tileDimension);
        //System.out.println(entityID);

        this.tileDimension = tileDimension;
        this.screenCenterHeight = screenCenterHeight;
        this.screenCenterWidth = screenCenterWidth;

        setBehaviours(false, true);
        setMoveX(screenCenterWidth);
        setMoveY(screenCenterHeight);
        setMaxSpeed(4);
        setInitialJump(8);
        setAccelerationX(0.5f);
        setAccelerationY(1.3f);
        setColor(111, 0, 161);
        
    }

    public void paintComponent(Graphics2D p) {
        super.paintComponent(p);

        p.setColor(color);
        //p.fillRect((int)moveX*tileDimension, (int)moveY*tileDimension, getDimensionX() * tileDimension, getDimensionY() * tileDimension);
    }

    public void updatePosition(Grid grid, int directionX, int directionY) {

        //super.update(grid, directionX, directionY);

        //System.out.println("X coords: " + moveX);
        //System.out.println("Y coords: " + moveY);

    }

    public Color getPlayerColor() {
        return getColor();
    }

    public ArrayList<EntityParticle> getParticleList(){
        return super.getParticleList();
    }

    public void moveInGrid(Grid grid, int directionX, int directionY) {
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

        // FIX: to make these work we muist check everytime if move is allowed
        // for (int movX = 0; movX < directionX; movX++) {
        //     grid.moveViewRightOne();
        // }
        // if (directionX < 0) {
        //     for (int movX = directionX; movX >= 0; movX++) {
        //         grid.moveViewRightOne();
        //     }
        // }

        // for (int movY = 1; movY <= directionY; movY++) {
        //     grid.moveViewDownOne();
        // }
        // if (directionY < 0) {
        //     for (int movY = directionY; movY >= 0; movY++) {
        //         grid.moveViewUpOne();
        //     }
        // }

        // NOTE: this has to move in opposite direction
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


}
