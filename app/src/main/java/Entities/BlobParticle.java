package Entities;

import Blocks.Solids.SolidParticle;

public class BlobParticle extends SolidParticle {
    public BlobParticle() {
        setColors(255, 0, 0);
    }
    public BlobParticle(int r, int g, int b) {
        setColors(r, g, b);
    }

}
