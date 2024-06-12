package Grid;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import javax.crypto.AEADBadTagException;

import Blocks.Air;
import Blocks.Particle;
import Blocks.ParticleList;
import Blocks.Liquids.Water;
import Blocks.Solids.StaticSolid.Stone;
import Blocks.Solids.StaticSolid.Wood;

import SRandom.SRandom;
import FileHandler.FileHandler;

public class Grid {
    public ParticleList particleList = new ParticleList();

    // relative to viewport
    private final int VIEWPORT_ROWS; // n of rows that will be rendered by viewport
    private final int VIEWPORT_COLS;
    private final int ROWS; // real number of rows
    private final int COLS;
    public final int CHUNK_SIZE;
    public final int TILE_DIMENSION;

    // TODO: move to its own class?
    // offset to draw viewport and not all grid
    private final int gridOffset = 2; // number of more chunks loaded in both directions more than viewport (offset / 2 in each direction)
    private int chunkOffsetX = 0; // how many chunks moved in each direction
    private int chunkOffsetY = 0; // used for loader to know in which chunk we are
    private int viewportOffsetX = 0;
    private int viewportOffsetY = 0;

    public Particle[][] grid;

    //private ThreadUpdates threadUpdates;

    private Chunk[][] gridChunk;
    private final int CHUNK_ROWS; // should never change
    private final int CHUNK_COLS;

    public int[][] noiseGrid; //used for generating the world

    

    public Grid(int screenWidth, int screenHeight, int chunkSize, int tileDimension){
        CHUNK_SIZE = chunkSize;
        TILE_DIMENSION = tileDimension;

        VIEWPORT_ROWS = screenHeight / TILE_DIMENSION;
        VIEWPORT_COLS = screenWidth / tileDimension;
        ROWS = VIEWPORT_ROWS + 2 * gridOffset * CHUNK_SIZE;
        COLS = VIEWPORT_COLS + 2 * gridOffset * CHUNK_SIZE;

        // make viewport start at center of offset grid
        resetViewportOffset();

        CHUNK_ROWS = ROWS / CHUNK_SIZE;
        CHUNK_COLS = COLS / CHUNK_SIZE;

        grid = new Particle[ROWS][COLS];
        gridChunk = new Chunk[CHUNK_ROWS][CHUNK_COLS];
        generateEmptyGrid();

        FileHandler.createDefaultChunkFile(CHUNK_SIZE);

        // loadGridFromDisk();

        // threadUpdates = new ThreadUpdates(this.COLS);

    }

    public int getRows() {
        return ROWS;
    }

    public int getColumns() {
        return COLS;
    }

    public int getViewportRows() {
        return VIEWPORT_ROWS;
    }

    public int getViewportColumns() {
        return VIEWPORT_COLS;
    }

    // get entire row / col
    public Particle[] getRow(int rowN) {
        return grid[rowN];
    }
    public void setRow(int rowN, Particle[] row) {
        grid[rowN] = row;
    }
    public void getCol() {} // TODO:

    public int getChunkRows() {
        return CHUNK_ROWS;
    }
    public int getChunkColumns() {
        return CHUNK_COLS;
    }

    public Particle[][] getGrid() {
        return grid;
    }
    public Chunk[][] getChunks() {
        return gridChunk;
    }

    public int getGridOffset() {
        return gridOffset;
    }

    public int getViewportOffsetX() {
        return viewportOffsetX;
    }
    public int getViewportOffsetY() {
        return viewportOffsetY;
    }

