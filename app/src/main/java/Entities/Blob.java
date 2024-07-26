package Entities;

import java.util.Arrays;

import Blocks.Air;
import Blocks.Solids.SolidParticle;
import Debug.Debug;
import Grid.Grid;

// player that moves with cellular automata behaviour
public class Blob {

    private final int N_CELLS = 15;
    private BlobParticle[] blobs;
    private int[][] positions; // array of vec2

    // input of direction
    // will be reset at end of update
    public int directionJ;
    public int directionI;

    // originJ/I are top left coords of to be rectangle player
    public Blob(Grid grid, int originJ, int originI) {
        blobs = new BlobParticle[N_CELLS];
        for (int b = 0; b < blobs.length; b++) { // populate blobs array
            blobs[b] = new BlobParticle();
        }

        positions = new int[N_CELLS][2];
        // initiate as rectangle
        int arr_idx = 0;
        int width = 3;
        int height = 5;

        if (width * height != N_CELLS) Debug.error("WRONG NUMBER OF CELLS TO BE CREATED"); 

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                positions[arr_idx] = new int[]{originJ + j, originI + i};
                arr_idx++;
            }
        }

        for (int[] pos: positions)
            System.out.println(Arrays.toString(pos));

        setInGrid(grid);
    }

    // add all blobs to grid
    private void setInGrid(Grid grid) {
        for (int idx = 0; idx < blobs.length; idx++) {
            int[] coords = positions[idx];
            grid.grid[coords[0]][coords[1]] = blobs[idx];
        }
    }

    private void removeFromGrid(Grid grid) {
        for (int idx = 0; idx < blobs.length; idx++) {
            int[] coords = positions[idx];
            grid.grid[coords[0]][coords[1]] = new Air();
        }
    }

    private boolean availablePosition(Grid grid, int j, int i) {
        return grid.getAtPosition(j, i) instanceof SolidParticle;
    }

    // returns vector of where all the particles moved
    private int[] move(Grid grid) {
        int[] deltaVec = { 0 , 0 };


        for (int[] pos: positions) {

            // try and move to the left or right
            if (Math.abs(directionI) == 1) {
                // if even only one particle cant move dont move anything
                if (availablePosition(grid, pos[0], pos[1] + directionI)) {
                    directionI = 0;
                    break;
                }
            }

            // try and move up or down
            if (Math.abs(directionJ) == 1) {
                // if even only one particle cant move dont move anything
                if (availablePosition(grid, pos[0] + directionJ, pos[1])) {
                    directionJ = 0;
                    break;
                }
            }
        }

        deltaVec[0] = directionJ;
        deltaVec[1] = directionI;

        return deltaVec;
    }

    public void update(Grid grid) {
        removeFromGrid(grid);

        int[] delta = move(grid);

        // move all particles
        for (int[] pos: positions) {
            pos[0] += delta[0];
            pos[1] += delta[1];

        }

        ///////// MOVE VIEW

        // move up
        if (delta[0] == -1) {

            if (grid.moveViewUpOne()) {
                for (int[] pos: positions) {
                    pos[0] += grid.CHUNK_SIZE;
                }
            };
        }

        // move down
        if (delta[0] == 1) {

            if (grid.moveViewDownOne()) {
                for (int[] pos: positions) {
                    pos[0] -= grid.CHUNK_SIZE;
                }
            };
        }

        // move left
        if (delta[1] == -1) {

            if (grid.moveViewLeftOne()) {
                for (int[] pos: positions) {
                    pos[1] += grid.CHUNK_SIZE;
                }
            };
        }

        // move right
        if (delta[1] == 1) {

            if (grid.moveViewRightOne()) {
                for (int[] pos: positions) {
                    pos[1] -= grid.CHUNK_SIZE;
                }
            };
        }
        /////////


        setInGrid(grid);

    }


}

