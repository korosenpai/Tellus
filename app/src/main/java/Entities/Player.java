package Entities;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Graphics2D;

public class Player extends Entity{

    final int tileDimension;
    final int screenHeight;
    final int screenWidth;

    int moveX;
    int moveY;
    
    boolean hasLanded = true;

    public Player(int tileDimension, int screenHeight, int screenWidth) {
        
        super();

        this.tileDimension = tileDimension;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        moveX = (screenWidth / 2);
        moveY = (screenHeight / 2);

        setMaxSpeed(10);
        setAccelerationX(1);
        setAccelerationY(1.3f);
        setColor(111, 0, 161);
        
    }

    public void paintComponent(Graphics2D p) {
        super.paintComponent(p);

        p.setColor(color);
        p.fillRect((int)moveX, (int)moveY, 3 * tileDimension, 5 * tileDimension);
    }

    public void updatePosition(int x, int y) {
        updateVelocityX(x);
        updateVelocityY(y);

        moveX = moveX + getVelocityX();
        moveY = moveY + getVelocityY();
    }

    public int getMoveX() {
        return moveX;
    }

    public int getMoveY() {
        return moveY;
    }

    public Color getPlayerColor() {
        return getColor();
    }


}
