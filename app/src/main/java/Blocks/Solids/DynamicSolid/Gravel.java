package Blocks.Solids.DynamicSolid;

import java.util.Random;

public class Gravel extends DynamicParticle {
    
    public Gravel() {
        super();

        // make them shift a bit to create texture
        // original sand = 182, 155, 99
        int offset = getColorOffset();
        setColors(33 + offset, 34 + offset, 28 + offset);

        setMaxSpeed(5);
        setAcceleration(0.6f);

    }
}
