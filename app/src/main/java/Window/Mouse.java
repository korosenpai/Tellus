package Window;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Mouse extends MouseMotionAdapter {
    private int x;
    private int y;

    private int radius = 2; // draw area around mouse

    public void mouseMoved(MouseEvent e) {
        // divide by tile size to get what position in grid mouse is currently hovering onto
        x = e.getX();
        y = e.getY();
        // System.out.println("Mouse x: " + x + " y: " + y);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }
}
