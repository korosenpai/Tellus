package Blocks.Liquids;

import java.util.Random;

import Blocks.Liquids.LiquidParticle;

public class Water extends LiquidParticle{
    
    public Water() {
        super();

        // make them shift a bit to create texture
        // int offset = getColorOffset();
        // setColors(-4 + offset, 32 + offset, 205 + offset);
        setColors(46, 82, 255);

        setMaxSpeed(10);
        setAcceleration(1.6f);

    }
}
