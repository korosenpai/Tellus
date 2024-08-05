package Sidebar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import MusicPlayer.MusicPlayer;

public class SidebarPanel extends JPanel implements ActionListener {
    private enum Elements {
        PICKAXE,
        SAND,
        SNOW,
        WOOD,
        WATER,
        GRAVEL,
        FIRE,
        STONE
    };

    private final int VOFFSET = 50; // vertical offset
    private final int HOFFSET = 30; // horizontal offset
    private final int ICON_DISTANCE = 100; // distance from each button
    private final int ICON_SIZE = 96; // TODO: maybe pump up to 128?
    private final int WIDTH;
    private final int HEIGHT;

    private final String SIDEBAR_IMG_ASSET_DIR = "src/main/assets/images/sidebar/";

    private String selectedElement;
    private myBtn[] buttons;

    public SidebarPanel() {
        this.WIDTH = ICON_DISTANCE * 2 + HOFFSET * 2 - 5; // -5 bc numbers dont math and it look better
        this.HEIGHT = ICON_DISTANCE * 4 + VOFFSET * 2;

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // set window size
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // all drawing from this component will be done in an offscreen painting buffer -> improves performance
        setLayout(null);

        myBtn pickaxeBtn = new myBtn(ICON_SIZE, Elements.PICKAXE.name(), HOFFSET, VOFFSET, SIDEBAR_IMG_ASSET_DIR + "pickaxe");
        pickaxeBtn.addActionListener(this);
        add(pickaxeBtn);

        myBtn sandBtn = new myBtn(ICON_SIZE, Elements.SAND.name(), HOFFSET + ICON_DISTANCE, VOFFSET, SIDEBAR_IMG_ASSET_DIR + "sand");
        sandBtn.addActionListener(this);
        add(sandBtn);

        myBtn snowBtn = new myBtn(ICON_SIZE, Elements.SNOW.name(), HOFFSET, VOFFSET + ICON_DISTANCE, SIDEBAR_IMG_ASSET_DIR + "snow");
        snowBtn.addActionListener(this);
        add(snowBtn);

        myBtn woodBtn = new myBtn(ICON_SIZE, Elements.WOOD.name(), HOFFSET + ICON_DISTANCE, VOFFSET + ICON_DISTANCE, SIDEBAR_IMG_ASSET_DIR + "wood");
        woodBtn.addActionListener(this);
        add(woodBtn);

        myBtn waterBtn = new myBtn(ICON_SIZE, Elements.WATER.name(), HOFFSET, VOFFSET + ICON_DISTANCE * 2, SIDEBAR_IMG_ASSET_DIR + "water");
        waterBtn.addActionListener(this);
        add(waterBtn);

        myBtn gravelBtn = new myBtn(ICON_SIZE, Elements.GRAVEL.name(), HOFFSET + ICON_DISTANCE, VOFFSET + ICON_DISTANCE * 2, SIDEBAR_IMG_ASSET_DIR + "gravel");
        gravelBtn.addActionListener(this);
        add(gravelBtn);

        myBtn fireBtn = new myBtn(ICON_SIZE, Elements.FIRE.name(), HOFFSET, VOFFSET + ICON_DISTANCE * 3, SIDEBAR_IMG_ASSET_DIR + "fire");
        fireBtn.addActionListener(this);
        add(fireBtn);

        myBtn stoneBtn = new myBtn(ICON_SIZE, Elements.STONE.name(), HOFFSET + ICON_DISTANCE, VOFFSET + ICON_DISTANCE * 3, SIDEBAR_IMG_ASSET_DIR + "stone");
        stoneBtn.addActionListener(this);
        add(stoneBtn);

        buttons = new myBtn[]{pickaxeBtn, sandBtn, snowBtn, woodBtn, waterBtn, gravelBtn, fireBtn, stoneBtn };
    }

    public void actionPerformed(ActionEvent e) {
        String elem = e.getActionCommand();


        if (elem != selectedElement) {
            selectedElement = elem;
        }
        else {
            // if same element is pressed, deselect it
            // so you can manually change elements with the numbers
            selectedElement = null;
        }

        // iterate through the buttons and show icon of pressed only for the selected element
        for (myBtn btn: buttons) {
            if (btn.ELEMENT == selectedElement) btn.setPressed();
            else btn.setReleased();
        }
    }

    public boolean isElementSelected() {
        return selectedElement != null;
    }
    public int getSelectedElementID() {
        // same of particleList
        switch (selectedElement) {
            case "PICKAXE":
                return -1;

            case "SAND":
                return 1;

            case "SNOW":
                return 2;

            case "WOOD":
                return 3;

            case "WATER":
                return 4;

            case "GRAVEL":
                return 5;

            case "FIRE":
                return 8;

            case "STONE":
                return 9;

            default:
                return 1;
        }

    }

}

class myBtn extends JButton {
    public final String ELEMENT;
    private final String ICONPATH;
    public boolean isIconPressed; // if icon is showin pressed

    public myBtn(int size, String elem, int x, int y, String iconPath) {
        ELEMENT = elem;
        ICONPATH = iconPath;

        setReleased();
        setBounds(x, y, size, size);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setContentAreaFilled(false);

        setActionCommand(ELEMENT);

        // addActionListener(new ActionListener() {
        //     @Override
        //     public void actionPerformed(ActionEvent e) {

        //         // do not play sound when releasing
        //         // condition is reversed bc it first runs action listener in sidebar,
        //         // so by the time this line arrives the condition has already switched
        //         // if (isIconPressed)
        //         //     MusicPlayer.playFile("sidebar/button-press.wav");

        //         // TODO: mayube also play something for an element
        //         // like selecting fire also starts fire sound

        //     }

        // });
    }

    public void setPressed() {
        MusicPlayer.playFile("button-press.wav");
        setIcon(new ImageIcon(ICONPATH + "-pressed.png"));
        isIconPressed = true;
    }
    public void setReleased() {
        if (isIconPressed) MusicPlayer.playFile("button-released.wav");

        setIcon(new ImageIcon(ICONPATH + ".png"));
        isIconPressed = false;
    }

}

