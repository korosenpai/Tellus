package FileHandler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import SRandom.SRandom;
import WorldGen.Caves;
import Blocks.Particle;
import Blocks.Air;
import Blocks.Solids.StaticSolid.Stone;
import Utils.Debug;
import Entities.BlobParticle;
import Entities.EntityParticle;
import Grid.Grid;

// TODO: to refactor the shit out of this

public class FileHandler {
    public static final String ROOT_SAVE_DIR = "src/main/saves/";

    // directory where the files will be saved
    public static String getChunkSaveDir(int chunkSize) {
        return ROOT_SAVE_DIR + chunkSize + "/" + SRandom.getSeed() + "/";
    }

    // path to file to chunks
    public static String getChunkFilenameFromCoords(int[] chunkCoords, int chunkSize) {
        return getChunkSaveDir(chunkSize) + chunkCoords[0] + "_" + chunkCoords[1] + ".ser";
    }

    private static void saveToDisk(Particle[][] chunkArray, String filename) {
        try {
            // create chunk and seed folder if they dont exist
            Path saveFolder = Paths.get(getChunkSaveDir(chunkArray.length)); // chunkarray is of length CHUNK_SIZE
            if (!Files.exists(saveFolder))
                Files.createDirectories(saveFolder);

            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(chunkArray);

            out.close();
            fileOut.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            Debug.error("could not save to file: " + filename);
        }
    }

    // this is the chunk that gets loaded in absence of worldGen (when there is not file saved)
    // public static void createDefaultChunkFile(int chunkSize) {
    //     Particle[][] defaultChunk = new Particle[chunkSize][chunkSize];

    //     // all air except last layer is Stone
    //     for (int j = 0; j < chunkSize - 1; j++) {
    //         for (int i = 0; i < chunkSize; i++) {
    //             defaultChunk[j][i] = new Air();
    //         }
    //     }
    //     for (int i = 0; i < chunkSize; i++) {
    //         defaultChunk[chunkSize - 1][i] = new Stone();
    //     }

    //     saveToDisk(defaultChunk, getChunkSaveDir(chunkSize) + "default.ser");
    // }


    // unload a particle chunk at certain chunkCoords
    // NOTE: all other functions must pass chunkCoords with the chunk offset
    // it will not automatically check for it in case you need something off from chunk offset
    // MUST BE IN CHUNK_ROW FORMAT (ROW / CHUNKSIZE)

    public static void saveChunkToDisk(int[] chunkCoords, Grid grid) {

        Particle[][] toSave = new Particle[grid.CHUNK_SIZE][grid.CHUNK_SIZE];

        // clone chunk from grid
        for (int j = 0; j < toSave.length; j++) {
            for (int i = 0; i < toSave.length; i++) {
                Particle PToSave  = grid.getAtPosition(
                    j + chunkCoords[0] * grid.CHUNK_SIZE, // must be offset to the chunk you are in
                    i + chunkCoords[1] * grid.CHUNK_SIZE
                ).clone();

                if (PToSave instanceof BlobParticle) PToSave = new Air();

                toSave[j][i] = PToSave;
            }
        }

        // these new coords are the "virtual" chunk we are at (obtained via the offset)
        // and its the coords that will create the filename
        chunkCoords[0] += grid.getChunkOffsetY();
        chunkCoords[1] += grid.getChunkOffsetX();

        // save to file
        String filename = getChunkFilenameFromCoords(chunkCoords, grid.CHUNK_SIZE);
        Debug.debug("saving chunk in: " + filename);
        saveToDisk(toSave, filename);

    }

    // NOTE: ROW MEANT AS HORIZONTAL LINE
    // get leftmost elem and find all other to save
    public static void saveChunkRowToDisk(int rowN, Grid grid) {
        // TODO: fix all this strange column, row mixture it has become unbereable

        for (int i = 0; i < grid.getColumns() / grid.CHUNK_SIZE; i++ ) {
            saveChunkToDisk(new int[]{rowN, i}, grid);
        }

    }
    public static void saveChunkColToDisk(int colN, Grid grid) {
        for (int j = 0; j < grid.getRows() / grid.CHUNK_SIZE; j++) {
            saveChunkToDisk(new int[]{j, colN}, grid);
        }
    }

    public static void saveWholeGridToDisk(Grid grid) {
        // should NEVER fail (in theory)
        // go to each row and save it
        for (int j = 0; j < grid.getRows() / grid.CHUNK_SIZE; j++) {
            saveChunkRowToDisk(j, grid);
        }
    }

    // should never fail (file is created at startup)
    // public static Particle[][] DEFAULT_CHUNK; // to open file only once
    // private static Particle[][] loadDefaultChunk(int chunkSize) {
    //     System.out.println("loading default chunk...");
    //     if (DEFAULT_CHUNK != null) return DEFAULT_CHUNK.clone();

