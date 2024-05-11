package Window;
//gradlew build && gradlew run  I'm lazy XD

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
import Blocks.ParticleList;

import Entities.Player;


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

    public ParticleList particleList = new ParticleList();
    public int currentSelectedParticle = 1;
    public Color currentSelectedParticleColor = particleList.getColorOfParticle(currentSelectedParticle);

    public Player player;
    public int playerDirectionX = 0;
    public int playerDirectionY = 0;

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
        player = new Player(tileDimension, screenHeight, screenWidth);

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
    
        // make particle moved be able to move again
        grid.setGridHasMovedFalse();


    }

    //called by repaint in actionPerformed
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; // 2d gives more access on geometry, coords, ...

        drawGrid(g2);
        drawMouse(g2);
        drawPlayer(g2);

        g2.dispose(); // frees up memory
    }

    public void setOnClick() {
        int x = mouse.getX();
        int y = mouse.getY();
        if ( 0 > x || x > screenWidth - 1 || 0 > y || y > screenHeight - 1 ) return; // check if out of bounds

        grid.setCursor(x, y, mouse.getRadius(), currentSelectedParticle);
    }

 /*    public void setMouseOnClick(){
        List<int[]> positions = drawMousePoints(g)(null); // Pass null to avoid drawing
            for (int[] position : positions) {
                setOnClick(position[0], position[1]);
            }
        }
    } */
/*     public void setOnClick() {
        int x = mouse.getX();
        int y = mouse.getY();

        int radius = mouse.getRadius() * tileDimension;
        int circleCentreX = (mouse.getX() / tileDimension) * tileDimension;
        int circleCentreY = (mouse.getY() / tileDimension) * tileDimension;
        
        int c0 = (((circleCentreX + radius) / tileDimension) * tileDimension); //c0 stands for 0 degrees on the circumference
        int c180 = (((circleCentreX - radius) / tileDimension) * tileDimension); //c180 stands for 180 degrees on the circumference
        int c90 = (((circleCentreY + radius) / tileDimension) * tileDimension); //c90 stands for 90 degrees on the circumference
        int c270 = (((circleCentreY - radius) / tileDimension) * tileDimension); //c270 stands for 270 degrees on the circumference 

        if ( 0 > x || x > screenWidth - 1 || 0 > y || y > screenHeight - 1 ) return; // check if out of bounds
            for (int i = c180; i <= c0; i += tileDimension) {
                for (int j = c270; j <= c90; j += tileDimension) {
                    // Your existing condition to check if the pixel is within the circle
                    if (radius / tileDimension == 0){
                        grid.setParticle(mouse.getY() / tileDimension, mouse.getX() / tileDimension, new Sand());
                    } else {
                        if (Math.sqrt((i - circleCentreX) * (i - circleCentreX) + (j - circleCentreY) * (j - circleCentreY)) <= radius) {
                            grid.setParticle(i, j, new Sand());
                        }
                    }
                }
            }
            //grid.setParticle(mouse.getY() / tileDimension, mouse.getX() / tileDimension, new Sand());
       
    } */

    



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

    public void drawMouse(Graphics2D g) {
        //g.setColor(new Color(currentSelectedParticle.getColorRed(), currentSelectedParticle.getColorGreen(), currentSelectedParticle.getColorBlue()));

        g.setColor(currentSelectedParticleColor);
        
        /*
         int radiusInPixels = mouse.getRadius() * tileDimension;
         int centerX = mouse.getX() / tileDimension * tileDimension;
         int centerY = mouse.getY() / tileDimension * tileDimension;

         g.fillOval(centerX - radiusInPixels, centerY - radiusInPixels, radiusInPixels * 2, radiusInPixels * 2);
        */
        
        int radius = mouse.getRadius() * tileDimension;
        int circleCentreX = (mouse.getX() / tileDimension) * tileDimension;
        int circleCentreY = (mouse.getY() / tileDimension) * tileDimension;
        
        int c0 = (((circleCentreX + radius) / tileDimension) * tileDimension); //c0 stands for 0 degrees on the circumference
        int c180 = (((circleCentreX - radius) / tileDimension) * tileDimension); //c180 stands for 180 degrees on the circumference
        int c90 = (((circleCentreY + radius) / tileDimension) * tileDimension); //c90 stands for 90 degrees on the circumference
        int c270 = (((circleCentreY - radius) / tileDimension) * tileDimension); //c270 stands for 270 degrees on the circumference       
        
        //int radiusInTiles = radius / tileDimension;
        // Calculate the number of tiles the circle spans in both directions
        //int numTilesX = Math.abs(c180 - c0) / tileDimension;
        //int numTilesY = Math.abs(c270 - c90) / tileDimension;

        // Adjust the loop conditions based on the actual number of tiles the circle spans
        for (int x = c180; x <= c0; x += tileDimension) {
            for (int y = c270; y <= c90; y += tileDimension) {
                if (Math.sqrt((x - circleCentreX) * (x - circleCentreX) + (y - circleCentreY) * (y - circleCentreY)) <= radius) {
                    g.fillRect(x, y, tileDimension, tileDimension);
                }
            }
        }
    }

    // this sends the direction of the player to the player's position updater and draws it
    public void drawPlayer(Graphics2D p) {
        player.updatePosition(playerDirectionX, playerDirectionY);
        player.paintComponent(p);
    }
    


    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key) {
                //full list here https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
                case 10: // enter
                    restart = true;
                    break;

                case 27: // esc
                    windowShouldClose = true;
                    break;
                
                //keyboards input to switch currently selected particle
                case 112: //F1
                    currentSelectedParticle = 1;
                    break;
            
                case 113: //F2
                    currentSelectedParticle = 2;
                    break;
                
                case 114: //F3
                    currentSelectedParticle = 3;
                    break;
                
                case 115: //F4
                    currentSelectedParticle = 4;
                    break;
                
                case 116: // F5
                    currentSelectedParticle = 5;
                    break;

                /* case 68: // D
                    playerX++;
                case 65: // A
                    playerX--;
                case 87: // W
                    playerY--;
                case 83: // S
                    playerY++;
                    break; */

                default:
                    break;
            }

            // checks the motion key pressed
            // TODO: multiple keys pressed at the same time
            if (key == 68){
                playerDirectionX = 1;
            } else if (key == 65){
                playerDirectionX = -1;
            }
            if (key == 87){
                playerDirectionY = -1;
            } else if (key == 83){
                playerDirectionY = 1;
            }

            // get ovverriden every input, we dont care we are not yandere dev we can, gls amio
            currentSelectedParticleColor = particleList.getColorOfParticle(currentSelectedParticle);

        }

        @Override
        public void keyReleased(KeyEvent e){
            int key = e.getKeyCode();
            if (key == 68){
                playerDirectionX = 0;
            } else if (key == 65){
                playerDirectionX = 0;
            }
            if (key == 87){
                playerDirectionY = 0;
            } else if (key == 83){
                playerDirectionY = 0;
            }
        }

    }

}
