package Window;
//gradlew build && gradlew run  I'm lazy XD

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import Blocks.Particle;
import Blocks.ParticleList;
import Debug.Debug;
import Entities.Blob;
import Entities.Entity;
import Entities.EntityParticle;
import Entities.Player;
import Grid.Grid;
import MusicPlayer.MusicPlayer;
import Sidebar.SidebarPanel;


public class Window extends JPanel implements ActionListener {
    
    final int screenWidth;
    final int screenHeight;
    final int chunkSize;
    final int sidebarWidth;
    final int tileDimension;

    int FPS;
    int DELAY;
    Timer timer;


    int calculatedFps = 0;
    long timeAtLastFrame = 0;

    // if not rendering in time for timer wait that first is done rendering and skip frame
    private boolean currentlyRendering;

    private static Grid grid;
    private int gridOffset;
    private boolean toDrawChunks;
    private boolean restart;

    private Mouse mouse = new Mouse();

    private boolean windowShouldClose = false; // set when hit esc to quit

    public ParticleList particleList = new ParticleList();
    public int currentSelectedParticle = 1;
    public Color currentSelectedParticleColor = particleList.getColorOfParticle(currentSelectedParticle);

    public int currentlySelectedTemplate = 0;

    public Player player;
    public int playerDirectionX = 0;
    public int playerDirectionY = 1;
    public Blob bplayer;

    private JFrame sidebarWindow;
    private SidebarPanel sidebarPanel;
    private boolean showSidebar = false;
    private Point sidebarPosition;

    //public ArrayList<Entity> entityList;

    public Window(int screenWidth, int screenHeight,int chunkSize, int gridOffset, int sidebarWidth, int tileDimension, int fps) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.gridOffset = gridOffset;
        this.chunkSize = chunkSize;
        this.sidebarWidth = sidebarWidth;
        this.tileDimension = tileDimension;
        // this.rows = screenHeight / tileDimension;
        // this.columns = screenWidth / tileDimension;

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

