package Grid;


import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.List;
import java.util.ArrayList;

import Blocks.Particle;


public class GridTemplates implements Serializable{
    
    public static List<List<List<Particle>>> templates = new ArrayList<>();

    public static void saveCurrentGrid(Grid grid){
        
        List<List<Particle>> gridList = new ArrayList<>();
        for (Particle[] row : grid.grid) {
            List<Particle> rowList = new ArrayList<>();
            for (Particle particle : row) {
                rowList.add(particle);
            }
            gridList.add(rowList);
        }
        templates.add(gridList); 

        String filePath = "/app/src/main/java/Window/GridTemplates.ser"; // Replace with your desired absolute path
        File dir = new File(filePath.substring(0, filePath.lastIndexOf('/')));
        if (!dir.exists()) {
            dir.mkdirs(); // Create the directory if it doesn't exist
        }

        try (FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(templates);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<List<List<Particle>>> loadGridTemplate() {
        List<List<List<Particle>>> data = null;
        try (FileInputStream fis = new FileInputStream("Tellus/app/src/main/java/Window/GridTemplates.ser");
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            data = (List<List<List<Particle>>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static Particle[][] loadCurrentlySelectedGrid(int currentlySelectedTemplate) {
        List<List<List<Particle>>> templates = loadGridTemplate();
        List<List<Particle>> currentTemplate = templates.get(currentlySelectedTemplate);

        int rows = currentTemplate.size();
        int columns = currentTemplate.get(0).size();
        Particle[][] currentGridArray = new Particle[rows][columns];
        for (int i = 0; i< rows; i++) {
            List<Particle> row = currentTemplate.get(i);
            for (int j = 0; j <columns; j++) {
                currentGridArray[i][j] = row.get(j);
            }
        }
    
        return currentGridArray;
    }   




}
