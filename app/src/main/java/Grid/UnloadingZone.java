package Grid;

import java.util.Arrays;

import Blocks.Particle;

public class UnloadingZone {

    private final int CHUNK_SIZE;

    private Particle[][] UZ_UP; // gets up corners
    private int UZ_UP_INDEX; // where to put next element

    private Particle[][] UZ_DOWN; // gets down corners
    private Particle[][] UZ_LEFT;
    private Particle[][] UZ_RIGHT;

    public UnloadingZone(int chunkSize, int rows, int cols) {
        CHUNK_SIZE = chunkSize;

        UZ_UP = new Particle[CHUNK_SIZE][CHUNK_SIZE * rows];
        UZ_DOWN = new Particle[CHUNK_SIZE][CHUNK_SIZE];
        UZ_LEFT = new Particle[CHUNK_SIZE][CHUNK_SIZE];
        UZ_RIGHT = new Particle[CHUNK_SIZE][CHUNK_SIZE];

    }

    public void saveToUZ(Particle[][] UZDirection, int UZIndex, Particle[] fromGrid) {
        for (int j = 0; j < fromGrid.length; j++) {
            UZDirection[UZIndex][j] = fromGrid[j].clone();
        }
        System.out.println(Arrays.toString(UZDirection));

        UZIndex++;
    }

    // save UZ_* to disk if it gets full and reset it, or save it back to the grid if it gets emptier
    public void shiftUp(Grid grid, int moveDistance) {
        // System.out.println("shift up");
        grid.shiftGridUp(moveDistance);
        for (int j = 0; j < moveDistance; j++) {
            // add to uzUP
            saveToUZ(UZ_UP, UZ_UP_INDEX, grid.getRow(grid.getRows() - 1 - j));

            grid.clearRow(grid.getRows() - 1 - j);

        }

    }
    public void shiftDown(Grid grid, int moveDistance) {
        // System.out.println("shift down");
        grid.shiftGridDown(moveDistance);
        for (int j = 0; j < moveDistance; j++)
            grid.clearRow(j);
    }
    public void shiftLeft(Grid grid, int moveDistance) {
        // System.out.println("shift left");
        /*
         * grid is rotated, so we have to shift everything up
         */
        grid.shiftGridLeft(moveDistance);
        for (int j = 0; j < moveDistance; j++)
            grid.clearColumn(grid.getColumns() - 1 - j);
    }
    public void shiftRight(Grid grid, int moveDistance) {
        // System.out.println("shift right");
        grid.shiftGridRight(moveDistance);
        for (int j = 0; j < moveDistance; j++)
            grid.clearColumn(j);
    }
}
