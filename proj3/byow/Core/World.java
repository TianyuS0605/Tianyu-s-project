package byow.Core;

import byow.TileEngine.*;

import java.io.Serializable;
import java.util.*;
import static java.lang.Math.*;

public class World implements Serializable {
    private long seed = 0;
    private int worldWidth;
    private int worldHeight;
    private TETile[][] world;
    private ArrayList<Room> rooms;
    private int numRooms;
    private Random r;
    private Position heroPosition = new Position(-1, -1);
    private Position ghostPosition = new Position(-1, -1);
    public int goldAmount = 0;
    public int score = 0;
    public boolean gameOver = false;
//    private Position[] ghostPositions = new Position[2];

    public Position getHeroPosition() {
        return heroPosition;
    }

    public World(int width, int height, long SEED) {
        seed = SEED;
        r = new Random(SEED);
        this.worldWidth = width;
        this.worldHeight = height;
        this.world = this.generateNewWorld(worldWidth, worldHeight);
        numRooms = r.nextInt(5) + 4;
        goldAmount = r.nextInt(5) + 10;
        rooms = new ArrayList<>();
    }

    public TETile[][] generateNewWorld(int width, int height) {
        TETile[][] tile = new TETile[width][height];
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                tile[w][h] = Tileset.NOTHING;
            }
        }
        return tile;
    }

    public void placeOneRoom() {
        Boolean bool = false;
        Room room = new Room(r);
        while (!bool) {
            bool = true;
            for (int j = room.leftX; j <= room.rightX; j++) {
                for (int k = room.leftY; k <= room.rightY; k++) {
                    if (world[j][k].character() != Tileset.NOTHING.character()
                            || world[min(j + 2, 79)][k].character() != Tileset.NOTHING.character()
                            || world[max(j - 2, 0)][k].character() != Tileset.NOTHING.character()
                            || world[j][min(k + 2, 29)].character() != Tileset.NOTHING.character()
                            || world[j][max(k - 2, 0)].character() != Tileset.NOTHING.character()
                    ) {
                        bool = false;
                        room = new Room(r);
                        break;
                    }
                }
                if (!bool) {
                    break;
                }
            }
        }
        room.paintRoom(room, world);
        rooms.add(room);
    }

    public void placeOneHallway(int roomOneIndex, int roomTwoIndex) {
        Room roomOne = rooms.get(roomOneIndex);
        Room roomTwo = rooms.get(roomTwoIndex);
        int widthDifference = Math.max(roomOne.leftX - roomTwo.rightX, roomTwo.leftX - roomOne.rightX);
        int heightDifference = Math.max(roomOne.leftY - roomTwo.rightY, roomTwo.leftY - roomOne.rightY);
        if (widthDifference >= heightDifference) {
            Room roomLeft = roomOne;
            Room roomRight = roomTwo;
            if (roomOne.leftX - roomTwo.rightX > roomTwo.leftX - roomOne.rightX) {
                roomLeft = roomTwo;
                roomRight = roomOne;
            }
            int outLeftY = r.nextInt(roomLeft.height) + roomLeft.leftY;
            int outRightY = r.nextInt(roomRight.height) + roomRight.leftY;
            int inflectionPointX = 3 + roomLeft.rightX;
            if (widthDifference > 2) {
                inflectionPointX += r.nextInt(widthDifference - 2);
            }
            horExtension(roomLeft.rightX + 1, inflectionPointX, outLeftY);
            verExtension(inflectionPointX, Math.min(outLeftY, outRightY), Math.max(outLeftY, outRightY));
            horExtension(inflectionPointX, roomRight.leftX - 1, outRightY);
        } else {
            Room roomDown = roomOne;
            Room roomUp = roomTwo;
            if (roomOne.leftY - roomTwo.rightY > roomTwo.leftY - roomOne.rightY) {
                roomDown = roomTwo;
                roomUp = roomOne;
            }
            int outDownX = r.nextInt(roomDown.width) + roomDown.leftX;
            int outUpX = r.nextInt(roomUp.width) + roomUp.leftX;
            int inflectionPointY = 3 + roomDown.rightY;
            if (heightDifference > 2) {
                inflectionPointY += r.nextInt(heightDifference - 2);
            }
            verExtension(outDownX, roomDown.rightY + 1, inflectionPointY);
            horExtension(Math.min(outDownX, outUpX), Math.max(outDownX, outUpX), inflectionPointY);
            verExtension(outUpX, inflectionPointY, roomUp.leftY - 1);
        }
    }

    public void placeAllRooms() {
        int i = 0;
        while (i < numRooms) {
            this.placeOneRoom();
            i++;
        }
    }

    public void placeAllHallways() {
        for (int i = 0; i < numRooms - 1; i++) {
            placeOneHallway(i, i + 1);
        }
        placeOneHallway(numRooms - 1, 0);
    }

    public void addWall() {
        for (int i = 0; i < worldWidth; i++) {
            for (int j = 0; j < worldHeight; j++) {
                if (world[i][j].character() != Tileset.FLOOR.character()) {
                    if (world[max(0, i - 1)][j].character() == Tileset.FLOOR.character()
                            || world[i][min(j + 1, 29)].character() == Tileset.FLOOR.character()
                            || world[min(79, i + 1)][j].character() == Tileset.FLOOR.character()
                            || world[i][max(0, j - 1)].character() == Tileset.FLOOR.character()
                            || world[max(0, i - 1)][min(29, j + 1)].character() == Tileset.FLOOR.character()
                            || world[min(79, i + 1)][min(29, j + 1)].character() == Tileset.FLOOR.character()
                            || world[max(0, i - 1)][max(j - 1, 0)].character() == Tileset.FLOOR.character()
                            || world[min(79, i + 1)][max(0, j - 1)].character() == Tileset.FLOOR.character()) {
                        world[i][j] = Tileset.WALL;
                    }
                }
            }
        }
    }

    public void addDoor() {
        int doorX;
        int doorY = 0;
        while (true) {
            doorX = r.nextInt(worldWidth);
            int numOfWall = 0;
            for (int i = 0; i < worldHeight; i++) {
                if (world[doorX][i] == Tileset.WALL) {
                    numOfWall++;
                }
            }
            if (numOfWall == 0) {
                continue;
            }
            int randomY = r.nextInt(numOfWall) + 1;
            for (int i = 0; i < worldHeight; i++) {
                if (world[doorX][i] == Tileset.WALL) {
                    doorY = i;
                    randomY--;
                }
                if (randomY == 0) {
                    break;
                }
            }
            break;
        }
        world[doorX][doorY] = Tileset.LOCKED_DOOR;
    }

    public void addCharacter() {
        int heroPosX = r.nextInt(this.worldWidth);
        int heroPosY = r.nextInt(this.worldHeight);
        while (world[heroPosX][heroPosY] != Tileset.FLOOR) {
            heroPosX = r.nextInt(this.worldWidth);
            heroPosY = r.nextInt(this.worldHeight);
        }
        world[heroPosX][heroPosY] = Tileset.AVATAR;
        heroPosition = new Position(heroPosX, heroPosY);

        int ghostPosX = r.nextInt(this.worldWidth);
        int ghostPosY = r.nextInt(this.worldHeight);
        while (world[ghostPosX][ghostPosY] != Tileset.FLOOR ||
                abs(heroPosX - ghostPosX) < 5 || abs(heroPosY -ghostPosY) < 5) {
            ghostPosX = r.nextInt(this.worldWidth);
            ghostPosY = r.nextInt(this.worldHeight);
        }
        world[ghostPosX][ghostPosY] = Tileset.GHOST;
        ghostPosition = new Position(ghostPosX, ghostPosY);

//        for (int i = 0; i < 2; i++) {
//            int ghostPosX = r.nextInt(this.worldWidth);
//            int ghostPosY = r.nextInt(this.worldHeight);
//            while (world[ghostPosX][ghostPosY] != Tileset.FLOOR ||
//                    abs(heroPosX - ghostPosX) < 5 || abs(heroPosY -ghostPosY) < 5) {
//                ghostPosX = r.nextInt(this.worldWidth);
//                ghostPosY = r.nextInt(this.worldHeight);
//            }
//            world[ghostPosX][ghostPosY] = Tileset.GHOST;
//            ghostPositions[i] = new Position(ghostPosX, ghostPosY);
//        }
    }

    public void addGold() {
        for (int i = 0; i < goldAmount; i++) {
            int goldPosX = r.nextInt(this.worldWidth);
            int goldPosY = r.nextInt(this.worldHeight);
            while (world[goldPosX][goldPosY].character() != Tileset.FLOOR.character()) {
                goldPosX = r.nextInt(this.worldWidth);
                goldPosY = r.nextInt(this.worldHeight);
            }
            world[goldPosX][goldPosY] = Tileset.GOLD;
        }
    }


    public void generateWorld() {
        this.placeAllRooms();
        this.placeAllHallways();
        this.addWall();
        this.addDoor();
        this.addCharacter();
        this.addGold();
    }

    public TETile[][] getWholeWorld() {
        return world;
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(80, 50);
        World world = new World(80, 50, 15032978);
        world.generateWorld();
        ter.renderFrame(world.getWholeWorld());
    }

    public void horExtension(int leftX, int rightX, int Y) {
        for (int i = leftX; i <= rightX; i++) {
            world[i][Y] = Tileset.FLOOR;
        }
    }

    public void verExtension(int X, int downY, int upY) {
        for (int i = downY; i <= upY; i++) {
            world[X][i] = Tileset.FLOOR;
        }
    }

    public boolean moveHero(int directionX, int directionY) {

        if (heroPosition.x + directionX >= 0 && heroPosition.x + directionX < worldWidth
                && heroPosition.y + directionY >= 0 && heroPosition.y + directionY < worldHeight
                && (world[heroPosition.x + directionX][heroPosition.y + directionY].character() == Tileset.FLOOR.character()
                    || world[heroPosition.x + directionX][heroPosition.y + directionY].character() == Tileset.GOLD.character()
                    || world[heroPosition.x + directionX][heroPosition.y + directionY].character() == Tileset.GHOST.character())) {
            if (world[heroPosition.x + directionX][heroPosition.y + directionY].character() == Tileset.GHOST.character()) {
                gameOver = true;
            } else if (world[heroPosition.x + directionX][heroPosition.y + directionY].character() == Tileset.GOLD.character()) {
                score++;
                goldAmount--;
                if (goldAmount == 0) {
                    gameOver = true;
                }
            }
            world[heroPosition.x][heroPosition.y] = Tileset.FLOOR;
            world[heroPosition.x + directionX][heroPosition.y + directionY] = Tileset.AVATAR;
            heroPosition = new Position(heroPosition.x + directionX, heroPosition.y + directionY);
            return true;
        }
        return false;
    }

}