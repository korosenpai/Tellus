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
import Blocks.Particle;
import Grid.Grid;

// TODO: to refactor the shit out of this

public class FileHandler {
    public static final String ROOT_SAVE_DIR = "src/main/saves/";

    // directory where the files will be saved
    public static String getChunkSaveDir(int chunkSize) {
        return ROOT_SAVE_DIR + chunkSize + "/" + SRandom.getSeed();
    }

    // path to file to chunks
    public static String getChunkFilenameFromCoords(int[] chunkCoords, int chunkSize) {
        return getChunkSaveDir(chunkSize) + "/" + chunkCoords[0] + "_" + chunkCoords[1] + ".ser";
    }

    // unload a particle chunk at certain chunkCoords
    public static void saveChunkToDisk(int[] chunkCoords, Grid grid) {
        Particle[][] toSave = new Particle[grid.CHUNK_SIZE][grid.CHUNK_SIZE];

        // clone chunk from grid
        for (int j = 0; j < toSave.length; j++) {
            for (int i = 0; i < toSave.length; i++) {
                toSave[j][i] = grid.getAtPosition(
                    j + chunkCoords[0] * grid.CHUNK_SIZE, // must be offset to the chunk you are in
                    i + chunkCoords[1] * grid.CHUNK_SIZE
                ).clone();
            }
        }

        // save to file
        String filename = getChunkFilenameFromCoords(chunkCoords, grid.CHUNK_SIZE);
        System.out.println("saving chunk in: " + filename);

        try {
            // create chunk and seed folder if they dont exist
            Path saveFolder = Paths.get(getChunkSaveDir(grid.CHUNK_SIZE));
            if (!Files.exists(saveFolder))
                Files.createDirectories(saveFolder);

            FileOutputStream fileOut = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(toSave);

            out.close();
            fileOut.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("could not save to file: " + filename);
        }

    }

    // NOTE: ROW MEANT AS HORIZONTAL LINE
    // get leftmost elem and find all other to save
    public static void saveChunkRowToDisk(int rowN, Grid grid) {
        // TODO: fix all this strange column, row mixture it has become unbereable

        for (int i = 0; i < grid.getColumns() / grid.CHUNK_SIZE; i++ ) {
            saveChunkToDisk(new int[]{rowN, i}, grid);
        }

    }
    public static void saveChunkColToDisk(int colN, Grid grid) {}

    public static void saveWholeGridToDisk(Grid grid) {
        // go to each row and save it
        for (int j = 0; j < grid.getRows() / grid.CHUNK_SIZE; j++) {
            saveChunkRowToDisk(j, grid);
        }
    }

    // NOTE: can fail, if it doesnt find the file simply get chunk from grid
    public static Particle[][] loadChunkFromDisk(int[] chunkCoords, Grid grid) {
        Particle[][] loaded = new Particle[grid.CHUNK_SIZE][grid.CHUNK_SIZE];
        String filename = getChunkFilenameFromCoords(chunkCoords, grid.CHUNK_SIZE);

        System.out.println("loading chunk from: " + filename);

        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            loaded = (Particle[][])in.readObject();

            in.close();
            fileIn.close();
        }
        catch (Exception e) {
            System.out.println("could not load chunk: " + filename);
            // get chunk from grid
            for (int j = 0; j < loaded.length; j++) {
                for (int i = 0; i < loaded.length; i++) {
                    loaded[j][i] = grid.getAtPosition(
                        j + chunkCoords[0] * grid.CHUNK_SIZE, // must be offset to the chunk you are in
                        i + chunkCoords[1] * grid.CHUNK_SIZE
                    );
                }
            }
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
    public static Particle[][] loadChunkColFromDisk(int colN, Grid grid) {return new Particle[0][0];}

    public static Particle[][] loadWholeGridFromDisk(Grid grid) {
        Particle[][] newGrid = new Particle[grid.getRows()][grid.getColumns()];

        // go to each row and save it
        // for (int j = 0; j < grid.getRows() / grid.CHUNK_SIZE; j++) {
        //     for (int i = 0; i < grid.getColumns() / grid.CHUNK_SIZE; i++) {
        //         Particle[][] singleChunk = load
        //     }
        // }
        return new Particle[0][0];
    }

}
