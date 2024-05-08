package Window;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import Blocks.Particle;

class Grid {
    final int screenWidth;
    final int screenHeight;

    private final int rows;
    private final int columns;
    public Particle[][] grid = {{}};
    

    public Grid(int screenWidth, int screenHeight, int tileDimension){
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.rows = screenHeight / tileDimension;
        this.columns = screenWidth / tileDimension;
        grid = new Particle[this.rows][this.columns];
    }

    public static void main(String[] args) {
        Grid grid = new Grid(800, 800, 20);
        grid.generateRandomizedGrid();
        System.out.println(Arrays.toString(grid.grid[0]));
        grid.print();
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
        
        for (int i = 0; i < this.columns; i++){
            Particle[] row = new Particle[this.columns];
            for (int j = 0; j < this.rows; j++){
                row[j] = new Particle();
            }
            grid[i] = row;
        }
    }

    public void generateRandomizedGrid(){ 
        /*mostly for debugging purposes
        generates a random 2d array of numbers between 0 and 255
        */
        for (int i = 0; i < this.columns; i++){
            Particle[] row = new Particle[this.columns];
            for (int j = 0; j < this.rows; j++){
                row[j] = new Particle();
            }
            grid[i] = row;
        }
    }

    public Particle getAtPosition(int j, int i) {
        return grid[j][i];
    }

    public void setParticle(int j, int i, Particle particle) {
        grid[j][i] = particle;
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

        if (j < rows) { //check if element is not in the last row
            if (i > 0) lowerNeighbors[0] = grid[j + 1][i - 1]; //bottomleft
            lowerNeighbors[1] = grid[j + 1][i]; //bottom
            if (i < columns) lowerNeighbors[2] = grid[j + 1][i + 1]; //bottomright
        }
        return lowerNeighbors;
    }

    public Particle[] getUpperNeighbors(int j, int i) {
        /* return the three lower cells of grid[i][j] 
         * if neighbor is out of bound returns null
        */
        Particle[] upperNeighbors = new Particle[]{null, null, null};

        if (j > 0) { //check if element is not in the last row
            if (i > 0) upperNeighbors[0] = grid[j - 1][i - 1]; //upperleft
            upperNeighbors[1] = grid[j - 1][i]; //upper
            if (i < columns) upperNeighbors[2] = grid[j - 1][i + 1]; //upperright
        }
        return upperNeighbors;
    }

    public Particle[] getSideNeighbors(int j, int i){
        Particle[] sideNeighbors = new Particle[]{null, null};
        
        if (i > 0) sideNeighbors[0] = grid[j][i - 1];
        if (i < columns) sideNeighbors[1] = grid[j][i + 1];
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
