package Sidebar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

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

    private String selectedElement;

    private final String SIDEBAR_ASSET_DIR = "src/main/assets/sidebar/";

    public SidebarPanel() {
        this.WIDTH = 250;
        this.HEIGHT = 500;

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // set window size
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // all drawing from this component will be done in an offscreen painting buffer -> improves performance
        setLayout(null);

        myBtn pickaxeBtn = new myBtn(Elements.PICKAXE.name(), 30, 50, new ImageIcon(SIDEBAR_ASSET_DIR + "pickaxe.png"));
        pickaxeBtn.addActionListener(this);
        add(pickaxeBtn);

        myBtn sandBtn = new myBtn(Elements.SAND.name(), 130, 50, new ImageIcon(SIDEBAR_ASSET_DIR + "sand.png"));
        sandBtn.addActionListener(this);
        add(sandBtn);

        myBtn snowBtn = new myBtn(Elements.SNOW.name(), 30, 150, new ImageIcon(SIDEBAR_ASSET_DIR + "snow.png"));
        snowBtn.addActionListener(this);
        add(snowBtn);

        myBtn woodBtn = new myBtn(Elements.WOOD.name(), 130, 150, new ImageIcon(SIDEBAR_ASSET_DIR + "wood.png"));
        woodBtn.addActionListener(this);
        add(woodBtn);

        myBtn waterBtn = new myBtn(Elements.WATER.name(), 30, 250, new ImageIcon(SIDEBAR_ASSET_DIR + "water.png"));
        waterBtn.addActionListener(this);
        add(waterBtn);

        myBtn gravelBtn = new myBtn(Elements.GRAVEL.name(), 130, 250, new ImageIcon(SIDEBAR_ASSET_DIR + "gravel.png"));
        gravelBtn.addActionListener(this);
        add(gravelBtn);

        myBtn fireBtn = new myBtn(Elements.FIRE.name(), 30, 350, new ImageIcon(SIDEBAR_ASSET_DIR + "fire.png"));
        fireBtn.addActionListener(this);
        add(fireBtn);

        myBtn stoneBtn = new myBtn(Elements.STONE.name(), 130, 350, new ImageIcon(SIDEBAR_ASSET_DIR + "stone.png"));
        stoneBtn.addActionListener(this);
        add(stoneBtn);
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

        // System.out.println(selectedElement);
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

    public myBtn(String elem, int x, int y, ImageIcon icon) {
        ELEMENT = elem;

        setIcon(icon);
        setBounds(x, y, SIZE, SIZE);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        setContentAreaFilled(false);

        setActionCommand(ELEMENT);
    }

    public void setPressed() {

    }
    public void setReleased() {

    }

}

