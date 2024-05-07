package Window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.Timer;
import javax.swing.JPanel;

import Blocks.*;


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
        this.addMouseMotionListener(mouse);
    }

    

    public void start() {
        restart = false;
        // if (timer == null) {
        //     timer = new Timer(DELAY, this);
        //     timer.setRepeats(true);
        //     timer.start();
        // }

        grid = new Grid(screenWidth, screenHeight, tileDimension);
        grid.generateRandomizedGrid();
    }

    public class MyKeyAdapter extends KeyAdapter {
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

    public boolean getWindowShouldClose() {
        return windowShouldClose;
    }

    //called every timer clock cycle
    public void actionPerformed(ActionEvent event){
        //equivalent to pygame.display.update()
        //updates screen every clock cycle
    }

    public void update() {
        if (restart) start();
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

    public void drawGrid(Graphics2D g){        
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++) {
                int color = grid.getAtPosition(i, j).getID();
                g.setColor(new Color(color, color, color));
                g.fillRect(j*tileDimension, i*tileDimension, tileDimension, tileDimension);                
            }
        }
    }

    // add support to draw as arc
    public void drawMouse(Graphics2D g) {
        g.setColor(new Color(255, 255, 255));
        // (pos / tiledimension) * tiledimension works because java rounds to int the one in brackets so then we can treat it as i or j of drawGrid()
        g.fillRect((mouse.getX() / tileDimension) * tileDimension, (mouse.getY() / tileDimension) * tileDimension, tileDimension, tileDimension);                
    }



    public Particle[] getNeighbors(int j, int i) {
        return grid.getNeighbors(j, i);
    }

    public Particle[] getLowerNeighbors(int j, int i) {
        return grid.getLowerNeighbors(j, i);
    }

    public Particle[] getUpperNeighbors(int j, int i) {
        return grid.getUpperNeighbors(j, i);
    }

    public Particle[] getSideNeighbors(int j, int i) {
        return grid.getSideNeighbors(j, i);
    }
}
