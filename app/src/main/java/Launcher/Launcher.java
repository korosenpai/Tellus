package Launcher;

import javax.swing.JFrame;

import Blocks.Particle;
import Blocks.Solids.DynamicSolid.Sand;
import FileHandler.FileHandler;
// import MusicPlayer.MusicPlayer;
import SRandom.SRandom;
import Window.Window;

public class Launcher {
    static private JFrame screen;
    static private Window window;

    static final int SEED = 42; // TODO: all random is determined with this seed
    

    // NOTE: leave them different to debug
    // static final int TILE_SIZE = 5;
    // static final int CHUNK_SIZE = 32;
    // static final int WIDTH = CHUNK_SIZE * TILE_SIZE * 8; // 1.280
    // static final int HEIGHT = CHUNK_SIZE * TILE_SIZE * 6; // 960
    static final int GRID_OFFSET = 2; // how many chunk to load more ON ONE SIDE

    static final int SIDEBAR_WIDTH = 0; //(int)(WIDTH * .3);
    // static final int SIDEBAR_WIDTH = (int)(WIDTH * .3);

    static final int TILE_SIZE = 20;
    static final int CHUNK_SIZE = 5;
    static final int WIDTH = CHUNK_SIZE * TILE_SIZE * 3; // 1.280
    static final int HEIGHT = CHUNK_SIZE * TILE_SIZE * 2; // 960

    static int FPS = 30;

    public static void main(String[] args) {
        SRandom.setSeed(SEED);
        // System.out.println("seed: " + SRandom.getSeed());

        screen = new JFrame();
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setResizable(false);
        screen.setTitle("Tellus");

        window = new Window(WIDTH, HEIGHT, CHUNK_SIZE, GRID_OFFSET,  SIDEBAR_WIDTH, TILE_SIZE, FPS);
        screen.add(window);
        screen.pack(); // resize window to fit preferred size (specified in gamepanel)

        screen.setLocationRelativeTo(null); // specify location of the window // null -> display at center of screen
        screen.setVisible(true); 
        
        // MusicPlayer player = new MusicPlayer();
        // player.playFile("src/main/assets/audio/blocks/landed.wav");

        window.start();

    }

}
