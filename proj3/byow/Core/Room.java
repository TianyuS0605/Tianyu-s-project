package byow.Core;

import byow.TileEngine.*;

import java.io.Serializable;
import java.util.Random;

public class Room implements Serializable {
    public int width;
    public int height;
    public int leftX;
    public int leftY;
    public int rightX;
    public int rightY;
    private Random r;

    public Room(Random random) {
        r = random;
        leftX = r.nextInt(73) + 1;
        leftY = r.nextInt(23) + 1;
        width = r.nextInt(5) + 2;
        height = r.nextInt(5) + 2;
        rightX = leftX + width - 1;
        rightY = leftY + height - 1;
    }

    public void paintRoom(Room room, TETile[][] world) {
        for (int i = room.leftX; i <= room.rightX; i++) {
            for (int j = room.leftY; j <= room.rightY; j++) {
                world[i][j] = Tileset.FLOOR;
            }
        }
    }
}