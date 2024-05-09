package Blocks.Solids.DynamicSolid;

import java.util.Random;


public class Snow extends DynamicParticle {
    
    public Snow() {
        super();

        // make them shift a bit to create texture
        int offset = getColorOffset(); // change luminosity
        setColors(155 + offset, 155 + offset, 155 + offset);

        // TODO: find way to make it go slower than 1 fps
        setMaxSpeed(1);
        setAcceleration(0.1f);

    }
}
