package WorldGen;

import Blocks.Particle;

public abstract interface Biome {
    abstract Particle[][] generateChunk(int chunkSize, int[] chunkCoords);
}