    public void generateEmptyGrid(){
        /*generates a 2d array of all zeros (id of empty cell)
        this is the general structure of the Grid
        WE NEED TO ITERATE FROM  BOTTOM TO TOP (L2R OR R2L DOESNT CHANGE ANYTHING) WHEN WE UPDATE
        for (int j = COLS-1; j > -1; j--) {
            for (int i = ROWS-1; i >-1; i--) where
        
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


        for (int j = 0; j < this.ROWS; j++) {
            for (int i = 0; i < this.COLS; i++) {
                grid[j][i] = new Air();
            }
        }

        // populate also the chunks
        for (int j = 0; j < CHUNK_ROWS; j++) {
            for (int i = 0; i < CHUNK_COLS; i++) {
                gridChunk[j][i] = new Chunk(CHUNK_SIZE);
            }
        }
        
    }

    public Particle[] generateEmptyParticleArray(int len) {
        Particle[] res = new Particle[len];

        for (int i = 0; i < len; i++) {
            res[i] = new Air();
        }

        return res;
    }


    public void updateParticle(int j, int i, int scanDirection) {
        // check if it is in a chunk that is not sleeping
        if (!gridChunk[j / CHUNK_SIZE][i / CHUNK_SIZE].getShouldStep()) return;

        if (grid[j][i] instanceof Air || grid[j][i].scanDirection != scanDirection || grid[j][i].hasMoved) return;

        grid[j][i].update(new int[]{j, i}, this);
    }

    public void updateGrid() {
        // first scan, bottom to top
        for (int j = ROWS-1; j > -1; j--){
            for (int i = COLS-1; i > -1; i--){
                updateParticle(j, i, 1);
            }
        }

        // second scan, top to bottom (for gases)
        for (int j = 0; j < ROWS; j++){
            for (int i = 0; i < COLS; i++){
                updateParticle(j, i, 2);
            }
        }

        // threadUpdates.update(this);
        
        // make all chunks ready for next frame
        for (int j = 0; j < CHUNK_ROWS; j++) {
            for (int i = 0; i < CHUNK_COLS; i++) {
                gridChunk[j][i].goToNextStep();
            }
        }
    }

    // after painting set all pixels who have moved
    //  able to make them move again
    public void setGridHasMovedFalse() {
        for (int j = ROWS-1; j > -1; j--){
            for (int i = COLS-1; i > -1; i--){
                if (grid[j][i] instanceof Air) continue;

                grid[j][i].hasMoved = false;
            }
        }
    }

    // generating cave system -------------------------------------------------------------------------------------------------------------------------


    public void generateWorld() {
        noiseGrid = new int[ROWS][COLS];
        generateRandomNoiseGrid(60);
        for (int i = 0; i < 12; i++) {
            smoothNoiseGrid();    
        }
        
        
        
        convertNoiseGrid();
        
    }

    private int[][] generateEmptyNoiseGrid(){
        return new int[ROWS][COLS];
    }


    private void convertNoiseGrid() {
        for (int j = 0; j < ROWS; j++){
            for (int i = 0; i < COLS; i++) {
                if (noiseGrid[j][i] == 1) grid[j][i] = new Stone();
                else if (noiseGrid[j][i] == 0) grid[j][i] = new Air();
            }
        }
    }




    private void generateRandomNoiseGrid(int noiseDensity) {
        for (int j = 0; j < ROWS; j++){
            for (int i = 0; i < COLS; i++) {
                if (i == 0 || i == COLS-1 || j == 0 || j == ROWS-1) noiseGrid[j][i] = 1;
                else noiseGrid[j][i] = (ThreadLocalRandom.current().nextInt(101) < noiseDensity)? 1: 0;
            }
        }
    }

    private void smoothNoiseGrid(){
        int[][] newGrid = generateEmptyNoiseGrid();
        for (int j = 0; j < ROWS; j++){
            for (int i = 0; i < COLS; i++) {
                int neighborWallCount = getSurroundingWallCount(i, j);
                if (neighborWallCount > 4) newGrid[j][i] = 1;
                else if (neighborWallCount <= 4) newGrid[j][i] = 0;
            }
        }

        noiseGrid = generateEmptyNoiseGrid();
        for (int j = 0; j < ROWS; j++){
            for (int i = 0; i < COLS; i++) {
                noiseGrid[j][i] = newGrid[j][i];
            }
        }
        
    }

    private int getSurroundingWallCount(int gridX, int gridY) {
        int wallCount = 0;
		for (int neighbourX = gridX - 1; neighbourX <= gridX + 1; neighbourX ++) {
			for (int neighbourY = gridY - 1; neighbourY <= gridY + 1; neighbourY ++) {
				if (neighbourX >= 0 && neighbourX < COLS && neighbourY >= 0 && neighbourY < ROWS) {
					if (neighbourX != gridX || neighbourY != gridY) {
						wallCount += noiseGrid[neighbourY][neighbourX];
					}
				}
				else {
					wallCount ++;
				}
			}
		}

		return wallCount;
	}





    //--------------------------------------------------------------------------------------------------------------------------------------------------





    public Particle getAtPosition(int j, int i) {
        if (j < ROWS && j >= 0 && i < COLS && i >= 0) {
            return grid[j][i];
        }
        return null;
    }

    public void setParticle(int j, int i, Particle particle) {
        grid[j][i] = particle;
    } 
    public void setParticleWithOffset(int j, int i, Particle particle) {
        grid[j + viewportOffsetY][i + viewportOffsetX] = particle;
    }

    public void setCursor(int mouseX, int mouseY, int radius, int particleID) {
        int circleCentreX = mouseX / TILE_DIMENSION;
        int circleCentreY = mouseY / TILE_DIMENSION;
        
        // min prevents to go out of bounds
        int c0 = Math.min(circleCentreX + radius, COLS - 1);
        int c180 = Math.max(circleCentreX - radius, 0);
        int c90 = Math.min(circleCentreY + radius, ROWS - 1);
        int c270 = Math.max(circleCentreY - radius, 0);

        // System.out.println(c0 + " : " + c180 + " : " + c90 + " : " + c270);

        // Adjust the loop conditions based on the actual number of tiles the circle spans
        for (int x = c180; x <= c0; x++) {
            for (int y = c270; y <= c90; y++) {
                if (Math.sqrt((x - circleCentreX) * (x - circleCentreX) + (y - circleCentreY) * (y - circleCentreY)) <= radius) {
                    Particle particle = particleList.getNewParticle(particleID);
                    // chance to not spawn all the blocks in the cursor
                    if (SRandom.nextFloat(1) <= particle.spawnRate) {
                        if (getAtPosition(y + viewportOffsetY, x + viewportOffsetX).canBeOverridden) {
                            setParticleWithOffset(y, x, particleList.getNewParticle(particleID));
                            wakeUpChunks(y + viewportOffsetY, x + viewportOffsetX);
                        }
                    }
                }
            }
        }

    }





    // NOTE: GETNEIGHBORS IN THE GRID FUNCTIONS 
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

        if (j < ROWS - 1) { //check if element is not in the last row
            if (i > 0) lowerNeighbors[0] = grid[j + 1][i - 1]; //bottomleft
            lowerNeighbors[1] = grid[j + 1][i]; //bottom
            if (i < COLS - 1) lowerNeighbors[2] = grid[j + 1][i + 1]; //bottomright
        }
        return lowerNeighbors;
    }

    public Particle getSingleLowerNeighbor(int j, int i, int offset) {
        /* return the single lower cell of grid[i][j] 
         * if neighbor is out of bound returns null
        */

        if (j < ROWS - offset - 1) { //check if element is not in the last row
            return grid[j + 1 + offset][i]; //bottom
            }
        return null;
    }

