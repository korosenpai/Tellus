package Blocks.Liquids;

import java.util.Random;

import Blocks.Liquids.LiquidParticle;

public class Water extends LiquidParticle{
    
    public Water() {
        super();

        // make them shift a bit to create texture
        setColors(46, 82, 262);

        setMaxSpeed(10);
        setAcceleration(1.2f);

    }
}
