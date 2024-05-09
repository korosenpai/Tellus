package Blocks.Liquids;

import java.util.Random;

import Blocks.Liquids.LiquidParticle;

public class Water extends LiquidParticle{
    
    public Water() {
        super();

        // make them shift a bit to create texture
        // original sand = 182, 155, 99
        int offset = getColorOffset();
        setColors(-4 + offset, 32 + offset, 212 + offset);

        setMaxSpeed(10);
        setAcceleration(1.2f);

    }
}
