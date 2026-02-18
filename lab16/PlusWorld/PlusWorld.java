package PlusWorld;
import org.junit.Test;
import static org.junit.Assert.*;

import byowTools.TileEngine.TERenderer;
import byowTools.TileEngine.TETile;
import byowTools.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of plus shaped regions.
 */
public class PlusWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // draw plus
        world = drawPlus(world);

        // draws the world to the screen
        ter.renderFrame(world);
    }

    private static TETile[][] drawPlus(TETile[][] world) {

        for (int y = HEIGHT / 3; y < HEIGHT / 3 + 9; y += 1) {
            if (y > HEIGHT / 3 + 2 &&  y < HEIGHT / 3 + 6) {
                for (int x = WIDTH / 2 - 4; x <= WIDTH / 2 + 4; x += 1) {
                    world[x][y] = Tileset.WALL;
                }
            } else {
                for (int x = WIDTH / 2 - 1; x <= WIDTH / 2 + 1; x += 1) {
                    world[x][y] = Tileset.WALL;
                }
            }
        }
        return world;
    }
}