        setupSidebar();
    }

    private void setupSidebar() {
        sidebarWindow = new JFrame();
        sidebarWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sidebarWindow.setResizable(false);
        sidebarWindow.setTitle("sidebar");

        sidebarPanel = new SidebarPanel();
        sidebarWindow.add(sidebarPanel);
        sidebarWindow.pack();

        sidebarWindow.setLocationRelativeTo(null); // specify location of the sliderWindow // unll -> display at center of screen
        sidebarWindow.setVisible(showSidebar); 
    }

    

    public void start() {

        grid = new Grid(screenWidth, screenHeight, chunkSize, tileDimension, gridOffset);
        //entityList = new ArrayList<>();
        // if (player == null) { // avoid creating double player
        //     player = new Player(tileDimension, grid.getRows() / 2, grid.getColumns() / 2, 0);
        //     //entityList.add(player);
        // }

        if (bplayer == null) bplayer = new Blob(grid, grid.getRows() / 2, grid.getColumns() / 2);

        restart = false;
        if (timer == null) { // keep same timer even if restarted
            timer = new Timer(DELAY, this);
            timer.setRepeats(true);
            timer.start();
        }
    }
    
    public void stop() {
        // https://stackoverflow.com/questions/15449022/show-prompt-before-closing-jframe
        JFrame ancestor = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this);
        ancestor.dispose();

        MusicPlayer.closeAll();

        System.exit(0);
    }

    public boolean getWindowShouldClose() {
        return windowShouldClose;
    }

    // NOTE: MAIN LOOP
    //called every timer clock cycle
    public void actionPerformed(ActionEvent event){
        if (currentlyRendering) {
            Debug.debug("frame skipped");
            return;
        };
        currentlyRendering = true;

        //System.out.println("millies elapsed since last frame: " + (System.currentTimeMillis() - timeAtLastFrame)); // aim at 15


        //equivalent to pygame.display.update()
        //updates screen every clock cycle
        if (restart) start();
        if (getWindowShouldClose()) stop();


        if (grid != null)
            grid.updateGrid();

        if (mouse.isDragged() || mouse.isPressed()) {
            setOnClick(); // set particle on the position of the mouse, when clicked
        };

        // select particle from sidebar
        if (sidebarPanel.isElementSelected()) {
            currentSelectedParticle = sidebarPanel.getSelectedElementID();
            currentSelectedParticleColor = particleList.getColorOfParticle(currentSelectedParticle);
        }

        //setEntities();

        if (player != null)
            player.updatePosition(grid, playerDirectionX, playerDirectionY);

        if (bplayer != null) bplayer.update(grid);

        repaint(); // calls paintComponent
    
        // make particle moved be able to move again
        grid.setGridHasMovedFalse();

        // remove unloaded particles
        System.gc();

        currentlyRendering = false;

        timeAtLastFrame = System.currentTimeMillis();
    }

    //called by repaint in actionPerformed
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; // 2d gives more access on geometry, coords, ...

        if (grid != null) {
            drawGrid(g2);
            if (toDrawChunks) drawChunks(g2);
        }

        drawMouse(g2);

        if (player != null) {
            drawPlayer(g2);
        }

        // print offsets on screen
        if (grid != null) {
            g2.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 30));
            g2.setColor(Color.WHITE);

            g2.drawString(
                "chunk offset xy: " + grid.getChunkOffsetX() + " " + grid.getChunkOffsetY(),
                20, 40
            );

            g2.drawString(
                "viewport offset xy: " + grid.getViewportOffsetX() + " " + grid.getViewportOffsetY(),
                20, 80
            );
        }

        g2.dispose(); // frees up memory
    }

    public void setOnClick() {
        int x = mouse.getX();
        int y = mouse.getY();
        if ( 0 > x || x > screenWidth - 1 || 0 > y || y > screenHeight - 1 ) return; // check if out of bounds

        grid.setCursor(x, y, mouse.getRadius(), currentSelectedParticle);
    }


    // TODO: change method to go j, i (if needed tbh idk if it will give problems)
    public void drawGrid(Graphics2D g){
        // grid is saved perpewndicular so it must be draw in opposite way
        for (int i = 0; i < grid.getViewportRows(); i++){
            for (int j = 0; j < grid.getViewportColumns(); j++) {
                Particle curr = grid.getAtPosition(i + grid.getViewportOffsetY(), j + grid.getViewportOffsetX());
                g.setColor(new Color(curr.getColorRed(), curr.getColorGreen(), curr.getColorBlue()));
                g.fillRect(j*tileDimension, i*tileDimension, tileDimension, tileDimension);
            }
        }
    }

    public void drawChunks(Graphics2D g) {
        // drawline(x1, y1, x2, y2)

        g.setColor(new Color(255, 255, 255));

        // how distant the bottom of the first chunk is to be drawn to the screen
        int offsetX = grid.CHUNK_SIZE - grid.getViewportOffsetX() % grid.CHUNK_SIZE;
        int offsetY = grid.CHUNK_SIZE - grid.getViewportOffsetY() % grid.CHUNK_SIZE;

        // vertical lines
        for (int i = 0; i < grid.getVisibleChunkColumns(); i++) {
            g.drawLine(
                (offsetX + i * chunkSize) * tileDimension,
                0,
                (offsetX + i * chunkSize) * tileDimension,
                screenHeight
            );

        }

        // horizontal lines
        for (int j = 0; j < grid.getVisibleChunkRows(); j++) {
            g.drawLine(
                0,
                (offsetY + j * chunkSize) * tileDimension,
                screenWidth,
                (offsetY + j * chunkSize) * tileDimension
            );

        }

        // draw active chunks
        // <= in case we are in middle of chunks so we need to render one more
        for (int j = 0; j <= grid.getVisibleChunkRows(); j++){
            for (int i = 0; i <= grid.getVisibleChunkColumns(); i++) {
                if (grid.getChunks()[j + grid.getGridOffset()][i + grid.getGridOffset()].getShouldStep()) {
                    // System.out.println("ji: " + j + " " + i);
                    // System.out.println("offsety/x: " + offsetY + " " + offsetX);
                    // System.out.println(offsetY - grid.CHUNK_SIZE);
                    g.setColor(new Color(255, 0, 0, 60));
                    g.fillRect(
                        // math wizardry
                        // give possibility of starting pos of rect to be negative to take care of small rects when they appear from borders
                        (i * chunkSize) * tileDimension,
                        (offsetY - grid.CHUNK_SIZE + j * chunkSize) * tileDimension,
                        chunkSize * tileDimension,
                        chunkSize * tileDimension
                    );
                }
            }
        }

    }

    // // converts from window's coordinate to snapped window to grid coordinates for drawing
    // private int snapToGrid(int coord) {
    //     // (pos / tiledimension) * tiledimension works 
    //     // because java rounds to int the one in brackets so then we can treat it as i or j of drawGrid()
    //     return (coord / tileDimension) * tileDimension;
    // }

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
        player.paintComponent(p);
    }


    // It should set in the grid the particles of all the Entities
    // public void setEntities() {
    // 
    //     for(int e = 0; e < entityList.size(); e++){
    //         Entity tempEntity = entityList.get(e);
    //         ArrayList tempParticleList = tempEntity.getParticleList();
    //         for(int p = 0; p < tempParticleList.size(); p++){
    //             int[] coords = tempEntity.fromPosToCoords(p);
    //             EntityParticle tempParticle = (EntityParticle)tempParticleList.get(p);
    //             grid.setParticle(coords[0], coords[1], tempParticle);
    //         }
    //     }
    // }

    
    


    private class MyKeyAdapter extends KeyAdapter {

        private boolean CTRL_PRESSED = false;

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            //System.out.println(key);
            switch (key) {
                //full list here https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
                case 10: // enter
                    restart = true;
                    break;

                case 27: // esc
                    windowShouldClose = true;
                    // grid.saveGridtoDisk();
                    break;

                // arrows up and down to increase / decrease cursor
                case 38:
                    mouse.incrementCursor();
                    break;
                case 40:
                    mouse.decrementCursor();
                    break;

                
                case 80:
                    currentSelectedParticle = -1;
                    break;

                // TODO: add all in one check
                //keyboards input to switch currently selected particle
                case 49: //F1
                    currentSelectedParticle = 1;
                    break;
            
                case 50: //F2
                    currentSelectedParticle = 2;
                    break;
                
                case 51: //F3
                    currentSelectedParticle = 3;
                    break;
                
                case 52: //F4
                    currentSelectedParticle = 4;
                    break;
                
                case 53: // F5
                    currentSelectedParticle = 5;
                    break;

                case 54: // F6
                    currentSelectedParticle = 6;
                    break;

                case 55:
                    currentSelectedParticle = 7;
                    break;

                case 56:
                    currentSelectedParticle = 8;
                    break;

                case 57:
                    currentSelectedParticle = 9;
                    break;

                case 58:  // rightarrow
                    currentSelectedParticle = (currentSelectedParticle +1) % ParticleList.getNumberOfParticleAvailable();
            
                case 37:
                    currentSelectedParticle = (currentSelectedParticle -1);
                    if (currentSelectedParticle < 0) currentSelectedParticle = ParticleList.getNumberOfParticleAvailable();
                    

                case 77:
                    //GridTemplates.saveCurrentGrid(grid);
                    break;

                default:
                    break;
            }

            // checks the motion key pressed
            if (key == 68){ // D
                playerDirectionX = 1;
                bplayer.directionI = 1;

            } 
            if (key == 65){ // A
                playerDirectionX = -1;
                bplayer.directionI = -1;
            }
            if (key == 87 || key == 32){ // W or sapce
                playerDirectionY = -1;
                bplayer.directionJ = -1;

            }
            if (key == 83){ // S
                playerDirectionY = 1;
                bplayer.directionJ = 1;
            }


            // debug // move viewport
            if (key == 72) // h
                grid.moveViewRightOne();
            if (key == 70) // f
                grid.moveViewLeftOne();
            if (key == 84) // t
                grid.moveViewUpOne();
            if (key == 71) // g
                grid.moveViewDownOne();;

            // control
            if (key == 17) CTRL_PRESSED = true;

            // toggle sidebar
            if (CTRL_PRESSED && key == 66) { // ctrl + b
                // sidebar about to be closed, save pos to variable
                if (showSidebar)  {
                    sidebarPosition = sidebarWindow.getLocationOnScreen();
                }

                showSidebar = !showSidebar;
                sidebarWindow.setVisible(showSidebar); 

                // if sidebar is visible set it to saved size
                if (showSidebar && sidebarPosition != null) {
                    // NOTE: I HAVE NO CLUE WHY THE FUCK THIS WORKS
                    // if i set one var at the time its happy, but if i start by putting
                    // both it doesnt work and sets it to the center
                    // whatever, i dont care, its enough that it works
                    sidebarWindow.setLocation(sidebarPosition.x, 100);
                    sidebarWindow.setLocation(sidebarPosition.x, sidebarPosition.y);
                }
            }


            // get ovverriden every input, we dont care we are not yandere dev we can, gls amio
            currentSelectedParticleColor = particleList.getColorOfParticle(currentSelectedParticle);

        }

        @Override
        public void keyReleased(KeyEvent e){
            int key = e.getKeyCode();
            if (key == 68){
                playerDirectionX = 0;
                bplayer.directionI = 0;
            } else if (key == 65){
                playerDirectionX = 0;
                bplayer.directionI = 0;
            }
            if (key == 87 || key == 32){
                playerDirectionY = 1;
                bplayer.directionJ = 0;
            } else if (key == 83){
                playerDirectionY = 1;
                bplayer.directionJ = 0;
            }

            if (key == 67) // c
                toDrawChunks = !toDrawChunks;
            // if (key == 80) // p
            //     grid.print();
            if (key == 79) { // o
                // grid.saveChunkToDisk(new int[]{0, 1});
                // grid.saveChunkRowToDisk(0);
                grid.saveGridtoDisk();
            }
            if (key == 73) // i
                // grid.loadChunkFromDisk(new int[]{0, 1});
                // grid.loadChunkRowFromDisk(0);
                grid.loadGridFromDisk();

            if (key == 96) { // numpad 0
                Debug.toggleDebugInfo();
            }
            if (key == 97) { // numpad 1
                Debug.toggleWarnings();
            }
            if (key == 98) { // numpad 2
                Debug.toggleErrors();
            }

            if (key == 17) CTRL_PRESSED = false;

        }

    }

}
