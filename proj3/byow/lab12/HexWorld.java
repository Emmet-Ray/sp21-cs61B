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
        int height = getHeight(size);
        int width = getWidth(size);

        int xPosition = x;
        int yPosition = y;
        int numberOfTile = size;
        TETile randonTile = randomTile();
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
        int tiltNum = RANDOM.nextInt(4);
        return switch (tiltNum) {
            case 0 -> Tileset.WALL;
            case 1 -> Tileset.FLOWER;
            case 2 -> Tileset.MOUNTAIN;
            case 3 -> Tileset.GRASS;
            default -> throw new IllegalStateException("Unexpected value: " + tiltNum);
        };
    }
    // todo : need to refactor
    private static void fillaRow(int size, int width, int numberOofTile, TETile[][] hexagonWorld, int xPosition, int yPosition, TETile tile) {
        int j = 0;
        int numberOfNothing = (width - numberOofTile) / 2;

        xPosition = fillRowHelper(hexagonWorld, xPosition, yPosition, Tileset.NOTHING, numberOfNothing);
        xPosition = fillRowHelper(hexagonWorld, xPosition, yPosition, tile, numberOofTile);
        xPosition = fillRowHelper(hexagonWorld, xPosition, yPosition, Tileset.NOTHING, numberOfNothing);

        /*
        for (int i = 0; i < numberOfNothing; i++, xPosition++) {
            hexagonWorld[xPosition][yPosition] = Tileset.NOTHING;
        }
        for (int i = 0; i < numberOofTile; i++, xPosition++) {
            hexagonWorld[xPosition][yPosition] = Tileset.TREE;
        }
        for (int i = 0; i < numberOfNothing; i++, xPosition++) {
            hexagonWorld[xPosition][yPosition] = Tileset.NOTHING;
        }
         */
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
    private static int getHeight(int size) {
        return 2 * size;
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
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] test = new TETile[WIDTH][HEIGHT];
        initializeWorld(test);
        // bottom left position
        int x = 10;
        int y = 10;
        int size = 3;
        int yBound = y + 9 * size;
        int xBound = x + 3 * (size + getWidth(size));
        int i = 0;
        int j = 0;
        for (int yPoisition = y; yPoisition < yBound; yPoisition += yStep(size), i += 1) {
            j = 0;
            if (i % 8 == 0) {
                singleHexagon(size, test, x + xStep(size), yPoisition);
                continue;
            }
            if (i % 2 == 0) {
               for (int xPosition = x; j < 3; xPosition += xStep(size), j += 1) {
                    singleHexagon(size, test, xPosition, yPoisition);
               }
            } else {
                for (int xPosition = x + xStep(size) / 2; j < 2; xPosition += xStep(size), j += 1) {
                    singleHexagon(size, test, xPosition, yPoisition);
                }
            }
        }

        /*
        singleHexagon(size, test, x, y);
        singleHexagon(size, test, 10, 10);
        singleHexagon(size, test, 17, 6);
         */

        ter.renderFrame(test);
    }

}
