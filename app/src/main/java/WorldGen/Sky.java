package WorldGen;

import Blocks.Air;
import Blocks.Particle;

public class Sky implements Biome {

    public Particle[][] generateChunk(int chunkSize, int[] chunkCoords) {
        Particle[][] generated = new Particle[chunkSize][chunkSize];

        // only air
        for (int j = 0; j < chunkSize; j++) {
            for (int i = 0; i < chunkSize; i++) {
                generated[j][i] = new Air();
            }
        }

        return generated;
    }
}
