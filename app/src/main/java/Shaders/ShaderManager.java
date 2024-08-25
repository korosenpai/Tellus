package Shaders;

import Grid.Grid;

import java.awt.Graphics2D;

import Blocks.Particle;


public class ShaderManager {

    private final Grid GRID;
    private final int TILE_DIMENSION;
    private Shader shader;

    private final int ROWS;
    private final int COLS;

    // UNIFORMS
    private int playerDirectionX = 0; // always 0 or +- 1
    private int playerDirectionY = 0;
    private int mouseSize = 1; // can also try with mouse position

    public ShaderManager(Grid grid, int tileDimension) {
        this.GRID = grid;
        this.TILE_DIMENSION = tileDimension;
        this.ROWS = grid.getViewportRows();
        this.COLS = grid.getViewportColumns();
        this.shader = new Shader(grid.getViewportRows(), grid.getViewportColumns());
    }

    public void setUniforms(int playerDirectionX, int playerDirectionY, int mouseSize) {
        this.playerDirectionX = playerDirectionX;
        this.playerDirectionY = playerDirectionY;
        this.mouseSize = Math.max(mouseSize, 1);
    }


    public int renders = 0;
    // TODO: apply multithreading
    // NOTE: same thing as window.drawgrid() to avoid calling millions of functions
    // for every pixel
    public void render(Graphics2D g) {
        renders++;

        // set uniforms once
        shader.DELTA_X += playerDirectionX;
        shader.DELTA_Y += playerDirectionY;

        for (int j = 0; j < ROWS; j++){
            for (int i = 0; i < COLS; i++) {
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


                // g.setColor(shader.mandelbrotSet(mouseSize));
                // g.setColor(shader.cave());
                g.setColor(shader.clear());

                g.fillRect(i * TILE_DIMENSION, j * TILE_DIMENSION, TILE_DIMENSION, TILE_DIMENSION);
            }
        }

    }


}