    public Particle[] getUpperNeighbors(int j, int i) {
        /* return the three lower cells of grid[i][j] 
         * if neighbor is out of bound returns null
        */
        Particle[] upperNeighbors = new Particle[]{null, null, null};

        if (j > 0) { //check if element is not in the last row
            if (i > 0) upperNeighbors[0] = grid[j - 1][i - 1]; //upperleft
            upperNeighbors[1] = grid[j - 1][i]; //upper
            if (i < COLS - 1) upperNeighbors[2] = grid[j - 1][i + 1]; //upperright
        }
        return upperNeighbors;
    }

    public Particle getSingleUpperNeighbor(int j, int i, int offset) {
        /* return the single upper cell of grid[i][j] 
         * if neighbor is out of bound returns null
        */

        if (j > 0 + offset) { //check if element is not in the first row
            return grid[j - 1 - offset][i]; //upper
            }
        return null;
    }

    public Particle[] getSideNeighbors(int j, int i){
        Particle[] sideNeighbors = new Particle[]{null, null};
        
        if (i > 0) sideNeighbors[0] = grid[j][i - 1];
        if (i < COLS - 1) sideNeighbors[1] = grid[j][i + 1];
        return sideNeighbors;
    }

    public Particle getSingleOffsetNeighbor(int j, int i, int offsetJ, int offsetI) {
        /* return the single offset cell of grid[i][j] 
         * if neighbor is out of bound returns null
         * used for the diagonal of the player
        */

        if (j > 0 + Math.abs(offsetJ) && j < ROWS - 1 - Math.abs(offsetJ)) { //check if element is not in the first row
            if (i > 0 + Math.abs(offsetI) && i < COLS - 1 - Math.abs(offsetI)) {
                return grid[j + offsetJ][i + offsetI];
            }
        }
        return null;
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
        int idx = 0;
        for (Particle[] row : grid) {
            System.out.println(idx + ": " + Arrays.toString(row));
            idx++;
        }
        // for (int j = 0; j < ROWS; j++) {
        //     System.out.print(j + ": ");
        //     for (int i = 0; i < COLS; i++) {
        //         System.out.print(grid[j][i].getClass().getName() + " ");
        //     }
        //     System.out.println();
        // }
        // System.out.println("-----------");
    }


