package Entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Grid.Grid;

public class Player extends Entity{

    final int tileDimension;
    final int centerY;
    final int centerX;

    public Player(int tileDimension, int centerY, int centerX, int entityID) {
        
        super(entityID, 3, 5, tileDimension);
        //System.out.println(entityID);

        this.tileDimension = tileDimension;
        this.centerY = centerY;
        this.centerX = centerX;

        setBehaviours(false, true);
        setMoveX(centerX/tileDimension/2);
        setMoveY(centerY/tileDimension/2);
        setMaxSpeed(4);
        setInitialJump(8);
        setAccelerationX(0.5f);
        setAccelerationY(0.7f);
        setColor(111, 0, 161);
        
    }

    public void paintComponent(Graphics2D p) {
        //super.paintComponent(p);

        p.setColor(color);
        //p.fillRect(moveX*tileDimension-10*32, moveY*tileDimension-10*32, getDimensionX() * tileDimension, getDimensionY() * tileDimension);
    }

    public void updatePosition(Grid grid, int directionX, int directionY) {
        
        int oldX = moveX;
        int oldY = moveY;

        super.update(grid, directionX, directionY);

        // difference between new coordinates and old coordinates
        int diffX = moveX - oldX;
        int diffY = moveY - oldY;

        // // moves the grid to compensate the movement of the player
        // for (int i = 0; i < Math.abs(diffX); i++)
        //     if (diffX < 0)
        //         grid.moveViewLeft(1);
        //     else if (diffX > 0)
        //         grid.moveViewRight(1);
        // for (int i = 0; i < Math.abs(diffY); i++)
        //     if (diffY < 0)
        //         grid.moveViewUp(1);
        //     else if (diffY > 0)
        //         grid.moveViewDown(1);

    }

    public Color getPlayerColor() {
        return getColor();
    }

    public ArrayList<EntityParticle> getParticleList(){
        return super.getParticleList();
    }


}