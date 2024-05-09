package Window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import Blocks.Particle;
import Blocks.Solids.DynamicSolid.Sand;


public class Window extends JPanel implements ActionListener {
    
    final int screenWidth;
    final int screenHeight;
    final int tileDimension;
    final int rows;
    final int columns;

    int FPS;
    int DELAY;
    Timer timer;

    private static Grid grid;
    private boolean restart;

    private Mouse mouse = new Mouse();

    private boolean windowShouldClose = false; // set when hit esc to quit

    public Window(int screenWidth, int screenHeight, int tileDimension, int fps) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.tileDimension = tileDimension;
        this.rows = screenHeight / tileDimension;
        this.columns = screenWidth / tileDimension;

        this.FPS = fps;
        this.DELAY = 1000 / FPS;

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black); //dunno if its actually even useful since we color also the empty cell with a black square
        this.setDoubleBuffered(true); //improves performance

        this.setFocusable(true); //for using keyAdapter
        this.requestFocusInWindow();
        
        this.addKeyListener(new MyKeyAdapter());

        this.addMouseWheelListener(mouse);// for mouse wheel detection, changes cursour radius
        this.addMouseMotionListener(mouse);
        this.addMouseListener(mouse);


    }

    

    public void start() {
        restart = false;
        if (timer == null) { // keep same timer even if restarted
            timer = new Timer(DELAY, this);
            timer.setRepeats(true);
            timer.start();
        }

        grid = new Grid(screenWidth, screenHeight, tileDimension);

    }
    
    public void stop() {
        JFrame ancestor = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this);
        ancestor.dispose();
        System.exit(0);
    }

    public boolean getWindowShouldClose() {
        return windowShouldClose;
    }

    // NOTE: MAIN LOOP
    //called every timer clock cycle
    public void actionPerformed(ActionEvent event){
        //equivalent to pygame.display.update()
        //updates screen every clock cycle
        if (restart) start();
        if (getWindowShouldClose()) stop();

        grid.updateGrid();

        if (mouse.isDragged() || mouse.isPressed()) {
            setOnClick(); // set particle on the position of the mouse, when clicked
        };
        
        

        repaint(); // calls paintComponent

    }

    //called by repaint in actionPerformed
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; // 2d gives more access on geometry, coords, ...

        drawGrid(g2);
        drawMouse(g2);

        g2.dispose(); // frees up memory
    }

    public void setOnClick() {
        int x = mouse.getX();
        int y = mouse.getY();
        if ( 0 > x || x > screenWidth - 1 || 0 > y || y > screenHeight - 1 ) return; // check if out of bounds
        grid.setParticle(mouse.getY() / tileDimension, mouse.getX() / tileDimension, new Sand());
    }

    



    // TODO: change method to go j, i (if needed tbh idk if it will give problems)
    public void drawGrid(Graphics2D g){        
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++) {
                Particle curr = grid.getAtPosition(i, j);
                int colorRed = curr.getColorRed();
                int colorGreen = curr.getColorGreen();
                int colorBlue = curr.getColorBlue();
                g.setColor(new Color(colorRed, colorGreen, colorBlue));
                g.fillRect(j*tileDimension, i*tileDimension, tileDimension, tileDimension);                
            }
        }
    }

    // converts from window's coordinate to snapped window to grid coordinates for drawing
    private int snapToGrid(int coord) {
        // (pos / tiledimension) * tiledimension works 
        // because java rounds to int the one in brackets so then we can treat it as i or j of drawGrid()
        return (coord / tileDimension) * tileDimension;
    }

    // TODO: add support to draw as circle
    public void drawMouse(Graphics2D g) {
        g.setColor(new Color(255, 255, 255));
        
  
        int radiusInPixels = mouse.getRadius() * tileDimension;
        int centerX = mouse.getX() / tileDimension * tileDimension;
        int centerY = mouse.getY() / tileDimension * tileDimension;
        
        g.setColor(Color.WHITE); // Example color
        g.fillOval(centerX - radiusInPixels, centerY - radiusInPixels, radiusInPixels * 2, radiusInPixels * 2);
        
        //g.fillRect(snapToGrid(mouse.getX()), snapToGrid(mouse.getY()), tileDimension, tileDimension); 
       /* 
        int radius = mouse.getRadius() * tileDimension;
        int circleCentreX = Math.round((mouse.getX() / tileDimension) * tileDimension);
        int circleCentreY = Math.round((mouse.getY() / tileDimension) * tileDimension);
        //g.fillOval(circleCentreX - radius, circleCentreY - radius, radius * 2, radius * 2);

        
        int c0 = Math.round((((circleCentreX + radius) / tileDimension) * tileDimension)); //c0 stands for 0 degrees on the circumference
        int c180 = Math.round((((circleCentreX - radius) / tileDimension) * tileDimension)); //c180 stands for 180 degrees on the circumference
        int c90 = Math.round((((circleCentreY + radius) / tileDimension) * tileDimension)); //c90 stands for 90 degrees on the circumference
        int c270 = Math.round((((circleCentreY - radius) / tileDimension) * tileDimension)); //c270 stands for 2700 degrees on the circumference
        
         
        for (int x = c180; x <= c0; x += tileDimension){
            for (int y = c270; y <= c90; y += tileDimension){
                if (Math.sqrt((x - circleCentreX)*(x - circleCentreX) + (y - circleCentreY)*(y - circleCentreY)) <= radius){
                g.fillRect(x, y, radius, radius); 
                }
            }
        }
        */
    }
    


    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                //full list here https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
                case 10: // enter
                    restart = true;
                    break;

                case 27: // esc
                    windowShouldClose = true;
                    break;
            
                default:
                    break;
            }
        }
    }


}
