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
    final int screenCenterHeight;
    final int screenCenterWidth;

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
                grid.moveViewLeftOne();
            else if (diffX > 0)
                grid.moveViewRightOne();
        for (int i = 0; i < Math.abs(diffY); i++)
            if (diffY < 0)
                grid.moveViewUpOne();
            else if (diffY > 0)
                grid.moveViewDownOne();

    }

    public Color getPlayerColor() {
        return getColor();
    }

    public ArrayList<EntityParticle> getParticleList(){
        return super.getParticleList();
    }


}

