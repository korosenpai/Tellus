package Entities;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Graphics2D;

public class Player extends Entity{

    final int tileDimension;
    final int screenHeight;
    final int screenWidth;
    
    boolean hasLanded = true;

    public Player(int tileDimension, int screenHeight, int screenWidth, int entityID) {
        
        super(entityID);
        //System.out.println(entityID);

        this.tileDimension = tileDimension;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        setMoveX(screenWidth/2);
        setMoveY(screenHeight/2);
        setDimension(5, 8);
        setMaxSpeed(10);
        setAccelerationX(1);
        setAccelerationY(1.3f);
        setColor(111, 0, 161);
        
    }

    public void paintComponent(Graphics2D p) {
        super.paintComponent(p);

        p.setColor(color);
        p.fillRect((int)moveX, (int)moveY, getDimensionX() * tileDimension, getDimensionY() * tileDimension);
    }

    public void updatePosition(int x, int y) {
        updateVelocityX(x);
        updateVelocityY(y);

        moveX = moveX + getVelocityX();
        moveY = moveY + getVelocityY();
    }

    public Color getPlayerColor() {
        return getColor();
    }


}
