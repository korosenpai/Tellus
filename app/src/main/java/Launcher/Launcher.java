package Launcher;

import javax.swing.JFrame;

import MusicPlayer.MusicPlayer;
import SRandom.SRandom;
import Window.Window;

// directly launch tellus without opening menu, must remain like this also cuz its fastter to use when debugging
public class Launcher {
    static private JFrame screen;
    static private Window window;

    static final int SEED = 42; // NOTE: all random is determined with this seed
    

    // NOTE: leave them different to debug
    static final int TILE_SIZE = 5;
    static final int CHUNK_SIZE = 32;
    static final int COLS = 8;
    static final int ROWS = 6;

    static final int WIDTH = CHUNK_SIZE * TILE_SIZE * COLS; // 1.280
    static final int HEIGHT = CHUNK_SIZE * TILE_SIZE * ROWS; // 960
    static final int GRID_OFFSET = 2; // how many chunk to load more ON ONE SIDE

    // static final int TILE_SIZE = 20;
    // static final int CHUNK_SIZE = 5;
    // static final int WIDTH = CHUNK_SIZE * TILE_SIZE * 3; // 1.280
    // static final int HEIGHT = CHUNK_SIZE * TILE_SIZE * 2; // 960

    static int FPS = 30;

    public static void main(String[] args) {
        SRandom.setSeed(SEED);

        screen = new JFrame();
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setResizable(false);
        screen.setTitle("Tellus");

        window = new Window(WIDTH, HEIGHT, CHUNK_SIZE, GRID_OFFSET, TILE_SIZE, FPS);
        screen.add(window);
        screen.pack(); // resize window to fit preferred size (specified in gamepanel)

        screen.setLocationRelativeTo(null); // specify location of the window // null -> display at center of screen
        screen.setVisible(true); 

        MusicPlayer.loadAll();

        window.start();

    }

}