    // wake up all chunks in the vicinity of particle (adjacent chunks)
    public void wakeUpChunks(int j, int i) {
        int chunkAtJ = j / CHUNK_SIZE;
        int chunkAtI = i / CHUNK_SIZE;
        
        // if particle is less than min or more than max it updates adjacent chunks
        // (it is close enough to the edges)
        int minChunkOffset = 6; // NOTE: to not make water outpace this
        int maxChunkOffset = CHUNK_SIZE - minChunkOffset - 1;


        // returns true if close to the chunks
        boolean closeToLeft = chunkAtI > 0 && i % CHUNK_SIZE < minChunkOffset;
        boolean closeToRight = chunkAtI < CHUNK_COLS - 1 && i % CHUNK_SIZE > maxChunkOffset;

        gridChunk[chunkAtJ][chunkAtI].setShouldStepNextFrame(); // the chunk the particle is in

        if (chunkAtJ < CHUNK_ROWS - 1 && j % CHUNK_SIZE > maxChunkOffset) {
            gridChunk[chunkAtJ + 1][chunkAtI].setShouldStepNextFrame(); // the chunk below
            if (closeToLeft) gridChunk[chunkAtJ + 1][chunkAtI - 1].setShouldStepNextFrame(); // bottom left
            if (closeToRight) gridChunk[chunkAtJ + 1][chunkAtI + 1].setShouldStepNextFrame(); // bottom right
        }

        if (chunkAtJ > 0 && j % CHUNK_SIZE < minChunkOffset) {
            gridChunk[chunkAtJ - 1][chunkAtI].setShouldStepNextFrame(); // the chunk above
            if (closeToLeft) gridChunk[chunkAtJ - 1][chunkAtI - 1].setShouldStepNextFrame(); // above left
            if (closeToRight) gridChunk[chunkAtJ - 1][chunkAtI + 1].setShouldStepNextFrame(); // above right
        }

        if (closeToLeft) gridChunk[chunkAtJ][chunkAtI - 1].setShouldStepNextFrame(); // side left
        if (closeToRight) gridChunk[chunkAtJ][chunkAtI + 1].setShouldStepNextFrame(); // side left

    }


    private void resetViewportOffset() {
        viewportOffsetX = viewportOffsetY = gridOffset * CHUNK_SIZE;
    }

