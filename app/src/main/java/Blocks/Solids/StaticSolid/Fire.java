package Blocks.Solids.StaticSolid;

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import Blocks.Gases.Smoke;

import Window.Grid;

public class Fire extends StaticParticle {
    
    int lifetime = Math.max(200, ThreadLocalRandom.current().nextInt(fireResistance));
    int hasLivedFor = 0;
    boolean hasMovedLastFrame = false;
    
    
    int[][] availableColors = new int[][]{
        new int[]{255, 31, 31},
        new int[]{234, 90, 0}, 
        new int[]{255, 105, 0},
        new int[]{238, 204, 9},
        new int[]{240, 161, 65}
    };

    int currentColor = ThreadLocalRandom.current().nextInt(availableColors.length);



    public Fire() {
        super();

        
        
        // make them shift a bit to create texture
        // original sand = 182, 155, 99
        int offset = getColorOffset();
        setColors(255, 31, 31 );

        canBeOverridden = true;
        spawnRate = 0.02f;
    }

    

    public int[] update(int[] coords, Grid grid){
        
        
        super.update(coords, grid);

        //implemented like a Gas
        hasLivedFor++;
        if (hasLivedFor >= lifetime) {
            grid.setParticle(coords[0], coords[1], new Smoke());
            return coords;
        }

        hasMovedLastFrame = !hasMovedLastFrame;
        if (hasMovedLastFrame) return coords;

        currentColor = (currentColor + 1) % (availableColors.length -1);
        setColors(availableColors[currentColor][0] , availableColors[currentColor][1] , availableColors[currentColor][2] );

        return coords;


        

    }
}
