package WorldGen;

public class BiomeManager {

    public static Biome getBiome(int chunkOffsetY, int chunkOffsetX) {

        // if (chunkOffsetY < 0) {
        //     return new Plains();
        // }
        // else {
        //     return new Caves();
        // }

        return new Caves();
    }
}