    //     Particle[][] loaded = new Particle[chunkSize][chunkSize];
    //     try {
    //         FileInputStream fileIn = new FileInputStream(getChunkSaveDir(chunkSize) + "default.ser");
    //         ObjectInputStream in = new ObjectInputStream(fileIn);

    //         loaded = (Particle[][])in.readObject();
    //         DEFAULT_CHUNK = loaded;

    //         in.close();
    //         fileIn.close();
    //     }
    //     catch (Exception e) {}

    //     return loaded.clone();
    // }

    // NOTE: can fail, if it doesnt find the file simply get chunk from grid
    public static Particle[][] loadChunkFromDisk(int[] chunkCoords, Grid grid) {
        Particle[][] loaded = new Particle[grid.CHUNK_SIZE][grid.CHUNK_SIZE];
        String filename = getChunkFilenameFromCoords(chunkCoords, grid.CHUNK_SIZE);

        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            loaded = (Particle[][])in.readObject();

            in.close();
            fileIn.close();

            Debug.debug("loaded chunk : " + filename);
        }
        catch (Exception e) {
            // System.out.println("could not load chunk: " + filename);
            //loaded = loadDefaultChunk(grid.CHUNK_SIZE);
            loaded = Caves.generateChunk(grid.CHUNK_SIZE, chunkCoords);
        }

        // for (Particle[] x: loaded) {
        //     System.out.println(Arrays.toString(x));
        // }

        // NOTE: why is it LBlocks.Particle?
        // System.out.println(loaded.getClass().getName());

        return loaded;
    }

    // get leftmost elem and find all other to load and add them in one long slice
    public static Particle[][] loadChunkRowFromDisk(int rowN, Grid grid) {
        Particle[][] loaded = new Particle[grid.CHUNK_SIZE][grid.getColumns()];

        // manually copy every element from loaded in grid
        // have to do this because loaded is LBlocks.Particle[]? may desserialization issues
        for (int i = 0; i < grid.getColumns() / grid.CHUNK_SIZE; i++ ) {
            Particle[][] singleChunk = loadChunkFromDisk(new int[]{rowN, i}, grid);

            for (int singleChunkJ = 0; singleChunkJ < singleChunk.length; singleChunkJ++) {
                for (int singleChunkI = 0; singleChunkI < singleChunk[0].length; singleChunkI++) {
                    loaded[singleChunkJ][singleChunkI + i * grid.CHUNK_SIZE] = singleChunk[singleChunkJ][singleChunkI];
                }
            }
        }

        return loaded;
    }
    public static Particle[][] loadChunkColFromDisk(int colN, Grid grid) {

        Particle[][] loaded = new Particle[grid.getRows()][grid.CHUNK_SIZE];

        for (int j = 0; j < grid.getRows() / grid.CHUNK_SIZE; j++) {
            Particle[][] singleChunk = loadChunkFromDisk(new int[]{j, colN}, grid);

            for (int singleChunkJ = 0; singleChunkJ < singleChunk.length; singleChunkJ++) {
                for (int singleChunkI = 0; singleChunkI < singleChunk[0].length; singleChunkI++) {
                    loaded[singleChunkJ + j * grid.CHUNK_SIZE][singleChunkI] = singleChunk[singleChunkJ][singleChunkI];
                }
            }
        }

        return loaded;
    }

    public static Particle[][] loadWholeGridFromDisk(Grid grid) {
        int[] chunkCoords = {grid.getChunkOffsetY(), grid.getChunkOffsetX()};

        Particle[][] newGrid = new Particle[grid.getRows()][grid.getColumns()];

        // go to each row and save it
        for (int j = 0; j < grid.getRows() / grid.CHUNK_SIZE; j++) {
            for (int i = 0; i < grid.getColumns() / grid.CHUNK_SIZE; i++) {
                Particle[][] singleChunk = loadChunkFromDisk(new int[]{ j + chunkCoords[0], i + chunkCoords[1] }, grid);


                // manually insert singleChunk in newGrid
                // cannot use something like array copy because it is Lparticle type? idk serialization problems
                for (int singleChunkJ = 0; singleChunkJ < singleChunk.length; singleChunkJ++) {
                    for (int singleChunkI = 0; singleChunkI < singleChunk[0].length; singleChunkI++) {
                        newGrid[singleChunkJ + j * grid.CHUNK_SIZE][singleChunkI + i * grid.CHUNK_SIZE] = singleChunk[singleChunkJ][singleChunkI];
                    }
                }
            }
        }
        return newGrid;
    }

}
