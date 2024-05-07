package Main;

import javax.swing.JFrame;

import Window.Window;

public class Main {
    static private JFrame screen;
    static private Window window;

    static final int WIDTH = 800;
    static final int HEIGHT = 800;
    static final int TILE_SIZE = 20;

    static int FPS = 30;

    static public void setupAndStartWindow() {
        screen = new JFrame();
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setResizable(false);
        screen.setTitle("rock-paper-scissors");

        window = new Window(WIDTH, HEIGHT, TILE_SIZE, FPS);
        screen.add(window);
        screen.pack(); // resize window to fit preferred size (specified in gamepanel)

        screen.setLocationRelativeTo(null); // specify location of the window // null -> display at center of screen
        screen.setVisible(true); 

        window.start();
    }

    public static void main(String[] args) {
        setupAndStartWindow();

    }

}
