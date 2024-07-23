package WorldGen;

import Blocks.Particle;

import Blocks.Air;
import Blocks.Solids.StaticSolid.Stone;
import SRandom.SRandom;

// stone caves
public class Caves {

    // TODO: change based on the height
    public static float noiseDensity = 0.04f; // percent to spawn stone


    // chunkcoords used to make simplex noise function know where we are
    public static Particle[][] generateChunk(int chunkSize, int[] chunkCoords) {
        Particle[][] generated = new Particle[chunkSize][chunkSize];

        for (int j = 0; j < chunkSize; j++) {
            for (int i = 0; i < chunkSize; i++) {


                double value = OpenSimplex2S.noise3_ImproveXY(
                    SRandom.getSeed(),
                    (i + chunkCoords[1] * chunkSize) * noiseDensity,
                    (j + chunkCoords[0] * chunkSize) * noiseDensity,
                    0.0
                );

                if (value < 0) generated[j][i] = new Stone();
                else generated[j][i] = new Air();
            }
        }


        return generated;
    }
}
