package WorldGen;

import Blocks.Particle;

import java.util.Arrays;

import Blocks.Air;
import Blocks.Solids.StaticSolid.Stone;
import SRandom.SRandom;

// stone caves
public class Caves {

    // TODO: change based on the height
    public static double noiseDensity = 0.03f; // percent to spawn stone

    // chunkcoords used to make simplex noise function know where we are
    public static Particle[][] generateChunk(int chunkSize, int[] chunkCoords) {
        //System.out.println(Arrays.toString(chunkCoords));

        Particle[][] generated = new Particle[chunkSize][chunkSize];

        for (int j = 0; j < chunkSize; j++) {
            for (int i = 0; i < chunkSize; i++) {

                // // playing with ocatves
                // double value = sumOctave(16, i + chunkCoords[1] * chunkSize, j + chunkCoords[0] * chunkSize, .5, 0, 255);
                // System.out.println(value);

                // if (value > 50) generated[j][i] = new Wood();
                // else if (value > -20 && SRandom.nextFloat(1) <= 0.09) generated[j][i] = new Stone();
                // else generated[j][i] = new Air();

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

    // private static double sumOctave(int numIterations, int x, int y, double persistence, int low, int high) {
    //     double value = 0;

    //     int maxAmp = 0;
    //     int amp = 1;
    //     double scale = noiseDensity;

    //     // add more smaller, (higher freq ) terms
    //     for (int iter = 0; iter < numIterations; iter++) {
    //             value += OpenSimplex2S.noise3_ImproveXY(
    //                 SRandom.getSeed(),
    //                 x * scale,
    //                 y * scale,
    //                 0.0
    //             ) * amp;
    //             maxAmp += amp;
    //             amp *= persistence;
    //             scale *= 2;
    //     }

    //     // average value of iterations
    //     if (maxAmp != 0)
    //         value /= maxAmp;

    //     // normalize the result
    //     value *= value * (high - low) / 2 + (high + low) / 2;

    //     return value;
    // }
}
