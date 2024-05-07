package Window;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Mouse extends MouseMotionAdapter {
    private int x;
    private int y;

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
}
