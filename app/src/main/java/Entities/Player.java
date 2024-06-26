package Entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Grid.Grid;

public class Player extends Entity{

    final int tileDimension;
    final int screenHeight;
    final int screenWidth;
    
    boolean hasLanded = true;

    public Player(int tileDimension, int screenHeight, int screenWidth, int entityID) {
        
        super(entityID, 3, 5, tileDimension);
        //System.out.println(entityID);

        this.tileDimension = tileDimension;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        setBehaviours(false, true);
        setMoveX(screenWidth/tileDimension/2);
        setMoveY(screenHeight/tileDimension/2);
        setMaxSpeed(4);
        setInitialJump(8);
        setAccelerationX(0.5f);
        setAccelerationY(1.3f);
        setColor(111, 0, 161);
        
    }

    public void paintComponent(Graphics2D p) {
        super.paintComponent(p);

        p.setColor(color);
        p.fillRect((int)moveX*tileDimension, (int)moveY*tileDimension, getDimensionX() * tileDimension, getDimensionY() * tileDimension);
    }

    public void updatePosition(Grid grid, int directionX, int directionY) {

        super.update(grid, directionX, directionY);

        //System.out.println("X coords: " + moveX);
        //System.out.println("Y coords: " + moveY);

    }

    public Color getPlayerColor() {
        return getColor();
    }

    public ArrayList<EntityParticle> getParticleList(){
        return super.getParticleList();
    }


}
