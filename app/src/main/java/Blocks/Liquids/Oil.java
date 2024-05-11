package Blocks.Liquids;

import java.util.Random;

import Blocks.Liquids.LiquidParticle;

public class Oil extends LiquidParticle{
    
    public Oil() {
        super();

        // make them shift a bit to create texture
        setColors(74, 80, -42);

        setMaxSpeed(10);
        setAcceleration(1.2f);

    }
}
