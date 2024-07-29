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

    private final int WIDTH;
    private final int HEIGHT;

    private final String SIDEBAR_ASSET_DIR = "src/main/assets/sidebar/";

    private String selectedElement;
    private myBtn[] buttons;

    public SidebarPanel() {
        this.WIDTH = 250;
        this.HEIGHT = 500;

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // set window size
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // all drawing from this component will be done in an offscreen painting buffer -> improves performance
        setLayout(null);

        myBtn pickaxeBtn = new myBtn(Elements.PICKAXE.name(), 30, 50, SIDEBAR_ASSET_DIR + "pickaxe");
        pickaxeBtn.addActionListener(this);
        add(pickaxeBtn);

        myBtn sandBtn = new myBtn(Elements.SAND.name(), 130, 50, SIDEBAR_ASSET_DIR + "sand");
        sandBtn.addActionListener(this);
        add(sandBtn);

        myBtn snowBtn = new myBtn(Elements.SNOW.name(), 30, 150, SIDEBAR_ASSET_DIR + "snow");
        snowBtn.addActionListener(this);
        add(snowBtn);

        myBtn woodBtn = new myBtn(Elements.WOOD.name(), 130, 150, SIDEBAR_ASSET_DIR + "wood");
        woodBtn.addActionListener(this);
        add(woodBtn);

        myBtn waterBtn = new myBtn(Elements.WATER.name(), 30, 250, SIDEBAR_ASSET_DIR + "water");
        waterBtn.addActionListener(this);
        add(waterBtn);

        myBtn gravelBtn = new myBtn(Elements.GRAVEL.name(), 130, 250, SIDEBAR_ASSET_DIR + "gravel");
        gravelBtn.addActionListener(this);
        add(gravelBtn);

        myBtn fireBtn = new myBtn(Elements.FIRE.name(), 30, 350, SIDEBAR_ASSET_DIR + "fire");
        fireBtn.addActionListener(this);
        add(fireBtn);

        myBtn stoneBtn = new myBtn(Elements.STONE.name(), 130, 350, SIDEBAR_ASSET_DIR + "stone");
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
            else {
                if (btn.isIconPressed) MusicPlayer.playFile("sidebar/button-released.wav");

                btn.setReleased();
            }
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
    private final int SIZE = 96;
    public final String ELEMENT;
    private final String ICONPATH;
    public boolean isIconPressed; // if icon is showin pressed

    public myBtn(String elem, int x, int y, String iconPath) {
        ELEMENT = elem;
        ICONPATH = iconPath;
        
        setReleased();
        setBounds(x, y, SIZE, SIZE);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setContentAreaFilled(false);

        setActionCommand(ELEMENT);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // do not play sound when releasing
                if (isIconPressed)
                    MusicPlayer.playFile("sidebar/button-press.wav");

                // TODO: mayube also play something for an element
                // like selecting fire also starts fire sound

            }

        });
    }

    public void setPressed() {
        setIcon(new ImageIcon(ICONPATH + "-pressed.png"));
        isIconPressed = true;
    }
    public void setReleased() {
        setIcon(new ImageIcon(ICONPATH + ".png"));
        isIconPressed = false;
    }

}

