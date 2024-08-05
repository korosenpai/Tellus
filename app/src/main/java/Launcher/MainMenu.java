package Launcher;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Utils.Constants;
import Utils.Debug;

import java.util.Timer;
import java.util.TimerTask;

import MusicPlayer.MusicPlayer;
import SRandom.SRandom;
import Window.Window;

public class MainMenu extends JPanel implements ActionListener {

    // TODO: scale everything to make it bigger!!!
    private static JFrame menuFrame;
    private final int MENU_WIDTH = 800;
    private final int MENU_HEIGHT = 600;

    int WIDTH;
    int HEIGHT;
    final int GRID_OFFSET = 2;

    Font MINECRAFT_FONT;

    // menu element resolutions
    final int logoWidth = 666;
    final int logoHeight = 116;

    final int playButtonWidth = 192;
    final int playbuttonHeight = 96;

    // modifiers
    // ValueModifier seedSizeModifier;
    // ValueModifier colsModifier;
    // ValueModifier fpsModifier;
    // ValueModifier tileSizeModifier;

    // modifiable in the menu
    private MutableInteger SEED = new MutableInteger(42, 1);
    private MutableInteger COLS = new MutableInteger(8, 1);
    private MutableInteger ROWS = new MutableInteger(6, 1);

    private MutableInteger FPS = new MutableInteger(30, 15);
    private MutableInteger CHUNK_SIZE = new MutableInteger(32, 8);
    private MutableInteger TILE_SIZE = new MutableInteger(5, 1);



    Button playButton;


    public void calculateResolution() {
        WIDTH = CHUNK_SIZE.get() * TILE_SIZE.get() * COLS.get(); 
        HEIGHT = CHUNK_SIZE.get() * TILE_SIZE.get() * ROWS.get();
    }

    // given width of element get coord to center it
    public int centerElemOnX(int width) {
        return (MENU_WIDTH - width) / 2;
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

        // font
        try {
            MINECRAFT_FONT = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/assets/fonts/Minecraft.ttf")).deriveFont(30f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(MINECRAFT_FONT);
        }
        catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        // load title
        JLabel logo = new JLabel();
        logo.setIcon(new ImageIcon(Constants.IMAGE_ASSET_DIR + "menu/logo.png"));
        logo.setBounds(centerElemOnX(logoWidth), 40, logoWidth, logoHeight);
        add(logo);

        // modifiers
        int leftColTitleX = 50;
        // tileSizeModifier = new ValueModifier(leftColTitleX, "tile-size.png", 120 + leftColTitleX, 210, TILE_SIZE, this);
        // seedSizeModifier = new ValueModifier(leftColTitleX, "seed.png", 120 + leftColTitleX, 280, SEED, this);
        // colsModifier = new ValueModifier(leftColTitleX, "cols.png", 120 + leftColTitleX, 350, COLS, this);
        ValueModifier seedSizeModifier = new ValueModifier(leftColTitleX, "seed.png", 120 + leftColTitleX, 210, SEED, this);
        ValueModifier colsModifier = new ValueModifier(leftColTitleX, "cols.png", 120 + leftColTitleX, 280, COLS, this);
        ValueModifier rowsModifier = new ValueModifier(leftColTitleX, "rows.png", 120 + leftColTitleX, 350, COLS, this);

        int rightColTitleX = 630;
        ValueModifier fpsModifier = new ValueModifier(rightColTitleX, "fps.png", MENU_WIDTH / 2, 210, FPS, this);
        ValueModifier chunkSizeModifier = new ValueModifier(rightColTitleX, "chunk-size.png", MENU_WIDTH / 2, 280, CHUNK_SIZE, this);
        ValueModifier tileSizeModifier = new ValueModifier(rightColTitleX, "tile-size.png", MENU_WIDTH / 2, 350, TILE_SIZE, this);

        // load play button
        playButton = new Button(centerElemOnX(playButtonWidth), MENU_HEIGHT - playbuttonHeight - 40, playButtonWidth, playbuttonHeight, "menu/play-btn");
        playButton.addActionListener(this);
        add(playButton);

        // footer to thank
        JLabel madeWithLove = new JLabel();
        madeWithLove.setIcon(new ImageIcon(Constants.IMAGE_ASSET_DIR + "menu/made-with-love.png"));
        madeWithLove.setBounds(MENU_WIDTH - 192 - 50, MENU_HEIGHT - 48 - 50, 192, 48);
        add(madeWithLove);


        MusicPlayer.loadAll();

    }


    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == playButton) {
            Debug.system("launching Tellus engine...");

            // NOTE: button starts to raise when window fades away, perfect effect
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startGame();
                    menuFrame.dispose(); // close main menu
                }
            }, 0);

        }

    }

    public void startGame() {
        calculateResolution();

        SRandom.setSeed(SEED.get());

        JFrame menuFrame = new JFrame();
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setResizable(false);
        menuFrame.setTitle("Tellus");

        Debug.system("SEED: " + SEED.get());
        Debug.system("WIDTH: " + WIDTH);
        Debug.system("HEIGHT: " + HEIGHT);
        Debug.system("CHUNK SIZE: " + CHUNK_SIZE.get());
        Debug.system("TILE SIZE: " + TILE_SIZE.get());
        Debug.system("FPS: " + FPS.get());

        Window window = new Window(WIDTH, HEIGHT, CHUNK_SIZE.get(), GRID_OFFSET, TILE_SIZE.get(), FPS.get());
        menuFrame.add(window);
        menuFrame.pack(); // resize window to fit preferred size (specified in gamepanel)

        menuFrame.setLocationRelativeTo(null); // specify location of the window // null -> display at center of menuFrame
        menuFrame.setVisible(true); 

        window.start();
    }

}

