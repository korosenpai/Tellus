package Blocks.Solids.StaticSolid;

public class Dirt extends StaticParticle {

    public Dirt() {
        super();

        // make them shift a bit to create texture
        // original sand = 182, 155, 99
        int offset = getColorOffset();
        setColors(63 + offset, 34 + offset, 13 + offset);
    }
}

