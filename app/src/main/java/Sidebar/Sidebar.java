package Sidebar;

public class Sidebar {

    private final int OFFSET; // how much the sidebar is displayed right (screenwidth distance from 0)
    private final int WIDTH;

    Sidebar(int offset, int width) {

        OFFSET = offset; // will usually be screenWidth
        WIDTH = width;
    }
}
