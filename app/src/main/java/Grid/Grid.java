package Grid;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import Blocks.Air;
import Blocks.Particle;
import Blocks.ParticleList;

public class Grid {
    public ParticleList particleList = new ParticleList();

    final int screenWidth;
    final int screenHeight;
    final int chunkSize;
    final int tileDimension;

    private final int rows;
    private final int columns;
    public Particle[][] grid = {{}};

    private ThreadUpdates threadUpdates;
    public Chunk[][] gridChunk;
    

    public Grid(int screenWidth, int screenHeight, int chunkSize, int tileDimension){
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.chunkSize = chunkSize;
        this.rows = screenHeight / tileDimension;
        this.columns = screenWidth / tileDimension;
        this.tileDimension = tileDimension;
        grid = new Particle[this.rows][this.columns];
        generateEmptyGrid();

        threadUpdates = new ThreadUpdates(this.columns);
    }

    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }

    public void generateEmptyGrid(){
        /*generates a 2d array of all zeros (id of empty cell)
        this is the general structure of the Grid
        WE NEED TO ITERATE FROM  BOTTOM TO TOP (L2R OR R2L DOESNT CHANGE ANYTHING) WHEN WE UPDATE
        for (int j = columns-1; j > -1; j--) {
            for (int i = rows-1; i >-1; i--) where
        
        [^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ 
         0 0 0 0 0 0 0 0 0 0 0  |
         0 0 0 0 0 0 0 0 0 0 0  |
         0 0 0 0 0 0 0 0 0 0 0  | j
         0 0 0 0 0 0 0 0 0 0 0  |
         0 0 0 0 0 0 0 0 0 0 0  v
         v v v v v v v v v v v] 
        
            ------------> 
                  i

        */


        for (int j = 0; j < this.rows; j++) {
            for (int i = 0; i < this.columns; i++) {
                grid[j][i] = new Air();
            }
        }
        
    }

    public Particle[][] getGrid() {
        return grid;
    }

    public void updateGrid() {
        // first scan, bottom to top
        for (int j = rows-1; j > -1; j--){
            for (int i = columns-1; i > -1; i--){
                if (grid[j][i] instanceof Air || grid[j][i].scanDirection != 1 || grid[j][i].hasMoved) continue;

                grid[j][i].update(new int[]{j, i}, this);
            }
        }

        // second scan, top to bottom
        for (int j = 0; j < rows; j++){
            for (int i = 0; i < columns; i++){
                if (grid[j][i] instanceof Air || grid[j][i].scanDirection != 2 || grid[j][i].hasMoved) continue;

                grid[j][i].update(new int[]{j, i}, this);
            }
        }

        // threadUpdates.update(this);
    }

    // after painting set all pixels who have moved
    //  able to make them move again
    public void setGridHasMovedFalse() {
        for (int j = rows-1; j > -1; j--){
            for (int i = columns-1; i > -1; i--){
                if (grid[j][i] instanceof Air) continue;

                grid[j][i].hasMoved = false;
            }
        }
    }


    public Particle getAtPosition(int j, int i) {
        return grid[j][i];
    }

    public void setParticle(int j, int i, Particle particle) {
        grid[j][i] = particle;
    }

    public void setCursor(int mouseX, int mouseY, int radius, int particleID) {
        int circleCentreX = mouseX / tileDimension;
        int circleCentreY = mouseY / tileDimension;
        
        // min prevents to go out of bounds
        int c0 = Math.min(circleCentreX + radius, columns - 1);
        int c180 = Math.max(circleCentreX - radius, 0);
        int c90 = Math.min(circleCentreY + radius, rows - 1);
        int c270 = Math.max(circleCentreY - radius, 0);

        // System.out.println(c0 + " : " + c180 + " : " + c90 + " : " + c270);

        // Adjust the loop conditions based on the actual number of tiles the circle spans
        for (int x = c180; x <= c0; x++) {
            for (int y = c270; y <= c90; y++) {
                if (Math.sqrt((x - circleCentreX) * (x - circleCentreX) + (y - circleCentreY) * (y - circleCentreY)) <= radius) {
                    Particle particle = particleList.getNewParticle(particleID);
                    // chance to not spawn all the blocks in the cursor
                    if (ThreadLocalRandom.current().nextFloat(1) <= particle.spawnRate) {
                        if (getAtPosition(y, x).canBeOverridden) {
                            setParticle(y, x, particleList.getNewParticle(particleID));
                        }
                    }
                }
            }
        }

    }

    // NOTE: GETNEIGHBORS FUNCTIONS
    // input: j = y; i = x
    // gives particles from left to right

    public Particle[] getNeighbors(int j, int i) {
        // concatenate in array all three methods to get neighbor from top left to bottom right
        return Stream.of(getUpperNeighbors(j, i), getSideNeighbors(j, i), getLowerNeighbors(j, i))
            .flatMap(Arrays::stream)
            .toArray(Particle[]::new);
    }

    public Particle[] getLowerNeighbors(int j, int i) {
        /* return the three lower cells of grid[i][j] 
         * if neighbor is out of bound returns null
        */
        Particle[] lowerNeighbors = new Particle[]{null, null, null};

        if (j < rows - 1) { //check if element is not in the last row
            if (i > 0) lowerNeighbors[0] = grid[j + 1][i - 1]; //bottomleft
            lowerNeighbors[1] = grid[j + 1][i]; //bottom
            if (i < columns - 1) lowerNeighbors[2] = grid[j + 1][i + 1]; //bottomright
        }
        return lowerNeighbors;
    }

    public Particle getSingleLowerNeighbor(int j, int i) {
        /* return the single lower cell of grid[i][j] 
         * if neighbor is out of bound returns null
        */
        Particle lower = new Particle() {};

        if (j < rows - 1) { //check if element is not in the last row
            lower = grid[j + 1][i]; //bottom
            }
        return lower;
    }

    public Particle[] getUpperNeighbors(int j, int i) {
        /* return the three lower cells of grid[i][j] 
         * if neighbor is out of bound returns null
        */
        Particle[] upperNeighbors = new Particle[]{null, null, null};

        if (j > 0) { //check if element is not in the last row
            if (i > 0) upperNeighbors[0] = grid[j - 1][i - 1]; //upperleft
            upperNeighbors[1] = grid[j - 1][i]; //upper
            if (i < columns - 1) upperNeighbors[2] = grid[j - 1][i + 1]; //upperright
        }
        return upperNeighbors;
    }

    public Particle[] getSideNeighbors(int j, int i){
        Particle[] sideNeighbors = new Particle[]{null, null};
        
        if (i > 0) sideNeighbors[0] = grid[j][i - 1];
        if (i < columns - 1) sideNeighbors[1] = grid[j][i + 1];
        return sideNeighbors;
    }


    /* public int getElementAtCell(int targetI, int targetJ) {
        
        return grid[targetI][targetJ];
    }

    public void switchWithCell(int thisI, int thisJ, int targetI, int targetJ) {
        int targetCell = getElementAtCell(targetI, targetJ);
        int thisCell = getElementAtCell(thisI, thisJ);
        grid[targetI][targetJ] = thisCell;
        grid[thisI][thisJ] = targetCell;
    }
    */

    public void print() {
        for (Particle[] row : grid) {
            System.out.println(Arrays.toString(row));
        }
    }




    
}
