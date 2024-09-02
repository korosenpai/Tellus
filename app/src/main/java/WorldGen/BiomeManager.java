package WorldGen;

public class BiomeManager {

    public static Biome getBiome(int chunkOffsetY, int chunkOffsetX) {

        // if (chunkOffsetY < -1)
        //     return new Sky();
        // if (chunkOffsetY < 0) // -1
        //     return new Plains();
        
        return new Caves();
    }
}
