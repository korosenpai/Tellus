package Launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.Timer;
import java.util.TimerTask;

import MusicPlayer.MusicPlayer;
import SRandom.SRandom;
import Window.Window;

public class MainMenu extends JPanel implements ActionListener {

    static JFrame menuFrame;
    static private final int MENU_WIDTH = 800;
    static private final int MENU_HEIGHT = 600;

    static int WIDTH;
    static int HEIGHT;
    static final int GRID_OFFSET = 2;

    static private final int FPS = 30;

    // modifiable in the menu
    static private int SEED = 42;
    static private int TILE_SIZE = 5;
    static private int CHUNK_SIZE = 32;
    static private int COLS = 8;
    static private int ROWS = 6;

    PlayButton playButton;


    public static void calculateResolution() {
        WIDTH = CHUNK_SIZE * TILE_SIZE * COLS; 
        HEIGHT = CHUNK_SIZE * TILE_SIZE * ROWS;
    }

    public static void main(String[] args) {
        menuFrame = new JFrame();
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setResizable(false);
        menuFrame.setTitle("Main Menu");

        MainMenu menu = new MainMenu();
        menuFrame.add(menu);
        menuFrame.pack();

        menuFrame.setLocationRelativeTo(null);
        menuFrame.setVisible(true);

    }

    public MainMenu() {
        this.setPreferredSize(new Dimension(MENU_WIDTH, MENU_HEIGHT));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        setLayout(null);

        this.setFocusable(true); //for using keyAdapter
        this.requestFocusInWindow();

        playButton = new PlayButton();
        playButton.addActionListener(this);
        add(playButton);

    }


    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == playButton) {
            // startgame
            System.out.println("start game");

            // NOTE: to be removed later and start immediately maybe
            // but for now this is proof of concept
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startGame();
                    menuFrame.dispose(); // close main menu
                }
            }, playButton.milliesTillReleased + 100);

        }

    }

    public void startGame() {
        calculateResolution();

        SRandom.setSeed(SEED);

        JFrame menuFrame = new JFrame();
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setResizable(false);
        menuFrame.setTitle("Tellus");

        Window window = new Window(WIDTH, HEIGHT, CHUNK_SIZE, GRID_OFFSET, TILE_SIZE, FPS);
        menuFrame.add(window);
        menuFrame.pack(); // resize window to fit preferred size (specified in gamepanel)

        menuFrame.setLocationRelativeTo(null); // specify location of the window // null -> display at center of menuFrame
        menuFrame.setVisible(true); 

        MusicPlayer.loadAll();

        window.start();
    }

}

class PlayButton extends JButton {
    public boolean isPressed;
    public int milliesTillReleased = 300;

    public PlayButton() {
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setContentAreaFilled(false);

        setBounds(100, 400, 192, 96);

        setReleased();

        // wait for a bit then release
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setPressed();

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        setReleased();
                    }
                }, milliesTillReleased);

            }
        });
    }

    public void setPressed() {
        MusicPlayer.playFile("sidebar/button-press.wav");
        setIcon(new ImageIcon("src/main/assets/images/menu/play-btn-pressed.png"));
        isPressed = true;
    }

    public void setReleased() {
        if (isPressed) MusicPlayer.playFile("sidebar/button-released.wav");
        setIcon(new ImageIcon("src/main/assets/images/menu/play-btn.png"));
        isPressed = false;
    }
}
