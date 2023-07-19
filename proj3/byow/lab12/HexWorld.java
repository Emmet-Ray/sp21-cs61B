package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final Random RANDOM = new Random();
    /**
     *  somehow give one hexagon
     *
     *  choose plan 2. because not sure could I return TETile[][]
     *
     *  method signature design choice :
     *  1. return a single hexagon, and leave the drawing work to others
         private static TETile[][] singleHexagon(int size)
     *  2. return nothing, pass in the bottom left position of the rectangle that contains the hexagon
     *     and may be fill out part of the whole world, and finally draw the whole world
         private static void singleHexagon(int size, TETile[][] int x, int y)
     *
     *  rules / invariants for [size] hexagon:
     *          1. size of rectangle to contain the hexagon : height : number of lines ; width : [size] + 2 * ([size] - 1)
     *          1. the first line has [size] "*"
     *          2. total number of lines : 2 * size
     *          3. up-down symmetric
     *          4. for the top half : add 2 "*" every level down
     *
     *  example :
     *      size 2 :
     *              **
     *             ****
     *             ****
     *              **
     *      size 3 :
     *              ***
     *             *****
     *            *******
     *            *******
     *             *****
     *              ***
     *
     * @param size length of the hexagon side
     * @param x   x position of the bottom-left position of the rectangle
     * @param y   y ...
     *
     *
     */
    private static void singleHexagon(int size, TETile[][] hexagonWorld, int x, int y) {
        // height & width of the rectangle that contains the hexagon
        int width = getWidth(size);

        int xPosition = x;
        int yPosition = y;
        int numberOfTile = size;
        TETile randonTile = randomTile();
        fillOut(hexagonWorld, xPosition, yPosition, randonTile, size, width, numberOfTile);

    }

    private static void fillOut(TETile[][] hexagonWorld, int xPosition, int yPosition, TETile randonTile, int size, int width, int numberOfTile) {
        // 下半部分
        for(int i = 0; i < size; i++, yPosition++) {
            fillaRow(size, width, numberOfTile, hexagonWorld,xPosition, yPosition, randonTile);
            numberOfTile += 2;
        }
        numberOfTile -= 2;
        // 上半部分
        for (int i = 0; i < size; i++, yPosition++) {
            fillaRow(size, width, numberOfTile, hexagonWorld,xPosition, yPosition, randonTile);
            numberOfTile -= 2;
        }
    }

    private static TETile randomTile() {
        int tiltNum = RANDOM.nextInt(10);
        return switch (tiltNum) {
            case 0 -> Tileset.WALL;
            case 1 -> Tileset.FLOWER;
            case 2 -> Tileset.MOUNTAIN;
            case 3 -> Tileset.GRASS;
            case 4 -> Tileset.FLOOR;
            case 5 -> Tileset.TREE;
            case 6 -> Tileset.UNLOCKED_DOOR;
            case 7 -> Tileset.WATER;
            case 8 -> Tileset.LOCKED_DOOR;
            case 9 -> Tileset.SAND;
            default -> throw new IllegalStateException("Unexpected value: " + tiltNum);
        };
    }
    private static void fillaRow(int size, int width, int numberOofTile, TETile[][] hexagonWorld, int xPosition, int yPosition, TETile tile) {
        int j = 0;
        int numberOfNothing = (width - numberOofTile) / 2;

        xPosition = fillRowHelper(hexagonWorld, xPosition, yPosition, Tileset.NOTHING, numberOfNothing);
        xPosition = fillRowHelper(hexagonWorld, xPosition, yPosition, tile, numberOofTile);
        fillRowHelper(hexagonWorld, xPosition, yPosition, Tileset.NOTHING, numberOfNothing);
    }
    private static int fillRowHelper(TETile[][] world, int xPosition, int yPosition, TETile content, int number) {
        for (int i = 0; i < number; i++, xPosition++) {
            // already fill out
            if (!checkWidth(xPosition) || !checkHeight(yPosition)) {
                continue;
            }
            if (content.equals(Tileset.NOTHING) && !world[xPosition][yPosition].equals(Tileset.NOTHING)) {
                continue;
            }
            world[xPosition][yPosition] = content;
        }
        return xPosition;
    }

    private static boolean checkWidth(int x) {
        return x >= 0 && x < WIDTH;
    }
    private static boolean checkHeight(int x) {
        return x >= 0 && x < HEIGHT;
    }
    private static int getWidth(int size) {
        return size + 2 * (size - 1);
    }

    private static void initializeWorld(TETile[][] world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }
    private static int xStep(int size) {
        return size + getWidth(size);
    }
    private static int yStep(int size) {
        return size;
    }

    private static void oneLevel(int level, TETile[][] hexWorld, int size, int x, int yPoisition) {
        int j = 0;
        if (level % 8 == 0) {
            singleHexagon(size, hexWorld, x + xStep(size), yPoisition);
            return;
        }
        // for "even level", print 3 single hexagon
        // for "odd level", print 2 single hexagon
        if (level % 2 == 0) {
            for (int xPosition = x; j < 3; xPosition += xStep(size), j += 1) {
                singleHexagon(size, hexWorld, xPosition, yPoisition);
            }
        } else {
            for (int xPosition = x + xStep(size) / 2; j < 2; xPosition += xStep(size), j += 1) {
                singleHexagon(size, hexWorld, xPosition, yPoisition);
            }
        }
    }

    public static void hexagonWorld(int size, TETile[][] hexWorld,int x, int y) {
        int level = 0;
        // total 9 level according to the lab 12 spec
        for (int yPoisition = y; level < 9; yPoisition += yStep(size), level += 1) {
            oneLevel(level, hexWorld, size, x, yPoisition);
        }
    }
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] test = new TETile[WIDTH][HEIGHT];
        initializeWorld(test);
        // bottom left position
        int x = 10;
        int y = 10;
        int size = 3;
        hexagonWorld(size, test, x, y);

        ter.renderFrame(test);
    }

}
