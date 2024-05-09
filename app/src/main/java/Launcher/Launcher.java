package Launcher;

import javax.swing.JFrame;

import Window.Window;

public class Launcher {
    static private JFrame screen;
    static private Window window;

    // NOTE: leave them different to debug
    static final int WIDTH = 1000;
    static final int HEIGHT = 800;
    static final int TILE_SIZE = 20;

    static int FPS = 30;

    public static void main(String[] args) {
        screen = new JFrame();
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setResizable(false);
        screen.setTitle("Tellus");

        window = new Window(WIDTH, HEIGHT, TILE_SIZE, FPS);
        screen.add(window);
        screen.pack(); // resize window to fit preferred size (specified in gamepanel)

        screen.setLocationRelativeTo(null); // specify location of the window // null -> display at center of screen
        screen.setVisible(true); 

        window.start();

    }

}
