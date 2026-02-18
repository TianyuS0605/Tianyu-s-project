package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.File;
import static byow.Core.Utils.*;


public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    private HomePage homePage = new HomePage(HEIGHT, WIDTH);
    private World world;
    private String[] header = {"nothing", "score: 0"};

    private Position mousePosition = new Position(-1, -1);

    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File HISTORY_FILE = join(CWD, "history.txt");

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        homePage.loadHomePage();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                Character input = StdDraw.nextKeyTyped();
                switch (input) {
                    case 'N': case 'n':
                        String seed = homePage.collectSeedFromInput();
                        startGame(seed);
                        break;
                    case 'L': case 'l':
                        if (!HISTORY_FILE.exists()) {
                            homePage.loadHistoryNotExistNotification();
                        } else {
                            world = Utils.readObject(HISTORY_FILE, World.class);
                            startGame("Load Game");
                        }
                        break;
                    case 'Q': case 'q':
                        System.exit(0);
                        break;
                    default:
                        homePage.loadNotSupportNotification();
                        break;
                }
            }
        }
    }


    public void startGame(String seedStr) {
        if (!seedStr.equals("Load Game")) {
            long seed = Long.parseLong(seedStr);
            world = new World(WIDTH, HEIGHT, seed);
            world.generateWorld();
        }
        ter.initialize(WIDTH, HEIGHT);
        ter.renderHeaderAndFrame(header, world.getWholeWorld());
        Character previousInput = ' ';
        while (true) {
            int mouseX = (int) StdDraw.mouseX();
            int mouseY = (int) StdDraw.mouseY();
            if (mouseX >= 0 && mouseX < WIDTH && mouseY >= 0 && mouseY < HEIGHT
                    && (mouseX != mousePosition.x || mouseY != mousePosition.y)) {
                mousePosition = new Position(mouseX, mouseY);
                header[0] = world.getWholeWorld()[mouseX][mouseY].description();
                ter.renderHeaderAndFrame(header, world.getWholeWorld());
            }
            if (StdDraw.hasNextKeyTyped() && !world.gameOver) {
                Character input = StdDraw.nextKeyTyped();
                switch (input) {
                    case 'A': case 'a':
                        moveHero(-1, 0);
                        previousInput = input;
                        break;
                    case 'W': case 'w':
                        moveHero(0, 1);
                        previousInput = input;
                        break;
                    case 'D': case 'd':
                        moveHero(1, 0);
                        previousInput = input;
                        break;
                    case 'S': case 's':
                        moveHero(0, -1);
                        previousInput = input;
                        break;
                    case 'Q': case 'q':
                        if (previousInput == ':') {
                            Utils.writeObject(HISTORY_FILE, world);
                            previousInput = ' ';
                            System.exit(0);
                        }
                        break;
                    case ':':
                        previousInput = input;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void moveHero(int x, int y) {
        world.moveHero(x, y);
        if (world.gameOver) {
            if (world.goldAmount == 0) {
                // Not complete yet
                StdDraw.clear();
                homePage.loadGameOverPage();
                interactWithKeyboard();
            } else {
                StdDraw.clear(Color.BLACK);
                homePage.loadGameOverPage();
                interactWithKeyboard();
            }
        } else {
            header[1] = "score: " + world.score;
            ter.renderHeaderAndFrame(header, world.getWholeWorld());
        }
    }



    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        int endIndex = 1;
        switch (input.charAt(0)) {
            case 'N': case 'n':
                if (input.contains("s")) {
                    endIndex = input.indexOf("s");
                } else {
                    endIndex = input.indexOf("S");
                }
                String seedStr = input.substring(1, endIndex);
                long seed = Long.parseLong(seedStr);
                world = new World(WIDTH, HEIGHT, seed);
                world.generateWorld();
                break;
            case 'L': case 'l':
                if (HISTORY_FILE.exists()) {
                    world = Utils.readObject(HISTORY_FILE, World.class);
                    endIndex = 0;
                }
                break;
            default:
                break;
        }

        String operationString = input.substring(endIndex + 1);
        for (char c : operationString.toCharArray()) {
            switch (c) {
                case 'A': case 'a':
                    world.moveHero(-1, 0);
                    break;
                case 'W': case 'w':
                    world.moveHero(0, 1);
                    break;
                case 'S': case 's':
                    world.moveHero(0, -1);
                    break;
                case 'D': case 'd':
                    world.moveHero(1, 0);
                    break;
                case 'Q': case 'q':
                    Utils.writeObject(HISTORY_FILE, world);
                    return world.getWholeWorld();
                default:
                    break;
            }
        }
        return world.getWholeWorld();
    }
}
