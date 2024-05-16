package Grid;

import Blocks.Air;
import Blocks.Particle;

public class Chunk {
    public int SIZE = 32;

    private Particle[][] elements = new Particle[SIZE][SIZE];

    public Chunk() {
        generateEmptyChunk();
    }

    public void generateEmptyChunk() {
        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE; i++) {
                elements[j][i] = new Air();
            }
        }
    }

    public void update(Grid grid) {
        // way for it to abstract so the particles continue to see grid as array of particles and not
        // of chunks

        // for (int j = SIZE - 1; j > -1; j--) {
        //     for (int i = SIZE - 1; i > -1; i--) {
        //         // if (grid[j][i] instanceof Air || grid[j][i].scanDirection != 1 || grid[j][i].hasMoved) continue;

        //         // grid[j][i].update(new int[]{j, i}, this);
        //     }
        // }

        // for (int j = 0; j < SIZE - 1; j++) {
        //     for (int i = 0; i < SIZE - 1; i++) {

        //     }
        // }

        System.out.println("updating chunk...");
    }
}
