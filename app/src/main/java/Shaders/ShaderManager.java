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

    private final int FPS;

    // UNIFORMS
    private int playerDirectionX = 0; // always 0 or +- 1
    private int playerDirectionY = 0;
    private int mouseSize = 1; // can also try with mouse position
    private float time = 0;

    public ShaderManager(Grid grid, int tileDimension, int fps) {
        this.GRID = grid;
        this.TILE_DIMENSION = tileDimension;
        this.ROWS = grid.getViewportRows();
        this.COLS = grid.getViewportColumns();
        this.shader = new Shader(grid.getViewportRows(), grid.getViewportColumns());

        this.FPS = fps;
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

        time += 1.0 / (float)FPS; // updates one every second

        // set uniforms once
        shader.DELTA_X += playerDirectionX;
        shader.DELTA_Y += playerDirectionY;
        shader.time = time;

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
                // g.setColor(shader.geogebra());
                g.setColor(shader.cave());
                // g.setColor(shader.clear());

                g.fillRect(i * TILE_DIMENSION, j * TILE_DIMENSION, TILE_DIMENSION, TILE_DIMENSION);
            }
        }

    }


}
