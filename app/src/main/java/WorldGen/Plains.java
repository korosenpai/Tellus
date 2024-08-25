package WorldGen;

import Blocks.Air;
import Blocks.Particle;
import Blocks.Solids.StaticSolid.Dirt;
import Blocks.Solids.StaticSolid.Grass;

public class Plains implements Biome {

    // TODO: small noise to create hills of dirt and grass
    public Particle[][] generateChunk(int chunkSize, int[] chunkCoords) {
        Particle[][] generated = new Particle[chunkSize][chunkSize];

        // all air except last layer is Dirt and grass
        for (int j = 0; j < chunkSize - 2; j++) {
            for (int i = 0; i < chunkSize; i++) {
                generated[j][i] = new Air();
            }
        }
        for (int i = 0; i < chunkSize; i++) {
            generated[chunkSize - 2][i] = new Grass();
            generated[chunkSize - 1][i] = new Dirt();
        }

        return generated;
    }
}
