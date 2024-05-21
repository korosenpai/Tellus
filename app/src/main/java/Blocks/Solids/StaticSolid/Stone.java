package Blocks.Solids.StaticSolid;

import java.util.Random;

public class Stone extends StaticParticle {
    
    public Stone() {
        super();
        
        
        // make them shift a bit to create texture
        // original sand = 182, 155, 99
        int offset = getColorOffset();
        setColors(108 + offset, 102 + offset, 84 + offset);
    }
}
