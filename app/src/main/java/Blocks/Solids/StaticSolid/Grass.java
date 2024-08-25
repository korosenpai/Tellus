package Blocks.Solids.StaticSolid;


public class Grass extends StaticParticle {

    public Grass() {
        super();


        int offset = getColorOffset();
        setColors(3 + offset, 54 + offset, 18 + offset);

        this.isFlammable = true;
        this.chanceToSpreadFire = 0.4f;
    }
}

