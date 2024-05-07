package Window;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
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

    public Particle getAtPosition(int i, int j) {
        return grid[i][j];
    }

    public Particle[] getNeighbors(int i, int j) {
        Particle[] neighbors = new Particle[]{null, null, null, null, null, null, null, null};
        return neighbors;

    }

    public Particle[] getLowerNeighbors(int i, int j) {
        /* return the three lower cells of grid[i][j] 
         * if neighbor is out of bound returns null
        */
        Particle[] lowerNeighbors = new Particle[]{null, null, null};

        if (j < rows) { //check if element is not in the last row
            if (i > 0) lowerNeighbors[0] = grid[i-1][j+1]; //bottomleft
            lowerNeighbors[1] = grid[i][j+1]; //bottom
            if (i < columns) lowerNeighbors[2] = grid[i+1][j+1]; //bottomright
        }
        return lowerNeighbors;
    }

    public Particle[] getUpperNeighbors(int i, int j) {
        /* return the three lower cells of grid[i][j] 
         * if neighbor is out of bound returns null
        */
        Particle[] upperNeighbors = new Particle[]{null, null, null};

        if (j > 0) { //check if element is not in the last row
            if (i > 0) upperNeighbors[0] = grid[i-1][j-1]; //upperleft
            upperNeighbors[1] = grid[i][j-1]; //upper
            if (i < columns) upperNeighbors[2] = grid[i+1][j-1]; //upperright
        }
        return upperNeighbors;
    }

    public Particle[] getSideNeighbors(int i, int j){
        Particle[] sideNeighbors = new Particle[]{null, null};
        
        if (i > 0) sideNeighbors[0] = grid[i-1][j];
        if (i < columns) {}
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
