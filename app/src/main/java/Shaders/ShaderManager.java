package Shaders;

import Grid.Grid;

import java.awt.Color;
import java.awt.Graphics2D;

import Blocks.Particle;


public class ShaderManager {

    private final Grid GRID;
    private final int TILE_DIMENSION;
    private Shader shader;

    // TODO:
    // private final int ROWS;
    // private final int COLS;

    public ShaderManager(Grid grid, int tileDimension) {
        this.GRID = grid;
        this.TILE_DIMENSION = tileDimension;
        this.shader = new Shader(grid.getViewportRows(), grid.getViewportColumns());
    }


    // TODO: apply multithreading
    // NOTE: same thing as window.drawgrid() to avoid calling millions of functions
    // for every pixel
    public void render(Graphics2D g) {
        for (int j = 0; j < GRID.getViewportRows(); j++){
            for (int i = 0; i < GRID.getViewportColumns(); i++) {
                Particle curr = GRID.getAtPosition(j + GRID.getViewportOffsetY(), i + GRID.getViewportOffsetX());

                shader.set(
                    curr.getColorRed(),
                    curr.getColorGreen(),
                    curr.getColorBlue(),
                    (double)j,
                    (double)i
                );

                // NOTE: to mix shaders
                // g.setColor(
                //     Utilities.mixColors(new Color[]{
                //         shader.centerCircle(),
                //         shader.white()

                //     })
                // );

                g.setColor(shader.geogebra());
                g.fillRect(i * TILE_DIMENSION, j * TILE_DIMENSION, TILE_DIMENSION, TILE_DIMENSION);
            }
        }

    }






}
