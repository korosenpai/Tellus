package Grid;

import FileHandler.FileHandler;

public class ThreadLoadUnload extends Thread {
    
    public void saveChunkRowToDisk(int rowN, Grid g) {
        FileHandler fileH = new FileHandler();
        start();
        FileHandler.saveChunkRowToDisk(rowN, g);
    }

    public void saveChunkColToDisk(int colN, Grid g) {
        start();
        FileHandler.saveChunkColToDisk(colN, g);
    }
}
