package Blocks.Solids.DynamicSolid;

import Window.Grid;


public class Snow extends DynamicParticle {
    public boolean hasMovedLastFrame; //way to make it go slower than 1 fps

    public Snow() {
        super();

        // make them shift a bit to create texture
        int offset = getColorOffset(); // change luminosity
        setColors(155 + offset, 155 + offset, 155 + offset);

        setMaxSpeed(1);
        setAcceleration(0);

        setBehaviours(true, true, true);

    }

    @Override
    public int[] update(int[] coords, Grid grid) {

        // move once every two frames
        if (!hasMovedLastFrame) super.update(coords, grid);
        hasMovedLastFrame = !hasMovedLastFrame;

        return coords;
    }
}
