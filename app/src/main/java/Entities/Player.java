package Entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Blocks.Air;
import Blocks.Particle;
import Blocks.Solids.SolidParticle;
import Grid.Grid;

public class Player extends Entity{

    final int tileDimension;
    final int screenHeight;
    final int screenWidth;

    public Player(int tileDimension, int screenHeight, int screenWidth, int entityID) {
        
        super(entityID, 3, 5, tileDimension);
        //System.out.println(entityID);

        this.tileDimension = tileDimension;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        setBehaviours(false, true);
        setMoveX(screenWidth/tileDimension/2 + 2*32);
        setMoveY(screenHeight/tileDimension/2 + 2*32);
        setMaxSpeed(4);
        setInitialJump(8);
        setAccelerationX(0.5f);
        setAccelerationY(1.3f);
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

        // calcola la differenza tra nuove e vecchie coordinate
        int diffX = moveX - oldX;
        int diffY = moveY - oldY;

        // muove la grid in modo da compensare il movimento del player e farlo rimanere al centro dello schermo
        for (int i = 0; i < Math.abs(diffX); i++)
            if (diffX < 0)
                grid.moveViewLeft(1);
            else if (diffX > 0)
                grid.moveViewRight(1);
        for (int i = 0; i < Math.abs(diffY); i++)
            if (diffY < 0)
                grid.moveViewUp(1);
            else if (diffY > 0)
                grid.moveViewDown(1);

    }

    public Color getPlayerColor() {
        return getColor();
    }

    public ArrayList<EntityParticle> getParticleList(){
        return super.getParticleList();
    }


}