    // TODO: check if these operations can be done
    // (in theory with chunk loading they should always be possible)

    public void moveViewUpOne() {
        viewportOffsetY--;
        if (viewportOffsetY <= CHUNK_SIZE) {
            System.out.println("unloading bottom and loading top...");
            chunkOffsetY--; // grid is shifting from origin to new chunks

            FileHandler.saveChunkRowToDisk(CHUNK_ROWS - 1, this); // save last row to disk

            resetViewportOffset();
            shiftGridDown(CHUNK_SIZE);

            loadChunkRowFromDisk(0);
        }
    }
    public void moveViewDownOne() {
        viewportOffsetY++;
        // when approaching last chunk
        if (viewportOffsetY + VIEWPORT_ROWS >= ROWS - CHUNK_SIZE) {
            chunkOffsetY++;
            System.out.println("unloading...");
            // save row 0
            resetViewportOffset();
            shiftGridUp(CHUNK_SIZE);
            loadChunkRowFromDisk(CHUNK_ROWS - 1);
        }
    }

    public void moveViewRightOne() {
        viewportOffsetX++;
    }
    public void moveViewLeftOne() {
        viewportOffsetX--;
    }

    public void saveChunkToDisk(int[] chunkCoords) {
        FileHandler.saveChunkToDisk(chunkCoords, this);
    }
    public void saveChunkRowToDisk(int rowN) {
        FileHandler.saveChunkRowToDisk(rowN, this);
    }
    public void saveChunkColToDisk(int colN) {
        FileHandler.saveChunkColToDisk(colN, this);
    }
    public void saveGridtoDisk() {
        // TODO:  make this accept chunkCOord (only y is needed i think)
        FileHandler.saveWholeGridToDisk(this);

    }

    public void loadChunkFromDisk(int[] chunkCoords) {
        FileHandler.loadChunkFromDisk(chunkCoords, this);
    }
    public void loadChunkRowFromDisk(int rowN) {
        Particle[][] loaded = FileHandler.loadChunkRowFromDisk(rowN + chunkOffsetY, this);

        // inject int grid chunks
        for (int i = 0; i < loaded.length; i++) {
            setRow(rowN * CHUNK_SIZE + i, loaded[i]);
        }

        // wake up every chunk in row
        // take element at half j for each chunk, middle elem in each chunk in i
        for (int i = 0; i < loaded[0].length / CHUNK_SIZE; i++) {
            wakeUpChunks(rowN + CHUNK_SIZE / 2, i * CHUNK_SIZE + CHUNK_SIZE / 2);
        }

        // set chunks injected to update
    }
    public void loadChunkColFromDisk(int colN) {
        FileHandler.loadChunkColFromDisk(colN, this);
    }
    public void loadGridFromDisk() {
        // TODO: implement chunk coords
        grid = FileHandler.loadWholeGridFromDisk(new int[]{0, 0}, this);

    }



    public void shiftGridDown(int moveDistance) {
        // src, srcIndex, dest, destIndex, len
        System.arraycopy(grid, 0, grid, moveDistance, ROWS - moveDistance);
    }
    public void shiftGridUp(int moveDistance) {
        System.arraycopy(grid, moveDistance, grid, 0, ROWS - moveDistance);
    }
    public void shiftGridLeft(int moveDistance) {
        for (int j = 0; j < ROWS; j++) {
            System.arraycopy(grid[j], moveDistance, grid[j], 0, COLS - moveDistance);
        }
    }
    public void shiftGridRight(int moveDistance) {
        for (int j = 0; j < ROWS; j++) {
            System.arraycopy(grid[j], 0, grid[j], moveDistance, COLS - moveDistance);
        }
    }

    // called after shiftUp to remove references to shifted particels
    public void clearRow(int rowN) {
        grid[rowN] = generateEmptyParticleArray(COLS);
    }
    public void clearColumn(int colN) {
        for (int j = 0; j < ROWS; j++)
            grid[j][colN] = new Air();
    }




}
