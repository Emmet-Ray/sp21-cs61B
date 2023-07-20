package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Graph;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public static final int ROOM_WIDTH = 10;
    public static final int ROOM_HEIGHT = 10;

    public static final int ROOM_NUMBER_MAX = 30;
    public static final int ROOM_NUMBER_MIN = 20;

    private Random random = null;

    private HashSet<Room> existingRooms = new HashSet<>();


    private class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public boolean equals(Object p) {
            Position position = (Position) p;
            return this.x == position.x && this.y == position.y;
        }


    }

    /**
     *  class for rooms & hallways
     *  hallway is a specific type of rooms
     */
    private class Room {
        // position of anchor i.e. left bottom point of the room
        Position position;
        int width;
        int length;
        boolean hallway;
        boolean vertical; // for hallways, if true, it's vertical, otherwise, it's horizontal.

        Room(Position position, int width, int length, boolean hallway,boolean vertical) {
            this.position = position;
            this.width = width;
            this.length = length;
            this.hallway = hallway;
            this.vertical = vertical;
        }
        /*
        *  decide whether this room is overlapping with existing rooms or not
         */
        public boolean overlap(HashSet<Room> existingRoom) {
            for (Room room : existingRoom) {
                if (overlapWithRoom(room)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * @Tag : source : https://www.geeksforgeeks.org/find-two-rectangles-overlap/#
         * @param room
         * @return
         */
        private boolean overlapWithRoom(Room room) {
            if (this.position.x >= room.shiftX(room.width) || room.position.x >= this.shiftX(this.width)) {
                return false;
            }
            if (this.position.y >= room.shiftY(room.length) || room.position.y >= this.shiftY(this.length)) {
                return false;
            }
            return true;
        }

        /**
         * @param dx
         * @return x position after shift dx relative to the anchor
         */
        public int shiftX(int dx) {
            return this.position.x + dx;
        }
        // same as shiftX
        public int shiftY(int dy) {
            return this.position.y + dy;
        }
    }
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
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
     * In other words, both of these calls:
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
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        Engine.fillOut(finalWorldFrame);

        // get seed from input string, for now, input just seed
        String seed = input;
        random = new Random(Long.parseLong(seed));

        drawRooms(finalWorldFrame);
        drawHallways(finalWorldFrame);
        return finalWorldFrame;
    }

    /**
     *  todo : need to decide the algorithm to generate hallways that link all rooms
     *          maybe : dog leg algorithm ???
     * @param finalWorldFrame
     */
    private void drawHallways(TETile[][] finalWorldFrame) {
        int i = 0;
        for (Room room : existingRooms) {
            if (i == 2) {
                break;
            }
            finalWorldFrame[room.position.x][room.position.y] = Tileset.FLOWER;
            i += 1;
        }
    }

    private void drawRooms(TETile[][] finalWorldFrame) {
        int x = random.nextInt(ROOM_NUMBER_MIN, ROOM_NUMBER_MAX);
        System.out.println("number of room : " + x);
        for (int i = 0; i < x; i++) {
            drawRandomRoom(finalWorldFrame);
        }
    }
    /**
     * draw a random room : random position, random width, random height
     *
     * need to deal with overlapping room
     *
     * @param world
     */
    private void drawRandomRoom(TETile[][] world) {

        Room room = getRandomRoom(false);
        while (room.overlap(existingRooms)) {
            room = getRandomRoom(false);
        }
        existingRooms.add(room);
        drawHelper(world, room);
    }

    private void drawHelper(TETile[][] world, Room room) {
        for (int i = 0; i < room.width; i++) {
            for (int j = 0; j < room.length; j++) {
                if (i % (room.width - 1) == 0 || j % (room.length - 1) == 0) {
                    world[room.shiftX(i)][room.shiftY(j)] = Tileset.WALL;
                } else {
                    world[room.shiftX(i)][room.shiftY(j)] = Tileset.FLOOR;
                }
            }
        }
    }

    /**
     *
     * @return a random position
     */
    private Position getRandomPosition() {
        int x = random.nextInt(WIDTH - ROOM_WIDTH);
        int y = random.nextInt(HEIGHT - ROOM_HEIGHT);
        Position p = new Position(x, y);
        return p;
    }

    private Room getRandomRoom(boolean hallway) {
        Room room = null;
        Position p = getRandomPosition();
        int length = random.nextInt(3, ROOM_HEIGHT);
        if (hallway) {
            int width = random.nextInt(3, 4);
            // todo : vertical
            room = new Room(p, width,length, true, false);
        } else {
            int width = random.nextInt(3, ROOM_WIDTH);
            room = new Room(p, width,length, false, false);
        }
        return room;
    }

    private static void fillOut(TETile[][] world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        Engine engine = new Engine();
        String seed = "123";
        TETile[][] world = engine.interactWithInputString(seed);
        ter.renderFrame(world);

    }
}