class MutableInteger {
    private int val;
    private int step;

    // step, how much to incr or decr
    public MutableInteger(int val, int step) {
        this.val = val;
        this.step = step;
    }
    public void incr() {
        val += step;
    }
    public void decr() {
        if (val - step < 0) return;

        val -= step;
    }
    public int get() {
        return val;
    }
}


class Button extends JButton {
    public String ICON;
    public boolean isPressed;
    public int milliesTillReleased = 300;

    public Button(int x, int y, int width, int height, String icon) {
        this.ICON = icon;
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setContentAreaFilled(false);

        setBounds(x, y, width, height);

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
        MusicPlayer.playFile("button-press.wav");
        setIcon(new ImageIcon( Constants.IMAGE_ASSET_DIR + ICON + Constants.BTN_PRESSED_EXT));
        isPressed = true;
    }

    public void setReleased() {
        if (isPressed) MusicPlayer.playFile("button-released.wav");
        setIcon(new ImageIcon(Constants.IMAGE_ASSET_DIR + ICON + Constants.IMG_EXT));
        isPressed = false;
    }
}

// increment and decrement button, with label to write on value
class ValueModifier {
    private Button incr;
    private Button decr;
    private final int ICON_SIZE = 64;
    private final int LABEL_SIZE_WIDTH = 96;

    private NumberLabel numberLabel;

    // value is a single item array because it needs to be passed as reference
    public ValueModifier(int titleSizeX, String titleFileName, int topLeftX, int topLeftY, MutableInteger val, MainMenu menu) {

        JLabel tileSizeLabel = new JLabel();
        tileSizeLabel.setIcon(new ImageIcon(Constants.IMAGE_ASSET_DIR + "menu/labels/" + titleFileName));
        tileSizeLabel.setBounds(titleSizeX, topLeftY + 16, 112, 32);
        menu.add(tileSizeLabel);

        incr = new Button(topLeftX, topLeftY, ICON_SIZE, ICON_SIZE, "menu/increment-btn");
        menu.add(incr);
        incr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                val.incr();
                numberLabel.render(val.get());
            }
        });

        numberLabel = new NumberLabel(topLeftX + ICON_SIZE, topLeftY, val.get(), menu);

        JLabel numLabelPlaque = new JLabel();
        numLabelPlaque.setIcon(new ImageIcon(Constants.IMAGE_ASSET_DIR + "menu/number-label.png"));
        numLabelPlaque.setBounds(topLeftX + ICON_SIZE, topLeftY, LABEL_SIZE_WIDTH, ICON_SIZE);
        menu.add(numLabelPlaque);


        decr = new Button(topLeftX + ICON_SIZE + LABEL_SIZE_WIDTH, topLeftY, ICON_SIZE, ICON_SIZE, "menu/decrement-btn");
        menu.add(decr);
        decr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                val.decr();
                numberLabel.render(val.get());
            }
        });
    }
}

// compose three images to create a label of a number
class NumberLabel {

    // max of three figures supported - 000..999
    private JLabel[] figures = new JLabel[3];

    private String num;

    // res of each image
    private final int WIDTH = 20;
    private final int HEIGHT = 32;

    public NumberLabel(int topLeftX, int topLeftY, int number, MainMenu menu) {

        set(number);

        for (int i = 0; i < figures.length; i++) {
            figures[i] = new JLabel();
            figures[i].setIcon(new ImageIcon(Constants.IMAGE_ASSET_DIR + "menu/nums/" + this.num.charAt(i) + ".png"));
            figures[i].setBounds(topLeftX + WIDTH * i + 15, topLeftY + 14, WIDTH, HEIGHT); // digits are to center it better
            menu.add(figures[i]);
            System.out.println(Constants.IMAGE_ASSET_DIR + "menu/nums/" + this.num.charAt(i) + ".png");
        }


    }

    // number should be same size of figures array
    public String formatNumber(String num) {
        if (num.length() > figures.length)
            // truncate start of number if it is too big
            return num.substring(num.length() - figures.length, figures.length + 1);

        return String.format("%0" + figures.length + "d", Integer.parseInt(num)); // "%03d"
    }

    public void set(int n) {
        this.num = formatNumber(String.valueOf(n));
    }

    // change labels based on new value
    public void render(int newVal) {
        set(newVal);

        for (int i = 0; i < figures.length; i++) {
            figures[i].setIcon(new ImageIcon(Constants.IMAGE_ASSET_DIR + "menu/nums/" + this.num.charAt(i) + ".png"));
        }
    }

}
