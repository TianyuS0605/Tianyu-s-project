package byow.Core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

import static java.lang.Character.isDigit;

public class HomePage {
    private int height;
    private int width;

    public static final String[] menu = {"New Game (N)","Load Game (L)", "Quit (Q)"};

    public HomePage(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public void loadHomePage() {
        initializeCanvas();
        paintMenu();
        StdDraw.show();
    }

    public void loadGameOverPage() {
        initializeCanvas();
        Font fontLarge = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(fontLarge);
        StdDraw.text(width  / 2, height, "GAME OVER");
        StdDraw.show();
        StdDraw.pause(3000);
    }

    public void loadNotSupportNotification() {
        StdDraw.setPenColor(Color.RED);
        Font fontSmall = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.text(width / 2, height -10 - menu.length * 3,
                "Operation is not supported. Please input valid operation (N ,L ,Q).");
        StdDraw.show();
        StdDraw.setPenColor(Color.WHITE);
    }

    public void loadHistoryNotExistNotification() {
        StdDraw.setPenColor(Color.RED);
        Font fontSmall = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.text(width / 2, height -10 - menu.length * 3,
                "You don't have history to load. Please start a new game.");
        StdDraw.show();
        StdDraw.setPenColor(Color.WHITE);
    }

    private void initializeCanvas() {
        StdDraw.setCanvasSize(width * 16, height * 16);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, width);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
    }

    private void paintMenu() {
        Font fontLarge = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(fontLarge);
        StdDraw.text(width  / 2, height, "CS61B: THE GAME");
        Font fontSmall = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontSmall);
        for (int i = 0; i < menu.length; i++) {
            StdDraw.text(width / 2, height - 10 - i * 3, menu[i]);
        }
    }

    public String collectSeedFromInput() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(width  / 2, height, "Input your seed to create map. Seed must be number and end with 's'");
        StdDraw.show();
        String seed = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                Character input = StdDraw.nextKeyTyped();
                if (input == 's') {
                    return seed;
                }
                if (!isDigit(input)) {
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(width  / 2, height, "Input your seed to create map. Seed must be number and end with 's'");
                    StdDraw.text(width  / 2, height - 5, "Input should be a number or 's'!");
                    StdDraw.show();
                    continue;
                }
                seed += input;
                StdDraw.clear(Color.BLACK);
                StdDraw.text(width  / 2, height, "Input your seed to create map. Seed must be number and end with 's'");
                StdDraw.text(width  / 2, height - 5, seed);
                StdDraw.show();
            }
        }
    }

}
